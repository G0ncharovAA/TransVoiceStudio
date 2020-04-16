package com.gmail.sasha.inverse.transvoicestudio

import android.app.Application
import com.gmail.sasha.inverse.transvoicestudio.business.MediaInteractor

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        MediaInteractor.prepareRecordsFolder()
    }
}