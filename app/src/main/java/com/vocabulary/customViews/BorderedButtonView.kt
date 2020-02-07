package com.vocabulary.customViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.vocabulary.R
import kotlinx.android.synthetic.main.view_bordered_button.view.*

class BorderedButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private val paddingDefault: Int = context.resources.getDimension(R.dimen.button_padding_with_icon).toInt()
    private val paddingTextIcon: Int = context.resources.getDimension(R.dimen.button_padding_with_text).toInt()

    private val cornerRadiusSquare: Float = context.resources.getDimension(R.dimen.button_square_corner_radius)
    private val cornerRadiusCircleBorder: Float = context.resources.getDimension(R.dimen.button_border_circle_corner_radius)
    private val cornerRadiusCircleContainer: Float = context.resources.getDimension(R.dimen.button_container_circle_corner_radius)

    private var buttonBorderCard: CardView? = null
    private var buttonContainerCard: CardView? = null
    private var buttonBackground: RelativeLayout? = null
    private var buttonText: TextView? = null
    private var buttonIcon: ImageView? = null

    private lateinit var type: BorderedButtonType

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_bordered_button, this, true)

        this.buttonBorderCard = view.cv_button_border
        this.buttonContainerCard = view.cv_button_container
        this.buttonBackground = view.rl_button_background
        this.buttonIcon = view.iv_button_icon
        this.buttonText = view.tv_button_text
    }

    fun inivView(sett: BorderedButtonSett) {
        initView(type = sett.type,
            borderBackgroundColorRes = sett.borderBackgroundColorRes,
            backgroundColorRes = sett.backgroundColorRes,
            backgroundDrawableRes = sett.backgroundDrawableRes,
            text = sett.text,
            iconRes = sett.iconRes)
    }

    fun initView(
        type: BorderedButtonType,
        borderBackgroundColorRes: Int? = null,
        backgroundColorRes: Int? = null,
        backgroundDrawableRes: Int? = null,
        text: String? = null,
        iconRes: Int? = null
        ) {


        when(type) {

            BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT -> {
                setCircleRadius()
                setDefaultPadding()
                hideImageView()
                showTextView(text = text)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes,
                    borderBackgroundColorRes = borderBackgroundColorRes)
            }

            BorderedButtonType.TYPE_CIRCLE_BORDER_ICON -> {
                setCircleRadius()
                setDefaultPadding()
                hideTextView()
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes,
                    borderBackgroundColorRes = borderBackgroundColorRes)
            }

            BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT_ICON -> {
                setCircleRadius()
                setTextIconPadding()
                showTextView(text = text)
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes,
                    borderBackgroundColorRes = borderBackgroundColorRes)
            }

            BorderedButtonType.TYPE_CIRCLE_SOLID_TEXT -> {
                setCircleRadius()
                setDefaultPadding()
                hideImageView()
                showTextView(text = text)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes)
            }
            BorderedButtonType.TYPE_CIRCLE_SOLID_ICON -> {
                setCircleRadius()
                setDefaultPadding()
                hideTextView()
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes)
            }
            BorderedButtonType.TYPE_CIRCLE_SOLID_TEXT_ICON -> {
                setCircleRadius()
                setTextIconPadding()
                showTextView(text = text)
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes)
            }

            BorderedButtonType.TYPE_SQUARE_BORDER_TEXT -> {
                setSquareRadius()
                setDefaultPadding()
                hideImageView()
                showTextView(text = text)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes,
                    borderBackgroundColorRes = borderBackgroundColorRes)
            }

            BorderedButtonType.TYPE_SQUARE_BORDER_ICON -> {
                setSquareRadius()
                setDefaultPadding()
                hideTextView()
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes,
                    borderBackgroundColorRes = borderBackgroundColorRes)
            }

            BorderedButtonType.TYPE_SQUARE_BORDER_TEXT_ICON -> {
                setSquareRadius()
                setTextIconPadding()
                showTextView(text = text)
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes,
                    borderBackgroundColorRes = borderBackgroundColorRes)
            }

            BorderedButtonType.TYPE_SQUARE_SOLID_TEXT -> {
                setSquareRadius()
                setDefaultPadding()
                hideImageView()
                showTextView(text = text)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes)
            }

            BorderedButtonType.TYPE_SQUARE_SOLID_ICON -> {
                setSquareRadius()
                setDefaultPadding()
                hideTextView()
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes)
            }

            BorderedButtonType.TYPE_SQUARE_SOLID_TEXT_ICON    -> {
                setSquareRadius()
                setTextIconPadding()
                showTextView(text = text)
                showImageView(iconRes = iconRes)
                setupBackground(backgroundColorRes = backgroundColorRes,
                    backgroundDrawableRes =  backgroundDrawableRes)
            }

        }

    }

    private fun setDefaultPadding() {
        buttonBackground?.setPadding(paddingDefault, 0, paddingDefault, 0)
    }

    private fun setTextIconPadding() {
        buttonBackground?.setPadding(paddingTextIcon, 0, paddingTextIcon, 0)
    }

    private fun setSquareRadius() {
        buttonBorderCard?.radius = cornerRadiusSquare
        buttonContainerCard?.radius = cornerRadiusSquare
    }

    private fun setCircleRadius() {
        buttonBorderCard?.radius = cornerRadiusCircleBorder
        buttonContainerCard?.radius = cornerRadiusCircleContainer
    }

    private fun setupBackground(backgroundColorRes: Int? = null,
                        backgroundDrawableRes: Int? = null,
                        borderBackgroundColorRes: Int? = null) {

        if(backgroundColorRes != null) {
            buttonBackground?.setBackgroundColor(ContextCompat.getColor(context, backgroundColorRes))
            if(borderBackgroundColorRes != null) {
                buttonBorderCard?.setCardBackgroundColor(ContextCompat.getColor(context, borderBackgroundColorRes))
            } else {
                buttonBorderCard?.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColorRes))
            }
        } else if(backgroundDrawableRes != null) {
            buttonBackground?.background = ContextCompat.getDrawable(context, backgroundDrawableRes)
            if(borderBackgroundColorRes != null) {
                buttonBorderCard?.setCardBackgroundColor(ContextCompat.getColor(context, borderBackgroundColorRes))
            } else {
                buttonBorderCard?.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
        } else {
            buttonBackground?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            buttonBorderCard?.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }


    }


    private fun showTextView(text: String?) {
        buttonText?.visibility = View.VISIBLE
        text?.let { buttonText?.text = text }
    }

    private fun hideTextView() {
        buttonText?.visibility = View.GONE
    }

    private fun showImageView(iconRes: Int?) {
        buttonIcon?.visibility = View.VISIBLE
        iconRes?.let { buttonIcon?.setImageDrawable(ContextCompat.getDrawable(context, iconRes)) }
    }

    private fun hideImageView() {
        buttonIcon?.visibility = View.GONE
    }

    class BorderedButtonSett(var type: BorderedButtonType,
                             var borderBackgroundColorRes: Int? = null,
                             var backgroundColorRes: Int? = null,
                             var backgroundDrawableRes: Int? = null,
                             var text: String? = null,
                             var iconRes: Int? = null)






    enum class BorderedButtonType {
        TYPE_CIRCLE_BORDER_TEXT,
        TYPE_CIRCLE_BORDER_ICON,
        TYPE_CIRCLE_BORDER_TEXT_ICON,

        TYPE_CIRCLE_SOLID_TEXT,
        TYPE_CIRCLE_SOLID_ICON,
        TYPE_CIRCLE_SOLID_TEXT_ICON,

        TYPE_SQUARE_BORDER_TEXT,
        TYPE_SQUARE_BORDER_ICON,
        TYPE_SQUARE_BORDER_TEXT_ICON,

        TYPE_SQUARE_SOLID_TEXT,
        TYPE_SQUARE_SOLID_ICON,
        TYPE_SQUARE_SOLID_TEXT_ICON
    }

}
