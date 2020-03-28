package ru.gonchar17narod.selferificator.data

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import ru.gonchar17narod.selferificator.App
import ru.gonchar17narod.selferificator.utlis.clear
import java.io.File

object MediaRepository {

    val MEDIA_FILE = "record.mp3"
    val recordFile = File(App.instance.filesDir, MEDIA_FILE)
    val mediaRecorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setOutputFile(recordFile.absolutePath)
    }
    val mediaPlayer = MediaPlayer().apply {
        setAudioStreamType(AudioManager.STREAM_MUSIC)
      //  setDataSource(App.instance, Uri.parse(recordFile.absolutePath))
    }

    fun startRecording() {
        mediaRecorder.prepare()
        mediaRecorder.start()
    }

    fun stopRecording() {
        mediaRecorder.stop()
    }

    fun startPlaying() {
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun stopPlaying() {
        mediaPlayer.stop()
    }

    fun clearRecord() =
        recordFile.clear()
}