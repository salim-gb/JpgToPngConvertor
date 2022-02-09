package com.example.jpgtopngconvertor

import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class ImagePicker(
    private val activityResultRegistry: ActivityResultRegistry,
    private val lifecycleOwner: LifecycleOwner,
    private val callback: (imageUri: Uri?) -> Unit
) {
    private val getContent = activityResultRegistry.register(
        REGISTRY_KEY,
        lifecycleOwner,
        ActivityResultContracts.GetContent(),
        callback
    )

    fun pickImage() {
        getContent.launch(MIMETYPE_IMAGES)
    }

    private companion object {
        private const val REGISTRY_KEY = "ImagePicker"
        private const val MIMETYPE_IMAGES = "image/jpeg"
    }
}