package com.vocabulary.ui.game.game_words

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vocabulary.R
import com.vocabulary.customViews.game_word_view.GameWordView
import com.vocabulary.managers.Injector
import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.game_words_models.GameWordsListModel
import com.vocabulary.ui.common.ExitSureDialog
import kotlinx.android.synthetic.main.activity_game_words.*

class GameWordsActivity : AppCompatActivity() {

    companion object {
        val EXTRA_WORDS_COUNT = "words_count"
        val EXTRA_ITEM_GUESS_COUNT = "item_guess_count"

        fun newInstance(context: Context,
                        wordsCount: Long,
                        itemToGuessCount: Long): Intent {
            return Intent(context, GameWordsActivity::class.java)
                .apply {
                    putExtra(EXTRA_WORDS_COUNT, wordsCount)
                    putExtra(EXTRA_ITEM_GUESS_COUNT, itemToGuessCount)
                }
        }
    }

    private lateinit var viewModel: GameWordsViewModel
    private var currentButtonState: GameWordsViewModel.GameWordsButtonNextState =
        GameWordsViewModel.GameWordsButtonNextState.BS_NOT_ENABLED_CHECK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.themeManager.onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_game_words)

        this.viewModel = ViewModelProviders.of(this@GameWordsActivity).get(GameWordsViewModel::class.java)

        intent.extras?.apply {
            this@GameWordsActivity.viewModel.setInitExtras(
                getLong(EXTRA_WORDS_COUNT, -1L),
                getLong(EXTRA_ITEM_GUESS_COUNT, -1L))
        }

        enableNextButton(false)
        game_words_view.setListener(object : GameWordView.GameWordViewSelectListener{
            override fun onGameWordSelected(gameWordItemModel: GameWordItemModel?) {
                viewModel.onGameWordSelected(gameWordItemModel)
            }

            override fun onReadyPressed() {
                viewModel.startPressed()
            }
        })


        this.btn_tooltip?.setOnClickListener {
            // TODO use tooltip
//            adapter.disableButtons(arrayListOf(1, 5))
            viewModel.tipsPressed()
        }

        this.btn_endgame?.setOnClickListener {
            val dialog = ExitSureDialog.newInstance(object : ExitSureDialog.ExitSureDialogListener {
                override fun onOKPressed() {
                    // TODO finish pressed
                }
            })
            dialog.show(supportFragmentManager, dialog.tag)
        }

        this.btn_game_next?.setOnClickListener {
            when(currentButtonState) {
                GameWordsViewModel.GameWordsButtonNextState.BS_NEXT -> {
                    viewModel.nextPressed()
                }
                GameWordsViewModel.GameWordsButtonNextState.BS_FINISH -> {
                    viewModel.nextPressed()
                }
                GameWordsViewModel.GameWordsButtonNextState.BS_ENABLED_CHECK -> {
                    viewModel.checkPressed()
                    game_words_view.showResults(false)
                }
            }
        }



        this.btn_game_dont_know?.setOnClickListener {
            viewModel.onGameWordSelected(null)
            game_words_view.showResults(true)
        }

        this.viewModel.apply {
            screenTitle.observe(this@GameWordsActivity,
                Observer<String> {
                    tv_game_title?.text = it
            })
            game.observe(this@GameWordsActivity,
                Observer<GameWordsListModel> {
                game_words_view.showNext(it)
            })
            buttonNextState.observe(this@GameWordsActivity,
                Observer<GameWordsViewModel.GameWordsButtonNextState> {
//                enableNextButton(it)
                updateNextButtonState(it)
            })
            tips.observe(this@GameWordsActivity,
                Observer<ArrayList<Long>> {
                game_words_view.setTips(it)
            })
            viewState.observe(this@GameWordsActivity,
                Observer<GameWordsViewState> {
                updateState(it)
                game_words_view.setState(it)
            })
            loadingPercent.observe(this@GameWordsActivity,
                Observer<Int> {
                game_words_view.setPercentage(it)
            })
            loadGames()
        }
    }

    private fun updateNextButtonState(buttonState: GameWordsViewModel.GameWordsButtonNextState) {
        when(buttonState) {
            GameWordsViewModel.GameWordsButtonNextState.BS_NEXT -> {
                btn_game_next.text = getString(R.string.btn_next)
                if(currentButtonState == GameWordsViewModel
                        .GameWordsButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(true)
                }
                this.currentButtonState = buttonState
            }
            GameWordsViewModel.GameWordsButtonNextState.BS_FINISH -> {
                btn_game_next.text = getString(R.string.btn_finish)
                if(currentButtonState == GameWordsViewModel
                        .GameWordsButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(true)
                }
                this.currentButtonState = buttonState
            }
            GameWordsViewModel.GameWordsButtonNextState.BS_ENABLED_CHECK -> {
                btn_game_next.text = getString(R.string.btn_check)
                if(currentButtonState == GameWordsViewModel
                        .GameWordsButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(true)
                }
                this.currentButtonState = buttonState
            }
            GameWordsViewModel.GameWordsButtonNextState.BS_NOT_ENABLED_CHECK -> {
                btn_game_next.text = getString(R.string.btn_check)
                if(currentButtonState != GameWordsViewModel
                        .GameWordsButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(false)
                }
                this.currentButtonState = buttonState
            }
        }
    }

    private fun updateState(state: GameWordsViewState) {
        if(state == GameWordsViewState.STATE_STARTED) {

//            enableNextButton(true)
            enableDontknowButton(true)
            enableEndgameButton(true)
            enableTipsButton(true)
        } else {
            enableNextButton(false)
            enableDontknowButton(false)
            enableEndgameButton(false)
            enableTipsButton(false)
        }
    }

    private fun enableNextButton(isEnable: Boolean) {
        if(isEnable) {
            btn_game_next.isEnabled = true
            Injector.themeManager.changeButtonTextColorToAccent(this, btn_game_next)
        } else {
            btn_game_next.isEnabled = false
            Injector.themeManager.changeButtonTextColorToGrey(this, btn_game_next)
        }
    }

    private fun enableDontknowButton(isEnable: Boolean) {
        if(isEnable) {
            btn_game_dont_know.isEnabled = true
            Injector.themeManager.changeButtonTextColorToAccent(this, btn_game_dont_know)
        } else {
            btn_game_dont_know.isEnabled = false
            Injector.themeManager.changeButtonTextColorToGrey(this, btn_game_dont_know)
        }
    }

    private fun enableTipsButton(isEnable: Boolean) {
        if(isEnable) {
            btn_tooltip.isEnabled = true
            Injector.themeManager.changeImageViewTintToWhite(this, btn_tooltip)
        } else {
            btn_tooltip.isEnabled = false
            Injector.themeManager.changeImageViewTintToGrey(this, btn_tooltip)
        }
    }

    private fun enableEndgameButton(isEnable: Boolean) {
        if(isEnable) {
            btn_endgame.isEnabled = true
            Injector.themeManager.changeImageViewTintToWhite(this, btn_endgame)
        } else {
            btn_endgame.isEnabled = false
            Injector.themeManager.changeImageViewTintToGrey(this, btn_endgame)
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
