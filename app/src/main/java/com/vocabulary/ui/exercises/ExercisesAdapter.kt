package com.vocabulary.ui.exercises

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.exercise_base_models.ExerciseBaseModel
import com.vocabulary.models.exercise_base_models.ExerciseType
import kotlinx.android.synthetic.main.item_exercise_base.view.*


class ExercisesAdapter(private var listener: ExercisesSelectListener):
    RecyclerView.Adapter<ExercisesAdapter.TestViewHolder>() {

    private lateinit var context: Context
    private var dataArr = ArrayList<ExerciseBaseModel>()

    fun replaceAll(arr: ArrayList<ExerciseBaseModel>) {
        dataArr.clear()
        dataArr.addAll(arr)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        this.context = parent.context
        return TestViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercise_base, parent, false))
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val game = dataArr[position]
        holder.gameName.text = context.getString(game.game_name_res)
        holder.wordsCount.displayedValues = game.words_count_arr
        holder.wordsCount.minValue = 0
        holder.wordsCount.maxValue = game.words_count_arr.lastIndex
        holder.rb_1.text = game.option_1_title
        holder.rb_2.text = game.option_2_title
        holder.rb_2.isEnabled = game.option_2_is_enable
        holder.rb_3.text = game.option_3_title
        holder.rb_3.isEnabled = game.option_3_is_enable




        holder.itemView.setOnClickListener {
            listener.onTestSelect(
                game.game_type,
                game.words_count_arr[holder.wordsCount.value].toInt(),
                holder.getCheckedItemsToGuess(game))
        }


    }

    inner class TestViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val guessSizeRadioGroup: RadioGroup = v.rg_base_game_guess_size
        val wordsCount: NumberPicker = v.np_base_game_words_count
        val gameName: TextView = v.tv_base_game_name
        val rb_1: RadioButton = v.rb_base_game_option_1
        val rb_2: RadioButton = v.rb_base_game_option_2
        val rb_3: RadioButton = v.rb_base_game_option_3

        init {
            wordsCount.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            rb_1.buttonTintList = Injector.themeManager.getAccentColorStateList(context)
            rb_1.setTextColor(Injector.themeManager.getAccentColorStateList(context))
            rb_2.buttonTintList = Injector.themeManager.getAccentColorStateList(context)
            rb_2.setTextColor(Injector.themeManager.getAccentColorStateList(context))
            rb_3.buttonTintList = Injector.themeManager.getAccentColorStateList(context)
            rb_3.setTextColor(Injector.themeManager.getAccentColorStateList(context))
        }

        fun getCheckedItemsToGuess(ex: ExerciseBaseModel) : Int {
            return if(rb_2.isChecked) {
                ex.option_2_title.toInt()
            } else if(rb_3.isChecked) {
                ex.option_3_title.toInt()
            } else {
                ex.option_1_title.toInt()
            }
        }
    }

    interface ExercisesSelectListener {
        fun onTestSelect(exerciseType: ExerciseType, wordsCount: Int, itemsToGuessCount: Int)
    }
}