package com.vocabulary.ui.settings

import androidx.lifecycle.ViewModel
import com.vocabulary.managers.Injector
import com.vocabulary.utils.JsonUtils

class SettingsViewModel : ViewModel() {

    fun loadJson(jsonString: String, result: () -> Unit) {
        val jsonArr = JsonUtils.parseJson(jsonString)

        Injector.languageManager.getCurrentLanguageIfSelected()?.let {
            Injector.dbManager.addJsonWords(it.tableWords, jsonArr, result)
        }
    }

}