package ru.gonchar17narod.selferificator.view.items

import android.content.Intent
import android.net.Uri
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_feed_web.view.*
import ru.gonchar17narod.selferificator.R
import ru.gonchar17narod.selferificator.business.FeedWebEntity

class FeedWebItem(
    val feedWebEntity: FeedWebEntity
) : Item() {

    override fun getLayout(): Int = R.layout.item_feed_web

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.itemView) {
            item_feed_web_view.loadUrl(feedWebEntity.url)
            frame_clickable.setOnClickListener {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            feedWebEntity.url
                        )
                    )
                )
            }
        }
    }
}