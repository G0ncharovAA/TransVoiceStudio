package com.gmail.sasha.inverse.transvoicestudio.data

import com.gmail.sasha.inverse.transvoicestudio.App
import com.gmail.sasha.inverse.transvoicestudio.R
import com.gmail.sasha.inverse.transvoicestudio.business.FeedWebEntity

object FeedRepository {

    fun getWebEntities(): List<FeedWebEntity>? =
        App.instance.resources.getStringArray(
            R.array.items_web_links
        ).map {
            FeedWebEntity(it)
        }
}