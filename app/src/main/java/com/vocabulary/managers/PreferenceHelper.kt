package com.vocabulary.managers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

abstract class PreferenceHelper(){


    fun getPref(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    protected fun getStringValue(context: Context, key: String):
            String? = getPref(context).getString(key, null)

    protected fun getBooleanValue(context: Context, key: String) :
            Boolean = getPref(context).getBoolean(key, false)

    protected fun getLongValue(context: Context, key: String) : Long? {
        val result: Long = getPref(context).getLong(key, -1L)
        return if(result != -1L) result else null
    }

    protected fun getIntValue(context: Context, key: String) : Int? {
        val result : Int = getPref(context).getInt(key, -1)
        return if(result != -1) result else null
    }


    protected fun <U> storeValue(context: Context, key: String, value: U?) {
        if (value == null) { getPref(context).edit().remove(key).apply() }
        else { getPref(context).edit().putString(key, value.toString()).apply() }

    }

    protected fun storeBoolean(context: Context, key: String, value: Boolean?) {
        if (value == null) { getPref(context).edit().remove(key).apply() }
        else {getPref(context).edit().putBoolean(key, value).apply() }
    }

    protected fun storeLong(context: Context, key: String, value: Long?) {
        if (value == null) { getPref(context).edit().remove(key).apply() }
        else {getPref(context).edit().putLong(key, value).apply() }
    }

    protected fun storeInt(context: Context, key: String, value: Int?) {
        if (value == null) { getPref(context).edit().remove(key).apply() }
        else {getPref(context).edit().putInt(key, value).apply() }
    }
}
