package com.vocabulary.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.vocabulary.R
import com.vocabulary.customViews.BorderedButtonView
import com.vocabulary.models.theme_models.CustomTheme
import com.vocabulary.models.theme_models.ThemeColorModel

class ThemeManager {

    lateinit var currentTheme: CustomTheme

    fun getThemes() : ArrayList<ThemeColorModel> = arrayListOf(
        ThemeColorModel(
            R.string.theme_ocean,
            CustomTheme.THEME_OCEAN,
            colorRes = R.color.theme_ocean_accent
        ),
        ThemeColorModel(
            R.string.theme_sunrise,
            CustomTheme.THEME_SUNRISE,
            colorRes = R.color.theme_sunrise_accent
        ),
        ThemeColorModel(
            R.string.theme_sundown,
            CustomTheme.THEME_SUNDOWN,
            colorRes = R.color.theme_sundown_accent
        ),
        ThemeColorModel(
            R.string.theme_grape,
            CustomTheme.THEME_GRAPE,
            colorRes = R.color.theme_grape_accent
        ),
        ThemeColorModel(
            R.string.theme_razz,
            CustomTheme.THEME_RAZZ,
            colorRes = R.color.theme_razz_accent
        ),
        ThemeColorModel(
            R.string.theme_autumn,
            CustomTheme.THEME_AUTUMN,
            colorRes = R.color.theme_autumn_accent
        ),
        ThemeColorModel(
            R.string.theme_spring,
            CustomTheme.THEME_SPRING,
            colorRes = R.color.theme_spring_accent
        )

    )

    fun setTheme(currentTheme: String?) {
        when(currentTheme) {
            CustomTheme.THEME_OCEAN.value -> this.currentTheme = CustomTheme.THEME_OCEAN
            CustomTheme.THEME_SUNRISE.value -> this.currentTheme = CustomTheme.THEME_SUNRISE
            CustomTheme.THEME_SUNDOWN.value -> this.currentTheme = CustomTheme.THEME_SUNDOWN
            CustomTheme.THEME_RAZZ.value -> this.currentTheme = CustomTheme.THEME_RAZZ
            CustomTheme.THEME_GRAPE.value -> this.currentTheme = CustomTheme.THEME_GRAPE
            CustomTheme.THEME_AUTUMN.value -> this.currentTheme = CustomTheme.THEME_AUTUMN
            CustomTheme.THEME_SPRING.value -> this.currentTheme = CustomTheme.THEME_SPRING
            else -> this.currentTheme = CustomTheme.THEME_OCEAN
        }
    }

    fun setTheme(theme: CustomTheme, activity: Activity) {
        if(theme != this.currentTheme) {
            this.currentTheme = theme
            Injector.storageManager.setTheme(currentTheme.value)
            activity.finish()
            activity.startActivity(Intent(activity, activity.javaClass))
        }
    }

    fun onActivityCreateSetTheme(activity: Activity) {
        when (currentTheme) {
            CustomTheme.THEME_OCEAN -> activity.setTheme(R.style.Theme_Custom_Ocean)
            CustomTheme.THEME_SUNRISE -> activity.setTheme(R.style.Theme_Custom_Sunrise)
            CustomTheme.THEME_SUNDOWN -> activity.setTheme(R.style.Theme_Custom_Sundown)
            CustomTheme.THEME_RAZZ -> activity.setTheme(R.style.Theme_Custom_Razz)
            CustomTheme.THEME_GRAPE -> activity.setTheme(R.style.Theme_Custom_Grape)
            CustomTheme.THEME_AUTUMN -> activity.setTheme(R.style.Theme_Custom_Autumn)
            CustomTheme.THEME_SPRING -> activity.setTheme(R.style.Theme_Custom_Spring)
            else -> activity.setTheme(R.style.Theme_Custom_Ocean)
        }
    }

