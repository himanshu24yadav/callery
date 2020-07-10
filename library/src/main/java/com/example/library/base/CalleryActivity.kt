package com.example.library.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.library.R
import com.example.library.databinding.LayoutCalleryActivityBinding

class CalleryActivity : BaseActivity() {

    private lateinit var mLayoutBinding: LayoutCalleryActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLayoutBinding = DataBindingUtil.setContentView(this,R.layout.layout_callery_activity)
        init()
    }

    private fun init() {
        initOpenCameraFragment()
    }

    private fun initOpenCameraFragment() {
        FragmentsController.openCamera(supportFragmentManager = supportFragmentManager,container = R.id.container)
    }

}