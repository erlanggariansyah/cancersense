package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.DateHelper
import com.dicoding.asclepius.model.AnalyzeHistory
import com.dicoding.asclepius.repository.AnalyzeHistoryRepository

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uri: String? = intent.getStringExtra("URI")
        val result: String? = intent.getStringExtra("RESULT")

        binding.resultImage.setImageURI(Uri.parse(uri))
        binding.resultText.text = result
    }
}