    fun getAccentColor(context: Context) : Int {
        return when (currentTheme) {
            CustomTheme.THEME_OCEAN -> ContextCompat.getColor(context, R.color.theme_ocean_accent)
            CustomTheme.THEME_SUNRISE -> ContextCompat.getColor(context, R.color.theme_sunrise_accent)
            CustomTheme.THEME_SUNDOWN -> ContextCompat.getColor(context, R.color.theme_sundown_accent)
            CustomTheme.THEME_RAZZ -> ContextCompat.getColor(context, R.color.theme_razz_accent)
            CustomTheme.THEME_GRAPE -> ContextCompat.getColor(context, R.color.theme_grape_accent)
            CustomTheme.THEME_AUTUMN -> ContextCompat.getColor(context, R.color.theme_autumn_accent)
            CustomTheme.THEME_SPRING -> ContextCompat.getColor(context, R.color.theme_spring_accent)
            else -> ContextCompat.getColor(context, R.color.theme_ocean_accent)
        }
    }

    fun getHalfAccentColor(context: Context) : Int {
        return when (currentTheme) {
            CustomTheme.THEME_OCEAN -> ContextCompat.getColor(context, R.color.theme_ocean_accent_half)
            CustomTheme.THEME_SUNRISE -> ContextCompat.getColor(context, R.color.theme_sunrise_accent_half)
            CustomTheme.THEME_SUNDOWN -> ContextCompat.getColor(context, R.color.theme_sundown_accent_half)
            CustomTheme.THEME_RAZZ -> ContextCompat.getColor(context, R.color.theme_razz_accent_half)
            CustomTheme.THEME_GRAPE -> ContextCompat.getColor(context, R.color.theme_grape_accent_half)
            CustomTheme.THEME_AUTUMN -> ContextCompat.getColor(context, R.color.theme_autumn_accent_half)
            CustomTheme.THEME_SPRING -> ContextCompat.getColor(context, R.color.theme_spring_accent_half)
            else -> ContextCompat.getColor(context, R.color.theme_ocean_accent_half)
        }
    }

