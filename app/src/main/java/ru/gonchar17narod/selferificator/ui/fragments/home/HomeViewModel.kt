package ru.gonchar17narod.selferificator.ui.fragments.home

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.lifecycle.*
import kotlinx.coroutines.launch
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
        setOnInfoListener { mr, what, extra ->

        }
    }
    val mMediaplayer = MediaPlayer().apply {
        setOnCompletionListener {
            resetPlayngStates()
        }
    }

    init {
        liveRecords.value = MediaInteractor.getAllRecords()
        setupRecorder()
        setupPlayer()
    }

    fun startRecording() =
        with (mMediaRecorder) {
            setOutputFile(MediaInteractor.getNewFile().absolutePath)
            prepare()
            start()
        }

    fun stopRecording() {
        mMediaRecorder.stop()
        setupRecorder()
        refreshRecordsList()
    }

    fun startPlaying(file: File) {
        if (
            (App.instance.getSystemService(Context.AUDIO_SERVICE) as AudioManager).isMusicActive
        ) {
            stopPlaying()
        }
        try {
            with(mMediaplayer) {
                setDataSource(
                    file.absolutePath
                )
                prepare()
                start()
            }
        } catch (e: Exception) {

        }
    }

    fun stopPlaying() {
        mMediaplayer.stop()
        setupPlayer()
        resetPlayngStates()
    }

    fun deleteRecord(record: Record) =
        viewModelScope.launch {
            try {
                MediaInteractor.deleteRecord(
                    record
                )
            } finally {
                refreshRecordsList()
            }
        }

    fun refreshRecordsList() =
        liveRecords.postValue(MediaInteractor.getAllRecords())

    override fun onCleared() {
        mMediaRecorder.release()
        mMediaplayer.release()
    }

    private fun resetPlayngStates() =
        liveRecords.value?.forEach {
            it.playing = false
        }

    private fun setupRecorder() {
        with(mMediaRecorder) {
            reset()
            setupAudio()
        }
    }

    private fun setupPlayer() {
        with(mMediaplayer) {
            reset()
            setupAudio()
        }
    }
}