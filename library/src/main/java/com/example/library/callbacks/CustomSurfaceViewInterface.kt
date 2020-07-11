package com.example.library.callbacks

import android.view.SurfaceHolder

interface CustomSurfaceViewInterface {
    fun onSurfaceDestroyed(holder: SurfaceHolder?)

    fun onSurfaceCreated(holder: SurfaceHolder?)
}