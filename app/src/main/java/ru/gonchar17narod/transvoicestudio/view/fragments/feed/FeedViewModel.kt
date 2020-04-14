package ru.gonchar17narod.transvoicestudio.view.fragments.feed


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gonchar17narod.transvoicestudio.business.FeedInteractor
import ru.gonchar17narod.transvoicestudio.business.FeedWebEntity


class FeedViewModel : ViewModel() {

    val liveFeedItems = MutableLiveData<List<FeedWebEntity>>().apply {
        value = FeedInteractor.getWebEntities()
    }
}