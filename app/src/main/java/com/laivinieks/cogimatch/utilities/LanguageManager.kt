package com.laivinieks.cogimatch.utilities

import android.content.SharedPreferences
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun getSelectedLanguage(): String {
        return sharedPreferences.getString(Constants.LANGUAGE, Locale.getDefault().language)
            ?: Locale.getDefault().language
    }

    fun setSelectedLanguage(language: String) {
        sharedPreferences.edit().putString(Constants.LANGUAGE, language).apply()
    }
}