package com.vocabulary.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.vocabulary.R
import com.vocabulary.models.CustomTheme

class ThemeManager {

    lateinit var currentTheme: CustomTheme

    fun setTheme(currentTheme: String?) {
        when(currentTheme) {
            CustomTheme.THEME_BLUE.value -> this.currentTheme = CustomTheme.THEME_BLUE
            CustomTheme.THEME_RED.value -> this.currentTheme = CustomTheme.THEME_RED
            else -> this.currentTheme = CustomTheme.THEME_BLUE
        }
    }

    fun changeToTheme(activity: Activity) {
        if(currentTheme == CustomTheme.THEME_BLUE) {
            currentTheme = CustomTheme.THEME_RED
        } else {
            currentTheme = CustomTheme.THEME_BLUE
        }
        Injector.storageManager.setTheme(currentTheme.value)


        activity.finish()
        activity.startActivity(Intent(activity, activity.javaClass))
//        activity.overridePendingTransition(
//            android.R.anim.fade_in,
//            android.R.anim.fade_out
//        )
    }

    fun onActivityCreateSetTheme(activity: Activity) {
        when (currentTheme) {
            CustomTheme.THEME_BLUE -> activity.setTheme(R.style.Theme_Custom_Blue)
            CustomTheme.THEME_RED -> activity.setTheme(R.style.Theme_Custom_Red)
        }
    }

    fun getAccentColor(context: Context) : Int {
        return when (currentTheme) {
            CustomTheme.THEME_BLUE -> ContextCompat.getColor(context, R.color.theme_blue)
            CustomTheme.THEME_RED -> ContextCompat.getColor(context, R.color.theme_red)
        }

    }

    fun customizeWordEditBackground(context: Context, circle: View, imageView: ImageView, isEnabled: Boolean) {
        when (currentTheme) {
            CustomTheme.THEME_BLUE -> {
                if (isEnabled) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit_blue))
                    circle.visibility = View.VISIBLE
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit))
                    circle.visibility = View.GONE
                }
            }
            CustomTheme.THEME_RED -> {
                if (isEnabled) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit_red))
                    circle.visibility = View.VISIBLE
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit))
                    circle.visibility = View.GONE
                }
            }
        }
    }

    fun customizeLetterBackground(context: Context, background: RelativeLayout, text: TextView, isSelected: Boolean) {
        when (currentTheme) {
            CustomTheme.THEME_BLUE -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_blue))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_blue_with_white_border)
                }
            }
            CustomTheme.THEME_RED -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_red))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_red_with_white_border)
                }
            }
        }
    }

}
