package com.headintheclouds.lyricsgrabber.receivers

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class BroadCastReceiverService: Service() {
    private val spotifyReceiver: SpotifyReceiver =
        SpotifyReceiver()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.spotify.music.metadatachanged")
        registerReceiver(spotifyReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(spotifyReceiver)
    }
}