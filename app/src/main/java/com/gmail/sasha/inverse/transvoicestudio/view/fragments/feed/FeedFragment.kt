package com.gmail.sasha.inverse.transvoicestudio.view.fragments.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.gmail.sasha.inverse.transvoicestudio.databinding.FragmentFeedBinding
import com.gmail.sasha.inverse.transvoicestudio.view.items.FeedWebItem

class FeedFragment : Fragment() {

    private val feedViewModel: FeedViewModel by viewModels()
    private val feedAdapter = GroupAdapter<GroupieViewHolder>()
    private var binding: FragmentFeedBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.feedRecyclerView?.adapter = feedAdapter
    }
}
