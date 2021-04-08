package com.mobicon.android.mvvm.view.fragment

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.kidmobi.R
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.initialize
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import java.util.*


class DeviceIdentityFragment : Fragment(R.layout.fragment_qrcode) {
    //private lateinit var binding: FragmentD
    private lateinit var imageView: ImageView
    private lateinit var qrCodeWriter: QRCodeWriter
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPrefsUtil: SharedPrefsUtil
    private lateinit var uniqueDeviceId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        sharedPrefsUtil = SharedPrefsUtil(context!!)
        val view = inflater.inflate(R.layout.fragment_qrcode, container, false)

        qrCodeWriter = QRCodeWriter()
        imageView = view.findViewById(R.id.qrCodeImage)

        try {
            sharedPrefsUtil.setDeviceId()
            uniqueDeviceId = sharedPrefsUtil.getDeviceId()
            saveDevice(uniqueDeviceId)
            val bitMatrix: BitMatrix =
                qrCodeWriter.encode(uniqueDeviceId, BarcodeFormat.QR_CODE, 750, 750)
            val bitmap: Bitmap = createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            print("QR ERROR: ${e.message}")
            e.printStackTrace()
        }

        return view
    }

    private fun saveDevice(uniqueDeviceId: String) {
        val db = FirebaseFirestore.getInstance()
        val calendar = Calendar.getInstance()
        val deviceInfo = MobileDeviceInfo().initialize()
        val device = MobileDevice().apply {
            deviceId = uniqueDeviceId
            info = deviceInfo
            createdAt = calendar.time
            updatedAt = calendar.time
        }

        val documentReference =
            db.collection(DbCollection.MobileDevices.name).document(uniqueDeviceId)

        documentReference.get().addOnSuccessListener {
            if (!it.exists()) {
                documentReference.set(device, SetOptions.merge())
            }
        }
    }

    private fun createBitmap(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, 750, 0, 0, width, height)
        return bitmap
    }
}

