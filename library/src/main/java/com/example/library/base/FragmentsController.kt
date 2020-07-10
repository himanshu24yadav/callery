package com.example.library.base

import androidx.fragment.app.FragmentManager
import com.example.library.utils.UtilConstants
import com.example.library.utils.addFragment
import com.example.library.view.CameraFragment

class FragmentsController {

    companion object {
        fun openCamera(supportFragmentManager: FragmentManager?,container: Int) {
            supportFragmentManager?.addFragment(fragment = CameraFragment(),container = container,fragmentTag = UtilConstants.CAMERA_FRAG_TAG,addToBackStack = false)
        }
    }
}