package com.vocabulary.models

import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.word_models.WordModel
import java.io.Serializable

class ExerciseResult {

    val failsModelList = ArrayList<Long>()

    val resultList = ArrayList<GameResult>()

    fun getCorrectResultWordIDs() : ArrayList<Long> {
        val resultArr = ArrayList<Long>()
        for(item in resultList) {
            if(item.isCorrect) {
                resultArr.add(item.correctWordID)
            }
        }
        return resultArr
    }

    fun setGameWordsResult(correctWord: WordModel, selectedWord: GameWordItemModel?) {

        if(selectedWord != null) {
            if(correctWord.id == selectedWord.modelID) {
                resultList.add(GameResult(correctWord.id, true, correctWord.word, correctWord.translation))
            } else {
                resultList.add(GameResult(correctWord.id, false, correctWord.word, correctWord.translation, selectedWord.word))
                failsModelList.add(correctWord.id)
            }
        } else {
            resultList.add(GameResult(correctWord.id, false, correctWord.word, correctWord.translation))
            failsModelList.add(correctWord.id)
        }
    }

    fun setGameLettersResult(correctWord: WordModel, resultString: String?) {
        if(resultString != null) {
            if(correctWord.word.equals(resultString)) {
                resultList.add(GameResult(correctWord.id, true, correctWord.word, correctWord.translation))
            } else {
                resultList.add(GameResult(correctWord.id, false, correctWord.word, correctWord.translation, resultString))
                failsModelList.add(correctWord.id)
            }
        } else {
            resultList.add(GameResult(correctWord.id, false, correctWord.word, correctWord.translation))
            failsModelList.add(correctWord.id)
        }
    }

}

class GameResult(
    val correctWordID: Long,
    val isCorrect: Boolean,
    val correctWord: String,
    val correctTranslation: String,
    val incorrectSelection: String? = null
) : Serializable

