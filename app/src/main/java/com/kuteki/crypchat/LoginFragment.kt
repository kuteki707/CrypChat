package com.kuteki.crypchat


import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import kotlin.concurrent.thread

private lateinit var dbUsername:String
private lateinit var dbPassword:String
private var goodCredentials:Boolean = false

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameField = view.findViewById<TextView>(R.id.login_username_text)
        val passwordField = view.findViewById<TextView>(R.id.login_password_text)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        loginButton.hideKeyboard()
        loginButton.setOnClickListener {
            var username = usernameField.text.toString()
            var password = passwordField.text.toString()
            Toast.makeText(activity, "Loading...", Toast.LENGTH_SHORT).show()
            db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            dbUsername = document.get("username").toString()
                            dbPassword = document.get("password").toString()
                            if (dbUsername == username && dbPassword == password.toSHA512()) {
                                goodCredentials = true
                                globalUsernameID = document.id
                                val intent =
                                    Intent(activity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                activity!!.finish()

                            }
                        }
                    }.addOnCompleteListener {
                        if(!goodCredentials){
                            Toast.makeText(
                                activity,
                                "Wrong credentials",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                    Toast.makeText(
                        activity,
                        "Wrong credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
public fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}