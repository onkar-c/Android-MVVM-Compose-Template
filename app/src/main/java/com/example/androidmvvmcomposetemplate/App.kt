package com.example.androidmvvmcomposetemplate

import android.app.Application

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize logging for debug builds
        //if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        //}
    }
}