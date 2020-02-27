package com.vocabulary.ui.tests

import android.content.Intent
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
import com.vocabulary.ui.common.BaseFragment
import com.vocabulary.ui.game.GameActivity
import com.vocabulary.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_tests.*

class TestsFragment : BaseFragment() {


    private lateinit var dashboardViewModel: TestsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel = ViewModelProviders.of(this).get(TestsViewModel::class.java)

        dashboardViewModel.text.observe(this, Observer {

        })

        btn_dasdasda?.setOnClickListener {
            startActivity(Intent(contextMain, GameActivity::class.java))
        }
    }
}