package com.capstone.capstonenft

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate

class NFT: Application() {
    var name = ""
    var privatekey = ""



    companion object {
        lateinit var instance: NFT
    }

    override fun onCreate() {
        super.onCreate()
        //다크모드 제거
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        instance = this

    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}