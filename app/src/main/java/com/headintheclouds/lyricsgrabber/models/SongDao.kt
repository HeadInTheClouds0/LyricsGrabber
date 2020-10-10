package com.headintheclouds.lyricsgrabber.models

import androidx.annotation.WorkerThread
import androidx.room.*


@Dao
interface SongDao {
    @WorkerThread
    @Query("SELECT * FROM Song ORDER BY SongArtist, SongTrack")
    fun getAll(): List<Song>

//    @WorkerThread
//    @Query("SELECT SongArtist FROM Song GROUP BY SongArtist ORDER BY SongArtist")
//    fun getAllSongArtists() : List<String>

    @WorkerThread
    @Query("SELECT * FROM song WHERE SongArtist LIKE :artist AND SongTrack LIKE :track ")
    fun findSong(artist: String, track: String): Song?

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg songs: Song)

//    @WorkerThread
//    @Delete
//    fun delete(user: Song)

    @WorkerThread
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(song: Song)

//    @WorkerThread
//    @Query("SELECT * FROM song WHERE SongArtist LIKE :searchTerm OR SongTrack LIKE :searchTerm ORDER BY SongArtist, SongTrack")
//    fun searchSong(searchTerm :String): LiveData<List<Song>>
}