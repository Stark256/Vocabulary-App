package com.vocabulary.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.vocabulary.R
import kotlinx.android.synthetic.main.view_empty_list_message.view.*

class EmptyListMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr){

    private var btnAdd: BorderedButtonView? = null
    private var textTitle: TextView? = null
    private var textMessage: TextView? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_empty_list_message, this, true)
        this.btnAdd = view.btn_add_new
        this.textTitle = view.tv_empty_title
        this.textMessage = view.tv_empty_message
    }

    fun initView(type: ListType) {
        when(type) {
            ListType.WORDS -> {
                this.textTitle?.text = context.getString(R.string.no_words_found_title)
                this.textMessage?.text = context.getString(R.string.no_words_found_message)
            }
            ListType.LANGUAGES -> {
                this.textTitle?.text = context.getString(R.string.no_languages_found_title)
                this.textMessage?.text = context.getString(R.string.no_languages_found_message)
            }
            ListType.TESTS -> {
                this.textTitle?.text = context.getString(R.string.no_tests_found_title)
                this.textMessage?.text = context.getString(R.string.no_tests_found_message)
            }
        }
    }

    enum class ListType {
        LANGUAGES,
        WORDS,
        TESTS
    }

}
