package ru.gonchar17narod.transvoicestudio.view.fragments.records

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
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
import kotlinx.android.synthetic.main.fragment_records.*
import kotlinx.android.synthetic.main.fragment_records.view.*
import kotlinx.android.synthetic.main.item_record.view.*
import ru.gonchar17narod.transvoicestudio.R

class RecordsFragment : Fragment() {

    private val recordsAdapter = RecordsAdapter()
    private lateinit var recordsViewModel: RecordsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_records, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        recordsViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )

        root.records_recycler_view.adapter = recordsAdapter
        ItemTouchHelper(SwipeToDeleteCallback()).attachToRecyclerView(root.records_recycler_view)

        recordsViewModel.liveRecords.observe(
            viewLifecycleOwner,
            Observer {
                root.records_recycler_view.adapter.let {
                    it?.notifyDataSetChanged()
                }
            }
        )
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        button_recording.setOnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    recordsViewModel.startRecording()
                    media_indicator.text = getString(R.string.recording)
                }
                ACTION_UP -> {
                    recordsViewModel.stopRecording()
                    media_indicator.text = getString(R.string.idle)
                }
            }
            false
        }
        button_playing.setOnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    recordsViewModel.liveRecords.value?.firstOrNull()?.apply {
                        recordsViewModel.startPlaying(
                            this
                        )
                        media_indicator.text = getString(R.string.playing)
                    }
                }
                ACTION_UP -> {
                    recordsViewModel.stopPlaying()
                    media_indicator.text = getString(R.string.idle)
                }
            }
            false
        }
        super.onActivityCreated(savedInstanceState)
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
            recordsViewModel.liveRecords.value?.size ?: 0

        override fun onBindViewHolder(holder: RecordHolder, position: Int) {
            recordsViewModel.liveRecords.value?.get(position)?.apply {
                with(holder.itemView) {
                    text_record_name.text = file.name
                    if (playing) {
                        item_button_play.setImageResource(R.drawable.ic_pause)
                        item_button_play.setOnClickListener {
                            recordsViewModel.stopPlaying()
                        }
                    } else {
                        item_button_play.setImageResource(R.drawable.ic_play)
                        item_button_play.setOnClickListener {
                            recordsViewModel.startPlaying(this@apply)
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
            recordsViewModel.liveRecords.value?.get(viewHolder.adapterPosition)?.apply {
                recordsViewModel.deleteRecord(this)
            }
        }
    }
}
