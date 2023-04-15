package javeriana.icm.taller2movil

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
//Un comentario
class ContactListActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            displayContactList()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
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
                displayContactList()
            }
        }
    }

    @SuppressLint("Range")
    private fun displayContactList() {
        val listView = findViewById<ListView>(R.id.contact_list)
        val contactList = mutableListOf<Map<String, Any>>()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.let {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                val contact = HashMap<String, Any>()
                contact["name"] = name
                val photoResId = R.drawable.contactos
                contact["photo"] = photoResId

                contactList.add(contact)
            }
            it.close()
        }

        val adapter = SimpleAdapter(
            this,
            contactList,
            R.layout.activity_contact_list,
            arrayOf("name", "photo"),
            intArrayOf(R.id.contact_name, R.id.contact_image)
        )

        adapter.setViewBinder { view, data, _ ->
            if (view.id == R.id.contact_image) {
                val imageView = view as ImageView
                val photoResId = data as Int
                imageView.setImageResource(photoResId)
                true
            } else {
                false
            }
        }

        listView.adapter = adapter
    }
}
