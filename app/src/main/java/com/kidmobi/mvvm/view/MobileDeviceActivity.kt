package com.kidmobi.mvvm.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.R
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.databinding.ActivityMobileDeviceBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.UserMobileDevice
import java.util.*

class MobileDeviceActivity : AppCompatActivity() {
    private lateinit var mobileDevice: MobileDevice
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMobileDeviceBinding

    //private var mobileDeviceViewModel = MobileDeviceViewModel()
    //private var userMobileDeviceViewModel = UserMobileDeviceViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobileDevice = intent.getSerializableExtra("device") as MobileDevice
        binding.deviceOwner.setText(mobileDevice.deviceOwnerName)
        // Admob ID: ca-app-pub-9250940245734350/2801074884

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun turnBack(view: View) {
        finish()
    }

    fun saveDeviceDetails(view: View) {
        val calendar = Calendar.getInstance()

        if (binding.deviceOwner.text.isNullOrEmpty()) {
            binding.deviceOwner.setError(
                "Bu alan boş olamaz.",
                ResourcesCompat.getDrawable(
                    baseContext.resources,
                    R.drawable.ic_baseline_error_24,
                    null
                )
            )
            return
        }

        mobileDevice.deviceOwnerName = binding.deviceOwner.text.toString()

        val userDevCollection = UserMobileDevice()
        userDevCollection.devices = mutableListOf(mobileDevice.deviceId)

        // Get user Device List
        val hashMap = hashMapOf("devices" to listOf(mobileDevice.deviceId))

        val documentReference =
            db.collection(DbCollection.UserMobileDevices.name).document(auth.uid.toString())

        documentReference.addSnapshotListener { doc, e ->
            if (doc != null) {
                if (doc.exists()) {
                    // Update
                    documentReference
                        .update("devices", FieldValue.arrayUnion(mobileDevice.deviceId))

                    Toast.makeText(
                        baseContext,
                        getString(R.string.mobile_device_is_added_your_network),
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //insert
                    documentReference.set(hashMap)
                }

            }
        }

        val mobileDeviceRef =
            db.collection(DbCollection.MobileDevices.name).document(mobileDevice.deviceId)
        mobileDeviceRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val tempDevice = document.toObject(MobileDevice::class.java)

                if (tempDevice != null) {
                    tempDevice.apply {
                        this.deviceOwnerName = mobileDevice.deviceOwnerName
                        this.updatedAt = calendar.time
                    }
                    mobileDeviceRef.set(tempDevice)
                }
            }
        }

        turnBack(view)

    }
}