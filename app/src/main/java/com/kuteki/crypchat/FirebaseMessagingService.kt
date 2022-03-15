package com.kuteki.crypchat

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        db.collection("users")
            .document(globalUsernameID)
            .update("fcm_token", token)
            .addOnSuccessListener {
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d("TAG", msg)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Cannot refresh the FCM token", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "FirebaseMsgService"
    }
}