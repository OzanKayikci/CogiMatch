package com.laivinieks.cogimatch.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.laivinieks.cogimatch.ui.MainActivity

import java.util.Locale

object LocaleHelper {
    fun setLocale(language: String?, context: Context) {

        val config = Configuration(context.resources.configuration)
        config.setToDefaults()
        val locale = if (language == "sys") Locale.getDefault() else {
            Locale(language)
        }
        Locale.setDefault(locale)
        config.setLocale(locale)

        context.createConfigurationContext(config);


    }



}