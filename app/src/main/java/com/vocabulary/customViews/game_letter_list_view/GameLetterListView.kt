package com.vocabulary.customViews.game_letter_list_view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.game_letters_models.GameLetterItemModel
import com.vocabulary.models.game_letters_models.GameLetterItemModelState
import kotlinx.android.synthetic.main.item_game_letter.view.*
import kotlinx.android.synthetic.main.view_game_letter_list.view.*

class GameLetterListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private val size_line_1: Int = context.resources.getDimension(R.dimen.game_letters_list_height_1_lines).toInt()
    private val size_line_2: Int = context.resources.getDimension(R.dimen.game_letters_list_height_2_lines).toInt()
    private val size_line_3: Int = context.resources.getDimension(R.dimen.game_letters_list_height_3_lines).toInt()
    private val size_line_4: Int = context.resources.getDimension(R.dimen.game_letters_list_height_4_lines).toInt()


    private var recyclerViewLetters: RecyclerView? = null
    private lateinit var adapter: GameLetterListViewAdapter
    private lateinit var type: GameLetterListViewType
    private val letterArrays = ArrayList<GameLetterItemModel>()
    private lateinit var listener: GameLetterListViewSelectListener

    private var cursorIndex = 0
    private var isCheckListIsFull = false
    private var isResultShown = false

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.view_game_letter_list, this, true)

        this.recyclerViewLetters = v.rv_game_letter_list

        // TODO init adapter
        this.adapter = GameLetterListViewAdapter(
            object : GameLetterListViewAdapter.GameLetterAdapterSelectListener {
                override fun onLetterPressed(position: Int) {
                    val selectedLetter = letterArrays[position]
                    listener.onLetterItemPressed(position, selectedLetter.letter, selectedLetter.uniqueID)
                }
            })
        this.recyclerViewLetters?.adapter = adapter
        this.recyclerViewLetters?.layoutManager = GridLayoutManager(context, 8)
    }



    fun initViewGuessItems(arr: ArrayList<GameLetterItemModel>,
                           listener: GameLetterListViewSelectListener) {
        this.listener = listener
        this.type = GameLetterListViewType.T_GUESS
        setCardSize(arr.size)
        isResultShown = false
        letterArrays.clear()
        adapter.reinit()
        letterArrays.addAll(arr)
        adapter.replaceAll(arr)
    }

    fun initViewCheckItems(size: Int,
                           listener: GameLetterListViewSelectListener) {
        this.listener = listener
        this.type = GameLetterListViewType.T_CHECK
        adapter.reinit()
        isResultShown = false
        cursorIndex = 0
        isCheckListIsFull = false
        setCardSize(size)
        generateCheckItems(size)
    }

    fun setItemGuess(letter: String?, uniqueID: Int, position: Int, fromCheck: Boolean) {
        if(!isResultShown) {
            if (fromCheck) {
                // TODO find by letter and make visible
                val letterModel = letterArrays.first { i -> i.uniqueID == uniqueID }
                letterModel.type = GameLetterItemModelState.GS_LETTER
                adapter.replaceAll(letterArrays)
            } else {
                // TODO set invisible for position
                val letterModel = letterArrays[position]
                letterModel.type = GameLetterItemModelState.GS_INVISIBLE
                adapter.replaceAll(letterArrays)
            }
        }

    }

    fun setItemCheck(letter: String?, uniqueID: Int, position: Int, fromGuess: Boolean) {
        if(!isResultShown) {
            if (fromGuess) {
                // TODO set letter for cusros position

                val letterModel = letterArrays[cursorIndex]
                letterModel.letter = letter
                letterModel.uniqueID = uniqueID
                letterModel.type = GameLetterItemModelState.GS_LETTER

                calculateCursorPosition()
                adapter.replaceAll(letterArrays)
            } else {
                // TODO make empty by position
                val letterModel = letterArrays[position]
                letterModel.type = GameLetterItemModelState.GS_EMPTY

                val cursorLetter = letterArrays[cursorIndex]
                if (cursorLetter.type == GameLetterItemModelState.GS_CURSOR) {
                    cursorLetter.type = GameLetterItemModelState.GS_EMPTY
                }

                calculateCursorPosition()
                adapter.replaceAll(letterArrays)
            }

            val isNowFull = isCheckListFull()

            if (isCheckListIsFull != isNowFull) {
                isCheckListIsFull = isNowFull
                listener.onCheckListIsFull(isNowFull)
            }
        }
    }

    fun canAddMore() : Boolean {
        var canAddMore = false
        for(item in letterArrays) {
            if(item.type == GameLetterItemModelState.GS_CURSOR) {
                canAddMore = true
                break
            }
        }
        return canAddMore
    }

    fun getCheckListResult() : String {
        var resultString = ""
        for(item in letterArrays) {
            if(item.type == GameLetterItemModelState.GS_LETTER) {
                resultString += item.letter ?: ""
            }
        }
        return resultString
    }

    private fun isCheckListFull() : Boolean {
        var isFull = true
        for(item in letterArrays) {
            if(item.type != GameLetterItemModelState.GS_LETTER) {
                isFull = false
            }
        }
        return isFull
    }

    private fun calculateCursorPosition() {
        var shouldSetCursor = true
        for(index in 0..letterArrays.size -1) {
            if(letterArrays[index].type ==
                GameLetterItemModelState.GS_EMPTY
                && shouldSetCursor) {
                letterArrays[index].type = GameLetterItemModelState.GS_CURSOR
                cursorIndex = index
                shouldSetCursor = false
            }
        }
    }

    private fun generateCheckItems(size: Int) {
        val arr = ArrayList<GameLetterItemModel>()
        for(i in 0..size-1) {
            if(i == 0) {
                arr.add(GameLetterItemModel(
                    GameLetterItemModelState.GS_CURSOR))
            } else {
                arr.add(GameLetterItemModel(
                    GameLetterItemModelState.GS_EMPTY))
            }
        }
        letterArrays.clear()
        letterArrays.addAll(arr)
        adapter.replaceAll(arr)
    }

    fun showGuessResults(correctWord: ArrayList<GameLetterItemModel>) {
        isResultShown = true
        letterArrays.clear()
        letterArrays.addAll(correctWord)
        adapter.showResult()
        adapter.replaceAll(letterArrays)
    }

    fun showCheckResult(correctWord: ArrayList<GameLetterItemModel>, dontKnow: Boolean) {
        isResultShown = true
        if(!dontKnow) {
            for (index in 0..letterArrays.size - 1) {
                val item = letterArrays[index]
                item.isCorrect = item.letter == correctWord[index].letter
            }
        } else {
            letterArrays.clear()
            letterArrays.addAll(correctWord)
        }
        adapter.showResult(dontKnow)
        adapter.replaceAll(letterArrays)
    }

    private fun setCardSize(size: Int) {

        if(size <= 8) {

            recyclerViewLetters?.layoutParams?.height = size_line_1

        } else if(size > 8 && size <= 16) {

            recyclerViewLetters?.layoutParams?.height = size_line_2

        } else if(size > 16 && size <= 24) {

            recyclerViewLetters?.layoutParams?.height = size_line_3

        } else if(size > 24 && size <= 32) {

            recyclerViewLetters?.layoutParams?.height = size_line_4

        }
    }

    interface GameLetterListViewSelectListener {
        fun onLetterItemPressed(position: Int, letter: String?, uniqueID: Int)
        fun onCheckListIsFull(isFull: Boolean)
    }

    enum class GameLetterListViewType {
        T_CHECK,
        T_GUESS
    }
}

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

