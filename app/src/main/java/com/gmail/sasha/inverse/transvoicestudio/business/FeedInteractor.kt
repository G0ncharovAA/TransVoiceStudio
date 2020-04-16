package com.gmail.sasha.inverse.transvoicestudio.business

import com.gmail.sasha.inverse.transvoicestudio.data.FeedRepository

object FeedInteractor {

    fun getWebEntities(): List<FeedWebEntity> =
        FeedRepository.getWebEntities() ?: emptyList()
}