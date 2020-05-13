package com.example.dotsandboxes

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import kotlin.concurrent.timer
import kotlin.math.pow

fun dist(x:Float, y:Float, points:ArrayList<Point>){
    for (i in (0 until points.size)){
        points[i].dist = ((x-points[i].x).pow(2) + (y-points[i].y).pow(2)).pow(0.5f)
    }
}

fun sort(points:ArrayList<Point>): ArrayList<Point> {
    var short = ArrayList<Point>()
    for (i in (0 until points.size)) {
        for (j in (0 until points.size)) {
            if (points[i].dist < points[j].dist){
                var temp = points[i]
                points[i] = points [j]
                points[j] = temp
            }
        }
    }
    short.add(points[0])
    short.add(points[1])
    return short
}

fun line(line:ArrayList<Point>, lines:ArrayList<Lines>, player: Player): Boolean {
    var newLine = Lines()
    var exist = false
    if (lines.size > 0){
        for (l in lines){
            if (((l.fromX == line[0].x && l.fromY == line[0].y) && (l.toX == line[1].x && l.toY == line[1].y)) || ((l.fromX == line[1].x && l.fromY == line[1].y) && (l.toX == line[0].x && l.toY == line[0].y))){
                exist = true
            }
        }
    }
    if (!exist) {
        newLine.fromX = line[0].x
        newLine.fromY = line[0].y
        newLine.toX = line[1].x
        newLine.toY = line[1].y
        newLine.col = player.color
        lines.add(newLine)
    }
    return exist
}

fun initPoints(points:ArrayList<Point>, num:Int, pointsPos: MutableList<MutableList<Point>> = ArrayList()){
    var y = Resources.getSystem().displayMetrics.heightPixels
    var x = Resources.getSystem().displayMetrics.widthPixels
    var tpoint = Point()

    for (i in (1 until num)) {
        var pointRow =  arrayListOf<Point>()
        pointRow.clear()
        for (j in (1 until num)) {
            if (x != null) {
                if (y != null) {
                    tpoint = Point()
                    tpoint.x = ((x / num) * j).toFloat()
                    tpoint.y = (((x / num) * i) + (y / 2) - x/3).toFloat()
                    Log.d("pts",tpoint.x.toString()+" "+tpoint.y.toString())
                }
            }
            points.add(tpoint)
            pointRow.add(tpoint)
        }
        pointsPos.add(pointRow)
    }
}

fun checkboxes(pointsPos: MutableList<MutableList<Point>> = ArrayList(), lines: ArrayList<Lines>, pointsDisp: MutableList<MutableList<Float>> = ArrayList()): Int {

    var xBound = pointsPos.size-2
    var yBound = pointsPos.size-2
    var boxes = 0
    var tPointsDisp: MutableList<MutableList<Float>> = ArrayList()
    for (i in (0..yBound)){
        for (j in (0..xBound)){
            var counter = 0
            if (lines.size > 0){
                for (l in lines){
                    if (((pointsPos[i][j].x == l.fromX && pointsPos[i][j].y == l.fromY) && (pointsPos[i][j+1].x == l.toX && pointsPos[i][j+1].y == l.toY)) || ((pointsPos[i][j+1].x == l.fromX && pointsPos[i][j+1].y == l.fromY) && (pointsPos[i][j].x == l.toX && pointsPos[i][j].y == l.toY))){
                        counter += 1
                    }
                    if (((pointsPos[i+1][j+1].x == l.fromX && pointsPos[i+1][j+1].y == l.fromY) && (pointsPos[i][j+1].x == l.toX && pointsPos[i][j+1].y == l.toY)) || ((pointsPos[i][j+1].x == l.fromX && pointsPos[i][j+1].y == l.fromY) && (pointsPos[i+1][j+1].x == l.toX && pointsPos[i+1][j+1].y == l.toY))){
                        counter += 1
                    }
                    if (((pointsPos[i][j].x == l.fromX && pointsPos[i][j].y == l.fromY) && (pointsPos[i+1][j].x == l.toX && pointsPos[i+1][j].y == l.toY)) || ((pointsPos[i+1][j].x == l.fromX && pointsPos[i+1][j].y == l.fromY) && (pointsPos[i][j].x == l.toX && pointsPos[i][j].y == l.toY))){
                        counter += 1
                    }
                    if (((pointsPos[i+1][j+1].x == l.fromX && pointsPos[i+1][j+1].y == l.fromY) && (pointsPos[i+1][j].x == l.toX && pointsPos[i+1][j].y == l.toY)) || ((pointsPos[i+1][j].x == l.fromX && pointsPos[i+1][j].y == l.fromY) && (pointsPos[i+1][j+1].x == l.toX && pointsPos[i+1][j+1].y == l.toY))){
                        counter += 1
                    }
                }
            }
            if(counter == 4){
                boxes += 1
                var tpoint = arrayListOf<Float>()
                tpoint.add(pointsPos[i][j].x)
                tpoint.add(pointsPos[i][j].y)
                tPointsDisp.add(tpoint)
            }
        }
    }
    var exist = false
    for (i in tPointsDisp){
        if(pointsDisp.size > 0){
            exist = false
            for(j in pointsDisp){

                if (j[0] == i[0] && j[1] == i[1]){
                    exist = true
                }
            }
            if (!exist){
                pointsDisp.add(i)
            }
        }
        else{
            pointsDisp.add(i)
        }
    }
    return boxes
}

