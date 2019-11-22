package com.vocabulary.application

import android.app.Application
import com.vocabulary.managers.Injector

class App: Application() {

//    companion object {
//        @JvmStatic
//        lateinit var instance: App
//    }

    override fun onCreate() {
        super.onCreate()
//        instance = this
        Injector.application = this
        Injector.initManagers()

    }
}
