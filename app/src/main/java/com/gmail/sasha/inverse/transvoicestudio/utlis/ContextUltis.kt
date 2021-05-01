package com.gmail.sasha.inverse.transvoicestudio.utlis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.gmail.sasha.inverse.transvoicestudio.BuildConfig
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
        ShareCompat.IntentBuilder.from(this@shareFile).setStream(
            this
        ).intent.apply {
            data = this@with
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            resolveActivity(getPackageManager())?.let {
                startActivity(this);
            }
        }
    }
