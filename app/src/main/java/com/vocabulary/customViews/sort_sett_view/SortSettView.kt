package com.vocabulary.customViews.sort_sett_view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.safeLet
import kotlinx.android.synthetic.main.view_sort_sett.view.*
import org.w3c.dom.Text

class SortSettView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr){

    private val whiteColor: Int = ContextCompat.getColor(context, R.color.white)
    private val transparentColor: Int = ContextCompat.getColor(context, R.color.transparent)
    private val accentColor: Int = Injector.themeManager.getAccentColor(context)

    private val ANIMATION_TIME = 500L
    private var selectedSortSett: SORT_SETT = SORT_SETT.SORT_A_Z
    private var isAnimate = false

    private var tvLeft: TextView? = null
    private var tvRight: TextView? = null
    private var btnLeft: LinearLayout? = null
    private var btnRight: LinearLayout? = null
    private var viewLeft: View? = null
    private var viewRight: View? = null
    private var buttonContainer: LinearLayout? = null
    private var viewContainer: LinearLayout? = null



    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_sort_sett, this, true)

        this.viewContainer = v.ll_segment_view_container
        this.buttonContainer = v.ll_segment_button_container
        this.tvLeft = v.tv_segment_left
        this.tvRight = v.tv_segment_right
        this.viewLeft = v.view_segment_left
        this.viewRight = v.view_segment_right
        this.btnLeft = v.btn_segment_left
        this.btnRight = v.btn_segment_right

        this.btnLeft?.setOnClickListener { onLeftClicked() }
        this.btnRight?.setOnClickListener { onRightClicked() }

        changeButtonImmediate(this.selectedSortSett)
    }

    private fun changeButtonImmediate(sortSett: SORT_SETT) {
        if(sortSett == SORT_SETT.SORT_A_Z) {
            this.tvLeft?.setTextColor(accentColor)
            this.tvRight?.setTextColor(whiteColor)
            this.viewLeft?.setBackgroundColor(whiteColor)
            this.viewRight?.setBackgroundColor(transparentColor)
        } else {
            this.tvLeft?.setTextColor(whiteColor)
            this.tvRight?.setTextColor(accentColor)
            this.viewLeft?.setBackgroundColor(transparentColor)
            this.viewRight?.setBackgroundColor(whiteColor)
        }
    }

    fun setSelectedSortSett(sortSett: SORT_SETT) {
        if(sortSett != this.selectedSortSett) {
            changeButtonImmediate(sortSett)
        }
        this.selectedSortSett = sortSett
    }

    fun getSelectedSortSett() : SORT_SETT = selectedSortSett

    private fun onLeftClicked() {
        if(!isAnimate && this.selectedSortSett != SORT_SETT.SORT_A_Z) {
            this.selectedSortSett = SORT_SETT.SORT_A_Z
            safeLet(this.viewContainer, this.buttonContainer) { viewCon, btnCon ->
                slideView(viewCon, btnCon.width, 0,{
                    isAnimate = true
                    this.tvLeft?.let { changeToAccent(it) }
                    this.tvRight?.let { changeToWhite(it) }
                }, {
                    slideView(viewCon, 0, btnCon.width,{
                        this.viewLeft?.setBackgroundColor(whiteColor)
                        this.viewRight?.setBackgroundColor(transparentColor)
                    }, {
                        isAnimate = false
                    })
                })
            }
        }
    }

    private fun onRightClicked() {
        if(!isAnimate && this.selectedSortSett != SORT_SETT.SORT_Z_A) {
            this.selectedSortSett = SORT_SETT.SORT_Z_A
            safeLet(this.viewContainer, this.buttonContainer) { viewCon, btnCon ->
                slideView(viewCon, btnCon.width, 0,{
                    isAnimate = true
                    this.tvRight?.let { changeToAccent(it) }
                    this.tvLeft?.let { changeToWhite(it) }
                }, {
                    slideView(viewCon, 0, btnCon.width,{
                        this.viewRight?.setBackgroundColor(whiteColor)
                        this.viewLeft?.setBackgroundColor(transparentColor)
                    }, {
                        isAnimate = false
                    })
                })
            }
        }
    }


    private fun changeToAccent(tv: TextView) {
        tv.animate()
            .alpha(0f)
            .setDuration(ANIMATION_TIME/2)
            .withEndAction {
                tv.setTextColor(accentColor)
                tv.animate()
                    .alpha(1f)
                    .setDuration(ANIMATION_TIME/2)
                    .start()
            }.start()
    }

    private fun changeToWhite(tv: TextView) {
        tv.animate()
            .alpha(0f)
            .setDuration(ANIMATION_TIME/2)
            .withEndAction {
                tv.setTextColor(whiteColor)
                tv.animate()
                    .alpha(1f)
                    .setDuration(ANIMATION_TIME/2)
                    .start()
            }.start()
    }

    private fun slideView(view: View,
                             currentWidth: Int,
                             newWidth: Int,
                             startAction: () -> Unit,
                             endAction: () -> Unit) {

        val slideAnimator = ValueAnimator
            .ofInt(currentWidth, newWidth)
            .setDuration(ANIMATION_TIME/2)

        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            view.layoutParams.width = value
            view.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(slideAnimator)
        animatorSet.doOnStart { startAction.invoke() }
        animatorSet.doOnEnd { endAction.invoke() }
        animatorSet.start()
    }

    enum class SORT_SETT {
        SORT_A_Z,
        SORT_Z_A
    }
}