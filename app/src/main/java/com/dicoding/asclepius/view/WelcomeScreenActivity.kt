package com.dicoding.asclepius.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityWelcomeScreenBinding

class WelcomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMainLogin.setOnClickListener {
            startActivity(Intent(this@WelcomeScreenActivity, LoginActivity::class.java))
        }
        binding.buttonMainRegister.setOnClickListener {
            startActivity(Intent(this@WelcomeScreenActivity, RegisterActivity::class.java))
        }
    }
}