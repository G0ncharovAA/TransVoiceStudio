package ru.gonchar17narod.selferificator.business

import ru.gonchar17narod.selferificator.data.FeedRepository

object FeedInteractor {

    fun getWebEntities(): List<FeedWebEntity> =
        FeedRepository.getWebEntities() ?: emptyList()
}