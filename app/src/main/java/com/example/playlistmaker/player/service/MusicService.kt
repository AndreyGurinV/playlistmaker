package com.example.playlistmaker.player.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.domain.AudioPlayerControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class MusicService : Service(), AudioPlayerControl {

    private val binder = MusicServiceBinder()
    private var player: MediaPlayer? = null
    private var songUrl = ""
    private var notificationText = "---------------------------"

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerState1 = _playerState.asStateFlow()

    private var timerJob: Job? = null

    override fun onBind(p0: Intent?): IBinder? {
        songUrl = p0?.getStringExtra("song_url") ?: ""
        notificationText = "${p0?.getStringExtra("artist_name") ?: ""} - ${p0?.getStringExtra("track_name") ?: ""}"
        preparePlayer()

        return binder
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        createNotificationChannel()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun preparePlayer() {
        if (songUrl.isEmpty()) return

        player?.setDataSource(songUrl)
        player?.prepareAsync()
        player?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }
        player?.setOnCompletionListener {
            _playerState.value = PlayerState.Prepared()
        }
    }

    override fun getPlayerState(): StateFlow<PlayerState> = playerState1

    override fun startPlayer() {
        player?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        timerJob?.cancel()
        player?.pause()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun playbackControl() {
        if (player?.isPlaying == true)
            pausePlayer()
        else
            startPlayer()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun showNotification() {
        if (player?.isPlaying == true)
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                getForegroundServiceTypeConstant()
            )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(player?.currentPosition ?: 0) ?: "00:00"
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (player?.isPlaying == true) {
                delay(300L)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
            hideNotification()
            _playerState.value = PlayerState.Prepared()
        }
    }

    private fun releasePlayer() {
        player?.stop()
        player?.setOnPreparedListener(null)
        player?.setOnCompletionListener(null)
        player?.release()
        player = null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Playlist Maker service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Playlist Maker")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(true)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel_"
        const val SERVICE_NOTIFICATION_ID = 100
    }
}