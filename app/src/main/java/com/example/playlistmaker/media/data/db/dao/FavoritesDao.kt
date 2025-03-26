package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.FavoritesEntity

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoritesEntity)
    @Delete
    suspend fun deleteTrack(track: FavoritesEntity)
    @Query("SELECT * FROM favorites_table")
    suspend fun getTracks(): List<FavoritesEntity>
    @Query("SELECT id FROM favorites_table")
    suspend fun getTracksIds(): List<String>
}