fun paintinit(paint: Paint){
    paint.isFilterBitmap = true
    paint.isAntiAlias =  true
    paint.color = Color.WHITE
}




@ExperimentalStdlibApi
fun retboxes(pointsPos: MutableList<MutableList<Point>> = ArrayList(), lines: ArrayList<Lines>, newPoint: ArrayList<Point>, orginal:Boolean): Int {
    var xBound = pointsPos.size-2
    var yBound = pointsPos.size-2
    var boxes = 0

    if(!orginal){
        var newline = Lines()
        newline.fromX = newPoint[0].x
        newline.fromY = newPoint[0].y
        newline.toX = newPoint[1].x
        newline.toY = newPoint[1].y
        lines.add(newline)
    }

    for (i in (0..yBound)){
        for (j in (0..xBound)){
            var counter = 0
            if (lines.size > 0){
                for (l in lines){
                    if (((pointsPos[i][j].x == l.fromX && pointsPos[i][j].y == l.fromY) && (pointsPos[i][j+1].x == l.toX && pointsPos[i][j+1].y == l.toY)) || ((pointsPos[i][j+1].x == l.fromX && pointsPos[i][j+1].y == l.fromY) && (pointsPos[i][j].x == l.toX && pointsPos[i][j].y == l.toY))){
                        counter += 1
                    }
                    if (((pointsPos[i+1][j+1].x == l.fromX && pointsPos[i+1][j+1].y == l.fromY) && (pointsPos[i][j+1].x == l.toX && pointsPos[i][j+1].y == l.toY)) || ((pointsPos[i][j+1].x == l.fromX && pointsPos[i][j+1].y == l.fromY) && (pointsPos[i+1][j+1].x == l.toX && pointsPos[i+1][j+1].y == l.toY))){
                        counter += 1
                    }
                    if (((pointsPos[i][j].x == l.fromX && pointsPos[i][j].y == l.fromY) && (pointsPos[i+1][j].x == l.toX && pointsPos[i+1][j].y == l.toY)) || ((pointsPos[i+1][j].x == l.fromX && pointsPos[i+1][j].y == l.fromY) && (pointsPos[i][j].x == l.toX && pointsPos[i][j].y == l.toY))){
                        counter += 1
                    }
                    if (((pointsPos[i+1][j+1].x == l.fromX && pointsPos[i+1][j+1].y == l.fromY) && (pointsPos[i+1][j].x == l.toX && pointsPos[i+1][j].y == l.toY)) || ((pointsPos[i+1][j].x == l.fromX && pointsPos[i+1][j].y == l.fromY) && (pointsPos[i+1][j+1].x == l.toX && pointsPos[i+1][j+1].y == l.toY))){
                        counter += 1
                    }
                }
            }
            if(counter == 4){
                boxes += 1
            }
        }
    }

    if(!orginal){
        lines.removeLast()
    }

    return boxes
}

fun moves(pointsPos: MutableList<MutableList<Point>> = ArrayList(), lines: ArrayList<Lines>, moves: MutableList<MutableList<Point>> = ArrayList()){
    var num = pointsPos.size - 1
    var exist = false
    moves.clear()
    for (x in (0..num)){
        for (y in (0..num)){
            //Log.d("size",lines.size.toString())
            if (lines.size > 0){
                if(x < num){
                    exist = false
                    for (l in lines){
                        if (((l.fromX == pointsPos[x][y].x && l.fromY == pointsPos[x][y].y) && (l.toX == pointsPos[x+1][y].x && l.toY == pointsPos[x+1][y].y)) || ((l.fromX == pointsPos[x+1][y].x && l.fromY == pointsPos[x+1][y].y) && (l.toX == pointsPos[x][y].x && l.toY == pointsPos[x][y].y))){
                            exist = true
                        }
                    }
                    if(!exist){
                        moves.add(arrayListOf<Point>(pointsPos[x][y], pointsPos[x+1][y]))
                        //Log.d("size",moves.last().toString())
                    }
                }
                if(y < num){
                    exist = false
                    for (l in lines){
                        if (((l.fromX == pointsPos[x][y].x && l.fromY == pointsPos[x][y].y) && (l.toX == pointsPos[x][y+1].x && l.toY == pointsPos[x][y+1].y)) || ((l.fromX == pointsPos[x][y+1].x && l.fromY == pointsPos[x][y+1].y) && (l.toX == pointsPos[x][y].x && l.toY == pointsPos[x][y].y))){
                            exist = true
                        }
                    }
                    if(!exist){
                        moves.add(arrayListOf<Point>(pointsPos[x][y], pointsPos[x][y+1]))

                    }
                }

            }
            for(i in moves){
                Log.d("size",x.toString()+" "+i.toString()+" "+i.size.toString()+" "+moves.size.toString())
            }
        }
    }
}

