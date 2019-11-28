package com.vocabulary.managers

import android.content.Context
import com.vocabulary.application.App

object Injector {

    var application: App? = null

    lateinit var languageManager: LanguageManager
    lateinit var dbManager: DBManager

    fun initManagers() {
        this.languageManager = LanguageManager()
    }

    fun initDBManager(context: Context) {
        this.dbManager = DBManager(context)
    }

}
