package com.example.dotsandboxes

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_start_screen.*
import kotlinx.android.synthetic.main.grid_popup.*
import kotlinx.android.synthetic.main.player_hardness.*
import kotlinx.android.synthetic.main.player_popup.*

class StartScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var playNum = 0
        var gridSize = 0
        var playerHardness = 0
        setContentView(R.layout.activity_start_screen)
        //Init
        var playerPopup = Dialog(this)
        var gridPopup = Dialog(this)
        var playerLevel = Dialog(this)
        playerPopup.setContentView(R.layout.player_popup)
        gridPopup.setContentView(R.layout.grid_popup)
        playerLevel.setContentView(R.layout.player_hardness)
        //Buttons
        button.setOnClickListener {

            playerPopup.show()
        }
        button2.setOnClickListener {
            playNum = 1
            playerLevel.show()
        }
        //Player Number
        playerPopup.player2.setOnClickListener {
            playNum = 2
            gridPopup.show()
        }
        playerPopup.player3.setOnClickListener {
            playNum = 3
            gridPopup.show()
        }
        playerPopup.player4.setOnClickListener {
            playNum = 4
            gridPopup.show()
        }
        //Player Level
        playerLevel.easy.setOnClickListener {
            playerHardness = 1
            gridPopup.show()
        }
        playerLevel.medium.setOnClickListener {
            playerHardness = 2
            gridPopup.show()
        }
        playerLevel.hard.setOnClickListener {
            playerHardness = 3
            gridPopup.show()
        }
        //Grid Number
        val gridButtons = arrayListOf<Button>(gridPopup.by4, gridPopup.by5, gridPopup.by6, gridPopup.by7)
        for(i in gridButtons){
            i.setOnClickListener {
                gridSize = gridButtons.indexOf(i) + 5
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("playerNum",playNum)
                intent.putExtra("gridSize", gridSize)
                if(playNum == 1)
                    intent.putExtra("playerHardness",playerHardness)
                startActivity(intent)
            }
        }
    }


}
