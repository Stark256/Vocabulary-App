package com.vocabulary.models

class LetterModel(val letter: String) : WordBaseItem() {
    var isSelected = true
    override fun getSortString(): String {
        return letter
    }
    override fun getType(): WordItemType = WordItemType.TYPE_LETTER
}
