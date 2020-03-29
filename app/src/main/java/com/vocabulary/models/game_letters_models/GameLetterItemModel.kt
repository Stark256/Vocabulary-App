package com.vocabulary.models.game_letters_models

class GameLetterItemModel(
    var type : GameLetterItemModelState,
    var uniqueID: Long = -1L,
    var letter: String? = null,
    var isCorrect: Boolean = false
)

enum class GameLetterItemModelState {
    GS_EMPTY,
    GS_LETTER,
    GS_CURSOR,
    GS_INVISIBLE
}