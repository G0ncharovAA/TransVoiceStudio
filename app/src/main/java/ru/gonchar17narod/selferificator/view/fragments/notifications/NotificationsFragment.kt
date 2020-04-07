package ru.gonchar17narod.selferificator.view.fragments.notifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_notifications.*
import ru.gonchar17narod.selferificator.R
import ru.gonchar17narod.selferificator.business.FeedWebEntity

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        feed_recycler_view.adapter = notificationsViewModel.feedAdapter
        super.onActivityCreated(savedInstanceState)
    }

    private fun openUrlWithBrowser(feedWebEntity: FeedWebEntity) =
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    feedWebEntity.url
                )
            )
        )
}
