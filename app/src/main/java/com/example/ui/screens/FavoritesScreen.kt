package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.FavoriteWallpaper
import com.example.data.Wallpaper
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: WallpaperViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val favorites by viewModel.favorites.collectAsState()
    val isPremium by viewModel.isPremiumUser.collectAsState()

    // Smart Auto Changer state variables
    var autoChangerEnabled by remember { mutableStateOf(false) }
    var frequencySelected by remember { mutableStateOf("Daily") }
    var sourceSelected by remember { mutableStateOf("All") }

    // Backup states
    var isBackingUp by remember { mutableStateOf(false) }
    var isRestoring by remember { mutableStateOf(false) }

    var selectedWallpaperForDetail by remember { mutableStateOf<Wallpaper?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Offline Lib & Sync", fontWeight = FontWeight.Bold, color = ImmersivePurple) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface),
                actions = {
                    // Quick stats badge showing DB items count
                    Surface(
                        color = ImmersivePurple.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, ImmersivePurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = "${favorites.size} Saved",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImmersivePurple,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
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
                .padding(horizontal = 16.dp)
        ) {
            // Check for empty favorites state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (favorites.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Empty",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Your collection is empty", color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Favorite active Wallpapers or save custom creations in the Studio to display here!",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    Text(
                        text = "My Personal Collection",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("favorites_grid"),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(favorites, key = { it.id }) { fav ->
                            Box(
                                modifier = Modifier
                                    .aspectRatio(0.7f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedWallpaperForDetail = Wallpaper(
                                            id = fav.id,
                                            title = fav.title,
                                            category = fav.category,
                                            imageUrl = fav.imageUrl,
                                            isAnimated = fav.isAnimated,
                                            isPremium = fav.isPremium,
                                            resolution = fav.resolution
                                        )
                                    }
                            ) {
                                AsyncImage(
                                    model = fav.imageUrl,
                                    contentDescription = fav.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                // Simple favorite icon to quickly remove
                                IconButton(
                                    onClick = { viewModel.toggleFavorite(Wallpaper(fav.id, fav.title, fav.category, fav.imageUrl, fav.isAnimated, fav.isPremium, fav.resolution)) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(30.dp)
                                        .padding(4.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Favorite",
                                        tint = NeonPink,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }

                                // Text banner
                                Text(
                                    text = fav.title,
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .padding(4.dp)
                                        .align(Alignment.BottomCenter),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Auto wallpaper controller
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Autorenew, contentDescription = "Auto changer", tint = NeonCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Smart Auto Changer Pack", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Rotates wallpapers on locker scheduler", color = Color.Gray, fontSize = 10.sp)
                            }
                        }

                        Switch(
                            checked = autoChangerEnabled,
                            onCheckedChange = {
                                if (it && !isPremium) {
                                    // Trigger reward ad to authorize feature
                                    viewModel.triggerRewardAd {
                                        autoChangerEnabled = true
                                        Toast.makeText(context, "🎁 Auto Changer activated using sponsorship!", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    autoChangerEnabled = it
                                    if (it) Toast.makeText(context, "🔄 Auto rotatory pack configured successfully!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = NeonCyan.copy(alpha = 0.4f),
                                uncheckedThumbColor = Color.DarkGray,
                                uncheckedTrackColor = DarkSurface
                            ),
                            modifier = Modifier.testTag("auto_changer_switch")
                        )
                    }

                    AnimatedVisibility(
                        visible = autoChangerEnabled,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column {
                            Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                            // Settings Frequency selector
                            Text("Changer Interval Frequency", color = Color.LightGray, fontSize = 11.sp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val options = listOf("3 Hours", "Daily", "Weekly")
                                for (opt in options) {
                                    val active = frequencySelected == opt
                                    Surface(
                                        onClick = { frequencySelected = opt },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (active) NeonCyan else DarkSurface,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = opt,
                                            color = if (active) Color.Black else Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Source
                            Text("Pack Source target", color = Color.LightGray, fontSize = 11.sp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val sources = listOf("All Items", "Favorites", "AMOLED Only")
                                for (src in sources) {
                                    val active = sourceSelected == src
                                    Surface(
                                        onClick = { sourceSelected = src },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (active) NeonPink else DarkSurface,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = src,
                                            color = if (active) Color.Black else Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Database backup & restore panel
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CloudSync, contentDescription = "Backup", tint = CyberYellow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("NexWall Backup & Restore", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Keep collection synced across your accounts", color = Color.Gray, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Backup button
                        Button(
                            onClick = {
                                isBackingUp = true
                                viewModel.backupFavorites { msg ->
                                    isBackingUp = false
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurface,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color.DarkGray)
                        ) {
                            if (isBackingUp) {
                                CircularProgressIndicator(color = CyberYellow, modifier = Modifier.size(18.dp))
                            } else {
                                Icon(Icons.Default.Backup, contentDescription = "Sync", tint = CyberYellow, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sync Backup", fontSize = 12.sp)
                            }
                        }

                        // Restore button
                        Button(
                            onClick = {
                                isRestoring = true
                                viewModel.restoreFavorites { msg ->
                                    isRestoring = false
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurface,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color.DarkGray)
                        ) {
                            if (isRestoring) {
                                CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(18.dp))
                            } else {
                                Icon(Icons.Default.Restore, contentDescription = "Restore", tint = NeonCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Restore Data", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Detail dialog pop-up on click favorite item
    selectedWallpaperForDetail?.let { wallpaper ->
        WallpaperDetailDialog(
            wallpaper = wallpaper,
            isPremiumUser = isPremium,
            isFavorited = favorites.any { it.id == wallpaper.id },
            onFavoriteToggle = { viewModel.toggleFavorite(wallpaper) },
            onUnlockViaAd = {
                viewModel.triggerRewardAd {
                    Toast.makeText(context, "🎁 Reward granted: Saved 4K unlocked!", Toast.LENGTH_LONG).show()
                }
            },
            onDismiss = { selectedWallpaperForDetail = null }
        )
    }
}
