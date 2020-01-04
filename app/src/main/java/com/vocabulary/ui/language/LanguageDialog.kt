package com.vocabulary.ui.language

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import com.vocabulary.R
import com.vocabulary.ui.common.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_language.*

class LanguageDialog: BaseDialogFragment() {

    companion object {
        const val ARG_LANGUAGE_STRING = "language_string"

        fun newInstance(language: String? = null, listener: LanguageDialogListener): LanguageDialog {
            return LanguageDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_LANGUAGE_STRING, language)
                }
                this.listener = listener
            }
        }
    }

    private var language: String? = null
    private lateinit var listener: LanguageDialogListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
        return inflater.inflate(R.layout.dialog_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            language = it.getString(ARG_LANGUAGE_STRING)
        }

        if(language != null) {
            tv_dialog_title.text = context?.getString(R.string.edit_language)
            et_language.setText(language)
            btn_ok.text = context?.getString(R.string.edit)
        }
        else {
            tv_dialog_title.text = context?.getString(R.string.add_new_language)
            btn_ok.text = context?.getString(R.string.add)
        }

        context?.let {
            et_language.requestFocus()
            showKeyboard(it)
        }

        btn_cancel.setOnClickListener { dismiss() }
        btn_ok.setOnClickListener {

            okPressed()
            listener.onOKPressed(et_language.text.toString()) {
                setResult(it)
            }
//            dismiss()
        }
    }

    private fun okPressed() {
        dialog?.setCanceledOnTouchOutside(false)
        pb_languages.visibility = View.VISIBLE
        btn_ok.visibility = View.GONE
        btn_cancel.visibility = View.GONE
        iv_warning.visibility = View.GONE
        tv_warning.visibility = View.GONE
    }

    fun setResult(warning: String?) {
        if(warning != null) {
            dialog?.setCanceledOnTouchOutside(true)
            pb_languages.visibility = View.GONE
            btn_ok.visibility = View.VISIBLE
            btn_cancel.visibility = View.VISIBLE
            iv_warning.visibility = View.VISIBLE
            tv_warning.visibility = View.VISIBLE
            tv_warning.text = warning
        } else {
            context?.let{ closeKeyboard(it) }
            dismiss()
        }
    }

    interface LanguageDialogListener {
        fun onOKPressed(title: String, result: (String?) -> Unit)
    }


}
