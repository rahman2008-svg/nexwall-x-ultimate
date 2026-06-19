package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Keep
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainLayout()
            }
        }
    }
}

// Routes representing screens
enum class ScreenRoute(val label: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    HOME("Home", Icons.Filled.Wallpaper, Icons.Outlined.Wallpaper),
    STUDIO("Studio", Icons.Filled.Brush, Icons.Outlined.Brush),
    LIVE_SANDBOX("Live 4D", Icons.Filled.FlashOn, Icons.Outlined.OfflineBolt),
    COLLECTION("Collection", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    PORTFOLIO("About", Icons.Filled.Info, Icons.Outlined.Info)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainLayout() {
    val viewModel: WallpaperViewModel = viewModel()
    var currentScreen by remember { mutableStateOf(ScreenRoute.HOME) }
    val adStatus by viewModel.adStatus.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                ScreenRoute.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            // Interstitial simulated ad check on change screens occasionally
                            if (screen == ScreenRoute.STUDIO || screen == ScreenRoute.LIVE_SANDBOX) {
                                viewModel.triggerInterstitialAd {
                                    currentScreen = screen
                                }
                            } else {
                                currentScreen = screen
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.activeIcon else screen.inactiveIcon,
                                contentDescription = screen.label,
                                tint = if (isSelected) ImmersivePurple else Color.LightGray
                            )
                        },
                        label = {
                            Text(
                                text = screen.label,
                                color = if (isSelected) ImmersivePurple else Color.LightGray,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = ImmersivePurple.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AmoledBlack)
        ) {
            // Screen transition display
            when (currentScreen) {
                ScreenRoute.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onFeaturedHeroClick = { currentScreen = ScreenRoute.LIVE_SANDBOX }
                )
                ScreenRoute.STUDIO -> StudioScreen(viewModel = viewModel)
                ScreenRoute.LIVE_SANDBOX -> LiveEngineScreen(viewModel = viewModel)
                ScreenRoute.COLLECTION -> FavoritesScreen(viewModel = viewModel)
                ScreenRoute.PORTFOLIO -> InfoScreen()
            }
        }
    }

    // Floating simulated AdMob overlays depending on active ViewModel request state
    adStatus?.let { status ->
        when (status) {
            "interstitial_loading" -> {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = NeonCyan)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Loading Ad...", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            "interstitial_shown" -> {
                SimulatedInterstitialDialog(onClose = { viewModel.dismissAd() })
            }
            "reward_shown" -> {
                SimulatedRewardDialog(onClose = { viewModel.dismissAd() })
            }
        }
    }
}

@Composable
fun SimulatedInterstitialDialog(onClose: () -> Unit) {
    var timerSeconds by remember { mutableStateOf(3) }
    LaunchedEffect(Unit) {
        while (timerSeconds > 0) {
            delay(1000)
            timerSeconds--
        }
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(16.dp),
            color = DarkSurface,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, NeonPink)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            color = NeonPink.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, NeonPink)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = "Sponsored", tint = NeonPink, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sponsored AdMob Interstitial", color = NeonPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Cool simulated ad image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = "Sponsor Logo", tint = CyberYellow, modifier = Modifier.size(44.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Unlock Premium Autopacks", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Upgrade to NexWall VIP Platinum containing 400+ fully-unlocked 4D Animated Live Engines, 2000+ extreme AMOLED assets, and zero sponsorships.",
                            textAlign = TextAlign.Center,
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }

                    // Bottom close / upgrade button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (timerSeconds <= 0) onClose()
                            },
                            enabled = timerSeconds <= 0,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkCard,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (timerSeconds > 0) "Close in ${timerSeconds}s" else "Close", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onClose() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonPink,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Upgrade Pro $2.99", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimulatedRewardDialog(onClose: () -> Unit) {
    var viewProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        val totalSteps = 35
        for (step in 1..totalSteps) {
            delay(100)
            viewProgress = step.toFloat() / totalSteps
        }
        delay(300)
        onClose()
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(24.dp),
            color = DarkSurface,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, CyberYellow)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.OndemandVideo, contentDescription = "Ad", tint = CyberYellow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AdMob Reward Sponsor Ad", color = CyberYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Viewing sponsored video to unlock your ultimate preview lock...",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = viewProgress,
                        color = CyberYellow,
                        trackColor = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${(viewProgress * 100).toInt()}% watched", color = Color.Gray, fontSize = 10.sp)
                }

                Text(
                    text = "Unlock token will be added to Room DAO on completion.",
                    color = Color.DarkGray,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


