package com.example.spirala1.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import java.io.File
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import com.example.spirala1.Fragments.NovaBiljkaFragment
import com.example.spirala1.Objects.SlikaState.testSlika
import com.example.spirala1.R
import java.io.ByteArrayOutputStream
import java.io.FileInputStream


class CameraActivity : AppCompatActivity() {

    private lateinit var preview : PreviewView
    private lateinit var slikajBtn2 : Button
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera)

        preview = findViewById(R.id.preview)
        slikajBtn2 = findViewById(R.id.uslikajBiljkuBtn2)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(preview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
            } catch(exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))

        slikajBtn2.setOnClickListener {

            if(testSlika) {
                val intent = Intent(this, NovaBiljkaFragment::class.java)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            imageCapture?.let { imageCapture ->
                val slika = File(getExternalFilesDir(null), "slika.jpg")
                val out = ImageCapture.OutputFileOptions.Builder(slika).build()

                imageCapture.takePicture(
                    out, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val bitmap = BitmapFactory.decodeStream(FileInputStream(slika))
                            val resizedBitmap = resizeBitmap(bitmap, 300, 300)
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
                            val byteArray = byteArrayOutputStream.toByteArray()
                            val intent = Intent().apply {
                                putExtra("captured_image", byteArray)
                            }
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                        override fun onError(exc: ImageCaptureException) {
                            exc.printStackTrace()
                        }
                    }
                )
            }
        }

    }
    fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

}
