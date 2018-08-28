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

class IncreasingBarView(ctx : Context) : View(ctx) {

    private val  paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class IBNode(var i : Int, val state : State = State()) {
        var next : IBNode? = null
        var prev : IBNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = IBNode(i + 1)
                next?.prev = this
            }
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawIBNode(i, state.scale, paint)
            prev?.draw(canvas, paint)
        }

        fun getNext(dir : Int, cb : () -> Unit) : IBNode {
            var curr : IBNode? = this.prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }
}