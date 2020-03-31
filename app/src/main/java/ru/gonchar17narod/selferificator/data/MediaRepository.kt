package ru.gonchar17narod.selferificator.data

import ru.gonchar17narod.selferificator.App
import ru.gonchar17narod.selferificator.business.Record
import java.io.File

object MediaRepository {

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

    fun deleteRecord(record: Record) =
        record.file.deleteRecursively()
}