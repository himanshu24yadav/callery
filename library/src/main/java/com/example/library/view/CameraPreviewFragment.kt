package com.example.library.view

import android.content.Context
import android.hardware.Camera
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.library.callbacks.CameraPreviewInterface
import kotlin.math.abs

class CameraPreviewFragment(mContext: Context?, private var mCamera:Camera?, private var mCameraPreviewInterface: CameraPreviewInterface?) : SurfaceView(mContext), SurfaceHolder.Callback {
    private var mHolder: SurfaceHolder? = null
    private var mSupportedPreviewSizes: List<Camera.Size>? = null
    private var mPreviewSize: Camera.Size? = null

    init {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = holder
        mHolder!!.addCallback(this)
        if (mCamera != null) mSupportedPreviewSizes =
            mCamera!!.parameters.supportedPreviewSizes
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        // The Surface has been created, now tell the camera where to draw the preview.
        if (mCameraPreviewInterface != null) mCameraPreviewInterface?.onSurfaceCreated(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        // empty. Take care of releasing the Camera preview in your activity.
        if (mCameraPreviewInterface != null) mCameraPreviewInterface?.onSurfaceDestroyed(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        try {
            if (mHolder!!.surface == null) {
                // preview surface does not exist
                return
            }

            // stop preview before making changes
            mCamera!!.stopPreview()

            // set preview size and make any resize, rotate or

            //to improve the camera quality
            val parameters = mCamera!!.parameters

            //set Picture Size
            val sizes = parameters.supportedPictureSizes
            var size = sizes[0]
            for (i in sizes.indices) {
                if (sizes[i].width > size.width) size = sizes[i]
            }
            parameters.setPictureSize(size.width, size.height)
            if (mPreviewSize != null) parameters.setPreviewSize(mPreviewSize!!.width, mPreviewSize!!.height)
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            parameters.jpegQuality = 100
            parameters.jpegThumbnailQuality = 100
            /**
             * Basically for Nexus 5x only since there is orientation issue with Nexus 5x device particularly.
             */
            if (Build.MODEL != null && Build.MODEL.equals("Nexus 5X", ignoreCase = true)) {
                parameters.setRotation(270)
                mCamera!!.setDisplayOrientation(270)
            } else {
                parameters.setRotation(90)
                mCamera!!.setDisplayOrientation(90)
            }
            mCamera!!.parameters = parameters

            // start preview with new settings
            mCamera!!.setPreviewDisplay(mHolder)
            mCamera!!.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = View.resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height)
        }
    }

    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        var optimalSize: Camera.Size? = null
        try {
            val ASPECT_TOLERANCE = 0.1
            val targetRatio = h.toDouble() / w
            if (sizes == null) return null
            var minDiff = Double.MAX_VALUE
            for (size in sizes) {
                val ratio = size.width.toDouble() / size.height
                if (abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
                if (abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = abs(size.height - h).toDouble()
                }
            }
            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE
                for (size in sizes) {
                    if (abs(size.height - h) < minDiff) {
                        optimalSize = size
                        minDiff = abs(size.height - h).toDouble()
                    }
                }
            }
            return optimalSize
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return optimalSize
    }
}