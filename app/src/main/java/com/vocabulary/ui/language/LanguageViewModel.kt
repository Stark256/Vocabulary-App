package com.vocabulary.ui.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.managers.Injector
import com.vocabulary.models.LanguageModel

class LanguageViewModel: ViewModel() {

    val languages = MutableLiveData<ArrayList<LanguageModel>>()
    val viewState = MutableLiveData<LanguageInitType>()
    private var previousLanguage: LanguageModel? = null

    init {
        this.previousLanguage = Injector.languageManager.getCurrentLanguageIfSelected()
    }

    fun checkIfNeedUpdate() {
        val currentLanguage = Injector.languageManager.getCurrentLanguageIfSelected()
       if(previousLanguage != null) {
           if(currentLanguage != null) {
               if(previousLanguage!!.id == currentLanguage.id) {
                   Injector.languageManager.needUpdate(false)
               } else {
                   Injector.languageManager.needUpdate(true)
               }
           } else {
               Injector.languageManager.needUpdate(true)
           }
       } else {
           if(currentLanguage != null) {
               Injector.languageManager.needUpdate(true)
           } else {
               Injector.languageManager.needUpdate(false)
           }
       }
    }

    fun addEditLanguage(
        languageModel: LanguageModel? = null,
        newLanguage: String? = null,
        result: (String?) -> Unit) {

        if(languageModel != null) {
            editLanguage(languageModel, newLanguage, result)
        } else {
            addLanguage(newLanguage, result)
        }
    }

    private fun addLanguage(newLanguage: String?, result: (String?) -> Unit) {
        Injector.dbManager.addLanguage(newLanguage) { res ->
            result.invoke(res)
            if (res == null) {
                getLanguages()
            }
        }
    }

    private fun editLanguage(languageModel: LanguageModel, newLanguage: String?, result: (String?) -> Unit) {
        Injector.dbManager.updateLanguageName(languageModel, newLanguage) { res ->
            result.invoke(res)
            if (res == null) {
                getLanguages()
            }
        }
    }

    fun deleteLanguage(languageModel: LanguageModel, result: () -> Unit) {
        if(Injector.languageManager.isSelected(languageModel)) {
            Injector.languageManager.deleteCurrentLanguage()
        }
        Injector.dbManager.deleteLanguage(languageModel) {
            result.invoke()
            getLanguages()
        }
    }


    fun getLanguages() {
        viewState.value = LanguageInitType.LANGUAGES_LOADING
        Injector.dbManager.getLanguages { result ->
            if(result.isNotEmpty()) { viewState.value = LanguageInitType.LANGUAGES_NOT_EMPTY }
            else { viewState.value = LanguageInitType.LANGUAGES_EMPTY }
            languages.value = result
        }
    }

    fun selectLanguage(languageModel: LanguageModel) {
        if(!Injector.languageManager.isSelected(languageModel)) {
            Injector.languageManager.setCurrentLanguage(languageModel)
        }
    }

    enum class LanguageInitType {
        LANGUAGES_EMPTY,
        LANGUAGES_NOT_EMPTY,
        LANGUAGES_LOADING
    }
}
