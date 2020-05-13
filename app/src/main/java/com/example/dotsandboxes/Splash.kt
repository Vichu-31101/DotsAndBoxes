package com.example.dotsandboxes

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity

class Splash(context: Context):View(context) {
    var colors = Colors()
    var lineP = Paint()
    var pointP = Paint()
    var wid = Resources.getSystem().displayMetrics.widthPixels
    var hei = Resources.getSystem().displayMetrics.heightPixels
    var start = false
    var space = 100
    var x1 = 0
    var x2 = 0
    init {
        lineP.strokeWidth = 20f
        pointP.color = Color.WHITE
        pointP.strokeWidth = 10f
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(!start){
            timer.start()
            start=true
        }

        canvas?.drawColor(colors.grey)

        //line1
        lineP.color=colors.green
        canvas?.drawLine(
            width/2 - space.toFloat()+x2,
            height/2 - space.toFloat(),
            width/2 - space.toFloat()+x1,
            height/2 - space.toFloat(),
            lineP
        )

        //line2
        lineP.color = colors.red
        canvas?.drawLine(
            width/2 + space.toFloat(),
            height/2 - space.toFloat()+x1,
            width/2 + space.toFloat(),
            height/2 - space.toFloat()+x2,
            lineP
        )

        //line3
        lineP.color = colors.blue
        canvas?.drawLine(
            width/2 + space.toFloat()-x1,
            height/2 + space.toFloat(),
            width/2 + space.toFloat()-x2,
            height/2 + space.toFloat(),
            lineP
        )

        //line3
        lineP.color = colors.yellow
        canvas?.drawLine(
            width/2 - space.toFloat(),
            height/2 + space.toFloat()-x1,
            width/2 - space.toFloat(),
            height/2 + space.toFloat()-x2,
            lineP
        )


        //Points
        canvas?.drawCircle(
            width/2 - space.toFloat(),
            height/2 - space.toFloat(),
            20f,
            pointP
        )
        canvas?.drawCircle(
            width/2 + space.toFloat(),
            height/2 - space.toFloat(),
            20f,
            pointP
        )
        canvas?.drawCircle(
            width/2 - space.toFloat(),
            height/2 + space.toFloat(),
            20f,
            pointP
        )
        canvas?.drawCircle(
            width/2 + space.toFloat(),
            height/2 + space.toFloat(),
            20f,
            pointP
        )



    }
    var timer = object: CountDownTimer(5015, 1) {
        override fun onFinish() {
            val intent = Intent(context, StartScreen::class.java)
            context.startActivity(intent)
        }

        override fun onTick(millisUntilFinished: Long) {
            invalidate()
            if(x1 < space*2){
                x1 += 5
            }
            else if(x2 < space*2){
                x2 += 5
            }
            else{
                x1=0
                x2=0
            }




        }
    }
}