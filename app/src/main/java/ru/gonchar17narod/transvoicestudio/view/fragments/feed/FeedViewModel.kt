package ru.gonchar17narod.transvoicestudio.view.fragments.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.gonchar17narod.transvoicestudio.business.FeedInteractor
import ru.gonchar17narod.transvoicestudio.view.items.FeedWebItem

class FeedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is feed Fragment"
    }
    val text: LiveData<String> = _text

    val feedAdapter = GroupAdapter<GroupieViewHolder>().apply {
        addAll(
            FeedInteractor.getWebEntities().map {
                FeedWebItem(
                    feedWebEntity = it
                )
            }
        )
    }
}