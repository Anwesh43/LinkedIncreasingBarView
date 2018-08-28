package com.anwesh.uiprojects.increasingbarview

/**
 * Created by anweshmishra on 28/08/18.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

val nodes : Int = 5

fun Canvas.drawIBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.strokeWidth = Math.min(w, h) / 60
    paint.strokeCap = Paint.Cap.ROUND
    paint.color = Color.parseColor("#4CAF50")
    val gap : Float = h / nodes
    val hRect : Float = gap / 4
    val sc : Float = Math.min(0.5f, Math.max(0f, scale - 0.5f)) * 2
    save()
    translate(w / 2, gap * i)
    drawLine(0f, 0f, 0f, gap * scale, paint)
    drawRect(RectF(0f,-hRect/2, gap * sc, hRect/2), paint)
    restore()
}