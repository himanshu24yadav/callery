package com.example.library.callbacks

import android.view.SurfaceHolder

interface CameraPreviewInterface {
    fun onSurfaceDestroyed(holder: SurfaceHolder?)

    fun onSurfaceCreated(holder: SurfaceHolder?)
}