package com.laivinieks.cogimatch.utilities

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider

import com.google.android.material.slider.RangeSlider
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.viewmodel.SettingsViewModel
import timber.log.Timber

class SoundSettings constructor(
    private val view: View,
    private val settingsViewModel: SettingsViewModel,
    private val sharedPreferences: SharedPreferences

) {

    private var musicSlider: RangeSlider = view.findViewById(R.id.rsMusic)
    private var sfxSlider: RangeSlider = view.findViewById(R.id.rsEffect)
    private var musicCheckBox: CheckBox = view.findViewById(R.id.cbMusic)
    private var sfxCheckBox: CheckBox = view.findViewById(R.id.cbSfx)


    init {
        val musicVolume = settingsViewModel.musicVolume.value
        val sfxVolume = settingsViewModel.sfxVolume.value

        fun sliderHandle(isMusic: Boolean, slider: RangeSlider) {

            slider.apply {
                setValues(if (isMusic) musicVolume else sfxVolume)
            }
            slider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {

                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    setVolume(isMusic, slider.values[0])


                    Log.d("music", "${slider.values[0]}")
                }
            })
            slider.addOnChangeListener { slider, value, fromUser ->

                Log.d("music", "volume")

            }

        }

        fun checkBoxHandle(isMusic: Boolean, checkBox: CheckBox) {

            checkBox.apply {
                val volume = (if (isMusic) musicVolume else sfxVolume)
                isChecked = volume != 0f
            }

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (!isChecked) {
                    setVolume(isMusic, 0f)
                    setSliderClickable(isMusic, false)

                } else {
                    setVolume(isMusic, Constants.DEF_VOLUME)
                    setSliderClickable(isMusic, true)
                }
            }
        }

        sliderHandle(true, musicSlider)
        sliderHandle(false, sfxSlider)

        checkBoxHandle(true, musicCheckBox)
        checkBoxHandle(false, sfxCheckBox)
    }

    private fun setSliderClickable(isMusic: Boolean, isClickable: Boolean) {

        if (isMusic) {
            Log.d("check", "clijkcab")
            musicSlider.isEnabled = isClickable
        } else
            sfxSlider.isEnabled = isClickable
    }

    private fun setVolume(isMusic: Boolean, value: Float) {


        val key = if (isMusic) Constants.MUSIC_VOLUME else Constants.SFX_VOLUME
        sharedPreferences.edit().putFloat(key, value).apply()
        settingsViewModel.setVolume(isMusic, value)
    }
}