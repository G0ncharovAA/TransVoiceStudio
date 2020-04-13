package ru.gonchar17narod.transvoicestudio.view.fragments.records

import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.gonchar17narod.transvoicestudio.business.MediaInteractor
import ru.gonchar17narod.transvoicestudio.business.RecordEntity
import ru.gonchar17narod.transvoicestudio.utlis.setupAudio
import java.lang.Exception

class RecordsViewModel(application: Application) : AndroidViewModel(application) {

    val liveRecords = MutableLiveData<List<RecordEntity>>()

    private val mMediaRecorder = MediaRecorder()
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
        } catch (e: Exception) {
            MediaInteractor.deleteLastRecord()
        } finally {
            setupRecorder()
            refreshRecordsList()
        }
    }

    fun startPlaying(record: RecordEntity) {
        resetPlayingStates()
        record.playing = true
        mediaPlayer?.let {
            it.release()
            mediaPlayer = null
        }
        try {
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
        } catch (e: Exception) {
            record.playing = false
        } finally {
            updatePlayingStates()
        }
    }

    fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
        resetPlayingStates()
        updatePlayingStates()
    }

    fun deleteRecord(record: RecordEntity) =
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