package com.example.ui.screens

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.live.NexWallpaperService
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveEngineScreen(
    viewModel: WallpaperViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLiveType by viewModel.currentLiveType.collectAsState()

    // Interactive adjustable engine parameters for real-time sandbox preview
    var particlesCount by remember { mutableStateOf(40f) }
    var renderingSpeed by remember { mutableStateOf(1f) }
    var waveAmplitude by remember { mutableStateOf(60f) }

    // Coroutine ticker to simulate animation frame changes in the Compose Canvas
    var ticks by remember { mutableStateOf(0L) }
    LaunchedEffect(renderingSpeed) {
        while (true) {
            ticks++
            // scale delay with speed factor
            val delayMs = (30 / renderingSpeed).toLong().coerceIn(10L, 100L)
            delay(delayMs)
        }
    }

    // In-Memory Particle generator for Sandbox preview
    val sandboxParticles = remember(particlesCount) {
        List(particlesCount.toInt()) {
            SandboxParticle(
                x = Random.nextFloat() * 800f,
                y = Random.nextFloat() * 1200f,
                vx = (Random.nextFloat() * 6f - 3f),
                vy = (Random.nextFloat() * 6f - 3f),
                radius = Random.nextFloat() * 8f + 3f,
                color = when (Random.nextInt(3)) {
                    0 -> NeonCyan
                    1 -> NeonPink
                    else -> CyberYellow
                }
            )
        }
    }

    // Update particle positions based on ticks
    LaunchedEffect(ticks) {
        if (currentLiveType == "particles") {
            for (p in sandboxParticles) {
                p.x += p.vx
                p.y += p.vy
                // rebound boundaries inside standard preview constraints
                if (p.x < 0 || p.x > 800) p.vx *= -1
                if (p.y < 0 || p.y > 1200) p.vy *= -1
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("4D Live Engine Sandbox", fontWeight = FontWeight.Bold, color = ImmersivePurple) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        },
        containerColor = AmoledBlack,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live sandbox drawing canvas box (resembles exact phone aspect ratio)
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(380.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black)
                    .testTag("live_canvas_sandbox")
            ) {
                // Interactive Compose Custom canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    when (currentLiveType) {
                        "particles" -> {
                            // Render atoms
                            // Draw connecting vectors
                            for (i in sandboxParticles.indices) {
                                val p1 = sandboxParticles[i]
                                for (j in i + 1 until sandboxParticles.size) {
                                    val p2 = sandboxParticles[j]
                                    val dx = (p1.x / 800f * width) - (p2.x / 800f * width)
                                    val dy = (p1.y / 1200f * height) - (p2.y / 1200f * height)
                                    val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                                    if (dist < 100f) {
                                        val opacity = (1f - (dist / 100f)).coerceIn(0f, 0.7f)
                                        drawLine(
                                            color = NeonCyan.copy(alpha = opacity),
                                            start = Offset(p1.x / 800f * width, p1.y / 1200f * height),
                                            end = Offset(p2.x / 800f * width, p2.y / 1200f * height),
                                            strokeWidth = 1f
                                        )
                                    }
                                }
                            }

                            // Draw individual nodes
                            for (p in sandboxParticles) {
                                drawCircle(
                                    color = p.color,
                                    radius = p.radius,
                                    center = Offset(p.x / 800f * width, p.y / 1200f * height)
                                )
                            }
                        }

                        "matrix" -> {
                            // Draw tumbling matrix characters
                            drawIntoCanvas { canvas ->
                                val paint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.rgb(0, 255, 70)
                                    textSize = 28f
                                    isAntiAlias = true
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }

                                val cols = 10
                                val spacing = width / cols
                                for (c in 0 until cols) {
                                    // custom height based on ticks and random offsets
                                    val offsetMultiplier = (ticks + c * 35) % 45
                                    val charY = offsetMultiplier * 14f
                                    val txt = "01ABCD".random().toString()
                                    
                                    if (charY <= height) {
                                        // paint lead bright white
                                        if (Random.nextInt(5) == 0) paint.color = android.graphics.Color.WHITE
                                        else paint.color = android.graphics.Color.rgb(0, 255, 70)

                                        canvas.nativeCanvas.drawText(
                                            txt,
                                            c * spacing + spacing / 2,
                                            charY,
                                            paint
                                        )
                                    }
                                }
                            }
                        }

                        "liquid_flow", "neon_glow" -> {
                            // Draw Sine Waves overlays
                            val p1 = Path().apply {
                                moveTo(0f, height)
                                lineTo(0f, height * 0.45f)
                                var xr = 0f
                                while (xr <= width) {
                                    val yr = height * 0.45f + waveAmplitude * 0.5f * kotlin.math.sin(xr * 0.015f + ticks * 0.05f)
                                    lineTo(xr, yr)
                                    xr += 8f
                                }
                                lineTo(width, height)
                                close()
                            }
                            drawPath(
                                path = p1,
                                color = NeonPink.copy(alpha = 0.4f)
                            )

                            val p2 = Path().apply {
                                moveTo(0f, height)
                                lineTo(0f, height * 0.58f)
                                var xr = 0f
                                while (xr <= width) {
                                    val yr = height * 0.58f + waveAmplitude * 0.6f * kotlin.math.sin(xr * 0.01f - ticks * 0.04f)
                                    lineTo(xr, yr)
                                    xr += 8f
                                }
                                lineTo(width, height)
                                close()
                            }
                            drawPath(
                                path = p2,
                                color = NeonCyan.copy(alpha = 0.35f)
                            )
                        }
                    }
                }

                // Title overlay badge relative to selected option
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Sandbox Live Preview",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Choose Live wallpaper engine type
            Text(
                text = "1. Active Live Shader System",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val liveSystems = listOf(
                    "particles" to "Space Atoms",
                    "matrix" to "Matrix Rain",
                    "liquid_flow" to "Neon Waves"
                )

                for ((type, label) in liveSystems) {
                    val active = currentLiveType == type
                    Button(
                        onClick = { viewModel.updateLiveWallpaperType(type) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("live_system_tab_$type"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (active) NeonCyan else DarkCard,
                            contentColor = if (active) Color.Black else Color.White
                        ),
                        border = BorderStroke(1.dp, Color.DarkGray)
                    ) {
                        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Particle Sandbox Controls
            Text(
                text = "2. Customize Physic Parameters",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Speed slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Motion Speed rate: ${String.format("%.1f", renderingSpeed)}x", color = Color.LightGray, fontSize = 12.sp)
                    Icon(Icons.Default.Speed, contentDescription = "Speed", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
                Slider(
                    value = renderingSpeed,
                    onValueChange = { renderingSpeed = it },
                    valueRange = 0.5f..2.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = NeonCyan,
                        inactiveTrackColor = DarkCard
                    )
                )
            }

            // Type specific sliders
            AnimatedVisibility(visible = currentLiveType == "particles") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Node Count: ${particlesCount.toInt()} atoms", color = Color.LightGray, fontSize = 12.sp)
                        Icon(Icons.Default.Grain, contentDescription = "Density", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    }
                    Slider(
                        value = particlesCount,
                        onValueChange = { particlesCount = it },
                        valueRange = 10f..70f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonPink,
                            activeTrackColor = NeonPink,
                            inactiveTrackColor = DarkCard
                        )
                    )
                }
            }

            AnimatedVisibility(visible = currentLiveType == "liquid_flow") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Wave Wave Amplitude: ${waveAmplitude.toInt()}px", color = Color.LightGray, fontSize = 12.sp)
                        Icon(Icons.Default.Transform, contentDescription = "Amplitude", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    }
                    Slider(
                        value = waveAmplitude,
                        onValueChange = { waveAmplitude = it },
                        valueRange = 20f..120f,
                        colors = SliderDefaults.colors(
                            thumbColor = CyberYellow,
                            activeTrackColor = CyberYellow,
                            inactiveTrackColor = DarkCard
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Activate system level live service
            Button(
                onClick = {
                    try {
                        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                            putExtra(
                                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                ComponentName(context, NexWallpaperService::class.java)
                            )
                        }
                        context.startActivity(intent)
                        Toast.makeText(context, "Redirecting to system Live Wallpaper Manager! Choose 'NexWall Engine'", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Service launching simulated on device emulator!", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("set_live_service_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPink,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.FlashOn, contentDescription = "Launch Engine")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Set System 4D Live Wallpaper", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Helpful instructional tip
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = DarkCard,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Help", tint = NeonCyan)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Setting a live 4D wallpaper runs a hardware-optimized thread that renders pure blacks efficiently, lowering battery consumption on AMOLED panels.",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// Simple in-memory particle representation
class SandboxParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val radius: Float,
    val color: Color
)
