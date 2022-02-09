package com.example.jpgtopngconvertor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.jpgtopngconvertor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding

    private val imagePicker = ImagePicker(activityResultRegistry, this) { imageUri ->
        binding.imageView.load(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            imagePicker.pickImage()
        }
    }
}