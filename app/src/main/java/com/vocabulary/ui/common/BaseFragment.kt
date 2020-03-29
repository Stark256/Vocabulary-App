package com.vocabulary.ui.common

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.vocabulary.ui.main.MainActivity

open class BaseFragment(): Fragment(){

    var isFragmentCreated  = false

    val contextMain: MainActivity
        get(){ return activity as MainActivity
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentCreated = true
    }

    override fun onDestroy() {
        super.onDestroy()
        isFragmentCreated = false
    }

    fun hideSoftKeyboard(activity: Activity, view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0)
    }

    fun showSoftKeyboard(activity: Activity, view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private var isKeyboardShowing = false

    fun onKeyboardVisibilityChanged(rootView: View, isOpened: (Boolean) -> Unit) {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.rootView.height

            val keyboardHeight = screenHeight - r.bottom

            if(keyboardHeight > screenHeight * 0.15) {
                // keyboard is opened
                if(!isKeyboardShowing) {
                    isKeyboardShowing = true
                    isOpened.invoke(true)
                }
            } else {
                // keyboard is closed
                if(isKeyboardShowing) {
                    isKeyboardShowing = false
                    isOpened.invoke(false)
                }
            }


        }
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
    }, InputFilter.LengthFilter(24))
}
