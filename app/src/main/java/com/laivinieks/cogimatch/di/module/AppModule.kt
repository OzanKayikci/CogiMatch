package com.laivinieks.cogimatch.di.module

import android.content.Context
import android.content.SharedPreferences
import com.laivinieks.cogimatch.utilities.Constants
import com.laivinieks.cogimatch.utilities.LanguageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideLanguageManager(sharedPreferences: SharedPreferences): LanguageManager {
        return LanguageManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext app: Context) = app.getSharedPreferences(
        Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideArcadeScore(sharedPreferences: SharedPreferences) =
        sharedPreferences.getInt(Constants.ARCADE_SCORE, 0)

    @Provides
    @Singleton
    fun provideMusicVolume(sharedPreferences: SharedPreferences) =
        sharedPreferences.getFloat(Constants.MUSIC_VOLUME, Constants.DEF_VOLUME)

    @Provides
    @Singleton
    fun provideSfxVolume(sharedPreferences: SharedPreferences) =
        sharedPreferences.getFloat(Constants.SFX_VOLUME, Constants.DEF_VOLUME)

    @Provides
    @Singleton
    fun provideLanguage(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(Constants.LANGUAGE, Locale.getDefault().language)
}