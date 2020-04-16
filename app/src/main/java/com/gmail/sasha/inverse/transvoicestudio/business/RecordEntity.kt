package com.gmail.sasha.inverse.transvoicestudio.business

import java.io.File

data class RecordEntity(
    val file: File,
    var playing: Boolean = false
)