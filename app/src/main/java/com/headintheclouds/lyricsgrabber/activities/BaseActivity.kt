package com.headintheclouds.lyricsgrabber.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    protected fun getActivityContext(): Context {
        return this
    }
}