package com.gmail.sasha.inverse.transvoicestudio.utlis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.gmail.sasha.inverse.transvoicestudio.BuildConfig
import com.gmail.sasha.inverse.transvoicestudio.R
import java.io.File

fun Context.isMicrophoneAvailable() =
    (ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED)

@SuppressLint("QueryPermissionsNeeded")
fun Activity.shareFile(file: File) =
    with(
        FileProvider.getUriForFile(
            this@shareFile,
            BuildConfig.FILES_AUTHORITY,
            file
        )
    ) {
       Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, this@with)
            type = "audio/*"

           startActivity(Intent.createChooser(this, getString(R.string.send_via)))
        }
    }
