package com.vocabulary.managers

class StorageManager: PreferenceHelper(){
    companion object {
        private const val CUSTOM_THEME_KEY = "theme"
        private const val CURRENT_LANGUAGE_KEY = "language"
    }

    /*-----LANGUAGES-----*/
    fun setCurrentLanguageID(languageID: Long){
        storeLong(Injector.application!!, CURRENT_LANGUAGE_KEY, languageID)
    }

    fun getCurrentLanguageID(): Long? = getLongValue(Injector.application!!, CURRENT_LANGUAGE_KEY)

    fun removeCurrentLanguageID() {
        storeLong(Injector.application!!, CURRENT_LANGUAGE_KEY, null)
    }
    /*-------------------*/


    fun setTheme(theme: String){
        storeValue(Injector.application!!, CUSTOM_THEME_KEY, theme)
    }

    fun getTheme(): String? = getStringValue(Injector.application!!, CUSTOM_THEME_KEY)

}
