package ru.gonchar17narod.selferificator.view.fragments.home

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.gonchar17narod.selferificator.App
import ru.gonchar17narod.selferificator.business.MediaInteractor
import ru.gonchar17narod.selferificator.business.Record
import ru.gonchar17narod.selferificator.utlis.setupAudio

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    val liveRecords = MutableLiveData<List<Record>>()

    private val mMediaRecorder = MediaRecorder().apply {
        setOnInfoListener { mr, what, extra ->
            Log.i("VERF", "what: $what extra: $extra")
        }
    }
    private var mediaPlayer: MediaPlayer? = null

    init {
        liveRecords.value = MediaInteractor.getAllRecords()
        setupRecorder()
    }

    fun startRecording() =
        with(mMediaRecorder) {
            setOutputFile(MediaInteractor.getNewFile().absolutePath)
            prepare()
            start()
        }

    fun stopRecording() {
        try {
            mMediaRecorder.stop()
            setupRecorder()
        } finally {
            refreshRecordsList()
        }
    }

    fun startPlaying(record: Record) {
        if (
            (App.instance.getSystemService(Context.AUDIO_SERVICE) as AudioManager).isMusicActive
        ) {
            Log.i("VERF", "Busy")
        }

        mediaPlayer?.let {
            it.release()
            mediaPlayer = null
            resetPlayingStates()
            record.playing = true
            updatePlayingStates()
        }

        mediaPlayer = MediaPlayer().apply {
            setupAudio()
            setOnCompletionListener {
                it.release()
                record.playing = false
                updatePlayingStates()
            }
            setDataSource(
                record.file.absolutePath
            )
            prepare()
            start()
        }
    }

    fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
        resetPlayingStates()
        updatePlayingStates()
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
    }

    private fun resetPlayingStates() =
        with(liveRecords) {
            value?.forEach {
                it.playing = false
            }
        }

    private fun updatePlayingStates() =
        with(liveRecords) {
            value = value // is there any more reliable way to update LivaData?
        }

    private fun setupRecorder() {
        with(mMediaRecorder) {
            reset()
            setupAudio()
        }
    }
}