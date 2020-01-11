package com.headintheclouds.lyricsgrabber.network

import android.text.Html
import com.headintheclouds.lyricsgrabber.helpers.setArtist
import com.headintheclouds.lyricsgrabber.helpers.setTrack
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

abstract class LyricSourceBase {
    companion object {
        const val artistPlaceHolder: String = "{{artist}}"
        const val titlePlaceHolder: String = "{{track}}"
    }
    abstract val sourceUrl: String

    private val years: MutableList<Int> = mutableListOf()

    init {
        for (i in 0..20) {
            years.add(2000 + i)
        }
    }

    fun downloadLyrics(songTitle: String, songArtist: String): String {
        val connect =
            Jsoup.connect(sourceUrl.setArtist(songArtist).setTrack(cleanTrackName(songTitle)))
        val doc = connect.get()
        val body = doc.body()
        var lyricsRes = getLyricsFromBody(body)

        if (lyricsRes.isNotEmpty()) {
            lyricsRes = Html.fromHtml(
                lyricsRes.replace("\n", "<br>"),
                Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
            ).toString()
        }

        return lyricsRes
    }

    abstract fun getLyricsFromBody(body: Element): String

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