package com.vocabulary.models.game_letters_models

import com.vocabulary.models.word_models.WordModel

class GameLettersModel(
    val correctWord: WordModel,
    val translation: String,
    val correctLetters: ArrayList<GameLetterItemModel>,
    val lettersToGuess: ArrayList<GameLetterItemModel>
) {
    fun isCorrectWord(string: String?) : Boolean {
        return correctWord.word.equals(string)
    }
}