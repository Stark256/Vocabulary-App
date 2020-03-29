package com.vocabulary.customViews.swipeable_view

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class OnSwipeTouchListener(
    context: Context,
    private val recyclerView: RecyclerView)
    : View.OnTouchListener {

    private val gestureDetector = GestureDetector(context, GestureListener())

    override fun onTouch(v: View, event: MotionEvent): Boolean {
//        return gestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return recyclerView.onTouchEvent(event)
    }

//    fun onTouch(event: MotionEvent): Boolean {
//        return gestureDetector.onTouchEvent(event)
//    }

    init {
        recyclerView.setOnTouchListener(this)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        fun getViewHolderInEvent(e: MotionEvent) : RecyclerView.ViewHolder? {
            val childView = recyclerView.findChildViewUnder(e.x, e.y)
            if(childView != null) {
                val holder: RecyclerView.ViewHolder? = recyclerView.findContainingViewHolder(childView)
                return holder
            }
            return null
        }

        private fun getViewHolderByPosition(position: Int) : RecyclerView.ViewHolder? {
            return recyclerView.findViewHolderForAdapterPosition(position)
        }

        private var openedItemPosition: Int = -1
        private fun swipeLeft(e: MotionEvent) {
            val holder = getViewHolderInEvent(e) as? SwipeableViewHolderInterface
            holder?.let {
                val swipeableItemView = it.getSwipableItemView()
                if(openedItemPosition != -1) {
                    (getViewHolderByPosition(openedItemPosition)
                            as? SwipeableViewHolderInterface)
                    ?.let {
                        it.getSwipableItemView().closeViewAfterOpening()
                    }
                }
                swipeableItemView.leftSwipe()
                openedItemPosition = swipeableItemView.itemPosition

            }
        }
        private fun swipeRight(e: MotionEvent) {
            (getViewHolderInEvent(e) as? SwipeableViewHolderInterface)
            ?.let {
                it.getSwipableItemView().rightSwipe()
                openedItemPosition = -1
            }
        }


        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            //onSwipeRight()
                            swipeRight(e1)
                        } else {
                            //onSwipeLeft()
                            swipeLeft(e1)
                        }
                        result = true
                    }
                } /*else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(
                        velocityY
                    ) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }*/
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }

    }
//    open fun onSwipeRight() {}
//
//    open fun onSwipeLeft() {}

//    open fun onSwipeTop() {}
//
//    open fun onSwipeBottom() {}
}