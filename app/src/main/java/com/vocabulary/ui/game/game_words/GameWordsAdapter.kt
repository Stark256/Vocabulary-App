package com.vocabulary.ui.game.game_words

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.customViews.game_word_button.GameWordButton
import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.getByModelID
import kotlinx.android.synthetic.main.item_game_word.view.*

class GameWordsAdapter(private val clickResult: (GameWordItemModel?) -> Unit) :
    RecyclerView.Adapter<GameWordsAdapter.GameWordViewHolder>() {

    private var showResult = false
    private var selectedItemID = -1L
    private var dataArr: ArrayList<GameWordItemModel> = ArrayList()

    fun replaceAll(arr: ArrayList<GameWordItemModel>) {
        this.showResult = false
        this.selectedItemID = -1
        this.dataArr.clear()
        this.dataArr.addAll(arr)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameWordViewHolder {
       return GameWordViewHolder(
           LayoutInflater.from(parent.context)
               .inflate(R.layout.item_game_word, parent, false)
       )
    }

    override fun getItemCount(): Int { return dataArr.size }

    override fun onBindViewHolder(holder: GameWordViewHolder, position: Int) {
        val item = dataArr[position]
        holder.gameWordView.setSelectListener(item, object :
            GameWordButton.GameWordButtonSelectListener {
            override fun onWordButtonItemSelected(gameWordItemModel: GameWordItemModel) {
                if(!showResult) {
                    selectedItemID = gameWordItemModel.modelID
                    clickResult.invoke(gameWordItemModel)
                    notifyDataSetChanged()
                }
            }
        })

        if(item.isActive) {
            if(selectedItemID == item.modelID) {
                holder.gameWordView.makeSelected()
            } else {
                holder.gameWordView.makeDefault()
            }
        } else {
            holder.gameWordView.makeInactive()

        }

        if(showResult) {
            holder.gameWordView.showResult()
        }
    }

    class GameWordViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val gameWordView = v.gwv_game_item
    }

    fun disableButtons(arr: ArrayList<Long>) {
        for(item in arr) {
            dataArr.getByModelID(item).isActive = false
        }
        notifyDataSetChanged()
    }

    fun showResults(dontKnow: Boolean) {
        showResult = true
        val newArr = ArrayList<GameWordItemModel>()
        for(item in dataArr) {
            if(dontKnow) {
                if(item.isTrue) {
                    newArr.add(item)
                }
            } else {
                if(item.modelID == selectedItemID || item.isTrue) {
                    newArr.add(item)
                }
            }
        }
        dataArr.clear()
        dataArr.addAll(newArr)
        notifyDataSetChanged()

    }
}