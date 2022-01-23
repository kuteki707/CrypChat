package com.kuteki.crypchat

import android.widget.TextView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.util.*

class MessageItemRecieved(val message:String, val timestamp:Date?): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_text_view_recieved).text = message
        viewHolder.itemView.findViewById<TextView>(R.id.timestamp_text_view_recieved).text = timestamp.toString().subSequence(4,16)
    }

    override fun getLayout() = R.layout.message_recieved
}