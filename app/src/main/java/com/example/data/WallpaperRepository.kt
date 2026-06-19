package com.example.data

import kotlinx.coroutines.flow.Flow

class WallpaperRepository(private val wallpaperDao: WallpaperDao) {
    val allFavorites: Flow<List<FavoriteWallpaper>> = wallpaperDao.getAllFavorites()

    suspend fun addFavorite(wallpaper: Wallpaper) {
        val favorite = FavoriteWallpaper(
            id = wallpaper.id,
            title = wallpaper.title,
            category = wallpaper.category,
            imageUrl = wallpaper.imageUrl,
            isAnimated = wallpaper.isAnimated,
            isPremium = wallpaper.isPremium,
            resolution = wallpaper.resolution
        )
        wallpaperDao.insertFavorite(favorite)
    }

    suspend fun removeFavorite(id: String) {
        wallpaperDao.deleteById(id)
    }

    fun getIsFavorite(id: String): Flow<Boolean> {
        return wallpaperDao.isFavorite(id)
    }
}
