package com.gmail.sasha.inverse.transvoicestudio.data

import android.content.Context

class SettingsRepository(
    appContext: Context,
    preferencesFileName: String,
) {

    private val GUIDE_INDEX_KEY = "GUIDE_INDEX_KEY"

    private val sharedPreferences by lazy {
        appContext.getSharedPreferences(
            preferencesFileName,
            Context.MODE_PRIVATE,
        )
    }

    fun getGuidePageIndex(): Int =
        sharedPreferences.getInt(
            GUIDE_INDEX_KEY,
            0,
        )

    fun setGuidePageIndex(index: Int) {
        with(
            sharedPreferences.edit()
        ) {
            putInt(GUIDE_INDEX_KEY, index)
            apply()
        }
    }
}