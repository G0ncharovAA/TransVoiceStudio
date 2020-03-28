package ru.gonchar17narod.selferificator.business

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.gonchar17narod.selferificator.App
import ru.gonchar17narod.selferificator.data.MediaRepository
import java.io.File

object MediaInteractor {

    val FILES = listOf(
        "record1.mp3",
        "record2.mp3",
        "record3.mp3",
        "record4.mp3",
        "record5.mp3",
        "record6.mp3",
        "record7.mp3",
        "record8.mp3",
        "record9.mp3"
    )

    var fileIndex = 0

    private fun advanceIndex() {
        fileIndex++
        if (fileIndex >= FILES.size) {
            fileIndex = 0
        }
    }

    private fun previousIndex() =
        if (fileIndex <= 0) {
            FILES.size - 1
        } else {
            fileIndex - 1
        }

    private fun nextIndex() =
        if (fileIndex >= (FILES.size - 1)) {
            0
        } else {
            fileIndex + 1
        }

    fun getNewFile(scope: CoroutineScope): File {
        scope.launch {
            with(File(App.instance.filesDir, FILES[previousIndex()])) {
                if (exists()) {
                    delete()
                }
            }
        }
        advanceIndex()
        return File(App.instance.filesDir, FILES[nextIndex()])
    }

    fun getCurrenFile() =
        File(App.instance.filesDir, FILES[5])
       // File(App.instance.filesDir, FILES[fileIndex])

    fun getAllRecords() =
        App.instance.filesDir.listFiles()
            ?.toList()
            ?.filter {
                it.name.endsWith(".mp3", true)
            }
            ?.sortedByDescending {
                it.name
            }
            ?.map {
                Record(
                    it
                )
            }

//    fun startRecording() {
//        MediaRepository.clearRecord()
//        MediaRepository.startRecording()
//    }
//
//    fun stopRecording() {
//        MediaRepository.stopRecording()
//    }
//
//    fun startPlaying() {
//        MediaRepository.startPlaying()
//    }
//
//    fun stopPlaying() {
//        MediaRepository.stopPlaying()
//    }
}