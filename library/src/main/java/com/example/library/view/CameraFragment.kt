package com.example.library.view

import android.content.Context
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.example.library.R
import com.example.library.base.BaseFragment
import com.example.library.callbacks.CameraPreviewInterface
import com.example.library.databinding.LayoutCameraFragmentBinding
import com.example.library.utils.UtilMethods
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraFragment : BaseFragment(),CameraPreviewInterface {

    private var mContext:Context? = null
    private lateinit var mLayoutBinding : LayoutCameraFragmentBinding
    private var mCamera:Camera? = null
    private var isCameraPermissionGranted = true
    private var mCameraPreviewFragment: CameraPreviewFragment? = null
    private var isSafeToTakePic: Boolean = false
    private var mCapturedImageFile: File? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_camera_fragment, container, false)
        return mLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        setCameraView()
    }

    private fun init() {
        initListeners()
    }

    private fun initListeners() {
        mLayoutBinding.cameraIv.setOnClickListener {
            captureImage()
        }
    }

    private fun setCameraView() {
        try {
            if (mCamera != null) {
                mCamera!!.startPreview()
            } else if (isCameraPermissionGranted) {
                mCamera = getCameraInstance()
                initSetCameraPreview()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSetCameraPreview() {
        try {
            if (mCamera != null) {
                mCameraPreviewFragment = CameraPreviewFragment(mContext, mCamera, this)
                val previewFrame: FrameLayout = mLayoutBinding.cameraPreviewFrame
                isSafeToTakePic = true
                previewFrame.removeAllViews()
                previewFrame.addView(mCameraPreviewFragment)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getCameraInstance(): Camera? {
        var cameraToShowPreview: Camera? = null
        try {
            cameraToShowPreview = Camera.open() // attempt to get a Camera instance
        } catch (e: java.lang.Exception) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace()
        }
        return cameraToShowPreview // returns null if camera is unavailable
    }

    @Throws(IOException::class, RuntimeException::class)
    private fun captureImage() {
        if (mCamera != null && isSafeToTakePic) {
            mCamera!!.takePicture(null, null, mPicture)
            isSafeToTakePic = false
        }
    }

    private val mPicture = PictureCallback { data, camera ->

        if (data == null) {
            isSafeToTakePic = true
            return@PictureCallback
        }

        try {
            if (mCamera != null) mCamera!!.startPreview()

            mCapturedImageFile = UtilMethods.createTempFile()
            putFileToStorage(data)

            //to deduce height and width of image without making bitmap
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, 0, data.size, bmOptions)
            val mHeight = bmOptions.outHeight
            val mWidth = bmOptions.outWidth
            bmOptions.inJustDecodeBounds = false

            Log.e("CAMERA_WH","$mHeight x $mWidth")
            Log.e("CAMERA_SIZE","${(mCapturedImageFile?.length())?.div(1024)?.div(1024)}")
            //UtilMethods.addImageToGallery(mCapturedImageFile!!.absolutePath, mContext, data.size, mWidth, mHeight)
            isSafeToTakePic = true
        } catch (e: IOException) {
            e.printStackTrace()
            isSafeToTakePic = true
        }
    }

    private fun putFileToStorage(bytes: ByteArray) {
        try {
            mCapturedImageFile?.let {
                val fos = FileOutputStream(it)
                fos.write(bytes)
                fos.close()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //overriden methods

    override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
        try {
            if (mCamera != null && mCameraPreviewFragment != null) {
                mCamera!!.setPreviewCallback(null)
                mCameraPreviewFragment!!.holder.removeCallback(mCameraPreviewFragment)
                mCamera!!.release()
                mCamera = null
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onSurfaceCreated(holder: SurfaceHolder?) {
        try {
            if (mCamera != null && holder != null) {
                mCamera!!.setPreviewDisplay(holder)
                mCamera!!.setDisplayOrientation(90)
                mCamera!!.startPreview()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}