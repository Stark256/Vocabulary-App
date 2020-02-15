package com.vocabulary.ui.words

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
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
import com.vocabulary.customViews.EmptyListMessageView
import com.vocabulary.customViews.swipeable_view.OnSwipeTouchListener
import com.vocabulary.customViews.swipeable_view.SwipeWordClickListener
import com.vocabulary.ui.common.BaseFragment
import com.vocabulary.managers.Injector
import com.vocabulary.models.LetterModel
import com.vocabulary.models.WordBaseItem
import com.vocabulary.models.WordModel
import com.vocabulary.ui.common.DeletingDialog
import com.vocabulary.ui.main.MainActivity
import com.vocabulary.ui.language.LanguageActivity
import com.vocabulary.ui.words_filter.WordsFilterFragment
import kotlinx.android.synthetic.main.button_badge_1.*
import kotlinx.android.synthetic.main.button_badge_2.*
import kotlinx.android.synthetic.main.fragment_words.*

class WordsFragment : BaseFragment(),
    SwipeWordClickListener,
    WordsFilterFragment.OnFilterStateChangeListener {

//    private val onNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        if(item.itemId != R.id.mi_words) {
//            (activity as MainActivity).changeFragment(item.itemId)
//        }
//        return@OnNavigationItemSelectedListener true
//    }
    private enum class SelectOrAdd { SELECT_LANGUAGE, ADD_WORD }
    private val filterFragmentKey = "FILTER_FRAGMENT"
    private lateinit var viewModel: WordsViewModel
    private lateinit var wordsAdapter: WordsAdapter
    private var isActive = false
    private var isAddOrSelect: SelectOrAdd = SelectOrAdd.ADD_WORD

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(WordsViewModel::class.java)
//        bnv_words.selectedItemId = R.id.mi_words
//        bnv_words.setOnNavigationItemSelectedListener(onNavListener)

        setupFilterFragment()

        this.view_empty_words.btnClickListener {
            when(isAddOrSelect) {
                SelectOrAdd.ADD_WORD -> { /* TODO open add word dialog */}
                SelectOrAdd.SELECT_LANGUAGE -> {
                    startActivity(Intent(this@WordsFragment.contextMain, LanguageActivity::class.java))
                }
            }
        }

        this.wordsAdapter = WordsAdapter(this)
        rv_words.layoutManager = LinearLayoutManager(contextMain)
        rv_words.adapter = wordsAdapter
        object : OnSwipeTouchListener(contextMain, rv_words){}

        btn_filter.setOnClickListener {
            hideSoftKeyboard(contextMain, tiet_search)
            Handler().postDelayed({
                showFilter()
            }, 150)
        }

        trans.setOnClickListener {
            hideFilter()
        }

        btn_add_word.setOnClickListener {
            //Injector.themeManager.changeToTheme(activity as MainActivity)
        }

        tiet_search.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard(contextMain, tiet_search)
            }
            return@setOnEditorActionListener false
        }

        viewModel.apply {
            isLoading.observe(this@WordsFragment, Observer<Boolean>{
                this@WordsFragment.pb_words.visibility = View.VISIBLE
                this@WordsFragment.view_empty_words.visibility = View.GONE
                this@WordsFragment.rv_words.visibility = View.GONE
            })
            words.observe(this@WordsFragment, Observer<ArrayList<WordBaseItem>?>{
                initList(it)
                if(!it.isNullOrEmpty()) { wordsAdapter.replaceAll(it) }
            })
            loadWords()
        }
    }

    override fun onResume() {
        super.onResume()
        if(::viewModel.isInitialized){
            viewModel.loadWords()
        }
    }

    private fun initList(arr: ArrayList<WordBaseItem>?) {
        if(arr != null) {
            if(arr.isEmpty()) {
                this.view_empty_words.initView(EmptyListMessageView.ListType.ADD_WORDS)
                this.pb_words.visibility = View.GONE
                this.view_empty_words.visibility = View.VISIBLE
                this.rv_words.visibility = View.GONE
            } else {
                this.pb_words.visibility = View.GONE
                this.view_empty_words.visibility = View.GONE
                this.rv_words.visibility = View.VISIBLE
            }
        } else {
            this.view_empty_words.initView(EmptyListMessageView.ListType.SELECT_LANGUAGES)
            this.pb_words.visibility = View.GONE
            this.view_empty_words.visibility = View.VISIBLE
            this.rv_words.visibility = View.GONE
        }
    }

    override fun onViewPressed(wordModel: WordModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditPressed(wordModel: WordModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeletePressed(wordModel: WordModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onApplyPressed(filters: ArrayList<LetterModel>) {
        hideFilter()
        showBadge()
        viewModel.setFilters(filters)
    }

    override fun onResetPressed() {
        hideFilter()
        hideBadge()
        viewModel.resetFilters()
    }

    override fun onLanguagePressed() {
        hideFilter()
        startActivity(Intent(this@WordsFragment.contextMain, LanguageActivity::class.java))
    }

    private fun showFilter() {
        val fragment = childFragmentManager.findFragmentByTag(filterFragmentKey)
        if(fragment is WordsFilterFragment) {
            (fragment as WordsFilterFragment).setFilterData(viewModel.filters)
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
            ?.setDuration(250)
            ?.start()
    }

    private fun hideBadge() {
        badge_filter
            ?.animate()
            ?.scaleX(0f)
            ?.scaleY(0f)
            ?.setInterpolator(LinearInterpolator())
            ?.setDuration(250)
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