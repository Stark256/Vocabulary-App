package com.vocabulary.utils

import android.app.Activity
import android.content.Intent
import com.vocabulary.R
import com.vocabulary.models.CustomTheme


object ThemeUtils {

    private var sTheme: CustomTheme = CustomTheme.THEME_OCEAN


    fun changeToTheme(activity: Activity) {
//        if(sTheme == CustomTheme.THEME_BLUE) {
//            sTheme = CustomTheme.THEME_RED
//        } else {
//            sTheme = CustomTheme.THEME_BLUE
//        }



        activity.finish()
        activity.startActivity(Intent(activity, activity.javaClass))
//        activity.overridePendingTransition(
//            android.R.anim.fade_in,
//            android.R.anim.fade_out
//        )
    }

    fun onActivityCreateSetTheme(activity: Activity) {
//        when (sTheme) {
//            CustomTheme.THEME_BLUE -> activity.setTheme(R.style.Theme_Custom_Blue)
//            CustomTheme.THEME_RED -> activity.setTheme(R.style.Theme_Custom_Red)
//            else -> activity.setTheme(R.style.Theme_Custom_Blue)
//        }
    }

}
