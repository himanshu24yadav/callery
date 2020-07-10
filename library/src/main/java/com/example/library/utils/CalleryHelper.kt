package com.example.library.utils

import android.app.Activity
import android.content.Intent
import com.example.library.base.CalleryActivity

class CalleryHelper {

    companion object {

        //if camera permission already given
        const val CAMERA_PERMISSION = "CAMERA_PERMISSION"

        //if storage permission already given
        const val STORAGE_PERMISSION = "STORAGE_PERMISSION"

        //if open for CAMERA only or CAMERA & GALLERY both. Default is CAMERA only
        const val OPEN_FOR = "OPEN_FOR"
        const val CAMERA_GALLERY = "CAMERA_GALLERY"
        const val CAMERA_ONLY = "CAMERA_ONLY"

        //max number of images allowed to be selected
        const val MAX_SELECTION = "MAX_IMAGE_SELECTION"
        const val MAX_SELECTION_DEFAULT = 5

        // give values in Int example 1,2,3 in MB
        const val MAX_IMAGE_SIZE_ALLOWED = "MAX_IMAGE_SIZE_ALLOWED"
        const val MAX_IMAGE_SIZE_ALLOWED_DEAFULT = 1

        @JvmOverloads
        fun perpareIntent(activity:Activity?,cameraPermission:Boolean = false,storagePermission:Boolean = false,openFor:String = CAMERA_ONLY,maxImageSelections:Int = MAX_SELECTION_DEFAULT,
                          maxImageSize:Int = MAX_IMAGE_SIZE_ALLOWED_DEAFULT) : Intent? {

            if(activity == null)
                return null

            val intent = Intent(activity,CalleryActivity::class.java)
            return intent.apply {
                putExtra(CAMERA_PERMISSION,cameraPermission)
                putExtra(STORAGE_PERMISSION,storagePermission)
                putExtra(OPEN_FOR,openFor)
                putExtra(MAX_SELECTION,maxImageSelections)
                putExtra(MAX_IMAGE_SIZE_ALLOWED,maxImageSize)
            }
        }

    }
}