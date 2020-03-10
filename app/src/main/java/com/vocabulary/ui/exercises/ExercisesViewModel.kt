package com.vocabulary.ui.exercises

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.managers.Injector
import com.vocabulary.models.exercise_base_models.ExerciseBaseModel

import com.vocabulary.utils.GameUtils

class ExercisesViewModel : ViewModel() {

    val exerciseList = MutableLiveData<ArrayList<ExerciseBaseModel>>()
    val viewState = MutableLiveData<ExercisesInitType>()

    fun loadExercises() {
        viewState.value = ExercisesInitType.EXERCISES_LOADING
        val currentLanguage = Injector.languageManager.getCurrentLanguageIfSelected()

        if(currentLanguage != null) {
            Injector.dbManager.getWordsCount(currentLanguage.tableWords) { count ->
                if(count < 4) {
                    viewState.value = ExercisesInitType.EXERCISES_NOT_ENOUGH_WORDS
                } else { initGames(count) }
            }

        } else {
            viewState.value = ExercisesInitType.EXERCISES_SELECT_LANGUAGE
        }
    }

    private fun initGames(wordsCount: Long) {
        val games = ArrayList<ExerciseBaseModel>()
        for(game in GameUtils.baseGames) {
            GameUtils.initGameBaseModel(game, wordsCount)
            games.add(game)
        }

        exerciseList.value = games
        viewState.value = ExercisesInitType.EXERCISES_NOT_EMPTY
    }

    enum class ExercisesInitType {
        EXERCISES_LOADING,
        EXERCISES_NOT_EMPTY,
        EXERCISES_SELECT_LANGUAGE,
        EXERCISES_NOT_ENOUGH_WORDS
    }
}