package ru.gonchar17narod.selferificator.view.items

import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_record.view.*
import ru.gonchar17narod.selferificator.R
import ru.gonchar17narod.selferificator.view.fragments.home.HomeViewModel

class RecordItem(
    val homeViewModel: HomeViewModel
) : Item() {

    override fun getLayout(): Int =
        R.layout.item_record

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        homeViewModel.liveRecords.value?.get(position)?.apply {
            with(viewHolder.itemView) {
                text_record_name.text = file.name
                item_button_play.setOnClickListener {
                    if (playing) {
                        homeViewModel.stopPlaying()
                        playing = false
                        item_button_play.text = context.getString(R.string.play)
                    } else {
                        homeViewModel.startPlaying(this@apply)
                        playing = true
                        item_button_play.text = context.getString(R.string.stop)
                    }
                }
            }
        }
    }

    override fun getSwipeDirs(): Int {
        return ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }
}