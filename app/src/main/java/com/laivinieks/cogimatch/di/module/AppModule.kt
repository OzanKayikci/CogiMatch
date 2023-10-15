package com.laivinieks.cogimatch.di.module

import android.content.Context
import android.content.SharedPreferences
import com.laivinieks.cogimatch.utilities.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext app: Context) = app.getSharedPreferences(
        Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideArcadeScore(sharedPreferences: SharedPreferences) =
        sharedPreferences.getInt(Constants.ARCADE_SCORE, 0)
}