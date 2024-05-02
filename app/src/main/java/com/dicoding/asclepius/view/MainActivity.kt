package com.dicoding.asclepius.view

import android.Manifest
import android.R.attr.maxHeight
import android.R.attr.maxWidth
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.SectionsPagerAdapter
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.DateHelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.model.AnalyzeHistory
import com.dicoding.asclepius.repository.AnalyzeHistoryRepository
import com.dicoding.asclepius.view.LiveDetectionActivity.Companion.CAMERAX_RESULT
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var analyzeHistoryRepository: AnalyzeHistoryRepository

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        analyzeHistoryRepository = AnalyzeHistoryRepository(this.application)

        imageClassifierHelper = ImageClassifierHelper(context = this, classifierListener = object: ImageClassifierHelper.ClassifierListener {
            override fun onError(error: String) {
                showToast("Error when analyzing image")
            }

            override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                runOnUiThread {
                    results?.let {
                        if (it.isNotEmpty()) {
                            val sortedCategories = it[0].categories.sortedByDescending { it?.score }
                            val displayResult = sortedCategories[0].let {
                                it.label.toString().trim() + " " + it.score
                            }

                            moveToResult(currentImageUri!!, displayResult)
                        } else {
                            showToast("Error when analyzing image")
                        }
                    }
                }
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        binding.searchView.setupWithSearchBar(binding.searchBar)

        val bottomNavigationItemListener = BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.cancersense -> {
                    startCameraX()

                    return@OnNavigationItemSelectedListener true
                }
                R.id.gallery -> {
                    startGallery()

                    return@OnNavigationItemSelectedListener true
                }
                R.id.analyze -> {
                    analyzeImage()

                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationItemListener)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun analyzeImage() {
        imageClassifierHelper.classifyStaticImage(currentImageUri!!)
    }

    private fun moveToResult(uri: Uri, result: String) {
        val analyzeHistory = AnalyzeHistory(0, uri.toString(), result, DateHelper.getCurrentDate())
        analyzeHistoryRepository.insert(analyzeHistory)

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("RESULT", result)
        intent.putExtra("URI", uri.toString())

        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            val destinationUri = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()

            openCropActivity(uri, Uri.fromFile(File(cacheDir, destinationUri)))
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun openCropActivity(sourceUri: Uri, destinationUri: Uri) {
        UCrop.of(sourceUri, destinationUri)
            .withMaxResultSize(maxWidth, maxHeight)
            .withAspectRatio(5f, 5f)
            .start(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)!!

            currentImageUri = resultUri
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError!!.message?.let { showToast(it) }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, LiveDetectionActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(LiveDetectionActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
