package ru.gonchar17narod.selferificator.business

import java.io.File

data class Record(
    val file: File,
    var playing: Boolean = false
)