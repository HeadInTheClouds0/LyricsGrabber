package com.headintheclouds.lyricsgrabber

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import com.headintheclouds.lyricsgrabber.models.AppDatabase

class AppLyricsGrabber : Application() {
    override fun onCreate() {
        super.onCreate()
//        createNotificationChannel()

//        val database = Room.databaseBuilder(this, AppDatabase::class.java, "LyricsDB").build()
    }

    companion object {
        const val CHANNEL_ID = "SpotifyReceiverChannelID"
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}