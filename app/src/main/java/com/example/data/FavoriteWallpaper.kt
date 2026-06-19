package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_wallpapers")
data class FavoriteWallpaper(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val imageUrl: String,
    val isAnimated: Boolean,
    val isPremium: Boolean,
    val resolution: String,
    val timestamp: Long = System.currentTimeMillis()
)
