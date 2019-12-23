package com.vocabulary.managers

import android.content.Context
import com.vocabulary.application.App

object Injector {

    var application: App? = null

    lateinit var languageManager: LanguageManager
    lateinit var storageManager: StorageManager
    lateinit var themeManager: ThemeManager
    lateinit var dbManager: DBManager

    fun initManagers() {
        // Storage Manager Should Init First
        this.storageManager = StorageManager()
        this.languageManager = LanguageManager()
        this.themeManager = ThemeManager()
        this.themeManager.setTheme(storageManager.getTheme())
    }

    fun initDBManager(context: Context) {
        this.dbManager = DBManager(context)
    }

}
