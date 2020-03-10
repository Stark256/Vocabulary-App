package com.vocabulary.models

import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.word_models.WordModel

class ExerciseResult(
//    val gameType: GameResultType
) {

    val failsModelList = ArrayList<ExerciseFailModel>()

    val resultList = ArrayList<GameResult>()

    fun setGameWordsResult(correctWord: WordModel, selectedWord: GameWordItemModel?) {

        if(selectedWord != null) {
            if(correctWord.id == selectedWord.modelID) {
                resultList.add(GameResult(true, correctWord.word, correctWord.translation))
            } else {
                resultList.add(GameResult(false, correctWord.word, correctWord.translation, selectedWord.word))
                failsModelList.add(ExerciseFailModel(wordID = correctWord.id))
            }
        } else {
            resultList.add(GameResult(false, correctWord.word, correctWord.translation))
            failsModelList.add(ExerciseFailModel(wordID = correctWord.id))
        }
    }

}

class GameResult(
    val isCorrect: Boolean,
    val correctWord: String,
    val correctTranslation: String,
    val incorrectSelection: String? = null
) {

}

//enum class GameResultType {
//    GR_WORDS,
//    GR_LETTERS
//}