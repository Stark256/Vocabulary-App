package com.vocabulary.managers

import com.vocabulary.application.App

object Injector {

    var application: App? = null

    lateinit var languageManager: LanguageManager
    lateinit var dbManager: DBManager

    fun initManagers() {
        this.languageManager = LanguageManager()
        this.dbManager = DBManager()
    }

}
