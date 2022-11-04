package com.example.ugd89_d_10747_project1


import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import android.hardware.Camera.CameraInfo.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var phyCamera: Camera? = null
    private var phyCameraView: CameraActivity? = null

    private var currentCameraId: Int = CAMERA_FACING_BACK
    private val cameraBack: Int = CAMERA_FACING_BACK

    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        try {
            phyCamera = Camera.open(currentCameraId)
        }catch (e: IOException){
            Log.d("FAIL", "TIDAK DAPAT TERHUBUNG KAMERA" + e.message)
        }

        if (phyCamera != null){
            phyCameraView = CameraActivity(this@MainActivity, phyCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(phyCameraView)
        }
        @SuppressLint("MissingInflatedId", "LocalSuppress")
        val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener{view: View? -> System.exit(0)}

        if (proximitySensor == null) {
            Toast.makeText(this, "PROXIMITY TIDAK DAPAT TERHUBUNG", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {

            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    if (currentCameraId == cameraBack) {
                        phyCamera?.stopPreview();
                    }
                    phyCamera?.release();

                    if(currentCameraId == CAMERA_FACING_BACK){
                        currentCameraId = CAMERA_FACING_FRONT;
                    }
                    else {
                        currentCameraId = CAMERA_FACING_BACK;
                    }
                    try {
                        phyCamera = Camera.open(currentCameraId)
                    }catch (e: IOException){
                        Log.d("FAIL", "TIDAK DAPAT TERHUBUNG KAMERA" + e.message)
                    }

                    if (phyCamera != null){
                        phyCameraView = CameraActivity(this@MainActivity, phyCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(phyCameraView)
                    }
                    @SuppressLint("MissingInflatedId", "LocalSuppress")
                    val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
                    imageClose.setOnClickListener{view: View? -> System.exit(0)}
                }



            } else {

            }
        }
    }
}

