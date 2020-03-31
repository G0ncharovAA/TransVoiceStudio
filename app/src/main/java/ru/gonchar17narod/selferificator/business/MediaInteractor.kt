package ru.gonchar17narod.selferificator.business

import ru.gonchar17narod.selferificator.data.MediaRepository
import java.io.File

object MediaInteractor {

    fun getNewFile(): File =
        MediaRepository.getNewFile()

    fun getAllRecords() =
        MediaRepository.getAllRecords()
            ?.sortedByDescending {
                it.name
            }
            ?.map {
                Record(
                    it
                )
            }

    fun deleteRecord(record: Record) =
        MediaRepository.deleteRecord(record)
}