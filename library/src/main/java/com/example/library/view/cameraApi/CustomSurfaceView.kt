package com.example.library.view.cameraApi

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Camera
import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.library.callbacks.CustomSurfaceViewInterface
import kotlin.math.abs

@SuppressLint("ViewConstructor")
@Suppress("DEPRECATION")
class CustomSurfaceView(mContext: Context?, private var mCamera:Camera?, private var mCustomSurfaceViewInterface: CustomSurfaceViewInterface?) : SurfaceView(mContext), SurfaceHolder.Callback {
    private var mHolder: SurfaceHolder? = null
    private var mSupportedPreviewSizes: List<Camera.Size>? = null
    private var mPreviewSize: Camera.Size? = null

    init {
        mHolder = holder
        mHolder!!.addCallback(this)
        if (mCamera != null) mSupportedPreviewSizes = mCamera!!.parameters.supportedPreviewSizes
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (mCustomSurfaceViewInterface != null) mCustomSurfaceViewInterface?.onSurfaceCreated(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        if (mCustomSurfaceViewInterface != null) mCustomSurfaceViewInterface?.onSurfaceDestroyed(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        try {
            if (mHolder!!.surface == null) {
                return
            }

            mCamera!!.stopPreview()

            val parameters = mCamera!!.parameters

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

            if (Build.MODEL != null && Build.MODEL.equals("Nexus 5X", ignoreCase = true)) {
                parameters.setRotation(270)
                mCamera!!.setDisplayOrientation(270)
            } else {
                parameters.setRotation(90)
                mCamera!!.setDisplayOrientation(90)
            }
            mCamera!!.parameters = parameters

            Log.e("CAMERA_PICTURE_WH","${parameters.pictureSize.width} x ${parameters.pictureSize.height}")
            Log.e("CAMERA_PREVIEW_WH","${parameters.previewSize.width} x ${parameters.previewSize.height}")

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
            val aspectTolerance = 0.1
            val targetRatio = h.toDouble() / w
            if (sizes == null) return null
            var minDiff = Double.MAX_VALUE
            for (size in sizes) {
                val ratio = size.width.toDouble() / size.height
                if (abs(ratio - targetRatio) > aspectTolerance) continue
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