package com.kuteki.crypchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuteki.crypchat.databinding.ActivityAboutMeBinding

class AboutMe : AppCompatActivity() {
    lateinit var binding:ActivityAboutMeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        binding = ActivityAboutMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}