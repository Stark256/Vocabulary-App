package com.vocabulary.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.ThemeColorModel
import com.vocabulary.ui.main.MainActivity
import com.vocabulary.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

//    private val onNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        if(item.itemId != R.id.mi_settings) {
//            (activity as MainActivity).changeFragment(item.itemId)
//        }
//        return@OnNavigationItemSelectedListener true
//    }

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var themeAdapter: ThemesColorAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        settingsViewModel.text.observe(this, Observer {

        })


//        bnv_settings.selectedItemId = R.id.mi_settings
//        bnv_settings.setOnNavigationItemSelectedListener(onNavListener)


        this.themeAdapter = ThemesColorAdapter(Injector.themeManager.currentTheme, Injector.themeManager.getThemes(),
            object : ThemesColorAdapter.ThemeColorClickListener {
                override fun colorPressed(newTheme: ThemeColorModel) {
                    Injector.themeManager.setTheme(newTheme.theme, activity as MainActivity)
                }
            })
        rv_themes.layoutManager = LinearLayoutManager(contextMain, RecyclerView.HORIZONTAL, false)
        rv_themes.adapter = themeAdapter


    }
}