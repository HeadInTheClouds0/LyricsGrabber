package com.headintheclouds.lyricsgrabber.network

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.text.Html
import com.headintheclouds.lyricsgrabber.helpers.replaceBreaks
import com.headintheclouds.lyricsgrabber.helpers.setArtist
import com.headintheclouds.lyricsgrabber.helpers.setTrack
import com.headintheclouds.lyricsgrabber.models.AppDatabase
import com.headintheclouds.lyricsgrabber.models.Song
import org.jsoup.Jsoup
import java.lang.Exception
import java.security.AccessControlContext

class LyricsDownloader {
    companion object {
        private val TAG = LyricsDownloader::class.java.name
    }

    private val backgroundHandlerThread: HandlerThread
    private val backgroundHandler: Handler

    val urls: MutableList<String> = mutableListOf(
        "http://lyrics.wikia.com/wiki/{{artist}}:{{track}}"
    )
    val years: MutableList<Int> = mutableListOf()

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
            var res = ""  //: String? = null
            var errorString: String? = null

            if (song != null && !song.lyrics.isNullOrEmpty()) {
                song.lyrics.let { s -> res = s!! }
            } else {
                urls.forEach {
                    try {
//        var url = "https://www.azlyrics.com/lyrics/{{artist}}/{{track}}.html"
//        url = url.replace("{{artist}}", artist.replace(" ", "")).replace("{{track}}", track.replace(" ", ""))
                        var url = it
                        url = url.setArtist(artist).setTrack(cleanTrackName(track)).replace(" ", "_")
                        val connect = Jsoup.connect(url)
//                    if(connect.response().statusCode() == 200) {
                        val doc = connect.get()
                        val body = doc.body()
                        val content = body.getElementById("WikiaPage")
                        val lyricBox = content.getElementsByClass("lyricbox").first()

                        res = lyricBox.toString() //.replaceBreaks()
//                    }
                    } catch (e: Exception) {
                        errorString = e.toString()
                    }
                }
            }

            res = Html.fromHtml(
                res.replace("\n", "<br>"),
                Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
            ).toString()

            if (song == null) {
                song = Song(0, artist, track, res)
                songDao.insertAll(song)
            } else {
                song.artist = artist; song.track = track; song.lyrics = res
                songDao.update(song)
            }

            function(song, errorString)
        }
    }

    private fun cleanTrackName(track: String): String {
        var res = track
        years.forEach {
            res = res.replace(" - $it Remaster", "", true)
            res = res.replace(" - $it Version", "", true)
        }
        res = res.replace(" - " + "Remastered Version", "", true)
        res = res.replace(" - " + "Remastered", "", true)

//        Sharp Dressed Man - 2008 Remaster

        return res
    }
}
