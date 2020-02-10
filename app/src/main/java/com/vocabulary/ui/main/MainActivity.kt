package com.vocabulary.ui.main

import android.R.attr
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vocabulary.R
import com.vocabulary.customViews.bottom_nav_view.BottomNavigationElevatedView
import com.vocabulary.managers.Injector
import com.vocabulary.ui.settings.SettingsFragment
import com.vocabulary.ui.tests.TestsFragment
import com.vocabulary.ui.words.WordsFragment
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

        bnev_main.initListener(object : BottomNavigationElevatedView.NavigationMoveListener{
            override fun onMove(
                fromD: BottomNavigationElevatedView.MOVE_DIRECTION,
                toD: BottomNavigationElevatedView.MOVE_DIRECTION
            ) {
                if(toD == BottomNavigationElevatedView.MOVE_DIRECTION.D_GAMES
                    && fromD == BottomNavigationElevatedView.MOVE_DIRECTION.D_WORDS) {
                    changeFragmentToGames(false)
                } else if(toD == BottomNavigationElevatedView.MOVE_DIRECTION.D_GAMES
                    && fromD == BottomNavigationElevatedView.MOVE_DIRECTION.D_SETTINGS) {
                    changeFragmentToGames(true)
                } else if(toD == BottomNavigationElevatedView.MOVE_DIRECTION.D_WORDS) {
                    changeFragmentToWords()
                } else if(toD == BottomNavigationElevatedView.MOVE_DIRECTION.D_SETTINGS) {
                    changeFragmentToSettings()
                }
            }
        })

    }

    fun showBackView() {
        bnev_main.showBackView()
    }
    fun hideBackView() {
        bnev_main.hideBackView()
    }

    private fun changeFragmentToWords() {
        replaceFragment(WordsFragment(), true)
    }

    private fun changeFragmentToSettings() {
        replaceFragment(SettingsFragment(), false)
    }

    private fun changeFragmentToGames(toLeft: Boolean) {
        replaceFragment(TestsFragment(), toLeft)

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

    private fun replaceFragment(fragment: Fragment, toLeft: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        if(toLeft) {
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
        } else {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
