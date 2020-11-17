package com.headintheclouds.lyricsgrabber.network

import com.headintheclouds.lyricsgrabber.helpers.removeSigns
import com.headintheclouds.lyricsgrabber.helpers.removeSpace
import com.headintheclouds.lyricsgrabber.helpers.setArtist
import com.headintheclouds.lyricsgrabber.helpers.setTrack
import org.jsoup.nodes.Element

class LyricSourceAZLyrics : LyricSourceBase() {
    override val sourceUrl: String
        get() = "https://www.azlyrics.com/lyrics/${artistPlaceHolder}/${titlePlaceHolder}.html"

    override fun sourceURLFor(artist: String, track: String): String {
        return sourceUrl.setArtist(artist.removeSpace().removeSigns()).setTrack(track.removeSpace().removeSigns()).toLowerCase()
    }
    override fun getLyricsFromBody(body: Element): String {
        val ringtone = body.getElementsByClass("ringtone")?.first()
        val parent = ringtone?.parent()
        val child = parent?.child(7)

        return child?.toString() ?: ""
    }
}