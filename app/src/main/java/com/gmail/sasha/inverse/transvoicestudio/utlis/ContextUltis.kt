package com.gmail.sasha.inverse.transvoicestudio.utlis

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isMicrophoneAvaileble() =
    (ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED)