package javeriana.icm.taller2movil

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.*

class LoadImageActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_image)

        imageView = findViewById(R.id.image_view)

        val galleryButton = findViewById<Button>(R.id.select_image_button)
        galleryButton.setOnClickListener {
            pickImageFromGallery()
        }

        val cameraButton = findViewById<Button>(R.id.take_photo_button)
        cameraButton.setOnClickListener {
            takePicture()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        imageView.setImageURI(imageUri)
                        saveImageToGallery(imageUri)
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    val imageUri = getImageUriFromBitmap(imageBitmap)
                    imageView.setImageBitmap(imageBitmap)
                    saveImageToGallery(imageUri)
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun saveImageToGallery(imageUri: Uri) {
        val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image.jpg")
        val outputStream = FileOutputStream(imageFile)
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        MediaStore.Images.Media.insertImage(contentResolver, imageFile.absolutePath, "image.jpg", null)

        Toast.makeText(this, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show()
    }

}

