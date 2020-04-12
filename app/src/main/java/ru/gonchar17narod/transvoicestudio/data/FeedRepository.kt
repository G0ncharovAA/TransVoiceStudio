package ru.gonchar17narod.transvoicestudio.data

import ru.gonchar17narod.transvoicestudio.App
import ru.gonchar17narod.transvoicestudio.R
import ru.gonchar17narod.transvoicestudio.business.FeedWebEntity

object FeedRepository {

    fun getWebEntities(): List<FeedWebEntity>? =
        App.instance.resources.getStringArray(
            R.array.items_web_links
        ).map {
            FeedWebEntity(it)
        }
}