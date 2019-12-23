package com.vocabulary.ui.words

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Bitmap


class WordsSwipeController(
    val drawableSwipeLeft: Drawable,
    val drawableSwipeRight: Drawable,
    val colorLeft: Int,
    val colorRight: Int
) : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

    val p = Paint()
    private var isSwipeEnabled = false

    fun getSwipeEnabled(): Boolean {
        return isSwipeEnabled
    }

    fun setSwipe() {
        this.isSwipeEnabled = !isSwipeEnabled
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return isSwipeEnabled
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val height: Float = itemView.bottom.toFloat() - itemView.top.toFloat()
            val width: Float = height / 3
            if(dX > 0) {
                p.color = colorLeft
                val backgroundLeft = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                val iconDestLeft = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width,
                    itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                c.drawRect(backgroundLeft, p)
                c.drawBitmap(getBitmap(drawableSwipeLeft), null, iconDestLeft, p)

            } else {
                p.color = colorRight
                val backgroundRight = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                val iconDestRight = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width,
                    itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                c.drawRect(backgroundRight, p)
                c.drawBitmap(getBitmap(drawableSwipeRight), null, iconDestRight, p)

            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if(direction == LEFT) {
            // TODO callback left
        } else if(direction == RIGHT) {
            // TODO callback right
        }
    }

    fun getBitmap(drawable: Drawable): Bitmap {
        val bitmap: Bitmap
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            bitmap = Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}

    //    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//
//        return if(viewHolder is WordsAdapter.LetterViewHolder) 0
//            else super.getSwipeDirs(recyclerView, viewHolder)
//    }
