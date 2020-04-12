package ru.gonchar17narod.transvoicestudio.utlis

import java.io.File

fun File.clear() {
    if (exists()) {
        delete()
    }
}