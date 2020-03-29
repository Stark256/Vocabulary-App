package com.vocabulary.customViews.progress_square_view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.safeLet
import kotlinx.android.synthetic.main.view_progress_square.view.*

class SmallProgressSquareView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    private val ANIMATION_SLIDE_TIME = 200L

    private var squareSize: Float = context.resources.getDimension(R.dimen.progress_square_view_card_size_small)
    private var squareMargin: Float = context.resources.getDimension(R.dimen.progress_square_view_card_margin_small)

    private var marker1: View? = null
    private var marker2: View? = null
    private var marker3: View? = null

    private var card1: CardView? = null
    private var card2: CardView? = null
    private var card3: CardView? = null
    private var card4: CardView? = null
    private var textProgress: TextView? = null

    private var moveDirection: ProgressSquareBaseDirection = VerticalDirection.D_DOWN
    private var verticalDirection: VerticalDirection = VerticalDirection.D_DOWN
    private var horizontalDirection: HorizontalDirection = HorizontalDirection.D_RIGHT

    private val offset: Float
        get() { return squareMargin + squareMargin + squareSize }

    private val arrCardPositions: ArrayList<Int> = arrayListOf(2, 3, 4)
    private var arrMovingIndex = 0
    private var cardMovingIndex = 1

    private var isMoving = false

    private var percentage : Int = 0

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_small_progress_square, this, true)

        this.marker1 = v.view_marker_1
        this.marker2 = v.view_marker_2
        this.marker3 = v.view_marker_3

        this.card1 = v.card_1
        this.card2 = v.card_2
        this.card3 = v.card_3
        this.card4 = v.card_4
        this.textProgress = v.tv_square_progress

        initViews()
    }

    private fun initViews() {
        marker1?.let{
            marker1!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    marker1!!.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    arrMovingIndex = 0
                    cardMovingIndex = 1
                    arrCardPositions.add(2)
                    arrCardPositions.add(3)
                    arrCardPositions.add(4)

                    textProgress?.setTextColor(Injector.themeManager.getGradient3(context))
                    textProgress?.text = String.format(context.getString(R.string.loading), percentage.toString())

                    moveCardToXY(getCardByIndex(cardMovingIndex), marker1?.x, marker1?.y, yOffset = offset)
                    moveCardToXY(getCardByIndex(arrCardPositions[0]), marker1?.x, marker1?.y)
                    moveCardToXY(getCardByIndex(arrCardPositions[1]), marker2?.x, marker2?.y)
                    moveCardToXY(getCardByIndex(arrCardPositions[2]), marker3?.x, marker3?.y)

                    customizeCardBackground(getCardByIndex(cardMovingIndex), 0)
                    customizeCardBackground(getCardByIndex(arrCardPositions[0]), 0)
                    customizeCardBackground(getCardByIndex(arrCardPositions[1]), 1)
                    customizeCardBackground(getCardByIndex(arrCardPositions[2]), 2)
                    start()
                }
            })
        }
    }

    fun start() {
        if(!isMoving) {
            this.isMoving = true
            moveNext()
        }
    }

    fun stop() {
        if(isMoving) {
            this.isMoving = false
        }
    }

    private fun moveNext() {
        if(isMoving) {
            when (moveDirection) {
                VerticalDirection.D_UP -> {
                    safeLet(
                        getCardByIndex(cardMovingIndex),
                        getCardByIndex(arrCardPositions[arrMovingIndex]),
                        getMarkerByIndex(arrMovingIndex)
                    )
                    { cardMoving, arrCard, marker ->

                        slideUpDown(
                            cardMoving,
                            arrCard,
                            cardMoving.y,
                            marker.y,
                            true, {}, {

                                val oldCardMovingIndex = cardMovingIndex
                                val oldArrMovingIndex = arrCardPositions[arrMovingIndex]

                                cardMovingIndex = oldArrMovingIndex
                                arrCardPositions[arrMovingIndex] = oldCardMovingIndex

                                updateHorizontalDirection()
                                this.moveDirection = horizontalDirection
                                moveNext()
                            })

                    }
                }
                VerticalDirection.D_DOWN -> {

                    safeLet(
                        getCardByIndex(cardMovingIndex),
                        getCardByIndex(arrCardPositions[arrMovingIndex]),
                        getMarkerByIndex(arrMovingIndex)
                    )
                    { cardMoving, arrCard, marker ->

                        slideUpDown(
                            cardMoving,
                            arrCard,
                            cardMoving.y,
                            marker.y,
                            false, {}, {
                                val oldCardMovingIndex = cardMovingIndex
                                val oldArrMovingIndex = arrCardPositions[arrMovingIndex]

                                cardMovingIndex = oldArrMovingIndex
                                arrCardPositions[arrMovingIndex] = oldCardMovingIndex

                                updateHorizontalDirection()
                                this.moveDirection = horizontalDirection
                                moveNext()
                            })

                    }
                }
                HorizontalDirection.D_LEFT -> {

                    safeLet(
                        getCardByIndex(cardMovingIndex),
                        getMarkerByIndex(getMarkerIndex())
                    )
                    { cardMoving, marker ->

                        slideLeftRight(
                            cardMoving,
                            cardMoving.x,
                            marker.x, {}, {
                                updateVerticalDirection()
                                updateArrMovingIndex()
                                customizeCardBackground(cardMoving, arrMovingIndex)
                                this.moveDirection = verticalDirection
                                moveNext()
                            })

                    }
                }
                HorizontalDirection.D_RIGHT -> {

                    safeLet(
                        getCardByIndex(cardMovingIndex),
                        getMarkerByIndex(getMarkerIndex())
                    )
                    { cardMoving, marker ->

                        slideLeftRight(
                            cardMoving,
                            cardMoving.x,
                            marker.x, {}, {
                                updateVerticalDirection()
                                updateArrMovingIndex()
                                customizeCardBackground(cardMoving, arrMovingIndex)
                                this.moveDirection = verticalDirection
                                moveNext()
                            })

                    }
                }
            }
        }
    }

    private fun moveCardToXY(cardView: CardView?, toX: Float?, toY: Float?, yOffset: Float = 0f) {
        safeLet(cardView, toX, toY) { card, sX, sY ->
            card.x = sX
            card.y = sY - yOffset
            card.requestLayout()
        }
    }

    private fun customizeCardBackground(cardView: CardView?, index: Int) {
        cardView?.let {
            when(index) {
                0 -> cardView.setCardBackgroundColor(Injector.themeManager.getGradient3(context))
                1 -> cardView.setCardBackgroundColor(Injector.themeManager.getGradient2(context))
                2 -> cardView.setCardBackgroundColor(Injector.themeManager.getGradient1(context))
            }
        }
    }

    private fun getMarkerIndex() : Int {
        return if(horizontalDirection == HorizontalDirection.D_RIGHT && arrMovingIndex == 0) {
            arrMovingIndex + 1
        } else if(horizontalDirection == HorizontalDirection.D_RIGHT && arrMovingIndex == 1) {
            arrMovingIndex + 1
        } else if(horizontalDirection == HorizontalDirection.D_LEFT && arrMovingIndex == 2) {
            arrMovingIndex -1
        } else if(horizontalDirection == HorizontalDirection.D_LEFT && arrMovingIndex == 1) {
            arrMovingIndex -1
        } else {
            0
        }
    }

    private fun updateArrMovingIndex() {
        if(horizontalDirection == HorizontalDirection.D_RIGHT) {
            if(arrMovingIndex == 0 || arrMovingIndex == 1) {
                arrMovingIndex++
            }
        } else if(horizontalDirection == HorizontalDirection.D_LEFT) {
            if(arrMovingIndex == 2 || arrMovingIndex == 1) {
                arrMovingIndex--
            }
        }


    }

    private fun updateVerticalDirection() {
        if(verticalDirection == VerticalDirection.D_DOWN) {
            verticalDirection = VerticalDirection.D_UP
        } else {
            verticalDirection = VerticalDirection.D_DOWN
        }
    }

    private fun updateHorizontalDirection() {
        if(horizontalDirection == HorizontalDirection.D_RIGHT
            && arrMovingIndex == 2) {
            horizontalDirection = HorizontalDirection.D_LEFT
        } else if(horizontalDirection == HorizontalDirection.D_LEFT
            && arrMovingIndex == 0) {
            horizontalDirection = HorizontalDirection.D_RIGHT
        }
    }


    private fun getCardByIndex(index: Int) : CardView? {
        return when(index) {
            1 -> this.card1
            2 -> this.card2
            3 -> this.card3
            4 -> this.card4
            else -> null
        }
    }

    private fun getMarkerByIndex(index: Int) : View? {
        return when(index) {
            0 -> this.marker1
            1 -> this.marker2
            2 -> this.marker3
            else -> null
        }
    }

    private fun animateChangingColor(textView: TextView, nextColor: Int) {

        val anim = ValueAnimator.ofArgb(textView.currentTextColor, nextColor)
        anim.addUpdateListener { valueAnimator ->
            textView.setTextColor((valueAnimator.animatedValue as Int))
        }
        anim.duration = ANIMATION_SLIDE_TIME
        anim.start()
    }

    private fun slideUpDown(card1: CardView,
                            card2: CardView,
                            currentY: Float,
                            newY: Float,
                            isUp: Boolean,
                            startAction: () -> Unit,
                            endAction: () -> Unit) {

        val slideAnimator = ValueAnimator
            .ofFloat(currentY, newY)
            .setDuration(ANIMATION_SLIDE_TIME)

        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            card1.y = value
            card2.y = if(isUp) value - offset else value + offset
            card1.requestLayout()
            card2.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(slideAnimator)
        animatorSet.doOnStart { startAction.invoke() }
        animatorSet.doOnEnd { endAction.invoke() }
        animatorSet.start()
    }

    private fun slideLeftRight(card: CardView,
                               currentX: Float,
                               newX: Float,
                               startAction: () -> Unit,
                               endAction: () -> Unit) {

        val slideAnimator = ValueAnimator
            .ofFloat(currentX, newX)
            .setDuration(ANIMATION_SLIDE_TIME)

        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            card.x = value
            card.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(slideAnimator)
        animatorSet.doOnStart { startAction.invoke() }
        animatorSet.doOnEnd { endAction.invoke() }
        animatorSet.start()
    }

    private enum class HorizontalDirection :
        ProgressSquareBaseDirection {
        D_RIGHT, D_LEFT
    }

    private enum class VerticalDirection :
        ProgressSquareBaseDirection {
        D_UP, D_DOWN
    }

    private interface ProgressSquareBaseDirection
}