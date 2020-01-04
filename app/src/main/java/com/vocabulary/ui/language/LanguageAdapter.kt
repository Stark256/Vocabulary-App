package com.vocabulary.ui.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.LanguageModel
import kotlinx.android.synthetic.main.item_language.view.*

class LanguageAdapter(val listener: LanguageClickListener): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private lateinit var context: Context
    private val dataArr = ArrayList<LanguageModel>()

    fun replaceAll(arr: ArrayList<LanguageModel>) {
        dataArr.clear()
        if(arr.isNotEmpty()) {
            dataArr.addAll(arr)
        }
        notifyDataSetChanged()
    }

    fun getItemByPosition(position: Int) : LanguageModel = dataArr[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        this.context = parent.context

        return LanguageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_language, parent, false))
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val item = dataArr[position]
        holder.name.text = item.name
        holder.count.text = item.wordsCount.toString()

        if(Injector.languageManager.isSelected(item)) {
            holder.check.check()
        } else {
            holder.check.uncheck()
        }

        holder.container.setOnClickListener {
            if(!Injector.languageManager.isSelected(item)) {
                this.listener.onLanguagePressed(item)
            }
        }

        holder.check.setOnClickListener {
            if(!Injector.languageManager.isSelected(item)) {
                this.listener.onLanguagePressed(item)
            }
        }
    }

    class LanguageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.tv_language_name
        val count = v.tv_language_words_count
        val check = v.view_check
        val container = v.rl_language_container
    }

    interface LanguageClickListener {
        fun onLanguagePressed(languageModel: LanguageModel)
    }

}
