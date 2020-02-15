package com.vocabulary.ui.tests

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vocabulary.R
import com.vocabulary.customViews.BorderedButtonView
import com.vocabulary.customViews.swipeable_view.SwipeWordClickListener
import com.vocabulary.models.WordModel
import com.vocabulary.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_tests.*

class TestsFragment : Fragment() {

//    private val onNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        //        when(item.itemId) {
////            R.id.mi_words -> {  }
////            R.id.mi_tests -> {  }
////            R.id.mi_settings -> { }
////        }
//        if(item.itemId != R.id.mi_tests) {
//            (activity as MainActivity).changeFragment(item.itemId)
//        }
//
//        return@OnNavigationItemSelectedListener true
//    }

    private lateinit var dashboardViewModel: TestsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel = ViewModelProviders.of(this).get(TestsViewModel::class.java)


        swipe_test.initWordView()
        swipe_test.setWordModel(0, WordModel(word = "Peoples", translation = "Люди", tableName = "test"), object : SwipeWordClickListener{
            override fun onViewPressed(wordModel: WordModel) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onEditPressed(wordModel: WordModel) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDeletePressed(wordModel: WordModel) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

//        swipe_test.initLanguageView()
//        swipe_test.setLanguageModel(LanguageModel(15, "TEST_SWIPE", "dada","das", 5L),
//            object : SwipeLanguageClickListener{
//                override fun onViewPressed(languageModel: LanguageModel) {
//                    Log.i("SWIPE", "VIEW PRESSED")
//                }
//
//                override fun onEditPressed(languageModel: LanguageModel) {
//                    Log.i("SWIPE", "EDIT PRESSED")
//                }
//
//                override fun onDeletePressed(languageModel: LanguageModel) {
//                    Log.i("SWIPE", "DELETE PRESSED")
//                }
//            })
//
//        swipe_test1.initLanguageView()
//        swipe_test1.setLanguageModel(LanguageModel(16, "TEST_SWIPE1", "dada","das", 5L),
//            object : SwipeLanguageClickListener{
//                override fun onViewPressed(languageModel: LanguageModel) {
//                    Log.i("SWIPE1", "VIEW PRESSED")
//                }
//
//                override fun onEditPressed(languageModel: LanguageModel) {
//                    Log.i("SWIPE1", "EDIT PRESSED")
//                }
//
//                override fun onDeletePressed(languageModel: LanguageModel) {
//                    Log.i("SWIPE1", "DELETE PRESSED")
//                }
//            })

        btn_bordered.initView(
//            BorderedButtonView.BorderedButtonType.TYPE_SQUARE_SOLID_TEXT,
            BorderedButtonView.BorderedButtonType.TYPE_CIRCLE_BORDER_TEXT_ICON,
            backgroundDrawableRes = R.drawable.background_gradient,
            borderBackgroundColorRes = R.color.white,
            iconRes = R.drawable.ic_delete,
            text = "Delete")

        btn_borderedd.initView(
            BorderedButtonView.BorderedButtonType.TYPE_SQUARE_SOLID_TEXT,
            backgroundColorRes = R.color.white,
            text = "Reset")

        Log.i("BUTTONS_SIZE", "BORDERED_WIDTH = ${btn_bordered.layoutParams.width}")
        Log.i("BUTTONS_SIZE", "BORDERED_HEIGHT = ${btn_bordered.layoutParams.height}")
        Log.i("BUTTONS_SIZE", "MATERIAL_WIDTH = ${btn_reset.layoutParams.width}")
        Log.i("BUTTONS_SIZE", "MATERIAL_HEIGHT = ${btn_reset.layoutParams.height}")


        dashboardViewModel.text.observe(this, Observer {

        })
//        bnv_tests.selectedItemId = R.id.mi_tests
//        bnv_tests.setOnNavigationItemSelectedListener(onNavListener)
    }
}