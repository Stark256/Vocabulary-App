package com.vocabulary.ui.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vocabulary.R
import com.vocabulary.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_tests.*

class TestsFragment : Fragment() {

    private val onNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        //        when(item.itemId) {
//            R.id.mi_words -> {  }
//            R.id.mi_tests -> {  }
//            R.id.mi_settings -> { }
//        }
        if(item.itemId != R.id.mi_tests) {
            (activity as MainActivity).changeFragment(item.itemId)
        }

        return@OnNavigationItemSelectedListener true
    }

    private lateinit var dashboardViewModel: TestsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel = ViewModelProviders.of(this).get(TestsViewModel::class.java)

        dashboardViewModel.text.observe(this, Observer {
            text_dashboard.text = it
        })
        bnv_tests.selectedItemId = R.id.mi_tests
        bnv_tests.setOnNavigationItemSelectedListener(onNavListener)
    }
}