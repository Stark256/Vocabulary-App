package com.vocabulary.customViews.swipeable_view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.vocabulary.R
import com.vocabulary.customViews.CheckView
import com.vocabulary.managers.Injector
import com.vocabulary.models.LanguageModel
import com.vocabulary.models.safeLet
import kotlinx.android.synthetic.main.layout_language.view.*
import kotlinx.android.synthetic.main.view_swipeable_language.view.*


class SwipeableItemView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr) {

    private val buttonWitdh: Int = context.resources.getDimension(R.dimen.swipeable_button_full_width).toInt()
    private val zeroWitdh: Int = context.resources.getDimension(R.dimen.swipeable_button_zero_width).toInt()
    private val buttonMarginEnd: Int = context.resources.getDimension(R.dimen.swipeable_button_marginEnd).toInt()
    private val viewMarginEnd: Int = context.resources.getDimension(R.dimen.swipeable_view_marginEnd).toInt()


    private var cardView: RelativeLayout? = null
    private var cardDelete: RelativeLayout? = null
    private var cardEdit: RelativeLayout? = null
    private var textEdit: TextView? = null
    private var textDelete: TextView? = null

//    private var isOpened = MutableLiveData<Boolean>(false)
//    private var toClose = MutableLiveData<Boolean>(false)
    private var closeDetached = false
    private var shouldClose = false
    private var isOpened = false
    private var isSwiping: Boolean = false
    lateinit var type: SwipeableInnerViewType
    lateinit var languageListener: SwipeLanguageClickListener
//    private var animatorSet = AnimatorSet()
    var itemPosition: Int = -1

    /*     Language Views     */
    private var textName: TextView? = null
    private var textCount: TextView? = null
    private var checkView: CheckView? = null
    /*                        */



//    init {
//        val view = LayoutInflater.from(context).inflate(R.layout.view_swipeable_language, this, true)
//
//        this.cardView = view.card_view
//        this.cardEdit = view.card_edit
//        this.cardDelete = view.card_delete
//        this.textEdit = view.tv_swipeable_edit
//        this.textDelete = view.tv_swipeable_delete
//
//
//        this.cardView?.setOnTouchListener(object : OnSwipeTouchListener() {
//            override fun onSwipeRight() {
//                if(!isSwiping) { closeView() }
//            }
//
//            override fun onSwipeLeft() {
//                if(!isSwiping) { openView() }
//            }
//        })
//
//        if(false) {
//            view_check.check()
//        } else {
//            view_check.uncheck()
//        }
//    }


    private fun initView() {
        when(type) {
            SwipeableInnerViewType.VIEW_LANGUAGE ->
                LayoutInflater.from(context).inflate(R.layout.view_swipeable_language, this, true)
                    .apply {
                        this@SwipeableItemView.cardView = card_view
                        this@SwipeableItemView.cardEdit = card_edit
                        this@SwipeableItemView.cardDelete = card_delete
                        this@SwipeableItemView.textEdit = tv_swipeable_edit
                        this@SwipeableItemView.textDelete = tv_swipeable_delete

                        this@SwipeableItemView.checkView = inc_language.view_check
                        this@SwipeableItemView.textName = inc_language.tv_language_name
                        this@SwipeableItemView.textCount = inc_language.tv_language_words_count

                    }
//            SwipeableInnerViewType.VIEW_WORD -> LayoutInflater.from(context).inflate(R.layout.view_swipeable_language, this, true)
//            SwipeableInnerViewType.VIEW_GAME -> LayoutInflater.from(context).inflate(R.layout.view_swipeable_language, this, true)
//            SwipeableInnerViewType.VIEW_CATEGORY -> LayoutInflater.from(context).inflate(R.layout.view_swipeable_language, this, true)

        }

//        this.cardView?.setOnTouchListener(object : OnSwipeTouchListener(context) {
//            override fun onSwipeRight() {
//                if(!isSwiping) { closeView() }
//            }
//
//            override fun onSwipeLeft() {
//                if(!isSwiping) { openView() }
//            }
//        })
    }

    fun initLanguageView() {
        this.type = SwipeableInnerViewType.VIEW_LANGUAGE
        initView()
    }

    fun setLanguageModel(position: Int, languageModel: LanguageModel, listener: SwipeLanguageClickListener) {
        this.itemPosition = position
        this.languageListener = listener

        this.textName?.text = languageModel.name
        this.textCount?.text = languageModel.wordsCount.toString()
        this.checkView?.setOnClickListener { languageListener.onViewPressed(languageModel) }
        this.cardEdit?.setOnClickListener { languageListener.onEditPressed(languageModel) }
        this.cardDelete?.setOnClickListener { languageListener.onDeletePressed(languageModel) }

        if(Injector.languageManager.isSelected(languageModel)) { this.checkView?.check() }
        else { this.checkView?.uncheck() }
    }

