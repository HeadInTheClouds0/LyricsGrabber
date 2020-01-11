package com.headintheclouds.lyricsgrabber.network

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.headintheclouds.lyricsgrabber.models.AppDatabase
import com.headintheclouds.lyricsgrabber.models.Song
import java.lang.Exception

class LyricsDownloader {
    companion object {
        private val TAG = LyricsDownloader::class.java.name
    }

    private val backgroundHandlerThread: HandlerThread
    private val backgroundHandler: Handler

    private val lyricSources: MutableList<LyricSourceBase> = mutableListOf(
        LyricSourceWikia()
    )

    private val years: MutableList<Int> = mutableListOf()

    init {
        backgroundHandlerThread = HandlerThread(TAG)
        backgroundHandlerThread.start()

        backgroundHandler = Handler(backgroundHandlerThread.looper)
        for (i in 0..20) {
            years.add(2000 + i)
        }
    }

    fun downloadLyrics(
        context: Context,
        artist: String,
        track: String,
        function: (song: Song?, error: String?) -> Unit
    ) {
        backgroundHandler.post {
            val db = AppDatabase.getInstance(context)
            val songDao = db.songDao()

            var song = songDao.findSong(artist, track)
            var errorString: String? = null

            if (song == null) {
                lyricSources.forEach {
                    try {
                        val lyrics = it.downloadLyrics(track, artist)
                        if (lyrics.isNotEmpty()){
                            song = Song(0, artist, track, lyrics)
                            songDao.insertAll(song!!)
                        }
                    } catch (e:Exception) {
                        errorString = e.toString()
                    }
                }
            }

            function(song, errorString)
        }
    }
}
