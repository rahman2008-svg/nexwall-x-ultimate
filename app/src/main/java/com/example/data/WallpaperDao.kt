package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {
    @Query("SELECT * FROM favorite_wallpapers ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteWallpaper>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteWallpaper)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteWallpaper)

    @Query("DELETE FROM favorite_wallpapers WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wallpapers WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>
}
