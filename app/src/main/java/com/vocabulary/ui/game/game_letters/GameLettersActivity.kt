package com.vocabulary.ui.game.game_letters

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vocabulary.R
import com.vocabulary.customViews.game_letter_view.GameLetterView
import com.vocabulary.managers.Injector
import com.vocabulary.models.GameResult
import com.vocabulary.models.game_letters_models.GameLettersModel
import com.vocabulary.ui.common.ExerciseResultDialog
import com.vocabulary.ui.common.ExitSureDialog
import kotlinx.android.synthetic.main.activity_game_letters.*

class GameLettersActivity : AppCompatActivity() {

    companion object {
        val EXTRA_WORDS_COUNT = "words_count"
        val EXTRA_ITEM_GUESS_COUNT = "item_guess_count"

        fun newInstance(context: Context,
                        wordsCount: Long,
                        itemToGuessCount: Long): Intent {
            return Intent(context, GameLettersActivity::class.java)
                .apply {
                    putExtra(EXTRA_WORDS_COUNT, wordsCount)
                    putExtra(EXTRA_ITEM_GUESS_COUNT, itemToGuessCount)
                }
        }
    }

    private lateinit var viewModel: GameLettersViewModel
    private var currentButtonState : GameLettersViewModel.GameLettersButtonNextState =
        GameLettersViewModel.GameLettersButtonNextState.BS_NOT_ENABLED_CHECK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.themeManager.onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_game_letters)

        this.viewModel = ViewModelProviders.of(this).get(GameLettersViewModel::class.java)

        intent.extras?.apply {
            this@GameLettersActivity.viewModel.setInitExtras(
                getLong(EXTRA_WORDS_COUNT, -1L),
                getLong(EXTRA_ITEM_GUESS_COUNT, -1L))
        }

        enableNextButton(false)
        game_letters_view.setListener(object : GameLetterView.GameLetterViewSelectListener {
            override fun onGameLetterSelected(result: String?) {
                viewModel.onGameLetterSelected(result)
            }

            override fun onReadyPressed() {
                viewModel.startPressed()
            }

            override fun onCheckListIsFull(isFull: Boolean) {
                viewModel.showCheck(isFull)
            }
        })

        this.btn_tooltip?.setOnClickListener {
            viewModel.tipsPressed()
        }

        this.btn_endgame?.setOnClickListener {
            showSureExitDialog()
        }

        this.btn_game_next?.setOnClickListener {
            when(currentButtonState) {
                GameLettersViewModel.GameLettersButtonNextState.BS_NEXT -> {
                    viewModel.nextPressed()
                    enableTipsButton(true)
                }
                GameLettersViewModel.GameLettersButtonNextState.BS_FINISH -> {
                    viewModel.finishPressed()
                }
                GameLettersViewModel.GameLettersButtonNextState.BS_ENABLED_CHECK -> {
                    viewModel.checkPressed()
                    game_letters_view.showResult(false,
                        viewModel.getCurrentGame())
                }
            }
        }

        this.btn_game_dont_know?.setOnClickListener {
            game_letters_view.showResult(true,
                viewModel.getCurrentGame())
            viewModel.checkPressed()
        }





        this.viewModel.apply {
            screenTitle.observe(this@GameLettersActivity, Observer<String> {
                tv_game_title?.text = it
            })
            viewState.observe(this@GameLettersActivity,
                Observer<GameLetterViewState>{
                updateState(it)
                game_letters_view.setState(it)
            })
            buttonNextState.observe(this@GameLettersActivity,
                Observer<GameLettersViewModel.GameLettersButtonNextState>{
                    updateNextButtonState(it)
            })
            game.observe(this@GameLettersActivity,
                Observer<GameLettersModel> {
                    game_letters_view.showNext(it)
            })
            loadingPercent.observe(this@GameLettersActivity,
                Observer<Int>{
                    game_letters_view.setPercentage(it)
                })
            tips.observe(this@GameLettersActivity,
                Observer<ArrayList<Long>>{
                    game_letters_view.setTips(it)
                    if(!showTipsButton) {
                        enableTipsButton(false)
                    }
            })
            showFinishDialog.observe(this@GameLettersActivity,
                Observer<ArrayList<GameResult>>{
//                    showResultDialog(it)
                    finishActivity()
            })
            loadGames()
        }
    }


    private fun updateNextButtonState(buttonState: GameLettersViewModel.GameLettersButtonNextState) {
        when(buttonState) {
            GameLettersViewModel.GameLettersButtonNextState.BS_NEXT -> {
                btn_game_next.text = getString(R.string.btn_next)
                if(currentButtonState == GameLettersViewModel
                        .GameLettersButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(true)
                }
                this.currentButtonState = buttonState
            }
            GameLettersViewModel.GameLettersButtonNextState.BS_FINISH -> {
                btn_game_next.text = getString(R.string.btn_finish)
                if(currentButtonState == GameLettersViewModel
                        .GameLettersButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(true)
                }
                this.currentButtonState = buttonState
            }
            GameLettersViewModel.GameLettersButtonNextState.BS_ENABLED_CHECK -> {
                btn_game_next.text = getString(R.string.btn_check)
                if(currentButtonState == GameLettersViewModel
                        .GameLettersButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(true)
                }
                this.currentButtonState = buttonState
            }
            GameLettersViewModel.GameLettersButtonNextState.BS_NOT_ENABLED_CHECK -> {
                btn_game_next.text = getString(R.string.btn_check)
                if(currentButtonState != GameLettersViewModel
                        .GameLettersButtonNextState
                        .BS_NOT_ENABLED_CHECK) {
                    enableNextButton(false)
                }
                this.currentButtonState = buttonState
            }
        }
    }

    private fun updateState(state: GameLetterViewState) {
        if(state == GameLetterViewState.STATE_STARTED) {

//            enableNextButton(true)
            enableDontknowButton(true)
            enableEndgameButton(true)
            enableTipsButton(true)
        } else if(state == GameLetterViewState.STATE_READY) {
            enableNextButton(false)
            enableDontknowButton(false)
            enableEndgameButton(true)
            enableTipsButton(false)
        } else {
            enableNextButton(false)
            enableDontknowButton(false)
            enableEndgameButton(false)
            enableTipsButton(false)
        }
    }

    private fun showResultDialog(resultList: ArrayList<GameResult>) {
        val dialog = ExerciseResultDialog.newInstance(resultList,
            object : ExerciseResultDialog.ExerciseResultDialogListener {
                override fun onFinishPressed() {
                    finishActivity()
                }
            })
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showSureExitDialog() {
        val dialog = ExitSureDialog.newInstance(object : ExitSureDialog.ExitSureDialogListener {
            override fun onOKPressed() {
                if(viewModel.finishImmediate) {
                    finishActivity()
                } else { viewModel.finishPressed() }
            }
        })
        dialog.show(supportFragmentManager, dialog.tag)
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

    private fun finishActivity() {
        finish()
    }

    override fun onBackPressed() {
        showSureExitDialog()
    }
}
