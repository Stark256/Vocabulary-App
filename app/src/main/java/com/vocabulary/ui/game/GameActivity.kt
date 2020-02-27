package com.vocabulary.ui.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vocabulary.R
import com.vocabulary.managers.Injector
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.themeManager.onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_game)


        // TODO set title
        this.tv_game_title?.text = "17 / 20"

        this.btn_tooltip?.setOnClickListener {
            // TODO use tooltip
        }

        this.btn_endgame?.setOnClickListener {
            // TODO show dialog are you sure
        }

        this.btn_game_check_next?.setOnClickListener {
            // TODO set check or next behavior
        }

        this.btn_game_dont_know?.setOnClickListener {
            // TODO set dont know behavior
        }
    }


}
