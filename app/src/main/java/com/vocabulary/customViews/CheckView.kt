package com.vocabulary.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.safeLet
import kotlinx.android.synthetic.main.view_check.view.*

class CheckView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private var root: LinearLayout? = null
    private var check: ImageView? = null


    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_check, this, true)

        this.root = view.root
        this.check = view.iv_check
    }

    fun check() {
        safeLet(root, check) { sRoot, sCheck ->
            Injector.themeManager.customizeCheckView(context, sRoot, sCheck, true)
        }
    }

    fun uncheck() {
        safeLet(root, check) { sRoot, sCheck ->
            Injector.themeManager.customizeCheckView(context, sRoot, sCheck, false)
        }    }


}
