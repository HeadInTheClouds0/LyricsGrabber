package com.headintheclouds.lyricsgrabber.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SpotifyReceiver : BroadcastReceiver() {
    interface SongChangedCallback {
        fun newTrack(artist: String, track: String)
    }

    interface PlaybackStateChangedCallback {
        fun stateChanged(isPlaying: Boolean)
    }

    private var songChangedCallback: SongChangedCallback? = null
    private var playbackStateChangedCallback: PlaybackStateChangedCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("SpotifyReceiver", "Ente")

        if (intent?.action == "com.spotify.music.metadatachanged") {
            if (context != null) {
//                val builder = NotificationCompat.Builder(context, AppLyricsGrabber.CHANNEL_ID)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(intent.getStringExtra("artist"))
//                    .setContentText(intent.getStringExtra("track"))
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setAutoCancel(true)
//
//                with(NotificationManagerCompat.from(context)){
//                    notify(0, builder.build() )
//                }

                songChangedCallback?.newTrack(
                    intent.getStringExtra("artist")!!,
                    intent.getStringExtra("track")!!
                )
            }
        } else if (intent?.action == "com.spotify.music.playbackstatechanged") {
            playbackStateChangedCallback?.stateChanged(intent.getBooleanExtra("playing", false))
        }
    }

    fun setSongChangedCallback(songChangedCallback: SongChangedCallback) {
        this.songChangedCallback = songChangedCallback
    }
    fun setPlaybackStateChangedCallback(playbackStateChangedCallback: PlaybackStateChangedCallback) {
        this.playbackStateChangedCallback = playbackStateChangedCallback
    }
}