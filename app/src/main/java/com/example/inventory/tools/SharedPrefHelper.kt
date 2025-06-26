package com.example.inventory.tools

import android.content.Context
import android.content.SharedPreferences

object SharedPrefHelper {

    private const val PREF_NAME = "app_prefs"
    private lateinit var sharedPref: SharedPreferences

    private const val IS_CONNECTED = "SHARED_PREF_IS_CONNECTED"
    private const val IS_NIGHT_MODE = "SHARED_PREF_IS_NIGHT_MODE"
    private const val LANG_KEY = "SHARED_PREF_LANG"

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun remove(key: String) {
        sharedPref.edit().remove(key).apply()
    }

    fun clearAll() {
        sharedPref.edit().clear().apply()
    }

    fun setUserConnected(value: Boolean) {
        sharedPref.edit().putBoolean(IS_CONNECTED, value).apply()
    }

    fun istUserConnected(default: Boolean = false): Boolean {
        return sharedPref.getBoolean(IS_CONNECTED, default)
    }

    fun setNightMode(value: Boolean) {
        sharedPref.edit().putBoolean(IS_NIGHT_MODE, value).apply()
    }

    fun isNightMode(default: Boolean = false): Boolean {
        return sharedPref.getBoolean(IS_NIGHT_MODE, default)
    }

    fun saveLang(lang: String) {
        sharedPref.edit().putString(LANG_KEY, lang).apply()
    }

    fun getLang(default: String = "fr"): String {
        return sharedPref.getString(LANG_KEY, default) ?: default
    }
}
