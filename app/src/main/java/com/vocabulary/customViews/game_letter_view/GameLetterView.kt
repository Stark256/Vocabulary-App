package com.vocabulary.customViews.game_letter_view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.google.android.material.button.MaterialButton
import com.vocabulary.R
import com.vocabulary.customViews.game_letter_list_view.GameLetterListView
import com.vocabulary.customViews.progress_square_view.ProgressSquareView
import com.vocabulary.models.game_letters_models.GameLettersModel
import com.vocabulary.models.safeLet
import com.vocabulary.ui.game.game_letters.GameLetterViewState
import kotlinx.android.synthetic.main.view_game_letter.view.*

class GameLetterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private val ANIMATION_SLIDE_TIME = 200L

    private val zeroWitdh: Int = context.resources.getDimension(R.dimen.swipeable_button_zero_width).toInt()

    private var translationTextView: TextView? = null
    private var rootCard: CardView? = null
    private var backCard: CardView? = null
    private var progressBar: ProgressSquareView? = null
    private var buttonStart: MaterialButton? = null
    private var letterListCheckView: GameLetterListView? = null
    private var letterListGuessView: GameLetterListView? = null

    private lateinit var listener: GameLetterViewSelectListener
    private var isFirstShowing = true

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_game_letter, this, true)

        this.translationTextView = v.tv_game_item_translation
        this.rootCard = v.cv_root_game_item
        this.backCard = v.cv_back_game_item
        this.progressBar = v.psv_game_letters_loading
        this.buttonStart = v.btn_start_game_letters
        this.letterListCheckView = v.gllv_game_letter_check
        this.letterListGuessView = v.gllv_game_letter_guess

        this.buttonStart?.setOnClickListener {
            listener.onReadyPressed()
        }

    }

    fun showNext(gameLettersModel: GameLettersModel) {
        if(isFirstShowing) {
            initData(gameLettersModel)
            showView()
            this.isFirstShowing = false
        } else {
            safeLet(this.rootCard, this.backCard) { root, back ->
                slideView(root, root.width, zeroWitdh, {}, {
                    root.visibility = View.INVISIBLE
                    root.alpha = 0f
                    initData(gameLettersModel)
                    showView()
                })
            }
        }
    }

    private fun initData(gameLettersModel: GameLettersModel) {
        this.translationTextView?.text = gameLettersModel.translation

        this.letterListGuessView?.visibility = View.VISIBLE
        this.letterListCheckView?.initViewCheckItems(gameLettersModel.correctLetters.size,
            object : GameLetterListView.GameLetterListViewSelectListener {
                override fun onLetterItemPressed(position: Int, letter: String?, uniqueID: Int) {
                    gllv_game_letter_guess.setItemGuess(letter, uniqueID,0, true)
                    gllv_game_letter_check.setItemCheck("", uniqueID, position, false)
                }

                override fun onCheckListIsFull(isFull: Boolean) {
                    listener.onCheckListIsFull(isFull)
                }
            })

        this.letterListGuessView?.initViewGuessItems(gameLettersModel.lettersToGuess,
            object : GameLetterListView.GameLetterListViewSelectListener {
                override fun onLetterItemPressed(position: Int, letter: String?, uniqueID: Int) {
                    if(gllv_game_letter_check.canAddMore()) {
                        gllv_game_letter_check.setItemCheck(letter, uniqueID, 0, true)
                        gllv_game_letter_guess.setItemGuess("", uniqueID, position, false)
                    }
                }

                override fun onCheckListIsFull(isFull: Boolean) {}
            })
    }

    fun showResult(dontKnow: Boolean, gameLettersModel: GameLettersModel) {
        if(!dontKnow) {
                val resultCheck = this.letterListCheckView?.getCheckListResult()
                if(gameLettersModel.isCorrectWord(resultCheck)) {
                    this.letterListCheckView?.showCheckResult(gameLettersModel.correctLetters, false)
                    this.letterListGuessView?.visibility = View.GONE
                } else {
                    this.letterListCheckView?.showCheckResult(gameLettersModel.correctLetters, false)
                    this.letterListGuessView?.showGuessResults(gameLettersModel.correctLetters)
                }
            listener.onGameLetterSelected(resultCheck)
        } else {
            this.letterListCheckView?.showCheckResult(gameLettersModel.correctLetters, true)
            this.letterListGuessView?.visibility = View.GONE
            listener.onGameLetterSelected(null)
        }
    }

    fun setListener(listener: GameLetterViewSelectListener) {
        this.listener = listener
    }

    fun setPercentage(percent: Int) {
        progressBar?.setPercentage(percent)
    }

    fun setState(state: GameLetterViewState) {
        when(state) {
            GameLetterViewState.STATE_LOADING -> {
                progressBar?.visibility = View.VISIBLE
//                progressBar?.start()
                buttonStart?.visibility = View.GONE
            }
            GameLetterViewState.STATE_READY -> {
                progressBar?.visibility = View.GONE
                progressBar?.stop()
                buttonStart?.visibility = View.VISIBLE
            }
            GameLetterViewState.STATE_STARTED -> {
                progressBar?.visibility = View.GONE
                progressBar?.stop()
                buttonStart?.visibility = View.GONE
            }
        }
    }


    fun setTips(arr: ArrayList<Long>) {
        // TODO
    }



    private fun showView() {
        safeLet(this.rootCard, this.backCard) { root, back ->

            root.animate()
                .alpha(1f)
                .setDuration(ANIMATION_SLIDE_TIME)
                .withStartAction {
                    root.visibility = View.VISIBLE
                    if(!isFirstShowing) {
                        val oldParams = root.layoutParams
                        oldParams.width = back.width
                        root.layoutParams = oldParams
                    }
                }
                .start()
        }
    }

    private fun hideView() {
        safeLet(this.rootCard, this.backCard) { root, back ->
            slideView(root, root.width, zeroWitdh, {

            }, {
                root.visibility = View.INVISIBLE
                root.alpha = 0f
                showView()
            })
        }
    }

    private fun slideView(card: View,
                          currentWidth: Int,
                          newWidth: Int,
                          startAction: () -> Unit,
                          endAction: () -> Unit) {

        val slideAnimator = ValueAnimator
            .ofInt(currentWidth, newWidth)
            .setDuration(ANIMATION_SLIDE_TIME)

        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            card.layoutParams.width = value
            card.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(slideAnimator)
        animatorSet.doOnStart { startAction.invoke() }
        animatorSet.doOnEnd { endAction.invoke() }
        animatorSet.start()
    }

    interface GameLetterViewSelectListener {
        fun onGameLetterSelected(result: String?)
        fun onReadyPressed()
        fun onCheckListIsFull(isFull: Boolean)
    }

}