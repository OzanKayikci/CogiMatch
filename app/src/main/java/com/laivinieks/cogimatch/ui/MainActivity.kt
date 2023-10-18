package com.laivinieks.cogimatch.ui


import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.databinding.ActivityMainBinding
import com.laivinieks.cogimatch.utilities.Constants
import com.laivinieks.cogimatch.viewmodel.MusicViewModel
import com.laivinieks.cogimatch.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mediaPlayer: MediaPlayer? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val musicViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[MusicViewModel::class.java]
    }
    private val settingsViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[SettingsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer.create(this, R.raw.forestwalk_320bit).apply {
            val volume = sharedPreferences.getFloat(Constants.MUSIC_VOLUME, Constants.DEF_VOLUME)
            setVolume(volume, volume)
        }
        mediaPlayer?.isLooping = true // Set music to loop

        Timber.plant(Timber.DebugTree())

        //handle navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun getObservables() {
        musicViewModel.isSplashScreenCompleted.observe(this) {
            Log.d("music", "music started- $it")

            if (it) {
                mediaPlayer?.start() // Start or resume background music
            }

        }

        settingsViewModel.musicVolume.observe(this) {
            mediaPlayer?.setVolume(it, it)
        }
    }

    override fun onResume() {
        super.onResume()

        getObservables()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause() // Pause background music when the activity is not in the foreground
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release MediaPlayer when the activity is destroyed
    }
}