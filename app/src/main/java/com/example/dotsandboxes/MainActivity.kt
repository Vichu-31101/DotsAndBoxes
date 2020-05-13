package com.example.dotsandboxes

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var playerNum = intent.getIntExtra("playerNum",2)
        var gridSize = intent.getIntExtra("gridSize",4)
        var playerLevel = intent.getIntExtra("playerHardness",0)

        var mediaPlayer: MediaPlayer? = MediaPlayer.create(applicationContext, R.raw.sound)
        setContentView(Game(this,playerNum,gridSize,playerLevel,applicationContext))

    }
}
