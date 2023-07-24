package io.github.tomgarden.kotlincoroutine

import android.app.Application
import android.util.Log
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("TOM_GARDEN","APP init Done")
    }

}