    fun initWordView() {
//        this.type = SwipeableInnerViewType.VIEW_WORD
        // TODO
    }

    fun initCategoryView() {
//        this.type = SwipeableInnerViewType.VIEW_CATEGORY
        // TODO
    }

    fun initGameView() {
//        this.type = SwipeableInnerViewType.VIEW_GAME
        // TODO
    }


    fun rightSwipe() {
        closeView()
    }

    fun leftSwipe() {
        openView()
    }

    private fun openView() {
        if(!isOpened && !isSwiping) {
            closeDetached = false
            shouldClose = false
            isSwiping = true
            isOpened = true

            safeLet(this.cardView, this.cardEdit, this.cardDelete) { view, edit, delete ->

                slideButtons(edit, delete, zeroWitdh, buttonWitdh, {
                    view.background = ContextCompat.getDrawable(context, R.drawable.swipeable_background_item_opened)
                    (view.layoutParams as RelativeLayout.LayoutParams).marginEnd = buttonMarginEnd
                    (delete.layoutParams as RelativeLayout.LayoutParams).marginEnd = viewMarginEnd
                    (edit.layoutParams as RelativeLayout.LayoutParams).marginEnd = buttonMarginEnd
                }, {
                    this.textEdit?.visibility = View.VISIBLE
                    this.textDelete?.visibility = View.VISIBLE
                    isSwiping = false

                    if(shouldClose) {
                        closeView()
                    } else if(closeDetached) {
                        closeViewImmediate()
                    }
                })

            }


        }
    }

    fun closeDetached() {
        closeDetached = true
        closeViewImmediate()

    }

    fun closeViewAfterOpening() {
        shouldClose = true
        closeView()
    }

    private fun closeViewImmediate() {
        if(isOpened && !isSwiping) {
            isOpened = false
            closeDetached = false
            safeLet(this.cardView, this.cardEdit, this.cardDelete) { view, edit, delete ->
                    view.background =
                        ContextCompat.getDrawable(context, R.drawable.swipeable_background_item_closed)
                    this.textEdit?.visibility = View.GONE
                    this.textDelete?.visibility = View.GONE
                    edit.layoutParams.width = zeroWitdh
                    delete.layoutParams.width = zeroWitdh
                    (edit.layoutParams as RelativeLayout.LayoutParams).marginEnd = zeroWitdh
                    (view.layoutParams as RelativeLayout.LayoutParams).marginEnd = viewMarginEnd
                    (delete.layoutParams as RelativeLayout.LayoutParams).marginEnd = zeroWitdh
                }
        }
    }

    private fun closeView() {
        if(isOpened && !isSwiping) {
            isSwiping = true
            isOpened = false
            shouldClose = false

            safeLet(this.cardView, this.cardEdit, this.cardDelete) { view, edit, delete ->

                slideButtons(edit, delete, buttonWitdh, zeroWitdh, {
                    view.background = ContextCompat.getDrawable(context, R.drawable.swipeable_background_item_closed)
                    (edit.layoutParams as RelativeLayout.LayoutParams).marginEnd = zeroWitdh
                    this.textEdit?.visibility = View.GONE
                    this.textDelete?.visibility = View.GONE
                }, {
                    (view.layoutParams as RelativeLayout.LayoutParams).marginEnd = viewMarginEnd
                    (delete.layoutParams as RelativeLayout.LayoutParams).marginEnd = zeroWitdh
                    isSwiping = false
                })
            }
        }
    }


    private fun slideButtons(btnView1: View,
                             btnView2: View,
                             currentWidth: Int,
                             newWidth: Int,
                             startAction: () -> Unit,
                             endAction: () -> Unit) {

        val slideAnimator = ValueAnimator
            .ofInt(currentWidth, newWidth)
            .setDuration(500)

        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            btnView1.layoutParams.width = value
            btnView2.layoutParams.width = value
            btnView1.requestLayout()
            btnView2.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(slideAnimator)
        animatorSet.doOnStart { startAction.invoke() }
        animatorSet.doOnEnd { endAction.invoke() }
        animatorSet.start()
    }


    enum class SwipeableInnerViewType {
        VIEW_LANGUAGE
//        ,
//        VIEW_WORD,
//        VIEW_GAME,
//        VIEW_CATEGORY
    }

}