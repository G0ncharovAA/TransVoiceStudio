package ru.gonchar17narod.selferificator.ui.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_record.view.*
import ru.gonchar17narod.selferificator.R
import ru.gonchar17narod.selferificator.ui.items.RecordItem

class HomeFragment : Fragment() {

    private val groupieAdapter = GroupAdapter<GroupieViewHolder>()
    private val recordsAdapter = RecordsAdapter()
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )

        root.records_recycler_view.adapter =
            recordsAdapter
        //groupieAdapter

        ItemTouchHelper(SwipeToDeleteCallback()).attachToRecyclerView(root.records_recycler_view)

        homeViewModel.liveRecords.observe(
            viewLifecycleOwner,
            Observer {
                root.records_recycler_view.adapter.let {
                    it?.notifyDataSetChanged()
                }
//                groupieAdapter.clear()
//                groupieAdapter.addAll(
//                    it.map {
//                        RecordItem(
//                            homeViewModel
//                        )
//                    }
//                )
            }
        )

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button_recording.setOnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    homeViewModel.startRecording()
                    media_indicator.text = getString(R.string.recording)
                }
                ACTION_UP -> {
                    homeViewModel.stopRecording()
                    media_indicator.text = getString(R.string.idle)
                }
            }
            false
        }

        button_playing.setOnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    homeViewModel.liveRecords.value?.first()?.apply {
                        homeViewModel.startPlaying(
                            this
                        )
                        media_indicator.text = getString(R.string.playing)
                    }
                }
                ACTION_UP -> {
                    homeViewModel.stopPlaying()
                    media_indicator.text = getString(R.string.idle)
                }
            }
            false
        }
    }

    private inner class RecordHolder(
        @SuppressLint("InflateParams") itemView: View =
            layoutInflater.inflate(
                R.layout.item_record,
                null,
                false
            ).apply {
                layoutParams = LinearLayout.LayoutParams(
                    MATCH_PARENT,
                    WRAP_CONTENT
                )
            }
    ) : RecyclerView.ViewHolder(itemView)

    private inner class RecordsAdapter : RecyclerView.Adapter<RecordHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = RecordHolder()

        override fun getItemCount() =
            homeViewModel.liveRecords.value?.size ?: 0

        override fun onBindViewHolder(holder: RecordHolder, position: Int) {
            homeViewModel.liveRecords.value?.get(position)?.apply {
                with(holder.itemView) {
                    text_record_name.text = file.name
                    if (playing) {
                        item_button_play.text = getString(R.string.stop)
                        item_button_play.setOnClickListener {
                            homeViewModel.stopPlaying()
                            playing = false
                            recordsAdapter.notifyItemChanged(position)
                        }
                    } else {
                        item_button_play.text = getString(R.string.play)
                        item_button_play.setOnClickListener {
                            homeViewModel.startPlaying(this@apply)
                            playing = true
                            recordsAdapter.notifyItemChanged(position)
                        }
                    }
                }
            }
        }
    }

    private inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.START or ItemTouchHelper.END
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            homeViewModel.liveRecords.value?.get(viewHolder.adapterPosition)?.apply {
                homeViewModel.deleteRecord(this)
            }
        }
    }
}
