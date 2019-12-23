package com.vocabulary.ui.words_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.LetterModel
import com.vocabulary.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_words_filter.*

class WordsFilterFragment : Fragment(){

    private lateinit var listener: OnFilterStateChangeListener
    private lateinit var adapter: WordsFilterAdapter
    private var filters = ArrayList<LetterModel>()
    private var isSwipeEnabled: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_words_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapter = WordsFilterAdapter()
        rv_letters.adapter = adapter
        rv_letters.layoutManager = GridLayoutManager(context, 6)
        adapter.replaceAll(filters)

        Injector.themeManager.customizeWordEditBackground(activity as MainActivity, edit_circle, btn_edit, isSwipeEnabled)

//        val arr = ArrayList<LetterModel>()
////        arr.add(LetterModel("A"))
////        arr.add(LetterModel("B"))
////        arr.add(LetterModel("C"))
////        arr.add(LetterModel("D"))
////        arr.add(LetterModel("E"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))
////        arr.add(LetterModel("S"))


        btn_apply.setOnClickListener {
            listener.onApplyPressed(this.filters)
        }

        btn_reset.setOnClickListener {
            listener.onResetPressed()
        }

        btn_edit.setOnClickListener {
            listener.onEditPressed()
            Injector.themeManager.customizeWordEditBackground(activity as MainActivity, edit_circle, btn_edit, !isSwipeEnabled)
        }

        btn_language.setOnClickListener {
            listener.onLanguagePressed()
        }
    }

    interface OnFilterStateChangeListener {
        fun onApplyPressed(filters: ArrayList<LetterModel>)
        fun onResetPressed()
        fun onEditPressed()
        fun onLanguagePressed()
    }

    fun setFilterData(filters: ArrayList<LetterModel>, isSwipeEnabled: Boolean) {
        if(filters.isNotEmpty()) {
            this.filters.clear()
            this.filters.addAll(filters)
        }
        this.isSwipeEnabled = isSwipeEnabled
        adapter.replaceAll(filters)

    }

    companion object {
        fun newInstance(listener: OnFilterStateChangeListener): WordsFilterFragment {
            val fragment = WordsFilterFragment()
            fragment.listener = listener
            return fragment
        }
    }
}
