package com.vocabulary.ui.words

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.vocabulary.R
import kotlinx.android.synthetic.main.dialog_word.*

class WordsDialog: DialogFragment() {

    companion object {
        const val ARG_WORD_STRING = "word_string"

        fun newInstance(language: String? = null, listener: WordsDialogListener): WordsDialog {
            return WordsDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_WORD_STRING, language)
                }
            }
        }
    }

    private var language: String? = null
    private lateinit var listener: WordsDialogListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
        return inflater.inflate(R.layout.dialog_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            language = it.getString(ARG_WORD_STRING)
        }

        if(language != null) {
            tv_dialog_title.text = context?.getString(R.string.edit_language)
//            et_language.setText(language)
            btn_ok.text = context?.getString(R.string.edit)
        }
        else {
            tv_dialog_title.text = context?.getString(R.string.add_new_language)
            btn_ok.text = context?.getString(R.string.add)
        }

        btn_cancel.setOnClickListener { dismiss() }
        btn_ok.setOnClickListener {
            // TODO validate language if exist
            // TODO show error if language already exist
            listener.onOKPressed("dasda")
            dismiss()
        }
    }

//    fun showExistError(error: String?) {
//        til_language?.isErrorEnabled = error != null
//        til_language?.error = error
//    }

    interface WordsDialogListener {
        fun onOKPressed(title: String)
    }

}
