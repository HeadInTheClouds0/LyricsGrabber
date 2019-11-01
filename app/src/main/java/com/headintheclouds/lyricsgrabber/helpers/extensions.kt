package com.headintheclouds.lyricsgrabber.helpers

fun String.setArtist(artist: String): String =  this.replace("{{artist}}", artist)
fun String.setTrack(track: String): String =  this.replace("{{track}}", track)
fun String.replaceBreaks(): String =  this.replace("<br>", System.getProperty("line.separator")!!)

