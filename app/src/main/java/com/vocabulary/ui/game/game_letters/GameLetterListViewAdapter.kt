package com.vocabulary.ui.game.game_letters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.game_letters_models.GameLetterItemModel
import com.vocabulary.models.game_letters_models.GameLetterItemModelState
import com.vocabulary.models.getByModelID
import kotlinx.android.synthetic.main.item_game_letter.view.*

class GameLetterListViewAdapter(
    private val listener: GameLetterAdapterSelectListener) : RecyclerView
.Adapter<GameLetterListViewAdapter.GameLetterListViewHolder>(){

    private val dataArr = ArrayList<GameLetterItemModel>()
    private lateinit var context: Context
    private var showResult = false
    private var reverseResult = false

    fun replaceAll(arr: ArrayList<GameLetterItemModel>) {
        this.dataArr.clear()
        this.dataArr.addAll(arr)
        notifyDataSetChanged()
    }

    fun disableButtons(arr: ArrayList<Long>) {
        for(item in arr) {
            dataArr.getByModelID(item).type = GameLetterItemModelState.GS_INVISIBLE
        }
        notifyDataSetChanged()
    }

    fun reinit() {
        showResult = false
        reverseResult = false
    }

    fun showResult(dontKnow: Boolean = false) {
        this.showResult = true
        if(dontKnow) {
            reverseResult = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLetterListViewHolder {
        this.context = parent.context
        return GameLetterListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_game_letter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun onBindViewHolder(holder: GameLetterListViewHolder, position: Int) {
        val item = dataArr[position]

        if(!showResult) {
            when(item.type) {
                GameLetterItemModelState.GS_EMPTY -> holder.makeEmpty()
                GameLetterItemModelState.GS_CURSOR -> holder.makeCursor()
                GameLetterItemModelState.GS_LETTER -> holder.makeLetter(item.letter)
                GameLetterItemModelState.GS_INVISIBLE-> holder.makeInvisible()
            }
        } else {
            if(reverseResult) {
                holder.showResultReverse(item.isCorrect, item.letter)
            } else {
                holder.showResult(item.isCorrect, item.letter)
            }
        }

        holder.itemView.setOnClickListener {
            if(!showResult && item.type == GameLetterItemModelState.GS_LETTER) {
                listener.onLetterPressed(position)
            }
        }
    }

    inner class  GameLetterListViewHolder(v: View)
        : RecyclerView.ViewHolder(v) {

        private val elevation0 = context.resources.getDimension(R.dimen.card_elevation_0)
        private val elevation4 = context.resources.getDimension(R.dimen.card_elevation_4)

        val textLetter: TextView = v.tv_game_letter
        val cardLetter: CardView = v.cv_game_letter
        val cardBack: RelativeLayout = v.rl_game_letter_back

        fun showResultReverse(isItemCorrect: Boolean, letter: String?) {
            textLetter.visibility = View.VISIBLE
            textLetter.text = letter
            Injector.themeManager.changeTextColorToWhite(context, textLetter)
            cardBack.background = ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
            if(isItemCorrect) {
                cardLetter.cardElevation = elevation4
                Injector.themeManager.changeCardBackgroundColorToGrey(context, cardLetter)
            } else {
                cardLetter.cardElevation = elevation4
                Injector.themeManager.changeCardBackgroundColorToAccent(context, cardLetter)
            }
        }

        fun showResult(isItemCorrect: Boolean, letter: String?) {
            textLetter.visibility = View.VISIBLE
            textLetter.text = letter
            Injector.themeManager.changeTextColorToWhite(context, textLetter)
            cardBack.background = ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
            if(isItemCorrect) {
                cardLetter.cardElevation = elevation4
                Injector.themeManager.changeCardBackgroundColorToAccent(context, cardLetter)
            } else {
                cardLetter.cardElevation = elevation4
                Injector.themeManager.changeCardBackgroundColorToGrey(context, cardLetter)
            }
        }

        fun makeEmpty() {
            textLetter.visibility = View.GONE
            cardLetter.cardElevation = elevation4
            Injector.themeManager.changeCardBackgroundColorToGrey(context, cardLetter)
            cardBack.background = ContextCompat.getDrawable(context, R.drawable.ic_clear)

        }

        fun makeInvisible() {
            textLetter.visibility = View.GONE
            cardLetter.cardElevation = elevation0
            Injector.themeManager.changeCardBackgroundColorToTransparent(context, cardLetter)
            cardBack.background = ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        }

        fun makeLetter(letter: String?) {
            textLetter.visibility = View.VISIBLE
            cardLetter.cardElevation = elevation4
            letter?.let { textLetter.text = it }
            Injector.themeManager.changeTextColorToAccent(context, textLetter)
            Injector.themeManager.changeCardBackgroundColorToWhite(context, cardLetter)
            cardBack.background = ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        }

        fun makeCursor() {
            textLetter.visibility = View.GONE
            cardLetter.cardElevation = elevation4
            Injector.themeManager.changeCardBackgroundColorToAccent(context, cardLetter)
            cardBack.background = ContextCompat.getDrawable(context, R.drawable.ic_center_focus)
        }


    }

    interface GameLetterAdapterSelectListener {
        fun onLetterPressed(position: Int)
    }
}
