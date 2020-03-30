package ru.gonchar17narod.selferificator.business

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.gonchar17narod.selferificator.App
import ru.gonchar17narod.selferificator.data.MediaRepository
import java.io.File

object MediaInteractor {

    fun getNewFile(): File =
        File(
            App.instance.filesDir,
            "${System.currentTimeMillis()}.mp3"
        )

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

    fun deleteRecord(record: Record) =
       record.file.deleteRecursively()
}