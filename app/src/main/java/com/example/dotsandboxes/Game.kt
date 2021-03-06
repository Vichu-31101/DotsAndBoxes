package com.example.dotsandboxes

import android.R
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Build
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi


class Game(context: Context,playerNumber:Int,gridSize:Int,playerLevel:Int,musicContext: Context) : View(context) {

    val colors = Colors()
    val pPoint :Paint
    val pLine :Paint
    val tLine :Paint
    val playerP :Paint
    val bgPaint :Paint
    var touchX = 0f
    var touchY = 0f
    var points = arrayListOf<Point>()
    var pointsPos: MutableList<MutableList<Point>> = ArrayList()
    var tline = arrayListOf<Point>()
    var lines = arrayListOf<Lines>()
    var num = gridSize
    var space = Resources.getSystem().displayMetrics.widthPixels
    //Player
    var pointsDisp = pointsDisp()
    var playerCol = arrayListOf<Int>(colors.green, colors.red, colors.blue, colors.yellow)
    var players = arrayListOf<Player>()
    var playerNum = playerNumber
    var playerController = 0
    var played = false
    //Computer
    // var computer = Computer()
    var comp = false
    var playerHardness = playerLevel
    //
    var boxes = 0
    var newBoxes = 0
    //
    var gameOver = false
    //
    var undo = Undo()
    var home = Home()
    //
    var vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    var mediaPlayer: MediaPlayer? = MediaPlayer.create(musicContext, com.example.dotsandboxes.R.raw.sound)


  

    init{


        playerP = Paint()
        pPoint = Paint()
        pLine = Paint()
        tLine = Paint()
        bgPaint = Paint()
        //
        paintinit(pPoint)
        paintinit(pLine)
        paintinit(tLine)
        paintinit(playerP)
        paintinit(bgPaint)
        //
        pLine.strokeWidth = 15f
        tLine.strokeWidth = 10f
        playerP.strokeWidth = 20f
        playerP.textSize = 100f

        //
        initPoints(points,num,pointsPos)
        space /= num
        //
        if (playerNum == 1){
            comp = true
            playerNum += 1
        }
        for (i in (0 until playerNum)){
            var tplayer = Player()
            tplayer.color = playerCol[i]
            players.add(tplayer)
        }

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.drawColor(colors.grey)

        gameDone()
        if(gameOver)
        {

            @Suppress("DEPRECATION")
            vibrator.vibrate(400)
            var scoreList = players.sortedByDescending{ it.score }
            if(scoreList[0].score == scoreList[1].score)
            {
                pPoint.textSize = 80f
                canvas?.drawText(
                    "DRAW",
                    width/2.toFloat()-100,
                    height-width/4.toFloat(),
                    pPoint
                )
            }
            else{
                canvas?.drawColor(scoreList[0].color)
            }
            pPoint.textSize = 80f
            canvas?.drawText(
                "HOME",
                home.x,
                home.y,
                pPoint
            )

        }
        else{
            pPoint.textSize = 40f
            canvas?.drawText(
                "UNDO",
                undo.x,
                undo.y,
                pPoint
            )
        }



        var p = playerNum+1
        bgPaint.color = colors.grey
        canvas?.drawRoundRect(
            0f,
            (pointsPos[0][0].y/2+20)-100,
            (width).toFloat(),
            (pointsPos[0][0].y/2+20) +20,
            20f,
            20f,
            bgPaint
        )

        canvas?.drawRoundRect(
            pointsPos[0][0].x-100,
            pointsPos[0][0].y-100,
            pointsPos[num-2][num-2].x+100,
            pointsPos[num-2][num-2].y+100,
            20f,
            20f,
            bgPaint
        )

        var j = 1
        if(playerNum > 0 && j <= playerNum){
            var p = playerNum+1
            for(i in players){
                playerP.color = i.color
                canvas?.drawText(
                    i.score.toString(),
                    (((width/p) * j)-20).toFloat(),
                    (pointsPos[0][0].y/2+20),
                    playerP
                )
                j += 1

            }

        }

        canvas?.drawCircle(
            (((width/p) * (playerController+1))).toFloat()+6,
            (pointsPos[0][0].y/2+20)+40,
            10f,
            pPoint
        )

        if(pointsDisp.pointsDPS.size > 0){
            var k = 0
            for(i in pointsDisp.pointsDPS){
                playerP.color = players[pointsDisp.playerNo[k]].color
                canvas?.drawCircle(
                    i[0]+(space/2),
                    i[1]+(space/2),
                    50f,
                    playerP
                )
                k += 1
            }
        }
        if(tline.size>0){
            tLine.color = playerCol[playerController]
            canvas?.drawLine(
                tline[0].x,
                tline[0].y,
                tline[1].x,
                tline[1].y,
                tLine
            )
        }
        if(lines.size > 0) {
            for (i in lines) {
                playerP.color = i.col

                canvas?.drawLine(
                    i.fromX,
                    i.fromY,
                    i.toX,
                    i.toY,
                    playerP
                )
            }
        }
        if(points.size > 0){
            for(i in points) {
                canvas?.drawCircle(
                    i.x,
                    i.y,
                    15f,
                    pPoint
                )
            }
        }


    }


