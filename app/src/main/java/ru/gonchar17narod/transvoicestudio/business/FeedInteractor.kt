package ru.gonchar17narod.transvoicestudio.business

import ru.gonchar17narod.transvoicestudio.data.FeedRepository

object FeedInteractor {

    fun getWebEntities(): List<FeedWebEntity> =
        FeedRepository.getWebEntities() ?: emptyList()
}