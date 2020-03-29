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
import com.vocabulary.ui.game.game_letters.GameLetterListViewAdapter
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

    fun disableButtons(arr: ArrayList<Long>) {
        adapter.disableButtons(arr)
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

    fun setItemGuess(letter: String?, uniqueID: Long, position: Int, fromCheck: Boolean) {
        if(!isResultShown) {
            if (fromCheck) {
                val letterModel = letterArrays.first { i -> i.uniqueID == uniqueID }
                letterModel.type = GameLetterItemModelState.GS_LETTER
                adapter.replaceAll(letterArrays)
            } else {
                val letterModel = letterArrays[position]
                letterModel.type = GameLetterItemModelState.GS_INVISIBLE
                adapter.replaceAll(letterArrays)
            }
        }
    }

    fun setItemCheck(letter: String?, uniqueID: Long, position: Int, fromGuess: Boolean) {
        if(!isResultShown) {
            if (fromGuess) {
                val letterModel = letterArrays[cursorIndex]
                letterModel.letter = letter
                letterModel.uniqueID = uniqueID
                letterModel.type = GameLetterItemModelState.GS_LETTER

                calculateCursorPosition()
                adapter.replaceAll(letterArrays)
            } else {
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
        fun onLetterItemPressed(position: Int, letter: String?, uniqueID: Long)
        fun onCheckListIsFull(isFull: Boolean)
    }

    enum class GameLetterListViewType {
        T_CHECK,
        T_GUESS
    }
}


