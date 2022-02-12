package com.example.jpgtopngconvertor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickImageContract : ActivityResultContract<String, Uri?>() {
    override fun createIntent(context: Context, input: String): Intent {
        val title = context.resources.getString(R.string.chooser_title)
        return Intent.createChooser(
            Intent(Intent.ACTION_PICK).apply { type = input },
            title
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (intent == null) return null
        if (resultCode != Activity.RESULT_OK) return null
        return intent.data
    }
}