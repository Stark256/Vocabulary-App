package com.vocabulary.managers

import com.vocabulary.models.LanguageModel

class LanguageManager {

    private var currentLanguage : LanguageModel? = null

    var isNeedUpdate = false

    fun resetNeedUpdate() { isNeedUpdate = false }
    fun needUpdate(need: Boolean) { isNeedUpdate = need }

    fun getCurrentLanguageIfSelected() : LanguageModel? {
        return currentLanguage
    }

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
        this.currentLanguage = null
    }
}
