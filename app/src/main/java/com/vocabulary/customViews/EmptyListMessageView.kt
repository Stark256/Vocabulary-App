package com.vocabulary.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.vocabulary.R
import kotlinx.android.synthetic.main.view_empty_list_message.view.*

class EmptyListMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr){

    private var btnAdd: MaterialButton? = null
    private var textTitle: TextView? = null
    private var textMessage: TextView? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_empty_list_message, this, true)
        this.btnAdd = view.btn_add_new
        this.textTitle = view.tv_empty_title
        this.textMessage = view.tv_empty_message
    }


    fun btnClickListener(click: () -> Unit) {
        this.btnAdd?.setOnClickListener { click.invoke() }
    }

    fun initView(type: ListType) {

        when(type) {
            ListType.ADD_WORDS -> {
                this.textMessage?.visibility = View.VISIBLE
                this.btnAdd?.visibility = View.VISIBLE
                this.textTitle?.text = context.getString(R.string.no_words_found_title)
                this.textMessage?.text = context.getString(R.string.no_words_found_message)
                this.btnAdd?.text = context.getString(R.string.button_add_new_word)
            }
            ListType.SELECT_LANGUAGES -> {
                this.textMessage?.visibility = View.VISIBLE
                this.btnAdd?.visibility = View.VISIBLE
                this.textTitle?.text = context.getString(R.string.no_languages_found_title)
                this.textMessage?.text = context.getString(R.string.no_languages_select_message)
                this.btnAdd?.text = context.getString(R.string.button_select_new_language)
            }
            ListType.ADD_LANGUAGES -> {
                this.textMessage?.visibility = View.VISIBLE
                this.btnAdd?.visibility = View.VISIBLE
                this.textTitle?.text = context.getString(R.string.no_languages_found_title)
                this.textMessage?.text = context.getString(R.string.no_languages_found_message)
                this.btnAdd?.text = context.getString(R.string.button_add_new_language)
            }
            ListType.FILTER_NOT_FOUND -> {
                this.textMessage?.visibility = View.GONE
                this.btnAdd?.visibility = View.GONE
                this.textTitle?.text = context.getString(R.string.no_words_found_title)
            }
            ListType.SELECT_LANGUAGES_EXERCISES -> {
                this.textMessage?.visibility = View.VISIBLE
                this.btnAdd?.visibility = View.GONE
                this.textTitle?.text = context.getString(R.string.no_languages_found_title)
                this.textMessage?.text = context.getString(R.string.no_languages_select_message)
            }
            ListType.NOT_ENOUGH_WORDS -> {
                this.textMessage?.visibility = View.VISIBLE
                this.btnAdd?.visibility = View.GONE
                this.textTitle?.text = context.getString(R.string.not_enough_words_for_exercises_title)
                this.textMessage?.text = context.getString(R.string.not_enough_words_for_exercises_message)
            }
        }
    }


    enum class ListType {
        ADD_LANGUAGES,
        SELECT_LANGUAGES,
        SELECT_LANGUAGES_EXERCISES,
        ADD_WORDS,
        FILTER_NOT_FOUND,
        NOT_ENOUGH_WORDS
    }

}
