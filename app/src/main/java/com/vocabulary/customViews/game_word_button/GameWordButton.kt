package com.vocabulary.customViews.game_word_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.vocabulary.R
import com.vocabulary.customViews.game_word_view.GameWordView
import com.vocabulary.managers.Injector
import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.safeLet
import kotlinx.android.synthetic.main.view_button_game_word.view.*

class GameWordButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private var cardResult: CardView? = null
    private var textResult: TextView? = null
    private var imageResult: ImageView? = null
    private var cardBorder: CardView? = null
    private var cardMain: CardView? = null
    private var container: RelativeLayout? = null
    private var textView: TextView? = null
    private var isViewSelected = false
    private var isViewActive = true

    private lateinit var selectListener: GameWordButtonSelectListener
//    var text: String
//        get() { return textView?.text.toString() ?: "" }
//        set(value) { textView?.text = value }

    lateinit var gameWordItemModel: GameWordItemModel

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_button_game_word, this, true)

        this.cardBorder = v.cv_game_word_border
        this.cardMain = v.cv_game_word
        this.container = v.rl_game_word_container
        this.textView = v.tv_game_word_text

        this.cardResult = v.cv_game_word_result
        this.textResult = v.tv_game_word_result
        this.imageResult = v.iv_game_word_result

        this.cardBorder?.setOnClickListener {
            if(isViewActive) {
//                makeSelected()
                selectListener.onWordButtonItemSelected(gameWordItemModel)
            }
        }
    }

    fun setSelectListener(gameWordItemModel: GameWordItemModel,
                          selectListener: GameWordButtonSelectListener) {
        this.gameWordItemModel = gameWordItemModel
        textView?.text = gameWordItemModel.word
        this.selectListener = selectListener
        hideResult()
    }

    fun makeInactive() {
        safeLet(this.cardBorder, this.cardMain, this.textView) { borderCard, mainCard, textV ->
            isViewActive = false
            if(isViewSelected) {
                isViewSelected = false
                Injector.themeManager.changeCardBackgroundColorToWhite(context, mainCard)
            }
            Injector.themeManager.changeCardBackgroundColorToGrey(context, borderCard)
            Injector.themeManager.changeTextColorToGrey(context, textV)
        }
    }

    fun makeSelected() {
        if(!isViewSelected) {
//            safeLet(this.cardBorder, this.cardMain, this.textView) { borderCard, mainCard, textV ->
//                isViewSelected = false
//                Injector.themeManager.changeCardBackgroundColorToWhite(context, mainCard)
//                Injector.themeManager.changeCardBackgroundColorToAccent(context, borderCard)
//                Injector.themeManager.changeTextColorToAccent(context, textV)
//            }
//        }

//        else {
            safeLet(this.cardBorder, this.cardMain, this.textView) { borderCard, mainCard, textV ->
                isViewSelected = true
                Injector.themeManager.changeCardBackgroundColorToAccent(context, mainCard)
                Injector.themeManager.changeCardBackgroundColorToAccent(context, borderCard)
                Injector.themeManager.changeTextColorToWhite(context, textV)
            }
        }
    }

    fun makeDefault() {
        safeLet(this.cardBorder, this.cardMain, this.textView) { borderCard, mainCard, textV ->
            if(isViewSelected) {
                isViewSelected = false
                Injector.themeManager.changeCardBackgroundColorToWhite(context, mainCard)
            }
            Injector.themeManager.changeCardBackgroundColorToAccent(context, borderCard)
            Injector.themeManager.changeTextColorToAccent(context, textV)
        }
    }

    private fun hideResult() {
        this.isViewActive = true
        this.cardResult?.visibility = View.GONE
    }

    fun showResult() {
        safeLet(this.cardResult, this.textResult, this.imageResult) { cvR, tvR, ivR ->
            cvR.visibility = View.VISIBLE
            isViewActive = false
            Injector.themeManager.customizeGameWordButtonResult(
                context, gameWordItemModel.isTrue, cvR, tvR, ivR)
        }
    }

    interface GameWordButtonSelectListener {
        fun onWordButtonItemSelected(gameWordItemModel: GameWordItemModel)
    }

}