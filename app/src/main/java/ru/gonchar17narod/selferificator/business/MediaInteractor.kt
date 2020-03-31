package ru.gonchar17narod.selferificator.business

import ru.gonchar17narod.selferificator.App
import java.io.File

object MediaInteractor {

    fun getNewFile(): File =
        File(
            App.instance.filesDir,
            "record_${System.currentTimeMillis()}.mp3"
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