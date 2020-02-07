package com.vocabulary.customViews.swipeable_view

import com.vocabulary.models.LanguageModel

interface SwipeLanguageClickListener {
    fun onViewPressed(languageModel: LanguageModel)
    fun onEditPressed(languageModel: LanguageModel)
    fun onDeletePressed(languageModel: LanguageModel)
}