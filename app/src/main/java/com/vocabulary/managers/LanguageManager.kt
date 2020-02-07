package com.vocabulary.managers

import com.vocabulary.models.LanguageModel

class LanguageManager {

    private var currentLanguage : LanguageModel? = null

    fun isSelected(languageModel: LanguageModel) : Boolean {
        if(this.currentLanguage != null && this.currentLanguage?.id == languageModel.id) {
            return true
        }
        return false
    }

    fun setCurrentLanguage(currentLanguage: LanguageModel) {
        this.currentLanguage = currentLanguage
        Injector.storageManager.setCurrentLanguageID(this.currentLanguage!!.id)
    }

    fun currentLanguageLoaded(currentLanguage: LanguageModel) {
        this.currentLanguage = currentLanguage
    }

    fun deleteCurrentLanguage() {
        Injector.storageManager.removeCurrentLanguageID()
    }
}
