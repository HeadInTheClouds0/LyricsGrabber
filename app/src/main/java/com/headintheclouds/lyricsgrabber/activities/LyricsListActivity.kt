package com.headintheclouds.lyricsgrabber.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.headintheclouds.lyricsgrabber.R
import com.headintheclouds.lyricsgrabber.helpers.LyricsListViewAdapter
import com.headintheclouds.lyricsgrabber.models.AppDatabase
import com.headintheclouds.lyricsgrabber.models.Song
import kotlinx.android.synthetic.main.activity_lyrics_list.*
import kotlinx.coroutines.*

class LyricsListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics_list)

        val db = AppDatabase.getInstance(getActivityContext())
        val songDao = db.songDao()

        val arrayList = ArrayList<Song>()
//        var i = 0
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
//        arrayList.add(Song(++i, "asd " + i, "dsa " + i, "asdasdasd " +i))
        LyricsListView.layoutManager = LinearLayoutManager(getActivityContext())
        LyricsListView.adapter = LyricsListViewAdapter(arrayList, getActivityContext())
        GlobalScope.launch {
            val songs = songDao.getAll()
            LyricsListView.adapter = LyricsListViewAdapter(songs, getActivityContext())
            (LyricsListView.adapter as LyricsListViewAdapter).notifyDataSetChanged()
        }
    }
}
