package com.kuteki.crypchat

import android.app.Activity
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.concurrent.thread

private val TAG = "Signup"

class SignupFragment : Fragment(R.layout.fragment_signup) {
    lateinit var username:String
    lateinit var password:String
    private var usernameAvailable = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signupButton = view.findViewById<Button>(R.id.signup_button)
        val usernameField = view.findViewById<TextView>(R.id.signup_username_text)
        val passwordField = view.findViewById<TextView>(R.id.signup_password_text)
        val passwordConfirmField = view.findViewById<TextView>(R.id.signup_password_confirm_text)
        val checkBox = view.findViewById<ImageView>(R.id.check_box)
        signupButton.hideKeyboard()
        checkBox.animate().alpha(1f).setDuration(500)
        usernameField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                db.collection("users")
                    .whereEqualTo("username", usernameField.text.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        checkBox.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                        usernameAvailable = true

                        if(usernameField.text.toString().contains(" ")){
                            usernameAvailable = false
                            checkBox.setImageResource(R.drawable.ic_baseline_not_interested_24)
                        }

                        for (document in documents) {
                            if (document.exists()) {
                                usernameAvailable = false
                                checkBox.setImageResource(R.drawable.ic_baseline_not_interested_24)
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity, "Unknown error, try again later", Toast.LENGTH_SHORT).show()
                    }
            }
        })
        signupButton.setOnClickListener {
            username = usernameField.text.toString()
            if (passwordField.text.toString() == passwordConfirmField.text.toString() && passwordField.text.toString() != "") {
                password = passwordField.text.toString()
                //Toast.makeText(activity, "Email is: $email \n Password is: $password", Toast.LENGTH_SHORT).show()


                if (usernameAvailable && usernameField.text.toString() != "") {
                    val user = hashMapOf(
                            "username" to username,
                            "password" to password.toSHA512(),
                            "fcm_token" to "",
                    )
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                                Toast.makeText(activity, "Succes!", Toast.LENGTH_SHORT).show()
                                globalUsernameID = documentReference.id
                                signupButton.isEnabled = false
                                val intent = Intent(activity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                activity!!.finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                }else if(usernameField.text.toString() != ""){
                    Toast.makeText(activity, "Username not available", Toast.LENGTH_SHORT).show()
                    usernameField.text = ""
                    usernameField.requestFocus()
                }else{
                    Toast.makeText(activity, "Username can't be empty", Toast.LENGTH_SHORT).show()
                }
            } else if(passwordField.text.toString() != "") {
                Toast.makeText(activity, "Passwords don't match", Toast.LENGTH_SHORT).show()
                passwordField.text = ""
                passwordConfirmField.text = ""
                passwordField.requestFocus()
            }else{
                Toast.makeText(activity, "Password can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}