package com.example.tasks.Activites

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tasks.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class QRCodeScanner : AppCompatActivity() {
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var surfaceView: SurfaceView
    private var isCameraPermissionGranted = false

    private lateinit var sharedPreferences: SharedPreferences
    private var isCameraPermissionSaved = false

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_scanner)

        surfaceView = findViewById(R.id.surface_view)
        textView = findViewById(R.id.text_view)
        button = findViewById(R.id.button)
        detector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()
        cameraSource = CameraSource.Builder(this, detector)
            .setAutoFocusEnabled(true)
            .build()

        sharedPreferences = getSharedPreferences("qr_code_reader", Context.MODE_PRIVATE)
        isCameraPermissionSaved =
            sharedPreferences.getBoolean("is_camera_permission_granted", false)

        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST
                )
            } else {
                isCameraPermissionGranted = true
                startCameraSource()
            }
        }

        detector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(barcodes.valueAt(0).displayValue))
                    startActivity(intent)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isCameraPermissionSaved) {
            startCameraSource()
        } else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isCameraPermissionGranted = true
            startCameraSource()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraSource.stop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isCameraPermissionGranted = true
                startCameraSource()
                sharedPreferences.edit().putBoolean("is_camera_permission_granted", true).apply()
            } else {
                Log.d(ContentValues.TAG, "Camera permission not granted")
            }
        }
    }

    private fun startCameraSource() {
        try {
            if (isCameraPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                cameraSource.start(surfaceView.holder)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST
                )
            }
        } catch (e: IOException) {
            Log.e(ContentValues.TAG, "Unable to start camera source.", e)
            cameraSource.release()
        }
    }
}