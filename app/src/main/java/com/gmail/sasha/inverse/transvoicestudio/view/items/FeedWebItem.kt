package com.gmail.sasha.inverse.transvoicestudio.view.items

import android.content.Intent
import android.net.Uri
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.gmail.sasha.inverse.transvoicestudio.R
import com.gmail.sasha.inverse.transvoicestudio.business.FeedWebEntity

class FeedWebItem(
    val feedWebEntity: FeedWebEntity
) : Item() {

    override fun getLayout(): Int = R.layout.item_feed_web

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        with(viewHolder.itemView) {
//            item_feed_web_view.loadUrl(feedWebEntity.url)
//            frame_clickable.setOnClickListener {
//                context.startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse(
//                            feedWebEntity.url
//                        )
//                    )
//                )
//            }
//        }
    }
}