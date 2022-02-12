package com.example.jpgtopngconvertor

import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.LifecycleOwner
import moxy.MvpPresenter

class MainPresenter(
    registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
) : MvpPresenter<MainView>() {

    private var imageUri: Uri? = null

    private val permissionsManager =
        PermissionsManager(registry, lifecycleOwner) { permissions ->
            if (permissions.entries.all { it.value }) {
                imagePicker.pickImage()
            }
        }

    private val imagePicker = ImagePicker(registry, lifecycleOwner) { uri ->
        imageUri = uri
        viewState.showPickedImage(uri)
    }

    fun onPickImageViewClick() {
        permissionsManager.checkPermissions { isGranted ->
            if (isGranted) {
                imagePicker.pickImage()
            } else {
                permissionsManager.requestPermissions()
            }
        }
    }

    fun onConvertImageClick() {
        imageUri?.let { uri ->
            viewState.convertImage(uri)
        }
    }
}