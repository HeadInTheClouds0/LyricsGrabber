package com.headintheclouds.lyricsgrabber.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.headintheclouds.lyricsgrabber.R
import com.headintheclouds.lyricsgrabber.models.Song
import kotlinx.android.synthetic.main.lyrics_list_item.view.*
import java.lang.ref.WeakReference

class LyricsListViewAdapter(
    private var songs: List<Song>,
    activityContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: WeakReference<Context> = WeakReference<Context>( activityContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LyricsViewHolder(LayoutInflater.from(context.get()).inflate(R.layout.lyrics_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LyricsViewHolder).title.text = songs[position].track
    }
}

class LyricsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.lyricListItemName
}
