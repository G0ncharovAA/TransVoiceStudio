package com.gmail.sasha.inverse.transvoicestudio.business

import com.gmail.sasha.inverse.transvoicestudio.App
import com.gmail.sasha.inverse.transvoicestudio.data.SettingsRepository

object SettingsInteractor {

    private const val PREFERENCES_FILE_NAME =
        "com.gmail.sasha.inverse.transvoicestudio.REFERENCES_FILE_KEY"

    private val settingsRepository by lazy {
        SettingsRepository(
            appContext = App.instance.applicationContext,
            preferencesFileName = PREFERENCES_FILE_NAME,
        )
    }

    fun getGuidePageIndex(): Int =
        settingsRepository.getGuidePageIndex()

    fun setGuidePageIndex(index: Int) =
        settingsRepository.setGuidePageIndex(index)
}