    //Game
    fun game(){
        if (!played) {
            mediaPlayer?.start()
            boxes = checkboxes(pointsPos, lines, pointsDisp.pointsDPS)
            newBoxes = boxes - newBoxes
            for (i in (1..newBoxes)) {
                pointsDisp.playerNo.add(playerController)
            }
            players[playerController].score += newBoxes
            if (newBoxes == 0) {
                playerController += 1
            }
            newBoxes = boxes
        }
    }
    fun gameDone(){
        if (boxes == (num-2)*(num-2)){
            gameOver = true
        }
    }

    @ExperimentalStdlibApi
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        touchX = event!!.x
        touchY = event!!.y
        dist(touchX, touchY, points)
        tline = sort(points)
        var eve = event?.action
        var undoC: Boolean = undo.dist(touchX,touchY)
        var homeC: Boolean = home.dist(touchX,touchY)
        if(undoC && eve == MotionEvent.ACTION_UP && !gameOver){
            tline.clear()
            if(lines.size>0){
                var oBoxes = checkboxes(pointsPos, lines, pointsDisp.pointsDPS)
                lines.removeLast()
                var nBoxes = checkboxes(pointsPos, lines, pointsDisp.pointsDPS)
                while(oBoxes != nBoxes){
                    pointsDisp.pointsDPS.removeLast()
                    players[pointsDisp.playerNo.last()].score -= 1
                    pointsDisp.playerNo.removeLast()
                    newBoxes =  checkboxes(pointsPos, lines, pointsDisp.pointsDPS)
                    oBoxes -= 1
                }
                if(playerController > 0){
                    playerController -= 1
                }
                else{
                    playerController = playerNum-1
                }
            }
        }
        else if(homeC && eve == MotionEvent.ACTION_UP && gameOver){
            Log.d("hey","hey")
            val intent = Intent(context, StartScreen::class.java)
            context.startActivity(intent)
        }
        else if(tline[0].dist < 300 ){
            if(eve == MotionEvent.ACTION_UP){

                    if(comp) {
                        if (playerController == 0) {
                            dist(touchX, touchY, points)
                            tline = sort(points)
                            played = line(tline, lines, players[playerController])
                            game()
                        }
                        postInvalidate()
                        while(playerController == 1 && boxes < (num-2)*(num-2)){
                            played = true
                            while (played){
                                if(playerHardness == 1){
                                    tline = eCompMove(pointsPos)
                                }
                                else if(playerHardness == 2){
                                    tline = mCompMove(pointsPos,lines)
                                }
                                else if(playerHardness == 3){
                                    tline = hCompMove(pointsPos,lines)
                                }
                                played = line(tline, lines, players[playerController])
                            }
                            game()
                        }

                    }
                    else{
                        dist(touchX, touchY, points)
                        tline = sort(points)
                        played = line(tline,lines,players[playerController])
                        game()
                    }
                    if(playerController >= playerNum){
                        playerController = 0
                    }
                }


        }
        else{
            tline.clear()
        }

        invalidate()


        return true
    }

}