package com.example.dotsandboxes

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mediaPlayer: MediaPlayer? = MediaPlayer.create(applicationContext, R.raw.sound)
        setContentView(Splash(this))

    }
}
