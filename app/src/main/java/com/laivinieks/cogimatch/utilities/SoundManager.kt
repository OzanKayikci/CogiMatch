package com.laivinieks.cogimatch.utilities

import android.content.Context
import android.content.SharedPreferences
import android.media.SoundPool


class SoundManager(
    context: Context,
    private val sharedPreferences: SharedPreferences,
    soundSource1: Int,
    soundSource2: Int,
    soundSource3: Int,
    soundSource4: Int
) {
    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(4).build()
    private var cardFlip: Int = 0
    private var closeCard: Int = 0
    private var matchedSound: Int = 0
    private var clockTicking: Int = 0
    private var soundEffectsArray: Array<Int>
    private var volume = sharedPreferences.getFloat(Constants.SFX_VOLUME, Constants.DEF_VOLUME)

    private var clockTickingStreamId: Int = 0

    init {
        cardFlip = soundPool.load(context, soundSource1, 1) // Load the sound from resources.
        closeCard = soundPool.load(context, soundSource2, 1) // Load the sound from resources.
        matchedSound = soundPool.load(context, soundSource3, 1) // Load the sound from resources.
        clockTicking = soundPool.load(context, soundSource4, 1)
        soundEffectsArray = arrayOf(cardFlip, closeCard, matchedSound, clockTicking)
    }

    fun playSound(index: Int) {
        volume = sharedPreferences.getFloat(Constants.SFX_VOLUME, Constants.DEF_VOLUME)
        val id = soundPool.play(soundEffectsArray[index], volume, volume, 1, 0, 1.0f)
        if (soundEffectsArray[index] == clockTicking) {
            clockTickingStreamId = id
        }
    }

    fun stopSound() {
        soundPool.stop(clockTickingStreamId)
    }


}