//CompMoves

@ExperimentalStdlibApi
fun hCompMove(pointsPos: MutableList<MutableList<Point>> = ArrayList(), lines: ArrayList<Lines>): ArrayList<Point> {
    var tlines = arrayListOf<Lines>()
    var moves: MutableList<MutableList<Point>> = ArrayList()
    moves(pointsPos, lines, moves)
    tlines = lines

    for(i in (0 until moves.size)){
        for(j in (0 until (moves.size-i-1))){
            var boxes1 = retboxes(pointsPos,tlines, moves[j] as ArrayList<Point>, false)
            var boxes2 = retboxes(pointsPos,tlines, moves[j+1] as ArrayList<Point>, false)
            if(boxes1 < boxes2){
                var temp: ArrayList<Point> = moves[j] as ArrayList<Point>
                moves[j] = moves[j+1]
                moves[j+1] = temp
            }
            else if(boxes1 == boxes2){
                var i = arrayListOf<Boolean>(true, false)
                if(i.random()){
                    var temp: ArrayList<Point> = moves[j] as ArrayList<Point>
                    moves[j] = moves[j+1]
                    moves[j+1] = temp
                }

            }
        }
    }
    var moveNo = 0
    var boxes = retboxes(pointsPos,tlines, moves[0] as ArrayList<Point>, false)
    var oBoxes = retboxes(pointsPos,tlines, moves[0] as ArrayList<Point>, true)

    var tMoves: MutableList<MutableList<Point>> = ArrayList()
    var i = 0


    if(boxes == oBoxes){

        var moveD = false
        while(!moveD && i < moves.size){
            var tl = Lines()
            tl.fromX = moves[i][0].x
            tl.fromY = moves[i][0].y
            tl.toX = moves[i][1].x
            tl.toY = moves[i][1].y
            tlines.add(tl)
            moves(pointsPos, tlines, tMoves)
            var newB = false
            for (i in tMoves){
                var b1 = retboxes(pointsPos, tlines, i as ArrayList<Point>, false)
                var b2 = retboxes(pointsPos, tlines, i, true)

                if(b1 != b2){
                    newB = true
                }
            }
            if(!newB){
                moveD = true
                moveNo = i
            }
            i++
            tlines.removeLast()
        }
    }


    var cline = ArrayList<Point>()
    if(moves.size > 0){
        cline = moves[moveNo] as ArrayList<Point>
    }
    return cline
}

@ExperimentalStdlibApi
fun mCompMove(pointsPos: MutableList<MutableList<Point>> = ArrayList(), lines: ArrayList<Lines>): ArrayList<Point> {

    var tlines = arrayListOf<Lines>()
    var moves: MutableList<MutableList<Point>> = ArrayList()
    moves(pointsPos, lines, moves)
    tlines = lines
    Log.d("list",tlines.size.toString())


    for(i in (0 until moves.size)){
        for(j in (0 until (moves.size-i-1))){
            var boxes1 = retboxes(pointsPos,tlines, moves[j] as ArrayList<Point>, false)
            var boxes2 = retboxes(pointsPos,tlines, moves[j+1] as ArrayList<Point>, false)
            if(boxes1 < boxes2){
                var temp: ArrayList<Point> = moves[j] as ArrayList<Point>
                moves[j] = moves[j+1]
                moves[j+1] = temp
            }
            else if(boxes1 == boxes2){
                var i = arrayListOf<Boolean>(true, false)
                if(i.random()){
                    var temp: ArrayList<Point> = moves[j] as ArrayList<Point>
                    moves[j] = moves[j+1]
                    moves[j+1] = temp
                }

            }
        }
    }

    var cline = ArrayList<Point>()
    if(moves.size > 0){
        cline = moves[0] as ArrayList<Point>
    }
    return cline
}

fun eCompMove(pointsPos: MutableList<MutableList<Point>> = ArrayList()): ArrayList<Point> {
    var num = pointsPos.size - 2
    var x = arrayListOf<Int>()
    var y = arrayListOf<Int>()
    for(i in (0..num)){
        x.add(i)
        y.add(i)
    }
    var fX = x.random()
    var fY = y.random()
    var tX = 0
    var tY = 0
    var i = listOf<Int>(0,1).random()
    if(i == 0){
        tX = fX + 1
        tY = fY
    }
    else{
        tX = fX
        tY = fY + 1
    }
    var cline = ArrayList<Point>()
    cline.add(pointsPos[fX][fY])
    cline.add(pointsPos[tX][tY])
    return cline
}