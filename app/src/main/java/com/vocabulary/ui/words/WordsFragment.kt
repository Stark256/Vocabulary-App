package com.vocabulary.ui.words

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vocabulary.R
import com.vocabulary.base.BaseFragment
import com.vocabulary.managers.Injector
import com.vocabulary.models.LetterModel
import com.vocabulary.models.WordBaseItem
import com.vocabulary.ui.MainActivity
import com.vocabulary.ui.words_filter.WordsFilterFragment
import com.vocabulary.utils.ThemeUtils
import kotlinx.android.synthetic.main.button_badge_1.*
import kotlinx.android.synthetic.main.button_badge_2.*
import kotlinx.android.synthetic.main.fragment_words.*

class WordsFragment : BaseFragment(), WordsFilterFragment.OnFilterStateChangeListener {

    private val onNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if(item.itemId != R.id.mi_words) {
            (activity as MainActivity).changeFragment(item.itemId)
        }
        return@OnNavigationItemSelectedListener true
    }
    private val filterFragmentKey = "FILTER_FRAGMENT"
    private lateinit var viewModel: WordsViewModel
    private lateinit var wordsAdapter: WordsAdapter
    private lateinit var swipeController: WordsSwipeController
    private var isActive = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(WordsViewModel::class.java)

        bnv_words.selectedItemId = R.id.mi_words
        bnv_words.setOnNavigationItemSelectedListener(onNavListener)

        // Swipe controller should be initialized before filter initializing
        this.swipeController = WordsSwipeController(
            ContextCompat.getDrawable(contextMain, R.drawable.ic_edit)!!,
            ContextCompat.getDrawable(contextMain, R.drawable.ic_delete)!!,
            Injector.themeManager.getAccentColor(contextMain),
            Color.RED)

        setupFilterFragment()

        this.wordsAdapter = WordsAdapter()
        rv_words.layoutManager = LinearLayoutManager(contextMain)
        rv_words.adapter = wordsAdapter



        ItemTouchHelper(swipeController).attachToRecyclerView(rv_words)

        btn_filter.setOnClickListener {
            hideSoftKeyboard(contextMain, tiet_search)
            Handler().postDelayed({
                showFilter()
            }, 150)
        }

        btn_add_word.setOnClickListener {
            Injector.themeManager.changeToTheme(activity as MainActivity)
        }

        tiet_search.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard(contextMain, tiet_search)
            }
            return@setOnEditorActionListener false
        }

        viewModel.apply {
            words.observe(this@WordsFragment, Observer<ArrayList<WordBaseItem>>{
                wordsAdapter.replaceAll(it)
            })
            loadWords()
        }
    }



    override fun onApplyPressed(filters: ArrayList<LetterModel>) {
        hideFilter()
        viewModel.setFilters(filters)
    }

    override fun onResetPressed() {
        hideFilter()
        viewModel.resetFilters()
    }

    override fun onEditPressed() {
        hideFilter()
        this.swipeController.setSwipe()
        // TODO enable editing
    }

    override fun onLanguagePressed() {
        hideFilter()
        // TODO open language activity
    }

    private fun showFilter() {
        val fragment = childFragmentManager.findFragmentByTag(filterFragmentKey)
        if(fragment is WordsFilterFragment) {
            (fragment as WordsFilterFragment).setFilterData(viewModel.filters, swipeController.getSwipeEnabled())
        }
        isActive = true
        updateConstrains()
    }

    private fun hideFilter() {
        isActive = false
        updateConstrains()
    }

    private fun updateConstrains() {
        val newConstrainSet = ConstraintSet()

        if(isActive) {
            newConstrainSet.clone(contextMain, R.layout.fragment_words_alt)
        } else {
            newConstrainSet.clone(contextMain, R.layout.fragment_words)
        }
        newConstrainSet.applyTo(root_words)
        val transition: Transition = ChangeBounds()
        transition.interpolator = OvershootInterpolator()
        androidx.transition.TransitionManager.beginDelayedTransition(root_words, transition)
    }

    private fun setupFilterFragment() {
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.fl_filter, WordsFilterFragment.newInstance(this), filterFragmentKey)
        ft.commit()
    }
}