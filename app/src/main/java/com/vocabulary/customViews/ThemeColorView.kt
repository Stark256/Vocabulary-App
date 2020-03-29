package com.vocabulary.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.vocabulary.R
import com.vocabulary.models.theme_models.ThemeColorModel
import kotlinx.android.synthetic.main.view_theme_color.view.*

class ThemeColorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){


    private var root: RelativeLayout? = null
    private var checkBackground: View? = null
    private var iconCheck: ImageView? = null
    private var cardColor: CardView? = null
    private var themeColor: View? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_theme_color, this, true)

        this.checkBackground = view.view_background
        this.iconCheck = view.iv_icon_check
        this.cardColor = view.cv_theme_color
        this.themeColor = view.view_theme_color
        this.root = view.root
    }

    fun selectTheme() {
        this.checkBackground?.visibility = View.VISIBLE
        this.iconCheck?.visibility = View.VISIBLE
    }

    fun unselectTheme() {
        this.checkBackground?.visibility = View.GONE
        this.iconCheck?.visibility = View.GONE
    }

    fun setTheme(themeColorModel: ThemeColorModel) {

        if(themeColorModel.colorRes != null) {
            this.themeColor?.setBackgroundColor(ContextCompat.getColor(context, themeColorModel.colorRes))
        } else if (themeColorModel.drawableRes != null) {
            this.themeColor?.background = ContextCompat.getDrawable(context, themeColorModel.drawableRes)
        }

    }


    fun listener(click: () -> Unit) {
        this.root?.setOnClickListener { click.invoke() }
    }


}
