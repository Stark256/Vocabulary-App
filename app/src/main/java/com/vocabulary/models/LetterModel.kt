package com.vocabulary.models

class LetterModel(val letter: String) : WordBaseItem() {
    var isSelected = false
    override fun getType(): WordItemType = WordItemType.TYPE_LETTER
}
