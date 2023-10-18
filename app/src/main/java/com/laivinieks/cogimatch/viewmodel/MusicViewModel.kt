package com.laivinieks.cogimatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {
    private val _isSplashScreenCompleted = MutableLiveData(false)
    val isSplashScreenCompleted: LiveData<Boolean>
        get() = _isSplashScreenCompleted

    fun setSplashScreenCompleted(completed: Boolean) {
        _isSplashScreenCompleted.value = completed
    }
}