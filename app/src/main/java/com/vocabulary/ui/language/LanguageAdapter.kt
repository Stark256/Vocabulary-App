package com.vocabulary.ui.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.customViews.swipeable_view.SwipeLanguageClickListener
import com.vocabulary.customViews.swipeable_view.SwipeableItemView
import com.vocabulary.customViews.swipeable_view.SwipeableViewHolderInterface
import com.vocabulary.models.LanguageModel
import kotlinx.android.synthetic.main.item_language.view.*

class LanguageAdapter(val listener: SwipeLanguageClickListener): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private lateinit var context: Context
    private val dataArr = ArrayList<LanguageModel>()

    fun replaceAll(arr: ArrayList<LanguageModel>) {
        dataArr.clear()
        if(arr.isNotEmpty()) {
            dataArr.addAll(arr)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        this.context = parent.context

        return LanguageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_language, parent, false))
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }


    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val item = dataArr[position]
//        holder.name.text = item.name
//        holder.count.text = item.wordsCount.toString()
//
//        if(Injector.languageManager.isSelected(item)) {
//            holder.check.check()
//        } else {
//            holder.check.uncheck()
//        }
//
//        holder.container.setOnClickListener {
//            if(!Injector.languageManager.isSelected(item)) {
//                this.listener.onLanguagePressed(item)
//            }
//        }
//
//        holder.check.setOnClickListener {
//            if(!Injector.languageManager.isSelected(item)) {
//                this.listener.onLanguagePressed(item)
//            }
//        }

        holder.swipeableView.setLanguageModel(position, item, listener)

    }

    override fun onViewDetachedFromWindow(holder: LanguageViewHolder) {
        holder.swipeableView.closeDetached()
        super.onViewDetachedFromWindow(holder)
    }

    class LanguageViewHolder(v: View) : SwipeableViewHolderInterface, RecyclerView.ViewHolder(v) {
        val swipeableView = v.swipeable_view_language
        init {
            swipeableView.initLanguageView()
        }

        override fun getSwipableItemView(): SwipeableItemView {
            return this.swipeableView
        }
    }



}
