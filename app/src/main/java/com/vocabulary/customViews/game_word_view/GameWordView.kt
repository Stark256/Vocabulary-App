package com.vocabulary.customViews.game_word_view

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.vocabulary.R
import com.vocabulary.customViews.progress_square_view.ProgressSquareView
import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.game_words_models.GameWordsListModel
import com.vocabulary.models.safeLet
import com.vocabulary.ui.game.game_words.GameWordsAdapter
import com.vocabulary.ui.game.game_words.GameWordsViewState
import kotlinx.android.synthetic.main.view_game_word.view.*

class GameWordView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private val ANIMATION_SLIDE_TIME = 200L

    private val zeroWitdh: Int = context.resources.getDimension(R.dimen.swipeable_button_zero_width).toInt()

    private var translationTextView: TextView? = null
    private var recyclerViewGameWords: RecyclerView? = null
    private var rootCard: CardView? = null
    private var backCard: CardView? = null
    private var progressBar: ProgressSquareView? = null
    private var buttonStart: MaterialButton? = null

    private lateinit var gameWordAdapter: GameWordsAdapter
    private lateinit var listener: GameWordViewSelectListener
    private var isFirstShowing = true


    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_game_word, this, true)

        this.translationTextView = v.tv_game_item_translation
        this.recyclerViewGameWords = v.rl_game_words
        this.rootCard = v.cv_root_game_item
        this.backCard = v.cv_back_game_item
        this.progressBar = v.psv_game_words_loading
        this.buttonStart = v.btn_start_game_words

        this.gameWordAdapter =
            GameWordsAdapter() { gameWordModel ->
                listener.onGameWordSelected(gameWordModel)
            }

        this.buttonStart?.setOnClickListener {
            listener.onReadyPressed()
        }

        this.recyclerViewGameWords?.adapter = this.gameWordAdapter
        this.recyclerViewGameWords?.layoutManager = GridLayoutManager(context, 2)
    }

    fun setPercentage(percent: Int) {
        progressBar?.setPercentage(percent)
    }

    fun setState(state: GameWordsViewState) {
        when(state) {
            GameWordsViewState.STATE_LOADING -> {
                progressBar?.visibility = View.VISIBLE
//                progressBar?.start()
                buttonStart?.visibility = View.GONE
            }
            GameWordsViewState.STATE_READY -> {
                progressBar?.visibility = View.GONE
                progressBar?.stop()
                buttonStart?.visibility = View.VISIBLE
            }
            GameWordsViewState.STATE_STARTED -> {
                progressBar?.visibility = View.GONE
                progressBar?.stop()
                buttonStart?.visibility = View.GONE
            }
        }
    }

    fun showNext(gameWordsListModel: GameWordsListModel) {
        if(isFirstShowing) {
            initData(gameWordsListModel)
            showView()
            this.isFirstShowing = false
        } else {
            safeLet(this.rootCard, this.backCard) { root, back ->
                slideView(root, root.width, zeroWitdh, {}, {
                    root.visibility = View.INVISIBLE
                    root.alpha = 0f
                    initData(gameWordsListModel)
                    showView()
                })
            }
        }
    }

    private fun initData(gameWordsListModel: GameWordsListModel) {
        this.translationTextView?.text = gameWordsListModel.translation
        this.gameWordAdapter.replaceAll(gameWordsListModel.arrWordItemModels)
    }

   fun setListener(listener: GameWordViewSelectListener) {
       this.listener = listener
   }

    fun setTips(arr: ArrayList<Long>) {
        gameWordAdapter.disableButtons(arr)
    }

    fun showResults(dontKnow: Boolean) {
        gameWordAdapter.showResults(dontKnow)
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

    interface GameWordViewSelectListener {
        fun onGameWordSelected(gameWordItemModel: GameWordItemModel?)
        fun onReadyPressed()
    }

}