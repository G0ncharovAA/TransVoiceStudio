package com.gmail.sasha.inverse.transvoicestudio.business

import com.gmail.sasha.inverse.transvoicestudio.data.MediaRepository
import java.io.File

object MediaInteractor {

    fun prepareRecordsFolder() =
        MediaRepository.prepareRecordsFolder()

    fun getNewFile(): File =
        MediaRepository.getNewFile()

    fun getAllRecords(): List<RecordEntity> =
        MediaRepository.getAllRecords()
            ?.sortedByDescending {
                it.name
            }
            ?.map {
                RecordEntity(
                    it
                )
            } ?: emptyList()

    suspend fun deleteRecord(record: RecordEntity) =
        MediaRepository.deleteRecord(record)

    fun deleteLastRecord() =
        MediaRepository.deleteLastRecord()
}