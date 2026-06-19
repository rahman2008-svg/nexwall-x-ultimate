package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.WallpaperData
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: WallpaperViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studioImage by viewModel.studioImageUrl.collectAsState()
    val blurLevel by viewModel.blurLevel.collectAsState()
    val colorFilter by viewModel.colorFilter.collectAsState()
    val darkOverlay by viewModel.darkOverlay.collectAsState()

    var activeScale by remember { mutableStateOf(1f) } // Crop/Scale slider simulation
    var isSaving by remember { mutableStateOf(false) }
    var renameDialogVisible by remember { mutableStateOf(false) }
    var wallpaperName by remember { mutableStateOf("Bespoke AMOLED") }

    // Derive Compose ColorMatrix depending on selected filter name
    val composeColorMatrix = remember(colorFilter) {
        when (colorFilter) {
            "sepia" -> ColorMatrix(
                floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            "monochromatic" -> ColorMatrix().apply { setToSaturation(0f) }
            "cyberpunk" -> ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 50f, // intense red shift
                    0f, 0.8f, 0f, 0f, 0f,
                    1.2f, 0f, 1.5f, 0f, 80f, // bluish purple injection
                    0f, 0f, 0f, 1f, 0f
                )
            )
            "neon_cool" -> ColorMatrix(
                floatArrayOf(
                    0.5f, 0f, 0f, 0f, 0f,
                    0f, 1.2f, 1.0f, 0f, 40f, // green and blue teal boost
                    0.8f, 0f, 1.4f, 0f, 60f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            "vintage" -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 30f,
                    0f, 0.9f, 0f, 0f, 10f,
                    0f, 0f, 0.7f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            else -> null // normal
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wallpaper Studio", fontWeight = FontWeight.Bold, color = ImmersivePurple) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface),
                actions = {
                    IconButton(onClick = {
                        // Reset all studio controls
                        viewModel.updateBlur(0f)
                        viewModel.updateColorFilter("none")
                        viewModel.updateDarkOverlay(0f)
                        activeScale = 1f
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Settings", tint = Color.LightGray)
                    }
                }
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
            // Interactive Real-Time Image rendering with applied effects
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(380.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .testTag("studio_preview_box")
            ) {
                // Background image subject to blur/matrix/filters
                AsyncImage(
                    model = studioImage,
                    contentDescription = "Studio Live Preview",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = activeScale,
                            scaleY = activeScale
                        )
                        .blur(radius = if (blurLevel > 0) blurLevel.dp else 0.1.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = composeColorMatrix?.let { ColorFilter.colorMatrix(it) }
                )

                // Dark Overlay Layer
                if (darkOverlay > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = darkOverlay))
                    )
                }

                // Decorative Ambient Vignette
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )

                // Label Badge inside preview
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Real-time Live Render",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Horizon of choose base image to edit
            Text(
                text = "1. Choose Base Wallpaper Layout",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(WallpaperData.wallpapers) { wp ->
                    val active = studioImage == wp.imageUrl
                    AsyncImage(
                        model = wp.imageUrl,
                        contentDescription = "Thumbnail",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = if (active) 2.dp else 0.dp,
                                color = if (active) NeonCyan else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.setStudioBaseImage(wp.imageUrl) },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Editing Parameters
            Text(
                text = "2. Customize Aesthetic Properties",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Blur Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Gauss Blur: ${blurLevel.toInt()}dp", color = Color.LightGray, fontSize = 12.sp)
                    Icon(Icons.Default.BlurOn, contentDescription = "Blur", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
                Slider(
                    value = blurLevel,
                    onValueChange = { viewModel.updateBlur(it) },
                    valueRange = 0f..25f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = NeonCyan,
                        inactiveTrackColor = DarkCard
                    ),
                    modifier = Modifier.testTag("blur_slider")
                )
            }

            // Crop Scale Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Zoom / Auto Crop: ${(activeScale * 100).toInt()}%", color = Color.LightGray, fontSize = 12.sp)
                    Icon(Icons.Default.Crop, contentDescription = "Crop", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
                Slider(
                    value = activeScale,
                    onValueChange = { activeScale = it },
                    valueRange = 1f..2.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonPink,
                        activeTrackColor = NeonPink,
                        inactiveTrackColor = DarkCard
                    )
                )
            }

            // Dark Overlay slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Dim Glow Overlay: ${(darkOverlay * 100).toInt()}%", color = Color.LightGray, fontSize = 12.sp)
                    Icon(Icons.Default.Brightness2, contentDescription = "Brightness", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
                Slider(
                    value = darkOverlay,
                    onValueChange = { viewModel.updateDarkOverlay(it) },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = CyberYellow,
                        activeTrackColor = CyberYellow,
                        inactiveTrackColor = DarkCard
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Color Filters selector
            Text(
                text = "Color FX Profile Filters",
                color = Color.LightGray,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            val filterList = listOf(
                "none" to "Normal",
                "sepia" to "Sepia Dream",
                "monochromatic" to "Noir B&W",
                "cyberpunk" to "Cyber Magenta",
                "neon_cool" to "Neon Ocean",
                "vintage" to "Aesthetic Retro"
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterList) { (filterVal, filterName) ->
                    val active = colorFilter == filterVal
                    Surface(
                        onClick = { viewModel.updateColorFilter(filterVal) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (active) NeonCyan else DarkCard,
                        border = BorderStroke(1.dp, Color.DarkGray)
                    ) {
                        Text(
                            text = filterName,
                            color = if (active) Color.Black else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save bespoke Studio creation
            Button(
                onClick = { renameDialogVisible = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_studio_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.SaveAlt, contentDescription = "Save Custom Wallpaper")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Compile & Save to Studio Lib", fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Decorative Brand Text
            Text(
                text = "Crafted in NexVora lab's studio by Prince AR Abdur Rahman",
                color = Color.DarkGray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    // Modal popup to name the created wallpaper
    if (renameDialogVisible) {
        AlertDialog(
            onDismissRequest = { renameDialogVisible = false },
            title = { Text("Compile Custom Wallpaper", color = NeonCyan, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Choose a name for your tailored aesthetic masterpiece:", color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = wallpaperName,
                        onValueChange = { wallpaperName = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = Color.DarkGray
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isSaving = true
                        viewModel.saveCustomStudioWallpaper(wallpaperName)
                        renameDialogVisible = false
                        Toast.makeText(context, "🌌 Saved and added to Favorites!", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                ) {
                    Text("Save to Favorites DAO")
                }
            },
            dismissButton = {
                TextButton(onClick = { renameDialogVisible = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = DarkSurface
        )
    }
}
