package com.headintheclouds.lyricsgrabber.helpers

import com.headintheclouds.lyricsgrabber.network.LyricSourceBase

fun String.setArtist(artist: String): String =  this.replace(LyricSourceBase.artistPlaceHolder, artist)
fun String.setTrack(track: String): String =  this.replace(LyricSourceBase.titlePlaceHolder, track)
fun String.replaceBreaks(): String =  this.replace("<br>", System.getProperty("line.separator")!!)

