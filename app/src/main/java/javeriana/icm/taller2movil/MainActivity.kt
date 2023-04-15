package javeriana.icm.taller2movil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnContacts = findViewById<ImageButton>(R.id.imgBtnContacts)
        val btnImages = findViewById<ImageButton>(R.id.imgBtnImages)
        val btnMaps = findViewById<ImageButton>(R.id.imgBtnMaps)

        createButtonsClickListeners(btnContacts, btnImages, btnMaps)

    }

    private fun createButtonsClickListeners (btnContacts : ImageButton, btnImages : ImageButton, btnMaps : ImageButton) {
        btnContacts.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, ContactListActivity::class.java)
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, ContactListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
