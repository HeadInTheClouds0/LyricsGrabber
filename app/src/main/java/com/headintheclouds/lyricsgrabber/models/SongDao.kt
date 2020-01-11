package com.headintheclouds.lyricsgrabber.models

import androidx.room.*


@Dao
interface SongDao {
    @Query("SELECT * FROM Song")
    fun getAll(): List<Song>

    @Query("SELECT * FROM song WHERE SongArtist LIKE :artist AND SongTrack LIKE :track ")
    fun findSong(artist: String, track: String): Song?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg songs: Song)

    @Delete
    fun delete(user: Song)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(song: Song)
}