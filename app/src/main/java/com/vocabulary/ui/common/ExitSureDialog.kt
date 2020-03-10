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
import kotlinx.android.synthetic.main.dialog_exit_sure.*

class ExitSureDialog : DialogFragment() {

    companion object {
        fun newInstance(listener: ExitSureDialogListener)
                : ExitSureDialog {
            return ExitSureDialog().apply {
                this.listener = listener
            }
        }
    }

    private lateinit var listener: ExitSureDialogListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
        return inflater.inflate(R.layout.dialog_exit_sure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_cancel?.setOnClickListener { dismiss() }
        btn_ok?.setOnClickListener {
            listener.onOKPressed()
            dismiss()
        }

    }

    interface ExitSureDialogListener {
        fun onOKPressed()
    }
}