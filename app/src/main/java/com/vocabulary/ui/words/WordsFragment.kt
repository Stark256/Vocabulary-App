package com.vocabulary.ui.words

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import com.vocabulary.R
import com.vocabulary.customViews.EmptyListMessageView
import com.vocabulary.customViews.sort_sett_view.SortSettView
import com.vocabulary.customViews.swipeable_view.OnSwipeTouchListener
import com.vocabulary.customViews.swipeable_view.SwipeWordClickListener
import com.vocabulary.ui.common.BaseFragment
import com.vocabulary.managers.Injector
import com.vocabulary.models.LetterModel
import com.vocabulary.models.WordBaseItem
import com.vocabulary.models.WordModel
import com.vocabulary.models.cloneList
import com.vocabulary.ui.common.DeletingDialog
import com.vocabulary.ui.language.LanguageActivity
import com.vocabulary.ui.words_filter.WordsFilterFragment
import kotlinx.android.synthetic.main.button_badge_1.*
import kotlinx.android.synthetic.main.button_badge_2.*
import kotlinx.android.synthetic.main.fragment_words.*

class WordsFragment : BaseFragment(),
    SwipeWordClickListener,
    WordsFilterFragment.OnFilterStateChangeListener {

    private enum class SelectOrAdd { SELECT_LANGUAGE, ADD_WORD }
    private val filterFragmentKey = "FILTER_FRAGMENT"
    private val ANIMATION_TIME = 250L
    private lateinit var viewModel: WordsViewModel
    private lateinit var wordsAdapter: WordsAdapter
    private var isActive = false
    private var isAddOrSelect: SelectOrAdd = SelectOrAdd.ADD_WORD

    private var isSearchFocused = false

    private var isSearchEnabled = false
    private var isAddEnabled = false
    private var isFilterEnabled = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(WordsViewModel::class.java)

        setupFilterFragment()

        this.view_empty_words.btnClickListener {
            when(isAddOrSelect) {
                SelectOrAdd.ADD_WORD -> { showWordDialog() }
                SelectOrAdd.SELECT_LANGUAGE -> {
                   onLanguagePressed()
                }
            }
        }

        this.wordsAdapter = WordsAdapter(this)
        rv_words.layoutManager = LinearLayoutManager(contextMain)
        rv_words.adapter = wordsAdapter
        object : OnSwipeTouchListener(contextMain, rv_words){}

        btn_filter.setOnClickListener {
            if(isFilterEnabled) {
                hideSoftKeyboard(contextMain, tiet_search)
                Handler().postDelayed({
                    showFilter()
                }, 150)
            }
        }

        trans.setOnClickListener {
            hideFilter()
        }

        onKeyboardVisibilityChanged(root_words) { isOpened: Boolean ->
            if(!isOpened) {
                tiet_search.clearFocus()
            }
        }

        btn_add_word.setOnClickListener {
            if(isAddEnabled) {
                if (isSearchFocused) {
                    tiet_search?.text?.clear()
                    hideSoftKeyboard(contextMain, tiet_search)
                    searchUnfocused()
                } else {
                    showWordDialog()
                }
            }
        }

        tiet_search.setOnFocusChangeListener { v, hasFocus ->
            if(isSearchEnabled) {
                if(hasFocus) { searchFocused() }
                else { searchUnfocused() }
            }
        }
        tiet_search.filters = getInputFilters()

        tiet_search.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard(contextMain, tiet_search)
            }
            return@setOnEditorActionListener false
        }

        viewModel.apply {
            initializingView.observe(this@WordsFragment, Observer<WordsViewModel.WordInitType>{
                initView(it)
            })
            showBadge.observe(this@WordsFragment, Observer<Boolean> { showBadge ->
                if(showBadge) showBadge()
                else hideBadge()
            })
            words.observe(this@WordsFragment, Observer<ArrayList<WordBaseItem>>{
                wordsAdapter.replaceAll(it)
            })
            getWords()
        }
    }

    override fun onResume() {
        super.onResume()
        if(::viewModel.isInitialized){
            viewModel.updateIfNeeded()
        }
    }

    private fun initView(type: WordsViewModel.WordInitType) {
        when(type) {
            WordsViewModel.WordInitType.WORDS_LOADING -> {
                this.view_empty_words.visibility = View.GONE
                this.pb_words.visibility = View.VISIBLE
                this.rv_words.visibility = View.GONE
                updateSearchEnabled(false)
                updateAddEnabled(false)
                updateFilterEnabled(false)
            }
            WordsViewModel.WordInitType.WORDS_NOT_EMPTY -> {
                this.view_empty_words.visibility = View.GONE
                this.pb_words.visibility = View.GONE
                this.rv_words.visibility = View.VISIBLE
                updateSearchEnabled(true)
                updateAddEnabled(true)
                updateFilterEnabled(true)
            }
            WordsViewModel.WordInitType.WORDS_EMPTY -> {
                this.view_empty_words.initView(EmptyListMessageView.ListType.ADD_WORDS)
                this.view_empty_words.btnClickListener { showWordDialog() }
                this.view_empty_words.visibility = View.VISIBLE
                this.pb_words.visibility = View.GONE
                this.rv_words.visibility = View.GONE
                updateSearchEnabled(false)
                updateAddEnabled(true)
                updateFilterEnabled(true)
            }
            WordsViewModel.WordInitType.LANGUAGE_NOT_SELECTED -> {
                this.view_empty_words.initView(EmptyListMessageView.ListType.SELECT_LANGUAGES)
                this.view_empty_words.btnClickListener { onLanguagePressed() }
                this.view_empty_words.visibility = View.VISIBLE
                this.pb_words.visibility = View.GONE
                this.rv_words.visibility = View.GONE
                updateSearchEnabled(false)
                updateAddEnabled(false)
                updateFilterEnabled(false)
            }
            WordsViewModel.WordInitType.FILTER_EMPTY -> {
                this.view_empty_words.initView(EmptyListMessageView.ListType.FILTER_NOT_FOUND)
                this.view_empty_words.visibility = View.VISIBLE
                this.pb_words.visibility = View.GONE
                this.rv_words.visibility = View.GONE
                updateSearchEnabled(false)
                updateAddEnabled(true)
                updateFilterEnabled(true)
            }
        }
    }


    override fun onViewPressed(wordModel: WordModel) {}

    override fun onEditPressed(wordModel: WordModel) {
        showWordDialog(wordModel)
    }

    override fun onDeletePressed(wordModel: WordModel) {
        showDeletingDialog(wordModel)
    }

    override fun onApplyPressed(sortSett: SortSettView.SORT_SETT, filters: ArrayList<LetterModel>) {
        hideFilter()
//        showBadge()
        viewModel.setFilterData(sortSett, filters)
    }

    override fun onResetPressed() {
        hideFilter()
//        hideBadge()
        viewModel.resetFilters()
    }

    override fun onLanguagePressed() {
        hideFilter()
        startActivity(Intent(this@WordsFragment.contextMain, LanguageActivity::class.java))
    }

    private fun updateSearchEnabled(isEnable: Boolean) {
        this.isSearchEnabled = isEnable
        tiet_search.isEnabled = isEnable
    }

    private fun updateAddEnabled(isEnable: Boolean) {
        this.isAddEnabled = isEnable
        btn_add_word.isEnabled = isEnable
        if(this.isAddEnabled) {
            Injector.themeManager.changeImageViewTintToAccent(contextMain, btn_add_word)
        } else {
            Injector.themeManager.changeImageViewTintToGrey(contextMain, btn_add_word)
        }
    }

    private fun updateFilterEnabled(isEnable: Boolean) {
        this.isFilterEnabled = isEnable
        btn_filter.isEnabled = isEnable
        if(this.isFilterEnabled) {
            Injector.themeManager.changeImageViewTintToAccent(contextMain, btn_filter)
        } else {
            Injector.themeManager.changeImageViewTintToGrey(contextMain, btn_filter)
        }
    }

    private fun showFilter() {
        val fragment = childFragmentManager.findFragmentByTag(filterFragmentKey)
        if(fragment is WordsFilterFragment) {
            (fragment as WordsFilterFragment)
                .setFilterData(viewModel.selectedSortSett, viewModel.filters.cloneList())
        }
        isActive = true
        contextMain.showBackView()
        updateConstrains()
    }

    private fun hideFilter() {
        isActive = false
        contextMain.hideBackView()
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

    private fun showBadge() {
        badge_filter
            ?.animate()
            ?.scaleX(1f)
            ?.scaleY(1f)
            ?.setInterpolator(LinearInterpolator())
            ?.setDuration(ANIMATION_TIME)
            ?.start()
    }

    private fun hideBadge() {
        badge_filter
            ?.animate()
            ?.scaleX(0f)
            ?.scaleY(0f)
            ?.setInterpolator(LinearInterpolator())
            ?.setDuration(ANIMATION_TIME)
            ?.start()
    }

    private fun searchFocused() {
        btn_add_word
            ?.animate()
            ?.rotation(45f)
            ?.setDuration(ANIMATION_TIME)
            ?.withStartAction { isSearchFocused = true }
            ?.withEndAction {
                Injector.themeManager.changeImageViewTintToSecondary(contextMain, btn_add_word)
            }
            ?.start()
    }

    private fun searchUnfocused() {
        btn_add_word
            ?.animate()
            ?.rotation(0f)
            ?.setDuration(ANIMATION_TIME)
            ?.withStartAction { isSearchFocused = false }
            ?.withEndAction {
                Injector.themeManager.changeImageViewTintToAccent(contextMain, btn_add_word)
            }
            ?.start()
    }

    private fun showDeletingDialog(wordModel: WordModel) {
        val dialog = DeletingDialog.newInstance(wordModel, object : DeletingDialog.DeletingDialogListener {
            override fun onOKPressed(result: () -> Unit) {
                viewModel.deleteWord(wordModel) {
                    result.invoke()
                }
            }
        })
        dialog.show(contextMain.supportFragmentManager, dialog.tag)
    }

    private fun showWordDialog(wordModel: WordModel? = null) {
        val dialog = WordDialog.newInstance(
            word = wordModel?.word,
            translation = wordModel?.translation,
            listener = object : WordDialog.WordsDialogListener {
                override fun onOKPressed(
                    word: String,
                    translation: String,
                    result: (String?) -> Unit
                ) {
                    viewModel.addEditWord(
                        wordModel = wordModel,
                        newWord = word,
                        newTranslation =  translation
                    ) { res ->
                        result.invoke(res)
                    }
                }
            })
        dialog.show(contextMain.supportFragmentManager, dialog.tag)
    }
}