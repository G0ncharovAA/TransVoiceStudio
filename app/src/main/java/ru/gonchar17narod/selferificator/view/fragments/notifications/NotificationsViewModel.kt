package ru.gonchar17narod.selferificator.view.fragments.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.gonchar17narod.selferificator.business.FeedInteractor
import ru.gonchar17narod.selferificator.business.FeedWebEntity
import ru.gonchar17narod.selferificator.view.items.FeedWebItem

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
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