package com.kuteki.crypchat

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import android.content.pm.ActivityInfo
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kuteki.crypchat.databinding.ActivityLauncherBinding

class LauncherActivity : AppCompatActivity() {
    lateinit var binding: ActivityLauncherBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("TAG", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        sharedPreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE)
        globalUsernameID = sharedPreferences.getString("username","")!!
        try {
            db.collection("users")
                    .document(globalUsernameID)
                    .get()
                    .addOnSuccessListener { document ->
                        if(document.exists()){
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }else{
                            globalUsernameID = ""
                            sharedPreferences.edit().putString("username", globalUsernameID).apply()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
        }
        catch (exception:IllegalArgumentException){
            globalUsernameID = ""
            sharedPreferences.edit().putString("username", globalUsernameID).apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }



    }
}