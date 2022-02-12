package com.example.jpgtopngconvertor

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

class PermissionsManager(
    private val registry: ActivityResultRegistry,
    private val lifecycleOwner: LifecycleOwner,
    private val callback: (Map<String, Boolean>) -> Unit
) {

    private val requestPermissions = registry.register(
        REGISTRY_KEY,
        lifecycleOwner,
        RequestMultiplePermissions(),
        callback
    )

    fun checkPermissions(callback: (Boolean) -> Unit) {
        callback(
            permissions.all {
                ContextCompat.checkSelfPermission(
                    lifecycleOwner as Activity,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    fun requestPermissions() {
        requestPermissions.launch(permissions)
    }

    private companion object {
        private const val REGISTRY_KEY = "cff"
        private val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}