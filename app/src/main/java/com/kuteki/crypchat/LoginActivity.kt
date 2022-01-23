package com.kuteki.crypchat

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kuteki.crypchat.databinding.ActivityLoginBinding
import java.security.MessageDigest

public val db = Firebase.firestore
public var globalUsernameID = ""

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR)


        val loginFragment = LoginFragment()
        val signupFragment = SignupFragment()
        SetFragmentView(loginFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.login_menu -> {
                        SetFragmentView(loginFragment)
                }
                R.id.signup_menu -> {
                    SetFragmentView(signupFragment)
                }
            }
            true
        }

    }

    fun SetFragmentView(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.login_fragment_view, fragment)
            addToBackStack(null)
            commit()
        }
    }
}
public fun String.toSHA512(): String {
    val bytes = MessageDigest.getInstance("SHA-512").digest(this.toByteArray())
    return bytes.toHex()
}

public fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}
