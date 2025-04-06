package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.TracksEntity

@Dao
interface TracksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackForPlaylist(track: TracksEntity)

    @Query("SELECT * FROM tracks_table")
    suspend fun getTracks(): List<TracksEntity>

    @Query("DELETE FROM tracks_table where id = :trackId")
    suspend fun removeTrack(trackId: Int)
}