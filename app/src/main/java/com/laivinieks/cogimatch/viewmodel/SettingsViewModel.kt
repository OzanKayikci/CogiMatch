package com.laivinieks.cogimatch.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laivinieks.cogimatch.ui.MainActivity
import com.laivinieks.cogimatch.utilities.Constants
import com.laivinieks.cogimatch.utilities.Language
import com.laivinieks.cogimatch.utilities.LanguageManager
import com.laivinieks.cogimatch.utilities.LocaleHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val languageManager: LanguageManager,
    @ApplicationContext private val appContext: Context,

    ) : ViewModel() {

    private var _musicVolume: MutableLiveData<Float> = MutableLiveData(Constants.DEF_VOLUME)
    val musicVolume get() = _musicVolume!!

    private var _sfxVolume: MutableLiveData<Float> = MutableLiveData(Constants.DEF_VOLUME)
    val sfxVolume get() = _sfxVolume!!


    fun setVolume(isMusic: Boolean, volume: Float) {
        if (isMusic) {
            _musicVolume.postValue(volume)

        } else {
            _sfxVolume.postValue(volume)
        }
    }

    fun setLanguage(language: Language, activity: Activity) {
        languageManager.setSelectedLanguage(language.value)
        LocaleHelper.setLocale(language.value, appContext).also {
            val intent = Intent(appContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity.finish()
            activity.startActivity(intent)
        }
    }


}