package com.vocabulary.customViews.bottom_nav_view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.safeLet
import kotlinx.android.synthetic.main.view_bottom_nav_elevated.view.*


class BottomNavigationElevatedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private val whiteColor: Int = ContextCompat.getColor(context, R.color.white)
    private val accentColor: Int = Injector.themeManager.getAccentColor(context)

    private var cardElevated: LinearLayout? = null
    private var btnContainer: LinearLayout? = null
    private var backgroundView: LinearLayout? = null

    /*  Words  */
    private var btnWrods: LinearLayout? = null
    private var tvWords: TextView? = null
    private var ivWords: ImageView? = null

    /*  Games  */
    private var btnGames: LinearLayout? = null
    private var tvGames: TextView? = null
    private var ivGames: ImageView? = null

    /*  Settings  */
    private var btnSettings: LinearLayout? = null
    private var tvSettings: TextView? = null
    private var ivSettings: ImageView? = null


    private var isMoving = false
    private var isButtonClickable = true
    private var selectedButton: MOVE_DIRECTION = MOVE_DIRECTION.D_WORDS
    private lateinit var listener: NavigationMoveListener
    enum class MOVE_DIRECTION { D_WORDS, D_GAMES, D_SETTINGS }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_bottom_nav_elevated, this, true)

        this.cardElevated = view.cv_elevated
        this.btnContainer = view.btns_container
        this.backgroundView = view.ll_top_background
        this.btnWrods = view.btn_words
        this.btnGames = view.btn_games
        this.btnSettings = view.btn_settings
        this.tvWords = view.tv_nav_words
        this.ivWords = view.iv_nav_words
        this.tvGames = view.tv_nav_games
        this.ivGames = view.iv_nav_games
        this.tvSettings = view.tv_nav_settings
        this.ivSettings = view.iv_nav_settings

        initElevatedCard()

        this.btnWrods?.setOnClickListener {
            if(!isMoving && isButtonClickable && selectedButton != MOVE_DIRECTION.D_WORDS) {
                listener.onMove(selectedButton, MOVE_DIRECTION.D_WORDS)
                moveToWords()
            }
        }
        this.btnGames?.setOnClickListener {
            if(!isMoving && isButtonClickable && selectedButton != MOVE_DIRECTION.D_GAMES) {
                listener.onMove(selectedButton, MOVE_DIRECTION.D_GAMES)
                moveToGames()
            }
        }
        this.btnSettings?.setOnClickListener {
            if(!isMoving && isButtonClickable && selectedButton != MOVE_DIRECTION.D_SETTINGS) {
                listener.onMove(selectedButton, MOVE_DIRECTION.D_SETTINGS)
                moveToSettings()
            }
        }

    }

    fun showBackView() {
        this.isButtonClickable = false
        this.backgroundView?.visibility = View.VISIBLE
    }

    fun hideBackView() {
        this.isButtonClickable = true
        this.backgroundView?.visibility = View.GONE
    }

    fun initListener(listener: NavigationMoveListener) {
        this.listener = listener
    }

    private fun selectButton(button: MOVE_DIRECTION) {
        when(button) {
            MOVE_DIRECTION.D_WORDS -> {
                safeLet(this.tvWords, this.ivWords) { tv, iv ->
                    changeToAccent(tv, iv)
                }
            }
            MOVE_DIRECTION.D_GAMES -> {
                safeLet(this.tvGames, this.ivGames) { tv, iv ->
                    changeToAccent(tv, iv)
                }
            }
            MOVE_DIRECTION.D_SETTINGS -> {
                safeLet(this.tvSettings, this.ivSettings) { tv, iv ->
                    changeToAccent(tv, iv)
                }
            }
        }
    }

    private fun deselectPreviousButton() {
        when(selectedButton) {
            MOVE_DIRECTION.D_WORDS -> {
                safeLet(this.tvWords, this.ivWords) { tv, iv ->
                    changeToWhite(tv, iv)
                }
            }
            MOVE_DIRECTION.D_GAMES -> {
                safeLet(this.tvGames, this.ivGames) { tv, iv ->
                    changeToWhite(tv, iv)
                }
            }
            MOVE_DIRECTION.D_SETTINGS -> {
                safeLet(this.tvSettings, this.ivSettings) { tv, iv ->
                    changeToWhite(tv, iv)
                }
            }
        }
    }

    private fun changeToAccent(tv: TextView, iv: ImageView) {
        tv.animate()
            .alpha(0f)
            .setDuration(125)
            .withEndAction {
                tv.setTextColor(accentColor)
                tv.animate()
                    .alpha(1f)
                    .setDuration(125)
                    .start()
            }
            .start()
        iv.animate()
            .alpha(0f)
            .setDuration(125)
            .withEndAction {
                iv.imageTintList = ColorStateList.valueOf(accentColor)
                iv.animate()
                    .alpha(1f)
                    .setDuration(125)
                    .start()
            }
            .start()
    }

    private fun changeToWhite(tv: TextView, iv: ImageView) {
        tv.animate()
            .alpha(0f)
            .setDuration(250)
            .withEndAction {
                tv.setTextColor(whiteColor)
                tv.animate()
                    .alpha(1f)
                    .setDuration(250)
                    .start()
            }
            .start()
        iv.animate()
            .alpha(0f)
            .setDuration(250)
            .withEndAction {
                iv.imageTintList = ColorStateList.valueOf(whiteColor)
                iv.animate()
                    .alpha(1f)
                    .setDuration(250)
                    .start()
            }
            .start()
    }


    private fun moveToWords() {
        safeLet(this.btnWrods, this.cardElevated) { btn, card ->
            animateButtons(card, card.x, btn.x, {
                isMoving = true
                deselectPreviousButton()
                selectButton(MOVE_DIRECTION.D_WORDS)
            }, {
                isMoving = false
                selectedButton = MOVE_DIRECTION.D_WORDS
            })
        }
    }

    private fun moveToGames() {
        safeLet(this.btnGames, this.cardElevated) { btn, card ->
            animateButtons(card, card.x, btn.x, {
                isMoving = true
                deselectPreviousButton()
                selectButton(MOVE_DIRECTION.D_GAMES)
            }, {
                isMoving = false
                selectedButton = MOVE_DIRECTION.D_GAMES
            })
        }
    }

    private fun moveToSettings() {
        safeLet(this.btnSettings, this.cardElevated) { btn, card ->
            animateButtons(card, card.x, btn.x, {
                isMoving = true
                deselectPreviousButton()
                selectButton(MOVE_DIRECTION.D_SETTINGS)
            }, {
                isMoving = false
                selectedButton = MOVE_DIRECTION.D_SETTINGS
            })
        }
    }


    private fun initElevatedCard() {
        safeLet(this.cardElevated, this.btnWrods, this.tvWords, this.ivWords) { card, btn, tv, iv ->
            btn.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    btn.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val oldParams = card.layoutParams
                    oldParams.width = btn.width

                    card.layoutParams = oldParams
                    card.x = btn.x
                    tv.setTextColor(accentColor)
                    iv.imageTintList = ColorStateList.valueOf(accentColor)
                }
            })

        }
    }

    private fun animateButtons(view: View,
                               currentX: Float,
                               newX: Float,
                               startAction: () -> Unit,
                               endAction: () -> Unit) {

        val slideAnimator = ValueAnimator
            .ofFloat(currentX, newX)
            .setDuration(500)

        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            view.x = value
            view.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(slideAnimator)
        animatorSet.doOnStart { startAction.invoke() }
        animatorSet.doOnEnd { endAction.invoke() }
        animatorSet.start()
    }

    interface NavigationMoveListener {
        fun onMove(fromD: MOVE_DIRECTION, toD: MOVE_DIRECTION)
    }
}