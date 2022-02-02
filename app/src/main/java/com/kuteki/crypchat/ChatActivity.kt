package com.kuteki.crypchat

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.kuteki.crypchat.databinding.ActivityChatBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding

    val messagesPath = db.collection("rooms").document(roomID)
            .collection("messages")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR)

        supportActionBar?.title = "Loading..."

        db.collection("rooms").document(roomID).get()
            .addOnSuccessListener {document->
                if(document.get("participant1") == globalUsernameID) {
                    db.collection("users").document(document.get("participant2").toString()).get()
                        .addOnSuccessListener { d ->
                            supportActionBar?.title = d.get("username").toString()
                        }
                }
                else{
                    db.collection("users").document(document.get("participant1").toString()).get()
                        .addOnSuccessListener { d ->
                            supportActionBar?.title = d.get("username").toString()
                        }
                }
            }

        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.adapter = adapter


        messagesPath.orderBy("timestamp").addSnapshotListener(EventListener { value, error ->
            if(!value!!.metadata.hasPendingWrites()) {
                for (documentChange in value!!.documentChanges) {
                    if (documentChange.document.data.get("sentBy").toString() == globalUsernameID) {
                        var message = decrypt(documentChange.document.data.get("sentBy").toString(),
                            documentChange.document.data.get("recievedBy").toString(),
                            documentChange.document.data.get("messageText").toString())
                        adapter.add(
                            MessageItemSent(
                                message,
                                documentChange.document.getDate("timestamp"),
                                true
                            )
                        )
                        binding.recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
                    } else if (documentChange.document.data.get("recievedBy").toString() == globalUsernameID) {
                        var message = decrypt(documentChange.document.data.get("sentBy").toString(),
                                documentChange.document.data.get("recievedBy").toString(),
                                documentChange.document.data.get("messageText").toString())
                        adapter.add(
                            MessageItemRecieved(
                                message,
                                documentChange.document.getDate("timestamp")
                            )
                        )
                        binding.recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
        })



        binding.sendMessageButton.setOnClickListener {
            if(binding.messageBoxView.text.toString()!= "") {
                val message = binding.messageBoxView.text.toString()

                db.collection("rooms").document(roomID).get().addOnSuccessListener {document ->
                    val encryptedMessage = encrypt(document.get("participant1").toString(),document.get("participant2").toString(),message)
                    val messageToSend = hashMapOf(
                        "sentBy" to globalUsernameID,
                        "recievedBy" to tempRecieverID,
                        "messageText" to encryptedMessage,
                        "timestamp" to FieldValue.serverTimestamp()
                    )

                    messagesPath.add(messageToSend)
                    db.collection("rooms").document(roomID).update("lastMessage",encryptedMessage)
                    db.collection("rooms").document(roomID).update("timestamp",FieldValue.serverTimestamp())
                    binding.messageBoxView.text.clear()
                    binding.recyclerViewChat
                }


            }
        }


    }
//    fun loadMessagesAtStart(adapter:GroupAdapter<GroupieViewHolder>){
//        messagesPath.orderBy("timestamp").get()
//            .addOnSuccessListener{documents ->
//                for(document in documents){
//                    if(document.get("sentBy").toString() == globalUsernameID){
//                        adapter.add(MessageItemSent(document.get("messageText").toString()))
//                    }
//                    else if(document.get("recievedBy").toString() == globalUsernameID){
//                        adapter.add(MessageItemRecieved(document.get("messageText").toString()))
//                    }
//                }
//            }
//    }


}
public fun encrypt(participant1:String,participant2:String,message:String,):String {
   //private encryption algorithm
    return encryptedMessage
}
public fun decrypt(participant1: String,participant2: String,encryptedMessage:String):String{
   //private decryption algorithm
    return decryptedMessage
}
