package ru.gonchar17narod.selferificator.ui.home

import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.lifecycle.*
import ru.gonchar17narod.selferificator.App
import ru.gonchar17narod.selferificator.business.MediaInteractor
import ru.gonchar17narod.selferificator.business.Record
import ru.gonchar17narod.selferificator.utlis.setupAudio
import java.io.File

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    val liveRecordingState = MutableLiveData<Boolean>()
    val livePlayingState = MutableLiveData<Boolean>()
    val liveRecords = MutableLiveData<List<Record>>()

    val mMediaRecorder = MediaRecorder().apply {

    }
    val mMediaplayer = MediaPlayer()

    init {
        liveRecords.value = MediaInteractor.getAllRecords()
        setupRecorder()
        setupPlayer()
    }

    fun startRecording() {
        mMediaRecorder.start()
    }

    fun stopRecording() {
        mMediaRecorder.stop()
        setupRecorder()
        refreshRecordsList()
    }

    fun startPlaying(file: File) {
        with (mMediaplayer) {
            setDataSource(
                file.absolutePath
            )
            prepare()
            start()
        }
    }

    fun stopPlaying() {
        mMediaplayer.stop()
        setupPlayer()
    }

    fun refreshRecordsList() =
        liveRecords.postValue(MediaInteractor.getAllRecords())

    override fun onCleared() {
        mMediaRecorder.release()
        mMediaplayer.release()
    }

    private fun setupRecorder() {
        with(mMediaRecorder) {
            reset()
            setupAudio()
            setOutputFile(MediaInteractor.getNewFile(viewModelScope).absolutePath)
            prepare()
        }
    }

    private fun setupPlayer() {
        with (mMediaplayer) {
            reset()
            setupAudio()
        }
    }
}