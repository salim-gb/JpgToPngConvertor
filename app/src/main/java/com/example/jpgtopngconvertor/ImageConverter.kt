package com.example.jpgtopngconvertor

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import io.reactivex.Single
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException

object ImageConverter {

    fun convertJpgToPng(contentResolver: ContentResolver, uri: Uri): Single<Pair<String, Bitmap>> {
        val pathToBitmap = getPathFromUri(contentResolver, uri)
        var pathImagePickedDir: String? = null
        var nameImagePicked: String? = null

        pathToBitmap?.let {
            pathImagePickedDir = splitPathToBitmap(pathToBitmap).first
            nameImagePicked = splitPathToBitmap(pathToBitmap).second
        }

        return Single.fromCallable {
            val pathImageOutput = "$pathImagePickedDir/$nameImagePicked.png"
            val imageOutputStream = FileOutputStream(pathImageOutput)

            val bitmap = getBitmapFromUri(contentResolver, uri)

            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream)) {
                return@fromCallable (pathImageOutput to BitmapFactory.decodeFile(pathImageOutput))
            } else {
                throw Exception("Conversion problem")
            }
        }
    }

    private fun splitPathToBitmap(pathToBitmap: String): Pair<String, String> {
        val pathImagePickedParts = pathToBitmap.split("/")

        val pathImagePickedDir = pathImagePickedParts
            .subList(1, pathImagePickedParts.size - 1)
            .joinToString(prefix = "/", separator = "/")

        val nameImagePicked = pathImagePickedParts[pathImagePickedParts.size - 1]
            .split(".")[0]

        return pathImagePickedDir to nameImagePicked
    }

    private fun getPathFromUri(contentResolver: ContentResolver, contentUri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            columnIndex.let {
                path = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return path
    }

    private fun getBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
        var fileDescriptor: FileDescriptor? = null

        parcelFileDescriptor?.let {
            fileDescriptor = parcelFileDescriptor.fileDescriptor
        }
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }
}