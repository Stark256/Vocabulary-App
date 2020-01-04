package com.vocabulary.ui.common

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.vocabulary.ui.MainActivity

open class BaseFragment(): Fragment(){

    var isFragmentCreated  = false

    val contextMain: MainActivity
        get(){ return activity as MainActivity
        }

    fun setToolbarTitle(toolbar: Toolbar, title: String){
        toolbar.title = title
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
}
