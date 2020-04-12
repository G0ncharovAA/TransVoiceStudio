package ru.gonchar17narod.transvoicestudio.utlis

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioEncoder.AAC
import android.media.MediaRecorder.AudioSource.MIC
import android.media.MediaRecorder.OutputFormat.MPEG_4

fun MediaRecorder.setupAudio() {
    setAudioSource(MIC)
    setOutputFormat(MPEG_4)
    setAudioEncoder(AAC)
}

fun MediaPlayer.setupAudio() {
    setAudioAttributes(
        AudioAttributes.Builder()
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .build()
    )
}