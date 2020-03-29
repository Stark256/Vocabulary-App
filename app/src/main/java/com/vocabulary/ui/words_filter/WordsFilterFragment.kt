package com.vocabulary.ui.words_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.vocabulary.R
import com.vocabulary.customViews.sort_sett_view.SortSettView
import com.vocabulary.models.word_models.LetterModel
import kotlinx.android.synthetic.main.fragment_words_filter.*

class WordsFilterFragment : Fragment(){

    private lateinit var listener: OnFilterStateChangeListener
    private lateinit var adapter: WordsFilterAdapter
    private var filters = ArrayList<LetterModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_words_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapter = WordsFilterAdapter()
        rv_letters.adapter = adapter
        rv_letters.layoutManager = GridLayoutManager(context, 5)

        btn_apply.setOnClickListener {
            listener.onApplyPressed(this.ssv_words_filter.getSelectedSortSett(), this.filters)
        }

        btn_reset.setOnClickListener {
            listener.onResetPressed()
        }

        btn_language.setOnClickListener {
            listener.onLanguagePressed()
        }
    }

    interface OnFilterStateChangeListener {
        fun onApplyPressed(sortSett: SortSettView.SORT_SETT, filters: ArrayList<LetterModel>)
        fun onResetPressed()
        fun onLanguagePressed()
    }

    fun setFilterData(sortSett: SortSettView.SORT_SETT, filters: ArrayList<LetterModel>) {

        this.filters.clear()
        this.filters.addAll(filters)
        this.filters.sortBy { it.letter }

        this.ssv_words_filter.setSelectedSortSett(sortSett)
        adapter.replaceAll(this.filters)

        if(this.filters.isEmpty()) {
            tv_filter_empty_title.visibility = View.VISIBLE
            rv_letters.visibility = View.INVISIBLE
            tv_sort_title.visibility = View.INVISIBLE
            ssv_words_filter.visibility = View.INVISIBLE
        } else {
            tv_filter_empty_title.visibility = View.GONE
            rv_letters.visibility = View.VISIBLE
            tv_sort_title.visibility = View.VISIBLE
            ssv_words_filter.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(listener: OnFilterStateChangeListener): WordsFilterFragment {
            val fragment = WordsFilterFragment()
            fragment.listener = listener
            return fragment
        }
    }
}
