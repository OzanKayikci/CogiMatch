package com.laivinieks.cogimatch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laivinieks.cogimatch.utilities.Constants

class SettingsViewModel : ViewModel() {

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


}