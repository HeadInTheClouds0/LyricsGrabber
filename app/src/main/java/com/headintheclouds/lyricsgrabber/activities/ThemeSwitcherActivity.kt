package com.headintheclouds.lyricsgrabber.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.headintheclouds.lyricsgrabber.R

import kotlinx.android.synthetic.main.activity_theme_switcher.*

class ThemeSwitcherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_switcher)
        setSupportActionBar(toolbar)
    }
}
