package com.vocabulary.ui.words

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.vocabulary.R
import com.vocabulary.ui.common.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_word.*


class WordDialog: BaseDialogFragment() {

    companion object {
        const val ARG_WORD_STRING = "word_string"
        const val ARG_TRANSLATION_STRING = "translation_string"

        fun newInstance(word: String? = null, translation: String? = null, listener: WordsDialogListener): WordDialog {
            return WordDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_WORD_STRING, word)
                    putString(ARG_TRANSLATION_STRING, translation)
                }
                this.listener = listener
            }
        }
    }

    private var word: String? = null
    private var translation: String? = null
    private lateinit var listener: WordsDialogListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
        return inflater.inflate(R.layout.dialog_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            word = it.getString(ARG_WORD_STRING)
            translation = it.getString(ARG_TRANSLATION_STRING)
        }

        if(word != null && translation != null) {
            tv_dialog_title.text = context?.getString(R.string.edit_word)
            btn_ok.text = context?.getString(R.string.edit)
            et_word.setText(word)
            et_translation.setText(translation)
        } else {
            tv_dialog_title.text = context?.getString(R.string.add_new_word)
            btn_ok.text = context?.getString(R.string.add)
        }

        context?.let {
            et_word.requestFocus()
            showKeyboard(it)
        }

        et_word.filters = getInputFilters()
        et_translation.filters = getInputFilters()

        et_translation.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                okPressed()
            }
            false
        }

        btn_cancel.setOnClickListener {
            context?.let { closeKeyboard(it) }
            dismiss()
        }

        btn_ok.setOnClickListener {
            okPressed()
        }
    }

    private fun okPressed() {
        dialog?.setCanceledOnTouchOutside(false)
        pb_word.visibility = View.VISIBLE
        btn_ok.visibility = View.GONE
        btn_cancel.visibility = View.GONE
        iv_warning.visibility = View.GONE
        tv_warning.visibility = View.GONE

        listener.onOKPressed(
            replaceWithPattern(et_word.text.toString()),
            replaceWithPattern(et_translation.text.toString()))
        {
            setResult(it)
        }
    }

    private fun setResult(warning: String?) {
        if(warning != null) {
            dialog?.setCanceledOnTouchOutside(true)
            pb_word.visibility = View.GONE
            btn_ok.visibility = View.VISIBLE
            btn_cancel.visibility = View.VISIBLE
            iv_warning.visibility = View.VISIBLE
            tv_warning.visibility = View.VISIBLE
            tv_warning.text = warning
        } else {
            context?.let { closeKeyboard(it) }
            dismiss()
        }
    }

    interface WordsDialogListener {
        fun onOKPressed(word: String, translation: String, result: (String?) -> Unit)
    }
}
