package com.gmail.sasha.inverse.transvoicestudio.view.fragments.records

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_records.*
import kotlinx.android.synthetic.main.fragment_records.view.*
import kotlinx.android.synthetic.main.item_record.view.*
import com.gmail.sasha.inverse.transvoicestudio.R
import com.gmail.sasha.inverse.transvoicestudio.utlis.shareFile

class RecordsFragment : Fragment() {

    private val recordsAdapter = RecordsAdapter()
    private lateinit var indicationAnimator: ObjectAnimator
    private lateinit var reversedIndicationAnimator: ObjectAnimator
    private lateinit var recordsViewModel: RecordsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_records, container, false)

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
                    indicateRecordingState()
                }
                ACTION_UP -> {
                    recordsViewModel.stopRecording()
                    indicateIdleState()
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
                        indicatePlayingState()
                    }
                }
                ACTION_UP -> {
                    recordsViewModel.stopPlaying()
                    indicateIdleState()
                }
            }
            false
        }
        indicationAnimator =
            (AnimatorInflater.loadAnimator(
                context,
                R.animator.rotation_animator
            ) as ObjectAnimator).apply {
                target = animated_indicator
                start()
            }
        reversedIndicationAnimator =
            (AnimatorInflater.loadAnimator(
                context,
                R.animator.rotation_animator_reversed
            ) as ObjectAnimator).apply {
                target = animated_indicator
                start()
            }
        indicateIdleState()
        super.onActivityCreated(savedInstanceState)
    }

    private fun indicateIdleState() {
        indicationAnimator.pause()
        reversedIndicationAnimator.pause()
        media_indicator.text = getString(R.string.idle)
    }

    private fun indicateRecordingState() {
        indicationAnimator.pause()
        reversedIndicationAnimator.resume()
        media_indicator.text = getString(R.string.recording)
    }

    private fun indicatePlayingState() {
        indicationAnimator.resume()
        reversedIndicationAnimator.pause()
        media_indicator.text = getString(R.string.playing)
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
                    this.setOnLongClickListener {
                        activity?.shareFile(
                            this@apply.file
                        )
                        return@setOnLongClickListener true
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
