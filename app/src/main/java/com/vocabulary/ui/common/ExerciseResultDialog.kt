package com.vocabulary.ui.common

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.GameResult
import kotlinx.android.synthetic.main.dialog_game_result.*
import kotlinx.android.synthetic.main.item_dialog_game_result.view.*

class ExerciseResultDialog : DialogFragment() {

    companion object {
        const val ARG_GAME_RESULTS_LIST = "game_results_list"

        fun newInstance(arr: ArrayList<GameResult>,
                        listener: ExerciseResultDialogListener)
                : ExerciseResultDialog {
            return ExerciseResultDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_GAME_RESULTS_LIST, arr)
                }
                this.listener = listener
            }
        }
    }

    private lateinit var listener: ExerciseResultDialogListener
    private lateinit var adapter: ExerciseResultDialogAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
        return inflater.inflate(R.layout.dialog_game_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultArr: ArrayList<GameResult> =
            arguments?.getSerializable(ARG_GAME_RESULTS_LIST) as? ArrayList<GameResult> ?: ArrayList()

        this.adapter = ExerciseResultDialogAdapter(resultArr)
        rv_game_result?.adapter = adapter
        rv_game_result?.layoutManager = LinearLayoutManager(context)

        btn_finish_result?.setOnClickListener {
            dismiss()
            listener.onFinishPressed()
        }
    }

    interface ExerciseResultDialogListener {
        fun onFinishPressed()
    }
}

class ExerciseResultDialogAdapter(private val dataArr: ArrayList<GameResult>)
    : RecyclerView.Adapter<ExerciseResultDialogAdapter.ExerciseResultViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseResultViewHolder {
        this.context = parent.context
        return ExerciseResultViewHolder(
            LayoutInflater.from(context).
                inflate(R.layout.item_dialog_game_result, parent, false))
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun onBindViewHolder(holder: ExerciseResultViewHolder, position: Int) {
        holder.bindView(dataArr[position])
    }

    inner class ExerciseResultViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val cardBorder: CardView = v.cv_game_result_border
        val indicatorContainer: RelativeLayout = v.rl_game_result_indicator_container
        val indicator: ImageView = v.iv_game_result_indicator
//        val textWordTitle: TextView = v.tv_game_result_word_title
        val textWordValue: TextView = v.tv_game_result_word_value
//        val textTranslationTitle: TextView = v.tv_game_result_translation_title
        val textTranslationValue: TextView = v.tv_game_result_translation_value
        val textIncorrectTitle: TextView = v.tv_game_result_incorrect_title
        val textIncorrectValue: TextView = v.tv_game_result_incorrect_value

        fun bindView(item: GameResult) {

            textWordValue.text = item.correctWord
            textTranslationValue.text = item.correctTranslation

            if(item.isCorrect) {
                Injector.themeManager.changeCardBackgroundColorToSecondary(context, cardBorder)
                indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_game_result_correct))
                indicatorContainer.background = Injector.themeManager.getDialogItemResultBackgroundDrawable(context)
                textIncorrectTitle.visibility = View.GONE
                textIncorrectValue.visibility = View.GONE
            } else {
                Injector.themeManager.changeCardBackgroundColorToGrey(context, cardBorder)
                indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_game_result_incorrect))
                indicatorContainer.background = ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_grey)
                textIncorrectTitle.visibility = View.VISIBLE
                textIncorrectValue.visibility = View.VISIBLE
                textIncorrectValue.text = item.incorrectSelection
            }
        }
    }
}