package com.gmail.sasha.inverse.transvoicestudio.utlis

import java.io.File

fun File.clear() {
    if (exists()) {
        delete()
    }
}