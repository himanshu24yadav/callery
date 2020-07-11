package com.example.library.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UtilMethods {
    companion object {

        @Throws(IOException::class)
        fun createTempFile(): File? {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(Date())
            val imageFileName = "PROD_$timeStamp"
            val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/Callery/")
            if (!storageDir.exists()) {
                val wallpaperDirectory = File(Environment.getExternalStorageDirectory().path+ "/Callery/")
                wallpaperDirectory.mkdirs()
            }
            return File.createTempFile(imageFileName, ".jpg", storageDir)
        }

        fun addImageToGallery(absolutePath: String, mContext: Context?, size: Int, mWidth: Int, mHeight: Int) {
            try {
                val values = ContentValues()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                }
                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                values.put(MediaStore.MediaColumns.DATA, absolutePath)
                values.put(MediaStore.Images.Media.SIZE, size)
                values.put(MediaStore.Images.Media.WIDTH, mWidth)
                values.put(MediaStore.MediaColumns.HEIGHT, mHeight)
                mContext?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}