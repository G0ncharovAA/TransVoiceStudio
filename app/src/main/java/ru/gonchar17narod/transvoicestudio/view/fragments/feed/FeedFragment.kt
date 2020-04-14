package ru.gonchar17narod.transvoicestudio.view.fragments.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.gonchar17narod.transvoicestudio.R
import ru.gonchar17narod.transvoicestudio.view.items.FeedWebItem

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private val feedAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProvider(this).get(FeedViewModel::class.java)

        feedViewModel.liveFeedItems.observe(
            viewLifecycleOwner,
            Observer {
                with(feedAdapter) {
                    clear()
                    addAll(
                        it.map {
                            FeedWebItem(it)
                        }
                    )
                    notifyDataSetChanged()
                }
            }
        )
        val root = inflater.inflate(R.layout.fragment_feed, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        feed_recycler_view.adapter = feedAdapter
        super.onActivityCreated(savedInstanceState)
    }
}
