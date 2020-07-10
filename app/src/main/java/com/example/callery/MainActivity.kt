package com.example.callery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.callery.databinding.ActivityMainBinding
import com.example.library.utils.CalleryHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutBinding : ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        layoutBinding.openCameraTv.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = CalleryHelper.perpareIntent(this,cameraPermission = true,storagePermission = true)
        intent?.let { startActivity(it) }
    }
}
