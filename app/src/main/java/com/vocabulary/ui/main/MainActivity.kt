package com.vocabulary.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.ui.settings.SettingsFragment
import com.vocabulary.ui.tests.TestsFragment
import com.vocabulary.ui.words.WordsFragment
import com.vocabulary.utils.ThemeUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.themeManager.onActivityCreateSetTheme(this)
        Log.d("THEME_CHANGING", "THEME CHANGED CHANGED CHANGED")

        setContentView(R.layout.activity_main)

        replaceFragment(WordsFragment())

        Injector.initDBManager(this@MainActivity)


        this.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        this.viewModel.loadAppData()

    }

    fun changeFragment(id: Int) {
        when(id) {
            R.id.mi_words -> { replaceFragment(WordsFragment()) }
            R.id.mi_tests -> { replaceFragment(TestsFragment()) }
            R.id.mi_settings -> { replaceFragment(SettingsFragment()) }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

}
