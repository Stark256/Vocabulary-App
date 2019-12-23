package com.vocabulary.ui.words

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.models.LetterModel
import com.vocabulary.models.WordBaseItem
import com.vocabulary.models.WordModel
import kotlinx.android.synthetic.main.item_filter_letter.view.*
import kotlinx.android.synthetic.main.item_words_letter.view.*
import kotlinx.android.synthetic.main.item_words_word.view.*

class WordsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var context: Context
    private val dataArr = ArrayList<WordBaseItem>()

    fun replaceAll(arr: ArrayList<WordBaseItem>) {
        dataArr.clear()
        if(arr.isNotEmpty()) {
            dataArr.addAll(arr)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return dataArr[position].getType().value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return when(viewType) {
            WordBaseItem.WordItemType.TYPE_WORD.value -> {
                WordViewHolder(LayoutInflater.from(context).inflate(R.layout.item_words_word, parent, false))
            }
            else -> {
                LetterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_words_letter, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataArr[position]
        when(item.getType()) {
            WordBaseItem.WordItemType.TYPE_WORD -> {
                (holder as WordViewHolder).word.text = (item as WordModel).word
                (holder as WordViewHolder).translate.text = (item as WordModel).translate
            }

            WordBaseItem.WordItemType.TYPE_LETTER -> {
                (holder as LetterViewHolder).letter.text = (item as LetterModel).letter
            }
        }
    }

    private class WordViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val word = v.tv_word
        val translate = v.tv_translate
    }

    class LetterViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val letter = v.tv_word_letter
    }
}
