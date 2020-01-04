package com.vocabulary.managers

import com.vocabulary.models.LanguageModel

class LanguageManager {

    var currentLanguage : LanguageModel? = null

    fun isSelected(languageModel: LanguageModel) : Boolean {
        if(this.currentLanguage != null && this.currentLanguage?.id == languageModel.id) {
            return true
        }
        return false
    }


}
