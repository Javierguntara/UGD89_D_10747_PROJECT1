package com.example.ugd89_d_10747_project1

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraActivity(context: Context?, private val phyCamera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder

    init {
        phyCamera.setDisplayOrientation(90)
        mHolder = holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        try {
            phyCamera.setPreviewDisplay(mHolder)
            phyCamera.startPreview()
        }catch (e: IOException){
            Log.d("error", "Camera error on SurfaceCreated" + e.message)
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        if(mHolder.surface == null) return
        try {
            phyCamera.setPreviewDisplay(mHolder)
            phyCamera.startPreview()
        }catch (e: IOException){
            Log.d("Error", "Camera error on SurfaceChanged"+ e.message)
        }
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        phyCamera.stopPreview()
        phyCamera.release()
    }
}
