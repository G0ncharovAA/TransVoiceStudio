package ru.gonchar17narod.selferificator.utlis

import java.io.File

fun File.clear() {
    if (exists()) {
        delete()
    }
}