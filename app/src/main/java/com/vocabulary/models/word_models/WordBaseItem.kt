package com.vocabulary.models.word_models

abstract class WordBaseItem {

    enum class WordItemType(val value: Int) {
        TYPE_LETTER(0),
        TYPE_WORD(1)
    }
    abstract fun getSortString() : String
    abstract fun getType(): WordItemType
}
