package com.vocabulary.ui.common

import android.content.Context
import androidx.fragment.app.DialogFragment
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.InputFilter
import android.text.Spanned
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import java.util.regex.Pattern


open class BaseDialogFragment: DialogFragment() {


    fun showKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun closeKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun getInputFilters() : Array<InputFilter> =
        arrayOf(object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence {


                var resultString = ""
                for(i in start..end-1) {
                    if(source != null ) {
//                        Pattern.compile("^[a-zA-Z ]+$").matcher(source[i].toString()).matches()) {

                        if(Character.isLetter(source[i]) || source[i].toString() == " ") {
                            resultString += source[i]
                        }
                    }
                }
                return resultString
            }
        }, InputFilter.LengthFilter(25))

    fun replaceWithPattern(string: String) : String {
        val ptn = Pattern.compile("\\s+")
        val mtch = ptn.matcher(string.trim().toLowerCase())
        return mtch.replaceAll(" ")

    }
 }
