package com.example.ui.screens

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WallpaperViewModel(application: Application) : AndroidViewModel(application) {

    private val db = WallpaperDatabase.getDatabase(application)
    private val repository = WallpaperRepository(db.wallpaperDao())

    // UI state states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _isPremiumUser = MutableStateFlow(false)
    val isPremiumUser = _isPremiumUser.asStateFlow()

    // Studio Editing State
    private val _studioImageUrl = MutableStateFlow(WallpaperData.wallpapers.first().imageUrl)
    val studioImageUrl = _studioImageUrl.asStateFlow()

    private val _blurLevel = MutableStateFlow(0f) // 0 to 25
    val blurLevel = _blurLevel.asStateFlow()

    private val _colorFilter = MutableStateFlow("none") // "none", "sepia", "monochromatic", "cyberpunk", "neon_cool", "vintage"
    val colorFilter = _colorFilter.asStateFlow()

    private val _darkOverlay = MutableStateFlow(0f) // 0 to 1
    val darkOverlay = _darkOverlay.asStateFlow()

    // Live Wallpaper Settings
    private val _currentLiveType = MutableStateFlow("particles")
    val currentLiveType = _currentLiveType.asStateFlow()

    // Simulated Ads states
    private val _adStatus = MutableStateFlow<String?>(null) // null, "banner", "interstitial_loading", "interstitial_shown", "reward_shown"
    val adStatus = _adStatus.asStateFlow()

    private val _unlockedPremiumFeaturesCount = MutableStateFlow(0)
    val unlockedPremiumFeaturesCount = _unlockedPremiumFeaturesCount.asStateFlow()

    // Custom wallpapers (Created by user)
    private val _customWallpapers = MutableStateFlow<List<Wallpaper>>(emptyList())
    val customWallpapers = _customWallpapers.asStateFlow()

    // Room Favorites
    val favorites: StateFlow<List<FavoriteWallpaper>> = repository.allFavorites
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Initialize Live Wallpaper type from SharedPreferences
        val prefs = application.getSharedPreferences("NexWallPrefs", Context.MODE_PRIVATE)
        _currentLiveType.value = prefs.getString("current_live_type", "particles") ?: "particles"
        _isPremiumUser.value = prefs.getBoolean("is_premium", false)
    }

    // Toggle Favorite
    fun toggleFavorite(wallpaper: Wallpaper) {
        viewModelScope.launch {
            val isFav = favorites.value.any { it.id == wallpaper.id }
            if (isFav) {
                repository.removeFavorite(wallpaper.id)
            } else {
                repository.addFavorite(wallpaper)
            }
        }
    }

    fun isFavorited(id: String): Flow<Boolean> {
        return repository.getIsFavorite(id)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }

    fun togglePremium() {
        _isPremiumUser.value = !_isPremiumUser.value
        val prefs = getApplication<Application>().getSharedPreferences("NexWallPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_premium", _isPremiumUser.value).apply()
    }

    // Live Wallpaper Config
    fun updateLiveWallpaperType(type: String) {
        _currentLiveType.value = type
        val prefs = getApplication<Application>().getSharedPreferences("NexWallPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("current_live_type", type).apply()
    }

    // Studio Editing functions
    fun setStudioBaseImage(url: String) {
        _studioImageUrl.value = url
    }

    fun updateBlur(blur: Float) {
        _blurLevel.value = blur
    }

    fun updateColorFilter(filter: String) {
        _colorFilter.value = filter
    }

    fun updateDarkOverlay(intensity: Float) {
        _darkOverlay.value = intensity
    }

    // Save customized studio creation
    fun saveCustomStudioWallpaper(title: String) {
        val uniqueId = "custom_${System.currentTimeMillis()}"
        val newWallpaper = Wallpaper(
            id = uniqueId,
            title = title,
            category = "My Studio",
            imageUrl = _studioImageUrl.value,
            resolution = "Custom 4K Creator",
            isPremium = false,
            author = "Prince AR (Studio Creator)"
        )
        _customWallpapers.value = _customWallpapers.value + newWallpaper
        // Automatically add to favorited Room database
        viewModelScope.launch {
            repository.addFavorite(newWallpaper)
        }
    }

    // Ad Trigger Simulations
    fun triggerInterstitialAd(onAdDismissed: () -> Unit) {
        if (_isPremiumUser.value) {
            onAdDismissed()
            return
        }
        viewModelScope.launch {
            _adStatus.value = "interstitial_loading"
            kotlinx.coroutines.delay(1000)
            _adStatus.value = "interstitial_shown"
            kotlinx.coroutines.delay(3000) // countdown simulation
            _adStatus.value = null
            onAdDismissed()
        }
    }

    fun triggerRewardAd(onRewardGranted: () -> Unit) {
        viewModelScope.launch {
            _adStatus.value = "reward_shown"
            kotlinx.coroutines.delay(3500) // Reward watching simulation
            _adStatus.value = null
            _unlockedPremiumFeaturesCount.value = _unlockedPremiumFeaturesCount.value + 1
            onRewardGranted()
        }
    }

    fun dismissAd() {
        _adStatus.value = null
    }

    // Backup & Restore Simulation
    fun backupFavorites(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val count = favorites.value.size
            kotlinx.coroutines.delay(1500)
            onSuccess("Successfully backed up $count wallpapers to Google Drive / NexVora Cloud!")
        }
    }

    fun restoreFavorites(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            // Seed a few favorites on restore
            val seedList = WallpaperData.wallpapers.take(4)
            for (wp in seedList) {
                if (!favorites.value.any { it.id == wp.id }) {
                    repository.addFavorite(wp)
                }
            }
            kotlinx.coroutines.delay(1500)
            onSuccess("Successfully restored wallpapers backup from Cloud Sync!")
        }
    }
}
