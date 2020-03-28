package ru.gonchar17narod.selferificator.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_record.view.*
import ru.gonchar17narod.selferificator.R

class HomeFragment : Fragment() {

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

        root.records_recycler_view.adapter = object : RecyclerView.Adapter<RecordHolder>() {

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
                        item_button_play.setOnClickListener {
                            if (playing) {
                                homeViewModel.stopPlaying()
                                playing = false
                                item_button_play.text = getString(R.string.play)
                            } else {
                                homeViewModel.startPlaying(file)
                                playing = true
                                item_button_play.text = getString(R.string.stop)
                            }
                        }
                    }
                }
            }
        }

        homeViewModel.liveRecords.observe(
            viewLifecycleOwner,
            Observer {
                root.records_recycler_view.adapter.let {
                    it?.notifyDataSetChanged()
                }
            }
        )
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button_recording.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    homeViewModel.startRecording()
                }
                MotionEvent.ACTION_UP -> {
                    homeViewModel.stopRecording()
                }
            }
            true
        }

        button_playing.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    homeViewModel.liveRecords.value?.first()?.file?.apply {
                        homeViewModel.startPlaying(
                            this
                        )
                    }
                }
                MotionEvent.ACTION_UP -> {
                    homeViewModel.stopPlaying()
                }
            }
            true
        }
    }

    private inner class RecordHolder(
        @SuppressLint("InflateParams") itemView: View =
            layoutInflater.inflate(
                R.layout.item_record,
                null,
                false
            )
    ) : RecyclerView.ViewHolder(itemView)
}
