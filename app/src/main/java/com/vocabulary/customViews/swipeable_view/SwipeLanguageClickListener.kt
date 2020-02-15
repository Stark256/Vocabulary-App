package com.vocabulary.customViews.swipeable_view

import com.vocabulary.models.LanguageModel
import com.vocabulary.models.WordModel

interface SwipeLanguageClickListener {
    fun onViewPressed(languageModel: LanguageModel)
    fun onEditPressed(languageModel: LanguageModel)
    fun onDeletePressed(languageModel: LanguageModel)
}

interface SwipeWordClickListener {
    fun onViewPressed(wordModel: WordModel)
    fun onEditPressed(wordModel: WordModel)
    fun onDeletePressed(wordModel: WordModel)
}