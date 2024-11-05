package com.example.project5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.project5.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val waterEntries = mutableListOf<WaterEntry>()
    private lateinit var adapter: WaterEntryAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Room database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "water_database"
        ).build()

        // Set up RecyclerView
        adapter = WaterEntryAdapter(waterEntries)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Fetch and observe entries
        db.waterEntryDao().getAllEntries().observe(this, { entries ->
            waterEntries.clear()
            waterEntries.addAll(entries)
            adapter.notifyDataSetChanged()
            updateAverageIntake()
        })

        // Save new entry on button click
        binding.submitButton.setOnClickListener {
            val volume = binding.waterIntakeInput.text.toString().toIntOrNull()
            if (volume != null) {
                val entry = WaterEntry(date = getCurrentDate(), volume = volume)
                lifecycleScope.launch {
                    db.waterEntryDao().insert(entry)
                    binding.waterIntakeInput.text.clear()
                }
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    binding.photoImageView.setImageURI(uri) // Set the selected image
                    binding.photoImageView.visibility = View.VISIBLE // Show the image view
                }
            }
        }


        // Show drawable on button click
        binding.showDrawableButton.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        // Launch the gallery without permission check
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*" // Only show images
        }
        galleryLauncher.launch(intent)
    }

    private fun getCurrentDate(): String {
        // Implement a method to get the current date as a string
        return "2024-11-04" // Placeholder
    }

    private fun updateAverageIntake() {
        lifecycleScope.launch {
            val average = db.waterEntryDao().getAverageWaterIntake()
            val averageText = if (average != null) {
                "Average Intake: ${average.toInt()} ml"
            } else {
                "Average Intake: 0 ml"
            }
            binding.averageIntakeTextView.text = averageText // Update the average intake TextView
        }
    }
}