    fun getGradient1(context: Context) : Int {
        return when (currentTheme) {
            CustomTheme.THEME_OCEAN -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_1)
            CustomTheme.THEME_SUNRISE -> ContextCompat.getColor(context, R.color.theme_sunrise_gradient_1)
            CustomTheme.THEME_SUNDOWN -> ContextCompat.getColor(context, R.color.theme_sundown_gradient_1)
            CustomTheme.THEME_RAZZ -> ContextCompat.getColor(context, R.color.theme_razz_gradient_1)
            CustomTheme.THEME_GRAPE -> ContextCompat.getColor(context, R.color.theme_grape_gradient_1)
            CustomTheme.THEME_AUTUMN -> ContextCompat.getColor(context, R.color.theme_autumn_gradient_1)
            CustomTheme.THEME_SPRING -> ContextCompat.getColor(context, R.color.theme_spring_gradient_1)
            else -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_1)
        }
    }

    fun getGradient2(context: Context) : Int {
        return when (currentTheme) {
            CustomTheme.THEME_OCEAN -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_2)
            CustomTheme.THEME_SUNRISE -> ContextCompat.getColor(context, R.color.theme_sunrise_gradient_2)
            CustomTheme.THEME_SUNDOWN -> ContextCompat.getColor(context, R.color.theme_sundown_gradient_2)
            CustomTheme.THEME_RAZZ -> ContextCompat.getColor(context, R.color.theme_razz_gradient_2)
            CustomTheme.THEME_GRAPE -> ContextCompat.getColor(context, R.color.theme_grape_gradient_2)
            CustomTheme.THEME_AUTUMN -> ContextCompat.getColor(context, R.color.theme_autumn_gradient_2)
            CustomTheme.THEME_SPRING -> ContextCompat.getColor(context, R.color.theme_spring_gradient_2)
            else -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_2)
        }
    }

    fun getGradient3(context: Context) : Int {
        return when (currentTheme) {
            CustomTheme.THEME_OCEAN -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_3)
            CustomTheme.THEME_SUNRISE -> ContextCompat.getColor(context, R.color.theme_sunrise_gradient_3)
            CustomTheme.THEME_SUNDOWN -> ContextCompat.getColor(context, R.color.theme_sundown_gradient_3)
            CustomTheme.THEME_RAZZ -> ContextCompat.getColor(context, R.color.theme_razz_gradient_3)
            CustomTheme.THEME_GRAPE -> ContextCompat.getColor(context, R.color.theme_grape_gradient_3)
            CustomTheme.THEME_AUTUMN -> ContextCompat.getColor(context, R.color.theme_autumn_gradient_3)
            CustomTheme.THEME_SPRING -> ContextCompat.getColor(context, R.color.theme_spring_gradient_3)
            else -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_3)
        }
    }

    fun getSecondaryColor(context: Context) : Int {
        return when (currentTheme) {
            CustomTheme.THEME_OCEAN -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_3)
            CustomTheme.THEME_SUNRISE -> ContextCompat.getColor(context, R.color.theme_sunrise_gradient_3)
            CustomTheme.THEME_SUNDOWN -> ContextCompat.getColor(context, R.color.theme_sundown_gradient_3)
            CustomTheme.THEME_RAZZ -> ContextCompat.getColor(context, R.color.theme_razz_gradient_3)
            CustomTheme.THEME_GRAPE -> ContextCompat.getColor(context, R.color.theme_grape_gradient_3)
            CustomTheme.THEME_AUTUMN -> ContextCompat.getColor(context, R.color.theme_autumn_gradient_3)
            CustomTheme.THEME_SPRING -> ContextCompat.getColor(context, R.color.theme_spring_gradient_3)
            else -> ContextCompat.getColor(context, R.color.theme_ocean_gradient_3)
        }
    }

    fun customizeSortSettItem(context: Context, isSelected: Boolean, cardView: CardView, text: TextView) {
        if(isSelected) {
            cardView.setCardBackgroundColor(context.getColor(R.color.white))
            text.setTextColor(getAccentColor(context))
        } else {
            cardView.setCardBackgroundColor(getAccentColor(context))
            text.setTextColor(context.getColor(R.color.white))
        }
    }

    fun changeImageViewTintToAccent(context: Context, iv: ImageView) {
        iv.imageTintList = ColorStateList.valueOf(getAccentColor(context))
    }

    fun changeImageViewTintToSecondary(context: Context, iv: ImageView) {
        iv.imageTintList = ColorStateList.valueOf(getSecondaryColor(context))
    }

    fun changeImageViewTintToGrey(context: Context, iv: ImageView) {
        iv.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey))
    }

    fun changeImageViewTintToWhite(context: Context, iv: ImageView) {
        iv.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
    }


    // -------  Game Word View  -------

    fun changeCardBackgroundColorToGrey(context: Context, cv: CardView) {
        cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey))
    }

    fun changeCardBackgroundColorToAccent(context: Context, cv: CardView) {
        cv.setCardBackgroundColor(getAccentColor(context))
    }

    fun changeCardBackgroundColorToSecondary(context: Context, cv: CardView) {
        cv.setCardBackgroundColor(getSecondaryColor(context))
    }

    fun changeCardBackgroundColorToWhite(context: Context, cv: CardView) {
        cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
    }

    fun changeTextColorToGrey(context: Context, tv: TextView) {
        tv.setTextColor(ContextCompat.getColor(context, R.color.grey))
    }

    fun changeTextColorToAccent(context: Context, tv: TextView) {
        tv.setTextColor(getAccentColor(context))
    }

    fun changeTextColorToWhite(context: Context, tv: TextView) {
        tv.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    // -------------------
    fun changeButtonTextColorToAccent(context: Context, button: MaterialButton) {
//        button.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        button.setTextColor(getAccentColor(context))
    }

    fun changeButtonTextColorToGrey(context: Context, button: MaterialButton) {
//        button.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
        button.setTextColor(ContextCompat.getColor(context, R.color.grey))
    }


    //------------ Radio Buttons

    fun getAccentColorStateList(context: Context) : ColorStateList {
         return when(currentTheme) {
            CustomTheme.THEME_OCEAN ->
                ColorStateList(
                 arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                 ), intArrayOf(
                    ContextCompat.getColor(context, R.color.grey),
                    ContextCompat.getColor(context, R.color.theme_ocean_accent)
                 )
             )
            CustomTheme.THEME_SUNRISE ->
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(
                        ContextCompat.getColor(context, R.color.grey),
                        ContextCompat.getColor(context, R.color.theme_sunrise_accent)
                    )
                )
            CustomTheme.THEME_SUNDOWN ->
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(
                        ContextCompat.getColor(context, R.color.grey),
                        ContextCompat.getColor(context, R.color.theme_sundown_accent)
                    )
                )
            CustomTheme.THEME_RAZZ ->
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(
                        ContextCompat.getColor(context, R.color.grey),
                        ContextCompat.getColor(context, R.color.theme_razz_accent)
                    )
                )
            CustomTheme.THEME_GRAPE ->
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(
                        ContextCompat.getColor(context, R.color.grey),
                        ContextCompat.getColor(context, R.color.theme_grape_accent)
                    )
                )
            CustomTheme.THEME_AUTUMN ->
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(
                        ContextCompat.getColor(context, R.color.grey),
                        ContextCompat.getColor(context, R.color.theme_autumn_accent)
                    )
                )
            CustomTheme.THEME_SPRING ->
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(
                        ContextCompat.getColor(context, R.color.grey),
                        ContextCompat.getColor(context, R.color.theme_spring_accent)
                    )
                )
             else ->
                 ColorStateList(
                     arrayOf(
                         intArrayOf(-android.R.attr.state_checked),
                         intArrayOf(android.R.attr.state_checked)
                     ), intArrayOf(
                         ContextCompat.getColor(context, R.color.grey),
                         ContextCompat.getColor(context, R.color.theme_ocean_accent)
                     )
                 )
        }

    }

    fun customizeGameWordButtonResult(
        context: Context, isCorrect: Boolean,
        cv: CardView, tv: TextView, iv: ImageView) {
        if(isCorrect) {
            cv.setCardBackgroundColor(getAccentColor(context))
            tv.setTextColor(ContextCompat.getColor(context, R.color.white))
            tv.text = context.getString(R.string.game_result_correct)
            iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_game_result_correct))
        } else {
            cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey))
            tv.setTextColor(ContextCompat.getColor(context, R.color.white))
            tv.text = context.getString(R.string.game_result_incorrect)
            iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_game_result_incorrect))
        }
    }

    fun getDialogItemResultBackgroundDrawable(context: Context) : Drawable? {
        return when(currentTheme) {
            CustomTheme.THEME_OCEAN -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_ocean)
            CustomTheme.THEME_SUNRISE -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_sunrise)
            CustomTheme.THEME_SUNDOWN -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_sundown)
            CustomTheme.THEME_RAZZ -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_razz)
            CustomTheme.THEME_GRAPE -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_grape)
            CustomTheme.THEME_AUTUMN -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_autumn)
            CustomTheme.THEME_SPRING -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_spring)
            else -> ContextCompat.getDrawable(context, R.drawable.dialog_item_result_background_ocean)
        }
    }

    //---------------

    fun getBorderedButton() : BorderedButtonView.BorderedButtonSett {
        return when (currentTheme) {
            CustomTheme.THEME_OCEAN -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT,
                borderBackgroundColorRes = R.color.white,
                backgroundDrawableRes = R.drawable.button_gradient_blue_dark
            )
            CustomTheme.THEME_SUNRISE -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT,
                borderBackgroundColorRes = R.color.white,
                backgroundDrawableRes = R.drawable.button_gradient_blue_light
            )
            CustomTheme.THEME_SUNDOWN -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT,
                borderBackgroundColorRes = R.color.white,
                backgroundDrawableRes = R.drawable.button_gradient_orange
            )
            CustomTheme.THEME_RAZZ -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT,
                borderBackgroundColorRes = R.color.white,
                backgroundDrawableRes = R.drawable.button_gradient_ping
            )
            CustomTheme.THEME_GRAPE -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT,
                borderBackgroundColorRes = R.color.white,
                backgroundDrawableRes = R.drawable.button_gradient_violet_light
            )
            CustomTheme.THEME_AUTUMN -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT,
                borderBackgroundColorRes = R.color.white,
                backgroundDrawableRes = R.drawable.button_gradient_blue_dark
            )
            CustomTheme.THEME_SPRING -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT,
                borderBackgroundColorRes = R.color.white,
                backgroundDrawableRes = R.drawable.button_gradient_blue_light
            )
            else -> BorderedButtonView.BorderedButtonSett(
                type = BorderedButtonView.BorderedButtonType.TYPE_SQUARE_SOLID_TEXT,
                backgroundColorRes = R.color.white
            )
        }
    }

    fun customizeCheckView(context: Context, circle: View, imageView: ImageView, isChecked: Boolean) {
        when (currentTheme) {
            CustomTheme.THEME_OCEAN -> {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white))
                    imageView.visibility = View.VISIBLE
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_ocean)
                } else {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_white_with_ocean_border)
                    imageView.visibility = View.GONE
                }
            }
            CustomTheme.THEME_SUNRISE -> {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white))
                    imageView.visibility = View.VISIBLE
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_sunrise)
                } else {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_white_with_sunrise_border)
                    imageView.visibility = View.GONE
                }
            }
            CustomTheme.THEME_SUNDOWN -> {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white))
                    imageView.visibility = View.VISIBLE
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_sundown)
                } else {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_white_with_sundown_border)
                    imageView.visibility = View.GONE
                }
            }
            CustomTheme.THEME_RAZZ -> {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white))
                    imageView.visibility = View.VISIBLE
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_razz)
                } else {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_white_with_razz_border)
                    imageView.visibility = View.GONE
                }
            }
            CustomTheme.THEME_GRAPE -> {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white))
                    imageView.visibility = View.VISIBLE
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_grape)
                } else {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_white_with_grape_border)
                    imageView.visibility = View.GONE
                }
            }
            CustomTheme.THEME_AUTUMN -> {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white))
                    imageView.visibility = View.VISIBLE
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_autumn)
                } else {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_white_with_autumn_border)
                    imageView.visibility = View.GONE
                }
            }
            CustomTheme.THEME_SPRING -> {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white))
                    imageView.visibility = View.VISIBLE
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_spring)
                } else {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.circle_white_with_spring_border)
                    imageView.visibility = View.GONE
                }
            }

        }
    }



    fun customizeLetterBackground(context: Context, background: RelativeLayout, text: TextView, isSelected: Boolean) {
        when (currentTheme) {
            CustomTheme.THEME_OCEAN -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_ocean_accent))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_ocean_with_white_border)
                }
            }
            CustomTheme.THEME_SUNRISE -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_sunrise_accent))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_sunrise_with_white_border)
                }
            }
            CustomTheme.THEME_SUNDOWN -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_sundown_accent))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_sundown_with_white_border)
                }
            }
            CustomTheme.THEME_RAZZ -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_razz_accent))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_razz_with_white_border)
                }
            }
            CustomTheme.THEME_GRAPE -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_grape_accent))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_grape_with_white_border)
                }
            }
            CustomTheme.THEME_AUTUMN -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_autumn_accent))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_autumn_with_white_border)
                }
            }
            CustomTheme.THEME_SPRING -> {
                if(isSelected) {
                    text.setTextColor(ContextCompat.getColor(context, R.color.theme_spring_accent))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_white)
                } else {
                    text.setTextColor(ContextCompat.getColor(context, R.color.white))
                    background.background = ContextCompat.getDrawable(context, R.drawable.circle_spring_with_white_border)
                }
            }
        }
    }

}
