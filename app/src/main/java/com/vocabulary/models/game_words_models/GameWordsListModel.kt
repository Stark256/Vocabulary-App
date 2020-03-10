package com.vocabulary.models.game_words_models

import com.vocabulary.models.word_models.WordModel
import java.io.Serializable

class GameWordsListModel (
    val correctWord: WordModel,
    val translation: String,
    val arrWordItemModels: ArrayList<GameWordItemModel>
) : Serializable