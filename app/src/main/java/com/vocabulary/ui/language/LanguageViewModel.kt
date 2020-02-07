package com.vocabulary.ui.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.managers.Injector
import com.vocabulary.models.LanguageModel

class LanguageViewModel: ViewModel() {


    val languages = MutableLiveData<ArrayList<LanguageModel>>()

    fun addEditLanguage(languageModel: LanguageModel? = null, newLanguage: String? = null, result: (String?) -> Unit) {
        if(languageModel != null && newLanguage != null) {
            editLanguage(languageModel, newLanguage, result)
        } else if(newLanguage != null) {
            addLanguage(newLanguage, result)
        }
    }

    private fun addLanguage(newLanguage: String, result: (String?) -> Unit) {
        Injector.dbManager.addLanguage(newLanguage) { res ->
            result.invoke(res)
            if (res == null) {
                getLanguages()
            }
        }
    }

    private fun editLanguage(languageModel: LanguageModel, newLanguage: String, result: (String?) -> Unit) {
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
        Injector.dbManager.getLanguages { result ->
            languages.value = result
        }
    }

    fun selectLanguage(languageModel: LanguageModel) {
        if(!Injector.languageManager.isSelected(languageModel)) {
            Injector.languageManager.setCurrentLanguage(languageModel)
        }
    }
}
