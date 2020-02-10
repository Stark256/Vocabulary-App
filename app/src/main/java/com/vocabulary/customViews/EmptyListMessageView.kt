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
                this.textTitle?.text = context.getString(R.string.no_words_found_title)
                this.textMessage?.text = context.getString(R.string.no_words_found_message)
                this.btnAdd?.text = context.getString(R.string.button_add_new_word)
            }
            ListType.SELECT_LANGUAGES -> {
                this.textTitle?.text = context.getString(R.string.no_words_found_title)
                this.textMessage?.text = context.getString(R.string.no_words_found_message)
                this.btnAdd?.text = context.getString(R.string.button_add_new_word)
            }
            ListType.ADD_LANGUAGES -> {
                this.textTitle?.text = context.getString(R.string.no_languages_found_title)
                this.textMessage?.text = context.getString(R.string.no_languages_found_message)
                this.btnAdd?.text = context.getString(R.string.button_add_new_language)
            }
            ListType.ADD_GAMES -> {
                this.textTitle?.text = context.getString(R.string.no_tests_found_title)
                this.textMessage?.text = context.getString(R.string.no_tests_found_message)
                this.btnAdd?.text = context.getString(R.string.button_add_new_test)
            }
        }
    }


    enum class ListType {
        ADD_LANGUAGES,
        SELECT_LANGUAGES,
        ADD_WORDS,
        ADD_GAMES
    }

}
