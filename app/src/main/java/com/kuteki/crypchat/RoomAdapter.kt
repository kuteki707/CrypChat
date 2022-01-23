//package com.kuteki.crypchat
//
//import android.content.Context
//import android.content.Intent
//import android.text.Layout
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.cardview.widget.CardView
//import androidx.core.content.ContextCompat.startActivity
//import androidx.recyclerview.widget.RecyclerView
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//
//
//class RoomAdapter(options: FirestoreRecyclerOptions<Room>) : FirestoreRecyclerAdapter<Room, RoomAdapter.RoomViewHolder>(options) {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
//        return RoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.room,parent,false))
//    }
//
//    override fun onBindViewHolder(holder: RoomViewHolder, position: Int, model: Room) {
//        holder.roomName.text = model.roomName
//        val time = model.timestamp?.toDate().toString()
//        if(time.length>16)
//            holder.timestampRoom.text = time.subSequence(11,16)
//    }
//
//    class RoomViewHolder(roomView: View):RecyclerView.ViewHolder(roomView){
//        val roomName:TextView = roomView.findViewById(R.id.username_room)
////        val messageRoom:TextView = roomView.findViewById(R.id.message_room)
//        val timestampRoom:TextView = roomView.findViewById(R.id.timestamp_room)
//        val cardView:CardView = roomView.findViewById(R.id.room_card_view)
//        init{
//            cardView.setOnClickListener {
//                db.collection("rooms")
//                        .whereEqualTo("roomName",roomName.text)
//                        .get()
//                        .addOnSuccessListener {
//
//                        }
//
//
//            }
//        }
//    }
//
//}