package ru.gonchar17narod.selferificator.data

import ru.gonchar17narod.selferificator.App
import ru.gonchar17narod.selferificator.R
import ru.gonchar17narod.selferificator.business.FeedWebEntity

object FeedRepository {

    fun getWebEntities(): List<FeedWebEntity>? =
        App.instance.resources.getStringArray(
            R.array.items_web_links
        ).map {
            FeedWebEntity(it)
        }
}