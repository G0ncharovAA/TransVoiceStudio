package com.gmail.sasha.inverse.transvoicestudio.view.fragments.feed


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.sasha.inverse.transvoicestudio.business.FeedInteractor
import com.gmail.sasha.inverse.transvoicestudio.business.FeedWebEntity


class FeedViewModel : ViewModel() {

    val liveFeedItems = MutableLiveData<List<FeedWebEntity>>().apply {
        value = FeedInteractor.getWebEntities()
    }
}