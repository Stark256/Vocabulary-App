package com.vocabulary.ui.common

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.vocabulary.R
import com.vocabulary.models.LanguageModel
import com.vocabulary.models.WordModel
import kotlinx.android.synthetic.main.dialog_delete.*

class DeletingDialog : DialogFragment(){

    companion object {
        const val ARG_LANGUAGE_MODEL = "language_model"
        const val ARG_WORD_MODEL = "word_model"
        const val ARG_TYPE = "model_type"
        const val TYPE_LANGUAGE = 1
        const val TYPE_WORD = 2


        fun newInstance(languageModel: LanguageModel,
                        listener: SureingDialogListener): DeletingDialog {
            return DeletingDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_LANGUAGE_MODEL, languageModel)
                    putInt(ARG_TYPE, TYPE_LANGUAGE)
                }
                this.listener = listener
            }
        }

        fun newInstance(wordModel: WordModel,
                        listener: SureingDialogListener): DeletingDialog {
            return DeletingDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_WORD_MODEL, wordModel)
                    putInt(ARG_TYPE, TYPE_WORD)
                }
                this.listener = listener
            }
        }
    }

    private lateinit var listener: SureingDialogListener

    private var type: Int = 0
    private var languageModel: LanguageModel? = null
    private var wordModel: WordModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
        return inflater.inflate(R.layout.dialog_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            this.type = it.getInt(ARG_TYPE)
            when(type) {
                TYPE_LANGUAGE -> this.languageModel = it.getSerializable(ARG_LANGUAGE_MODEL) as? LanguageModel
                TYPE_WORD -> this.wordModel = it.getSerializable(ARG_WORD_MODEL) as? WordModel
            }
        }

        when(type) {
            TYPE_LANGUAGE -> {
                tv_dialog_title?.text = context?.getString(R.string.message_sure_language)
                tv_warning?.text = context?.getString(R.string.message_deleting_language)
                tv_model_title?.visibility = View.VISIBLE
                tv_warning?.visibility = View.VISIBLE
                iv_warning?.visibility = View.VISIBLE
                context?.let {
                    tv_model_title?.text = String.format(context!!.getString(R.string.delete_language_model), languageModel!!.name, languageModel!!.wordsCount)
                }
            }
            TYPE_WORD -> {
                tv_dialog_title?.text = context?.getString(R.string.message_sure_word)
                tv_model_title?.visibility = View.VISIBLE
                tv_model_title?.text = wordModel?.word
            }
        }




        btn_cancel.setOnClickListener { dismiss() }
        btn_ok.setOnClickListener {
            okPressed()
            listener.onOKPressed() { dismiss() }
        }
    }


    private fun okPressed() {
        dialog?.setCanceledOnTouchOutside(false)
        pb_languages.visibility = View.VISIBLE
        btn_ok.visibility = View.GONE
        btn_cancel.visibility = View.GONE
    }

    interface SureingDialogListener {
        fun onOKPressed(result: () -> Unit)
    }

}
