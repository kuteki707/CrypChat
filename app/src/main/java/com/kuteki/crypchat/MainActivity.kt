package com.kuteki.crypchat

import android.app.DownloadManager
import com.google.firebase.firestore.EventListener
import androidx.appcompat.app.AppCompatDelegate
import android.content.pm.ActivityInfo
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.kuteki.crypchat.databinding.ActivityMainBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

public  var roomID:String = ""
public var tempRecieverID:String = ""
public var myUsername = ""
var isFirstLoad = true

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_crypchat_logo_orizontal)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR)

        //Toast.makeText(this, "Logged as $globalUsernameID", Toast.LENGTH_SHORT).show()

        sharedPreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("username", globalUsernameID).apply()

        val adapter = GroupAdapter<GroupieViewHolder>()
        listenForNewRooms(adapter)

        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMain.adapter = adapter

        adapter.setOnItemClickListener { item, view ->
            try {
                val roomItem = item as RoomItem
                db.collection("rooms").whereEqualTo("roomName",roomItem.room).get()
                    .addOnSuccessListener {documents ->
                        for(document in documents){
                            roomID = document.id
                            if(document.get("participant1") == globalUsernameID){
                                tempRecieverID = document.get("participant2").toString()
                                val intent = Intent(this,ChatActivity::class.java)
                                startActivity(intent)
                            }else if(document.get("participant2") == globalUsernameID){
                                tempRecieverID = document.get("participant1").toString()
                                val intent = Intent(this,ChatActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
            }catch (e:ClassCastException){
                val roomItem = item as RoomItem2
                db.collection("rooms").whereEqualTo("roomName",roomItem.room).get()
                    .addOnSuccessListener {documents ->
                        for(document in documents){
                            roomID = document.id
                            if(document.get("participant1") == globalUsernameID){
                                tempRecieverID = document.get("participant2").toString()
                                val intent = Intent(this,ChatActivity::class.java)
                                startActivity(intent)
                            }else if(document.get("participant2") == globalUsernameID){
                                tempRecieverID = document.get("participant1").toString()
                                val intent = Intent(this,ChatActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
            }


        }



    }

    fun listenForNewRooms(adapter:GroupAdapter<GroupieViewHolder>){
        var itemsPositions = mutableMapOf<String, Int>()

        db.collection("rooms").orderBy("timestamp",Query.Direction.DESCENDING).addSnapshotListener(EventListener { value, error ->
            if (!value!!.metadata.hasPendingWrites()){
                for (documentChange in value!!.documentChanges){
                    if (documentChange.document.data.get("participant1").toString() == globalUsernameID){
                        val tempParticipant = documentChange.document.data.get("participant2").toString()

                        db.collection("users").document(tempParticipant).get()
                            .addOnSuccessListener { document ->
                                if(documentChange.document.data.get("latestMessageSentBy") == globalUsernameID){
                                    val room = RoomItem(
                                        document.get("username").toString(),
                                        documentChange.document.data.get("roomName").toString(),
                                        decrypt(documentChange.document.get("participant1").toString(),
                                            documentChange.document.get("participant2").toString(),
                                            documentChange.document.data.get("lastMessage").toString()),
                                        documentChange.document.getDate("timestamp").toString().substring(4,16)
                                    )

                                    if (itemsPositions.get(documentChange.document.id) == null) {
                                        adapter.add(room)
                                        itemsPositions.put(documentChange.document.id, adapter.getAdapterPosition(room))
                                    } else {
                                        adapter.removeGroupAtAdapterPosition(itemsPositions.get(documentChange.document.id)!!)
                                        adapter.add(itemsPositions.get(documentChange.document.id)!!, room)
                                    }
                                }else{
                                    val room = RoomItem2(
                                        document.get("username").toString(),
                                        documentChange.document.data.get("roomName").toString(),
                                        decrypt(documentChange.document.get("participant1").toString(),
                                            documentChange.document.get("participant2").toString(),
                                            documentChange.document.data.get("lastMessage").toString()),
                                        documentChange.document.getDate("timestamp").toString().substring(4,16)
                                    )

                                    if (itemsPositions.get(documentChange.document.id) == null) {
                                        adapter.add(room)
                                        itemsPositions.put(documentChange.document.id, adapter.getAdapterPosition(room))
                                    } else {
                                        adapter.removeGroupAtAdapterPosition(itemsPositions.get(documentChange.document.id)!!)
                                        adapter.add(itemsPositions.get(documentChange.document.id)!!, room)
                                    }
                                }

                            }
                    }

                    if (documentChange.document.data.get("participant2").toString() == globalUsernameID){
                        val tempParticipant = documentChange.document.data.get("participant1").toString()

                        db.collection("users").document(tempParticipant).get()
                            .addOnSuccessListener { document ->
                                if(documentChange.document.data.get("latestMessageSentBy") == globalUsernameID){
                                    val room = RoomItem(
                                        document.get("username").toString(),
                                        documentChange.document.data.get("roomName").toString(),
                                        decrypt(documentChange.document.get("participant1").toString(),
                                            documentChange.document.get("participant2").toString(),
                                            documentChange.document.data.get("lastMessage").toString()),
                                        documentChange.document.getDate("timestamp").toString().substring(4,16)
                                    )

                                    if (itemsPositions.get(documentChange.document.id) == null) {
                                        adapter.add(room)
                                        itemsPositions.put(documentChange.document.id, adapter.getAdapterPosition(room))
                                    } else {
                                        adapter.removeGroupAtAdapterPosition(itemsPositions.get(documentChange.document.id)!!)
                                        adapter.add(itemsPositions.get(documentChange.document.id)!!, room)
                                    }
                                }else{
                                    val room = RoomItem2(
                                        document.get("username").toString(),
                                        documentChange.document.data.get("roomName").toString(),
                                        decrypt(documentChange.document.get("participant1").toString(),
                                            documentChange.document.get("participant2").toString(),
                                            documentChange.document.data.get("lastMessage").toString()),
                                        documentChange.document.getDate("timestamp").toString().substring(4,16)
                                    )

                                    if (itemsPositions.get(documentChange.document.id) == null) {
                                        adapter.add(room)
                                        itemsPositions.put(documentChange.document.id, adapter.getAdapterPosition(room))
                                    } else {
                                        adapter.removeGroupAtAdapterPosition(itemsPositions.get(documentChange.document.id)!!)
                                        adapter.add(itemsPositions.get(documentChange.document.id)!!, room)
                                    }
                                }
                            }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout_menu_main -> {
                Toast.makeText(this,"Log-out",Toast.LENGTH_SHORT).show()
                globalUsernameID = ""
                sharedPreferences.edit().putString("username", globalUsernameID).apply()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.about_me -> {
                val intent = Intent(this,AboutMe::class.java)
                startActivity(intent)
            }
            R.id.create_room -> {
                val window = PopupWindow(this)
                window.isFocusable = true
                val view = layoutInflater.inflate(R.layout.create_room,null)
                window.contentView = view
                val textBox = view.findViewById<TextView>(R.id.search_friend_text)
                val button = view.findViewById<Button>(R.id.chat_button)
                textBox.requestFocus()
                button.setOnClickListener {
                    if(textBox.text != ""){
                        Log.e("tag","pasul 1")
                        db.collection("users")
                                .whereEqualTo("username", textBox.text.toString())
                                .get()
                                .addOnSuccessListener { documents ->
                                    Log.e("tag","pasul 2")
                                    if(!documents.isEmpty) {
                                        for (document in documents) {
                                            if (document.exists()) {

                                                Log.e("tag","pasul 3")

                                                tempRecieverID = document.id

                                                var tempUser2 = ""
                                                var tempUser1 = ""

                                                db.collection("users").document(globalUsernameID).get()
                                                        .addOnSuccessListener {
                                                            document -> tempUser1 = document["username"].toString()
                                                            var user1tempid = document.id
                                                            Log.e("tag","pasul 4")

                                                            db.collection("users").document(tempRecieverID).get()
                                                                    .addOnSuccessListener {
                                                                        document -> tempUser2 = document["username"].toString()
                                                                        var user2tempid = document.id
                                                                        Log.e("tag","pasul 5")

                                                                        if(tempUser1 != "" && tempUser2 != "") {
                                                                            Log.e("tag","pasul 5")
                                                                            if(tempUser1.compareTo(tempUser2) > 0){
                                                                                var x = ""
                                                                                x= tempUser1
                                                                                tempUser1 = tempUser2
                                                                                tempUser2 = x
                                                                                x = user1tempid
                                                                                user1tempid = user2tempid
                                                                                user2tempid = x

                                                                                db.collection("rooms")
                                                                                        .whereEqualTo("roomName",tempUser1 + "," + tempUser2)
                                                                                        .get()
                                                                                        .addOnSuccessListener {documents ->
                                                                                            Log.e("tag","pasul 6")
                                                                                            if(!documents.isEmpty){
                                                                                                for (document in documents)
                                                                                                    if (document.exists()) {
                                                                                                        roomID = document.id
                                                                                                        val intent = Intent(this, ChatActivity::class.java)
                                                                                                        startActivity(intent)
                                                                                                    }

                                                                                            }else{
                                                                                                val roomInfo = hashMapOf(
                                                                                                        "latestMessageSentBy" to globalUsernameID,
                                                                                                        "roomName" to tempUser1 +","+ tempUser2,
                                                                                                        "timestamp" to FieldValue.serverTimestamp(),
                                                                                                        "participant1" to user1tempid,
                                                                                                        "participant2" to user2tempid,
                                                                                                        "lastMessage" to ""
                                                                                                )
                                                                                                db.collection("rooms")
                                                                                                        .add(roomInfo)
                                                                                                        .addOnSuccessListener {document1 ->
                                                                                                            roomID = document1.id
                                                                                                            val intent = Intent(this, ChatActivity::class.java)
                                                                                                            startActivity(intent)
                                                                                                        }
                                                                                            }


                                                                                        }
                                                                            }else{
                                                                                db.collection("rooms")
                                                                                        .whereEqualTo("roomName",tempUser1 + "," + tempUser2)
                                                                                        .get()
                                                                                        .addOnSuccessListener {documents ->
                                                                                            Log.e("tag","pasul 6")
                                                                                            if(!documents.isEmpty) {
                                                                                                for (document in documents)
                                                                                                    if (document.exists()) {
                                                                                                        roomID = document.id
                                                                                                        val intent = Intent(this, ChatActivity::class.java)
                                                                                                        startActivity(intent)
                                                                                                    }
                                                                                            }else{
                                                                                                val roomInfo = hashMapOf(
                                                                                                        "roomName" to tempUser1 +","+ tempUser2,
                                                                                                        "timestamp" to FieldValue.serverTimestamp(),
                                                                                                        "participant1" to user1tempid,
                                                                                                        "participant2" to user2tempid,
                                                                                                        "lastMessage" to ""

                                                                                                )
                                                                                                db.collection("rooms")
                                                                                                        .add(roomInfo)
                                                                                                        .addOnSuccessListener {document1 ->
                                                                                                            roomID = document1.id
                                                                                                            val intent = Intent(this, ChatActivity::class.java)
                                                                                                            startActivity(intent)
                                                                                                        }
                                                                                            }


                                                                                        }
                                                                            }
                                                                        }else{
                                                                            Toast.makeText(this, "Unknown error, try again later", Toast.LENGTH_SHORT).show()
                                                                        }
                                                                    }
                                                        }
                                                        .addOnFailureListener{
                                                            Log.e("tag","eroare")
                                                        }
                                            }
                                        }
                                    }else{
                                        Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Unknown error, try again later", Toast.LENGTH_SHORT).show()
                                }
                    }
                    window.dismiss()
                }
                window.showAsDropDown(binding.anchorPoint)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}