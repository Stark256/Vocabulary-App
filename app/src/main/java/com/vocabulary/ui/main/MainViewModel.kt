package com.vocabulary.ui.main

import androidx.lifecycle.ViewModel
import com.vocabulary.managers.Injector

class MainViewModel : ViewModel() {

    fun loadAppData() {
        Injector.storageManager.getCurrentLanguageID()?.let {
            Injector.dbManager.getLanguageByID(it) { languageModel ->
                languageModel?.let {
                    Injector.languageManager.currentLanguageLoaded(it)
                }
            }
        }

    }

}