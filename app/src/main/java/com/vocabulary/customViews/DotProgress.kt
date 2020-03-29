package com.vocabulary.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation


class DotProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){



    private var dotRadius = 10
    private var bounceDotRadius = 13
    private var dotPosition = 1
    private var dotAmount = 20
    private var circleRadius = 100



    init {

        circleRadius = 150
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate((this.width/2).toFloat(), (this.height/2).toFloat())
        val progressPaint = Paint()
        progressPaint.color = Color.RED
        createDotInCircle(canvas, progressPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    private fun createDotInCircle(canvas: Canvas?, progressPaint: Paint) {
        val angle = 360 / dotAmount
        val rwidth = 16
        val rheight = 6

        for(i in 1..dotAmount) {
            val x = (circleRadius * (Math.cos((angle * i)* (Math.PI / 180)))).toFloat()
            val y = (circleRadius * (Math.sin((angle * i)* (Math.PI / 180)))).toFloat()


            val rleft: Float = x - rwidth/2
            val rright: Float = x + rwidth/2
            val rtop: Float = y + rheight/2
            val rbottom: Float = y - rheight/2
            canvas?.save()

            canvas?.rotate((angle * i).toFloat(), x, y)


            canvas?.drawRect(RectF(rleft, rtop, rright, rbottom), progressPaint)
            canvas?.restore()

        }

    }

    private fun startAnimation() {
        val anim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                super.applyTransformation(interpolatedTime, t)
                invalidate()
            }
        }
        anim.duration = 100
        anim.repeatCount = Animation.INFINITE
        anim.interpolator = LinearInterpolator()
        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
                dotPosition++
                if(dotPosition > dotAmount) {
                    dotPosition = 1
                }
            }

            override fun onAnimationEnd(animation: Animation?) {}

            override fun onAnimationStart(animation: Animation?) {}
        })
        startAnimation(anim)
    }

}
