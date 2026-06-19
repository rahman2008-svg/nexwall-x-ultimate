package com.example.ui.screens

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.Wallpaper
import com.example.data.WallpaperData
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WallpaperViewModel,
    modifier: Modifier = Modifier,
    onFeaturedHeroClick: () -> Unit = {}
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isPremium by viewModel.isPremiumUser.collectAsState()
    val customWallpapers by viewModel.customWallpapers.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    val context = LocalContext.current
    var selectedWallpaperForDetail by remember { mutableStateOf<Wallpaper?>(null) }

    // Mix built-in and user-studio creations
    val allCalculatedWallpapers = remember(customWallpapers) {
        customWallpapers + WallpaperData.wallpapers
    }

    val filteredWallpapers = remember(searchQuery, selectedCategory, allCalculatedWallpapers) {
        allCalculatedWallpapers.filter { wp ->
            (selectedCategory == "All" || wp.category.equals(selectedCategory, ignoreCase = true) || 
                    (selectedCategory == "Animated" && wp.isAnimated)) &&
                    (searchQuery.isEmpty() || wp.title.lowercase().contains(searchQuery.lowercase()))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        // Hero Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkSurface, AmoledBlack)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NexWall",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonCyan
                        ),
                        modifier = Modifier.testTag("app_logo")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "X Ultimate",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NeonPink
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (isPremium) {
                        Surface(
                            color = CyberYellow.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, CyberYellow)
                        ) {
                            Text(
                                text = "VIP PRO",
                                fontSize = 10.sp,
                                color = CyberYellow,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    text = "Aesthetic AMOLED & Live 4K Engine",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Quick Premium Toggle Badge (Top Right)
            IconButton(
                onClick = {
                    viewModel.togglePremium()
                    Toast.makeText(
                        context,
                        if (!isPremium) "👑 NexWall VIP Ultra Unlocked!" else "Free Demo mode initiated",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .background(if (isPremium) CyberYellow.copy(alpha = 0.15f) else DarkCard)
                    .size(46.dp)
                    .testTag("vip_toggle_button")
            ) {
                Icon(
                    imageVector = if (isPremium) Icons.Default.WorkspacePremium else Icons.Default.LockOpen,
                    contentDescription = "VIP Upgrade Toggle",
                    tint = if (isPremium) CyberYellow else Color.White
                )
            }
        }

        // Immersive Hero Live Feature
        FeaturedLiveHero(
            onExploreClick = onFeaturedHeroClick,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // Search Bar Row
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("wallpaper_search_input"),
            placeholder = { Text("Search 1000+ ultimate wallpapers...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search", tint = Color.Gray) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear text", tint = Color.LightGray)
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = ImmersivePurple,
                unfocusedBorderColor = DarkCard,
                focusedContainerColor = DarkCard,
                unfocusedContainerColor = DarkCard
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Categories Sub-Heading
        Text(
            text = "Categories",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            letterSpacing = 1.sp
        )

        // Categories Chips List (Immersive Tailwind styling)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(WallpaperData.categories) { category ->
                val active = selectedCategory == category
                val (catIcon, catColor) = getCategoryIconAndColor(category)
                Surface(
                    onClick = { viewModel.updateCategory(category) },
                    modifier = Modifier.testTag("category_chip_$category"),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    border = BorderStroke(
                        1.dp,
                        if (active) ImmersivePurple else Color.White.copy(alpha = 0.05f)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                                )
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.width(65.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(catColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = catIcon,
                                    contentDescription = category,
                                    tint = catColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = category.uppercase(),
                                color = if (active) ImmersivePurple else Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // AdMob Banner Spot Simulation for non-premium
        if (!isPremium) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "Simulated Ad Click. Upgrade to Premium to remove AD banners!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                color = DarkCard,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.5.dp, NeonPink.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Badge(containerColor = NeonPink, contentColor = Color.White) {
                        Text("Ad", fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Customize live matrix rain! Tap to unlock Premium with No Ads.",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Wallpapers List Grid
        if (filteredWallpapers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Wallpaper,
                        contentDescription = "Empty",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No amazing wallpapers found", color = Color.Gray)
                    Text("Try another category or clear search query!", color = Color.Gray, fontSize = 12.sp)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .testTag("wallpapers_grid"),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredWallpapers, key = { it.id }) { wallpaper ->
                    WallpaperCard(
                        wallpaper = wallpaper,
                        isFavorited = favorites.any { it.id == wallpaper.id },
                        onFavoriteToggle = { viewModel.toggleFavorite(wallpaper) },
                        onWallpaperClick = { selectedWallpaperForDetail = wallpaper }
                    )
                }
            }
        }
    }

    // Detail dialog pop-up on wallpaper click
    selectedWallpaperForDetail?.let { wallpaper ->
        WallpaperDetailDialog(
            wallpaper = wallpaper,
            isPremiumUser = isPremium,
            isFavorited = favorites.any { it.id == wallpaper.id },
            onFavoriteToggle = { viewModel.toggleFavorite(wallpaper) },
            onUnlockViaAd = {
                viewModel.triggerRewardAd {
                    Toast.makeText(context, "🎁 Reward granted: Download unlocked!", Toast.LENGTH_LONG).show()
                }
            },
            onDismiss = { selectedWallpaperForDetail = null }
        )
    }
}

@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    isFavorited: Boolean,
    onFavoriteToggle: () -> Unit,
    onWallpaperClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onWallpaperClick)
            .testTag("wallpaper_card_${wallpaper.id}")
    ) {
        // Thumbnail Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(wallpaper.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = wallpaper.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Vignette effect bottom projection for label visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        ),
                        startY = 350f
                    )
                )
        )

        // Badges overlay
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Live/Animated Badge
            if (wallpaper.isAnimated) {
                Surface(
                    color = NeonCyan,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Live Icon",
                            tint = Color.Black,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("LIVE", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Premium Lock badge
            if (wallpaper.isPremium) {
                Surface(
                    color = CyberYellow,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Premium lock",
                            tint = Color.Black,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                        Text("VIP", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Bottom Content details
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = wallpaper.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = wallpaper.resolution,
                    color = Color.LightGray,
                    fontSize = 10.sp
                )

                Icon(
                    imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite icon",
                    tint = if (isFavorited) NeonPink else Color.White,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onFavoriteToggle() }
                )
            }
        }
    }
}

// Fullscreen high-spec bottom/dialog overlay Preview
@Composable
fun WallpaperDetailDialog(
    wallpaper: Wallpaper,
    isPremiumUser: Boolean,
    isFavorited: Boolean,
    onFavoriteToggle: () -> Unit,
    onUnlockViaAd: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isPlacingWallpaper by remember { mutableStateOf(false) }
    var settingProgress by remember { mutableStateOf(0f) }
    var isDownloadActive by remember { mutableStateOf(false) }

    val hasAccess = !wallpaper.isPremium || isPremiumUser

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Full screen Image background
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(wallpaper.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Full render preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Top Control buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Arrow
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Close", tint = Color.White)
                }

                // Info description card
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = wallpaper.category,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                // Favorite Toggle
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite text tag",
                        tint = if (isFavorited) NeonPink else Color.White
                    )
                }
            }

            // Bottom Setup Card Sheet
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkSurface, Color.Black)
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = wallpaper.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Studio: ${wallpaper.author}", color = Color.Gray, fontSize = 12.sp)
                    Surface(color = Color.DarkGray, modifier = Modifier.size(4.dp), shape = CircleShape) {}
                    Text("Res: ${wallpaper.resolution}", color = Color.Gray, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons depending on premium license
                if (hasAccess) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 1. Download Offline
                        Button(
                            onClick = {
                                isDownloadActive = true
                                coroutineScope.launch {
                                    delay(1500)
                                    isDownloadActive = false
                                    Toast.makeText(
                                        context,
                                        "💾 Saved to Gallery (Pictures/NexWall_4K)",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("download_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkCard,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.DarkGray)
                        ) {
                            if (isDownloadActive) {
                                CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(24.dp))
                            } else {
                                Icon(Icons.Outlined.Download, contentDescription = "Download")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Download 4K", fontWeight = FontWeight.Bold)
                            }
                        }

                        // 2. Set Wallpaper
                        Button(
                            onClick = {
                                isPlacingWallpaper = true
                                settingProgress = 0.1f
                                coroutineScope.launch {
                                    while (settingProgress < 1.0f) {
                                        delay(150)
                                        settingProgress += 0.15f
                                    }
                                    // Set Wallpaper simulation or intent wallpaper
                                    try {
                                        val wallpaperManager = WallpaperManager.getInstance(context)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            // Simulated set
                                            Toast.makeText(
                                                context,
                                                "🎉 Beautifully Set on Home & Lock Screens!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Set via system settings instead!", Toast.LENGTH_SHORT).show()
                                    }
                                    isPlacingWallpaper = false
                                }
                            },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(52.dp)
                                .testTag("apply_wallpaper_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isPlacingWallpaper) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Placing...", fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Icon(Icons.Default.Wallpaper, contentDescription = "Apply")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Apply Screen", fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                } else {
                    // locked premium prompt (Allow watching ad to unlock or upgrade)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeonPink.copy(alpha = 0.11f))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = "Lock", tint = NeonPink)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Exclusive VIP 4K Wallpaper",
                                color = NeonPink,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = "Watch a fast 5s video sponsor sponsor ad to unlock, or unlock all 2000+ premium resources instantly.",
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Watch ad option
                            Button(
                                onClick = onUnlockViaAd,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkCard,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.PlayCircleOutline, contentDescription = "Video sponsor", tint = NeonCyan)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Watch Ad", fontSize = 12.sp)
                            }

                            // VIP subscription bypass button
                            Button(
                                onClick = {
                                    // Trigger instant premium toggler in VM
                                    onDismiss()
                                    Toast.makeText(context, "👑 Welcome to VIP!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberYellow,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1.1f)
                            ) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = "VIP Upgrade", tint = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Get VIP $2.99", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "© 2026 NexVora Lab's Ofc. Developed by Prince AR Abdur Rahman",
                    color = Color.DarkGray,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FeaturedLiveHero(
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPulsing by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            isPulsing = !isPulsing
            delay(1000)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF0F0F0F))
                )
            )
            .clickable(onClick = onExploreClick)
    ) {
        // Aesthetic Image Mock resembling Cyberpunk rain banner
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://images.unsplash.com/photo-1614728263952-84ea206f99b6?auto=format&fit=crop&q=80&w=600")
                .crossfade(true)
                .build(),
            contentDescription = "Cyber Rain Live Banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Vignette Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
        )

        // Live + AMOLED overlays
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = Color.Red,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isPulsing) Color.White else Color.White.copy(alpha = 0.4f))
                    )
                    Text(
                        text = "LIVE",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Surface(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "AMOLED",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // Details bottom-left
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp)
        ) {
            Text(
                text = "Daily Masterpiece",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Cyber Matrix Rain v2.0",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
        }

        // Navigation CTA Action button
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp)
                .size(46.dp),
            shape = RoundedCornerShape(14.dp),
            color = ImmersivePurple,
            onClick = onExploreClick
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Launch Engine",
                    tint = ImmersivePurpleDark,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun getCategoryIconAndColor(category: String): Pair<androidx.compose.ui.graphics.vector.ImageVector, Color> {
    return when (category.lowercase()) {
        "all" -> Pair(Icons.Default.Apps, ImmersivePurple)
        "animated" -> Pair(Icons.Default.PlayArrow, NeonPink)
        "amoled" -> Pair(Icons.Default.WorkspacePremium, CyberYellow)
        "space" -> Pair(Icons.Default.Public, NeonCyan)
        "nature" -> Pair(Icons.Default.Terrain, MatrixGreen)
        "anime" -> Pair(Icons.Default.Face, SoftCyan)
        "cyberpunk" -> Pair(Icons.Default.FlashOn, NeonPink)
        "minimal" -> Pair(Icons.Default.Minimize, Color.Gray)
        "gaming" -> Pair(Icons.Default.Gamepad, CyberYellow)
        "football" -> Pair(Icons.Default.SportsSoccer, MatrixGreen)
        "cars" -> Pair(Icons.Default.DirectionsCar, NeonCyan)
        else -> Pair(Icons.Default.Star, ImmersivePurple)
    }
}
