package com.headintheclouds.lyricsgrabber.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.headintheclouds.lyricsgrabber.R
import com.headintheclouds.lyricsgrabber.helpers.setArtist
import com.headintheclouds.lyricsgrabber.helpers.setTrack
import com.headintheclouds.lyricsgrabber.models.AppDatabase
import com.headintheclouds.lyricsgrabber.models.Song
import com.headintheclouds.lyricsgrabber.network.LyricsDownloader
import com.headintheclouds.lyricsgrabber.receivers.SpotifyReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : LyricsActivity(), SpotifyReceiver.PlaybackStateChangedCallback,
    SpotifyReceiver.SongChangedCallback {

    private var lastState: Boolean = false
    private val spotifyReceiver: SpotifyReceiver = SpotifyReceiver()
    private val lyricsDownloader: LyricsDownloader = LyricsDownloader()
    private var mSong: Song? = null

    companion object {
        private val TAG = MainActivity::class.java.name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val onCreateOptionsMenu = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return onCreateOptionsMenu
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mainEditLyric) {
            showAddItemDialog(getActivityContext(), mSong)
        } else if (item?.itemId == R.id.mainSearchButton && mSong != null) {
            launchBrowserWithSearch(mSong!!)
        }
        return super.onOptionsItemSelected(item!!)
    }

    private fun saveSong(song: Song) {
        val db = AppDatabase.getInstance(getActivityContext())
        val songDao = db.songDao()
        if (song.uid == 0) songDao.insertAll(song)
        else songDao.update(song)
    }

    private fun showAddItemDialog(c: Context, song: Song?) {
        if (song != null) {
            val taskEditText = EditText(c)
            taskEditText.setText(song.lyrics)
            val dialog = AlertDialog.Builder(c)
                .setView(taskEditText)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    song.lyrics = taskEditText.text.toString()
                    mainLyricsTextView.text = song.lyrics

                    GlobalScope.launch {
                        saveSong(song)
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            dialog.show()
        }
    }

    private fun launchBrowserWithSearch(song: Song) {
        val url = "https://www.google.de/search?q={{artist}}+{{track}}+lyrics"
        val intent = Intent(Intent.ACTION_VIEW).setData(
            Uri.parse(
                url.setArtist(song.artist!!).setTrack(song.track!!).replace(" ", "+")
            )
        )
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainActivityToolBar)

        mainProgressBar.post { mainProgressBar.show() }
        spotifyReceiver.setPlaybackStateChangedCallback(this)
        spotifyReceiver.setSongChangedCallback(this)
        setTitleSubTitle(getString(R.string.track), getString(R.string.artist))

        nav_view_main.setNavigationItemSelectedListener {
            if (it.itemId == R.id.menuItemThemes) {
                drawer_layout_main.closeDrawers()
                return@setNavigationItemSelectedListener true
            } else if (it.itemId == R.id.menuItemLyrics) {
                startActivity(Intent(this, LyricsListActivity::class.java))
            }
            return@setNavigationItemSelectedListener false
        }
    }

    private fun setTitleSubTitle(track: String, artist: String) {
        runOnUiThread {
            supportActionBar?.title = track
            supportActionBar?.subtitle = artist
        }
    }

    override fun onStart() {
        super.onStart()

        val intentFilter = IntentFilter()
        intentFilter.addAction("com.spotify.music.metadatachanged")
        intentFilter.addAction("com.spotify.music.playbackstatechanged")
        registerReceiver(spotifyReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(spotifyReceiver)
    }


    private fun sendMediaEvent(keycode: Int) {
        val systemService =
            getActivityContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val eventTime = SystemClock.uptimeMillis()
        systemService.dispatchMediaKeyEvent(
            KeyEvent(
                eventTime,
                eventTime,
                KeyEvent.ACTION_DOWN,
                keycode,
                0
            )
        )
        systemService.dispatchMediaKeyEvent(
            KeyEvent(
                eventTime,
                eventTime,
                KeyEvent.ACTION_UP,
                keycode,
                0
            )
        )
    }

    fun playNextSong(view: View) {
        sendMediaEvent(KeyEvent.KEYCODE_MEDIA_NEXT)
    }

    fun playPrevSong(view: View) {
        sendMediaEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS)
    }

    fun pauseSong(view: View) {
        sendMediaEvent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
    }

    override fun stateChanged(isPlaying: Boolean) {
        if (lastState != isPlaying) {
            this.lastState = isPlaying
            mainPauseBtn.post {
                if (isPlaying) {
                    mainPauseBtn.setImageResource(android.R.drawable.ic_media_pause)
                } else {
                    mainPauseBtn.setImageResource(android.R.drawable.ic_media_play)
                }
            }
        }
    }

    override fun newTrack(artist: String, track: String) {
        mainProgressBar.post { mainProgressBar.show() }
        mSong = Song(0, artist, track, "")
        setTitleSubTitle(track, artist)
        mainLyricsTextView.post { mainLyricsTextView.text = "" }
        lyricsDownloader.downloadLyrics(
            getActivityContext(),
            artist,
            track
        ) { song, error ->
            mainProgressBar.post { mainProgressBar.hide() }
            mainLyricsTextView.post {
                if (error != null) mainLyricsTextView.text = error
                else {
                    mainLyricsTextView.text = song?.lyrics
                    mSong = song
                }
            }
        }
    }
}
