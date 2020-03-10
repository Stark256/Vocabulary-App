package com.vocabulary.models.game_words_models

import java.io.Serializable

class GameWordItemModel(
    val modelID: Long,
    val isTrue: Boolean,
    val word: String,
    var isActive: Boolean = true
) : Serializable