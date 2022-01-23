package com.kuteki.crypchat

import android.widget.TextView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class RoomItem(val corespondent:String ,val room:String, val message:String,val timestamp:String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.username_room).text = corespondent
        viewHolder.itemView.findViewById<TextView>(R.id.last_message_room).text = message
        viewHolder.itemView.findViewById<TextView>(R.id.timestamp_room).text = timestamp
    }

    override fun getLayout() = R.layout.room
}