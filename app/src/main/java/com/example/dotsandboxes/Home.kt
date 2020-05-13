package com.example.dotsandboxes

import android.content.res.Resources
import kotlin.math.pow

class Home {
    var y = (Resources.getSystem().displayMetrics.heightPixels-200).toFloat()
    var x = (Resources.getSystem().displayMetrics.widthPixels/2-125).toFloat()

    fun dist(touchX:Float, touchY:Float): Boolean {
        var dist = ((x-touchX).pow(2) + (y-touchY).pow(2)).pow(0.5f)
        return dist < 200
    }
}