package ru.gonchar17narod.transvoicestudio.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gonchar17narod.transvoicestudio.App
import ru.gonchar17narod.transvoicestudio.business.RecordEntity
import java.io.File

object MediaRepository {

    const val RECORDS_FOLDER_NAME = "records"
    private val recordsFolder = File(
        App.instance.filesDir,
        "$RECORDS_FOLDER_NAME${File.separator}"
    )

    fun prepareRecordsFolder() {
        if (!recordsFolder.exists()) {
            recordsFolder.mkdir()
        }
    }

    fun getNewFile(): File =
        File(
            recordsFolder,
            "record_${System.currentTimeMillis()}.mp3"
        )

    fun getAllRecords() =
        recordsFolder.listFiles()
            ?.toList()
            ?.filter {
                it.name.endsWith(".mp3", true)
            }

    suspend fun deleteRecord(record: RecordEntity) =
        withContext(Dispatchers.IO) {
            record.file.deleteRecursively()
        }

    fun deleteLastRecord() =
       getAllRecords()?.firstOrNull()?.deleteRecursively()
}