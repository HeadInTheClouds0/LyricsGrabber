package com.headintheclouds.lyricsgrabber.network

import org.jsoup.nodes.Element

class LyricSourceWikia: LyricSourceBase() {
    override val sourceUrl: String
        get() = "http://lyrics.wikia.com/wiki/$artistPlaceHolder:$titlePlaceHolder"

    override fun getLyricsFromBody(body: Element): String {
        val content = body.getElementById("WikiaPage")
        val lyricBox = content?.getElementsByClass("lyricbox")?.first()

        return lyricBox.toString()
    }
}