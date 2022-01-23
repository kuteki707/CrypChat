package com.kuteki.crypchat

import android.media.Image
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.Timestamp
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.util.*

class MessageItemSent(val message:String,val timestamp: Date?,val status: Boolean): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_text_view_sent).text = message
        viewHolder.itemView.findViewById<TextView>(R.id.timestamp_text_view_sent).text = timestamp.toString().subSequence(4,16)
        if(status){
            viewHolder.itemView.findViewById<ImageView>(R.id.sent_check_not).alpha = 0f
            viewHolder.itemView.findViewById<ImageView>(R.id.sent_check_yes).alpha = 1f
        }else{
            viewHolder.itemView.findViewById<ImageView>(R.id.sent_check_yes).alpha = 0f
            viewHolder.itemView.findViewById<ImageView>(R.id.sent_check_not).alpha = 1f
        }
    }

    override fun getLayout() = R.layout.message_sent

}