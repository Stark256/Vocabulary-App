package com.vocabulary.utils

import com.vocabulary.R
import com.vocabulary.models.exercise_base_models.ExerciseBaseModel
import com.vocabulary.models.exercise_base_models.ExerciseType


object GameUtils {

    private val baseWordsCountArr = arrayListOf(10L, 20L, 40L, 60L)

    val baseGames: ArrayList<ExerciseBaseModel> = arrayListOf(
        ExerciseBaseModel(
            ExerciseType.GAME_WORDS,
            R.string.game_words_title)
//        ,
//        GameBaseModel(
//            GamesType.GAME_LETTERS,
//            R.string.game_letters_title)
    )

    fun initGameBaseModel(gameBaseModel: ExerciseBaseModel, wordsCountInTable: Long) {
        val radioButtons = getRadioButtonsByGameType(gameBaseModel.game_type)

        when(gameBaseModel.game_type) {
            ExerciseType.GAME_WORDS -> {
                gameBaseModel.words_count_arr = getWordsCountList(wordsCountInTable)

                gameBaseModel.option_1_title = radioButtons[0].toString()
                gameBaseModel.option_2_title = radioButtons[1].toString()
                gameBaseModel.option_3_title = radioButtons[2].toString()

                gameBaseModel.option_2_is_enable = radioButtons[1] <= wordsCountInTable
                gameBaseModel.option_3_is_enable = radioButtons[2] <= wordsCountInTable

            }
            ExerciseType.GAME_LETTERS -> {
                gameBaseModel.words_count_arr = getWordsCountList(wordsCountInTable)

                gameBaseModel.option_1_title = radioButtons[0].toString()
                gameBaseModel.option_2_title = radioButtons[1].toString()
                gameBaseModel.option_3_title = radioButtons[2].toString()
// TODO
//                gameBaseModel.option_2_is_enable = radioButtons[1] <= wordsCountInTable
//                gameBaseModel.option_3_is_enable = radioButtons[2] <= wordsCountInTable
            }
        }
    }


    private fun getWordsCountList(wordsCountInTable: Long) : Array<String> {
        val resultList = ArrayList<String>()

        if(wordsCountInTable > baseWordsCountArr[0]) {
            for (itemCount in baseWordsCountArr) {
                if (wordsCountInTable >= itemCount) {
                    resultList.add(itemCount.toString())
                }
            }
            val lastCountItem: Long = ((wordsCountInTable /10L) * 10L)
            if(lastCountItem % 10L == 0L &&
                lastCountItem > resultList.last().toLong()) {
                resultList.add(lastCountItem.toString())
            }
        } else {
            if(wordsCountInTable % 2L == 0L) {
                resultList.add(wordsCountInTable.toString())
            }
        }

        return resultList.toTypedArray()
    }

    private fun getRadioButtonsByGameType(gamesType: ExerciseType) : ArrayList<Int>{
        return when(gamesType) {
            ExerciseType.GAME_WORDS -> {
                arrayListOf(4, 6, 10)
            }
            ExerciseType.GAME_LETTERS -> {
                arrayListOf(0, 4, 8)
            }
        }
    }
}
