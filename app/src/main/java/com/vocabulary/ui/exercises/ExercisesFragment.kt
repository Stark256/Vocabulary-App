package com.vocabulary.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vocabulary.R
import com.vocabulary.customViews.EmptyListMessageView
import com.vocabulary.models.exercise_base_models.ExerciseBaseModel
import com.vocabulary.models.exercise_base_models.ExerciseType
import com.vocabulary.ui.common.BaseFragment
import com.vocabulary.ui.game.game_letters.GameLettersActivity
import com.vocabulary.ui.game.game_words.GameWordsActivity
import kotlinx.android.synthetic.main.fragment_exercises.*


class ExercisesFragment : BaseFragment() {


    private lateinit var viewModel: ExercisesViewModel
    private lateinit var adapter: ExercisesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ExercisesViewModel::class.java)

        this.adapter = ExercisesAdapter(object : ExercisesAdapter.ExercisesSelectListener {
            override fun onTestSelect(exerciseType: ExerciseType,
                                      wordsCount: Int,
                                      itemsToGuessCount: Int) {
                when(exerciseType) {
                    ExerciseType.GAME_WORDS -> {
                        startActivity(GameWordsActivity.newInstance(
                            contextMain,
                            wordsCount.toLong(),
                            (itemsToGuessCount + 2).toLong()))
                    }
                    ExerciseType.GAME_LETTERS -> {
                        startActivity(GameLettersActivity.newInstance(
                            contextMain,
                            wordsCount.toLong(),
                            (itemsToGuessCount).toLong()))
                    }
                }
            }
        })
        this.rv_exercises.adapter = adapter
        this.rv_exercises.layoutManager = LinearLayoutManager(contextMain)


        viewModel.apply {
            viewState.observe(this@ExercisesFragment,
                Observer<ExercisesViewModel.ExercisesInitType> {
                initView(it)
            })
            exerciseList.observe(this@ExercisesFragment,
                Observer<ArrayList<ExerciseBaseModel>> {
                adapter.replaceAll(it)
            })
            loadExercises()
        }

    }

    private fun initView(type: ExercisesViewModel.ExercisesInitType) {
        when(type) {
            ExercisesViewModel.ExercisesInitType.EXERCISES_LOADING -> {
                this.rv_exercises.visibility = View.GONE
                this.pb_exercises.visibility = View.VISIBLE
                this.view_empty_exercises.visibility = View.GONE
            }
            ExercisesViewModel.ExercisesInitType.EXERCISES_NOT_EMPTY -> {
                this.rv_exercises.visibility = View.VISIBLE
                this.pb_exercises.visibility = View.GONE
                this.view_empty_exercises.visibility = View.GONE

            }
            ExercisesViewModel.ExercisesInitType.EXERCISES_SELECT_LANGUAGE -> {
                this.rv_exercises.visibility = View.GONE
                this.pb_exercises.visibility = View.GONE
                this.view_empty_exercises.visibility = View.VISIBLE
                this.view_empty_exercises.initView(EmptyListMessageView.ListType.SELECT_LANGUAGES_EXERCISES)
            }
            ExercisesViewModel.ExercisesInitType.EXERCISES_NOT_ENOUGH_WORDS -> {
                this.rv_exercises.visibility = View.GONE
                this.pb_exercises.visibility = View.GONE
                this.view_empty_exercises.visibility = View.VISIBLE
                this.view_empty_exercises.initView(EmptyListMessageView.ListType.NOT_ENOUGH_WORDS)
            }
        }
    }

}