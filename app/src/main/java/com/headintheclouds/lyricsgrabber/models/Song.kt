package com.headintheclouds.lyricsgrabber.models

import androidx.room.*

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "SongArtist") var artist: String?,
    @ColumnInfo(name = "SongTrack") var track: String?,
    @ColumnInfo(name = "SongLyrics") var lyrics: String?
)

