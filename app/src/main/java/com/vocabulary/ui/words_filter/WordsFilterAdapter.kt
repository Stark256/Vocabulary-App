package com.vocabulary.ui.words_filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.word_models.LetterModel
import kotlinx.android.synthetic.main.item_filter_letter.view.*

class WordsFilterAdapter: RecyclerView.Adapter<WordsFilterAdapter.WordFilterViewHolder>() {

    private lateinit var context: Context
    private val dataArr = ArrayList<LetterModel>()

    fun replaceAll(arr: ArrayList<LetterModel>) {
        dataArr.clear()
        if(arr.isNotEmpty()) {
            dataArr.addAll(arr)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordFilterViewHolder {
        this.context = parent.context
        return WordFilterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filter_letter, parent, false))
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun onBindViewHolder(holder: WordFilterViewHolder, position: Int) {
        val item = dataArr[position]
        holder.letter.text = item.letter

        Injector.themeManager.customizeLetterBackground(this.context, holder.container, holder.letter, item.isSelected)

        holder.container.setOnClickListener {
            item.isSelected = !item.isSelected
            notifyDataSetChanged()
        }
    }

    class WordFilterViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val letter = v.tv_filter_letter
        val container = v.rl_letter_container
    }
}
