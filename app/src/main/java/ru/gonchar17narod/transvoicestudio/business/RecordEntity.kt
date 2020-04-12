package ru.gonchar17narod.transvoicestudio.business

import java.io.File

data class RecordEntity(
    val file: File,
    var playing: Boolean = false
)