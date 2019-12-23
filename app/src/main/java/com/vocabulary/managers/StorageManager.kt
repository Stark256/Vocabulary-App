package com.vocabulary.managers

class StorageManager: PreferenceHelper(){
    companion object {
        private const val CUSTOM_THEME_KEY = "theme"
        private const val CURRENT_LANGUAGE_KEY = "language"
    }


    fun setLanguage(language: String){
        storeValue(Injector.application!!, CURRENT_LANGUAGE_KEY, language)
    }

    fun getLanguage(): String? = getStringValue(Injector.application!!, CURRENT_LANGUAGE_KEY)

    fun setTheme(theme: String){
        storeValue(Injector.application!!, CUSTOM_THEME_KEY, theme)
    }

    fun getTheme(): String? = getStringValue(Injector.application!!, CUSTOM_THEME_KEY)

}
