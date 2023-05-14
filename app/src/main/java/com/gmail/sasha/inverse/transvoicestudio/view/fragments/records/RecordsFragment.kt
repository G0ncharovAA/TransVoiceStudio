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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gmail.sasha.inverse.transvoicestudio.R
import com.gmail.sasha.inverse.transvoicestudio.databinding.FragmentRecordsBinding
import com.gmail.sasha.inverse.transvoicestudio.databinding.ItemRecordBinding
import com.gmail.sasha.inverse.transvoicestudio.utlis.shareFile

class RecordsFragment : Fragment() {

    private val recordsAdapter = RecordsAdapter()
    private lateinit var indicationAnimator: ObjectAnimator
    private lateinit var reversedIndicationAnimator: ObjectAnimator
    private val recordsViewModel: RecordsViewModel by viewModels()
    private var binding : FragmentRecordsBinding?  = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordsViewModel.liveRecords.observe(
            viewLifecycleOwner,
            Observer {
                binding?.recordsRecyclerView?.adapter.let {
                    it?.notifyDataSetChanged()
                }
            }
        )
        binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recordsRecyclerView?.adapter = recordsAdapter
        ItemTouchHelper(SwipeToDeleteCallback()).attachToRecyclerView(binding?.recordsRecyclerView)
        binding?.buttonRecording?.setOnTouchListener { v, event ->
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
        binding?.buttonPlaying?.setOnTouchListener { v, event ->
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
                target = binding?.animatedIndicator
                start()
            }
        reversedIndicationAnimator =
            (AnimatorInflater.loadAnimator(
                context,
                R.animator.rotation_animator_reversed
            ) as ObjectAnimator).apply {
                target = binding?.animatedIndicator
                start()
            }
        indicateIdleState()
    }
    private fun indicateIdleState() {
        indicationAnimator.pause()
        reversedIndicationAnimator.pause()
        binding?.mediaIndicator?.text = getString(R.string.idle)
    }

    private fun indicateRecordingState() {
        indicationAnimator.pause()
        reversedIndicationAnimator.resume()
        binding?.mediaIndicator?.text = getString(R.string.recording)
    }

    private fun indicatePlayingState() {
        indicationAnimator.resume()
        reversedIndicationAnimator.pause()
        binding?.mediaIndicator?.text = getString(R.string.playing)
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
                    val binding = ItemRecordBinding.bind(this)
                    binding.textRecordName.text = file.name
                    if (playing) {
                        binding.itemButtonPlay.setImageResource(R.drawable.ic_pause)
                        binding.itemButtonPlay.setOnClickListener {
                            recordsViewModel.stopPlaying()
                        }
                    } else {
                        binding.itemButtonPlay.setImageResource(R.drawable.ic_play)
                        binding.itemButtonPlay.setOnClickListener {
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
