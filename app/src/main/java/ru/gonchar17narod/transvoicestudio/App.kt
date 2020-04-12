package ru.gonchar17narod.transvoicestudio

import android.app.Application
import ru.gonchar17narod.transvoicestudio.business.MediaInteractor

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