package com.kidmobi.ui.view.fragment.tabs.dashboard

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.kidmobi.business.utils.misc.SharedPrefsUtil
import com.kidmobi.business.utils.extensions.checkSystemSettingsAdjustable
import com.kidmobi.databinding.FragmentDeviceIdentityBinding
import com.kidmobi.ui.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DeviceIdentityTabFragment : Fragment() {
    private lateinit var binding: FragmentDeviceIdentityBinding
    private lateinit var imageView: ImageView
    private lateinit var qrCodeWriter: QRCodeWriter

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil

    private lateinit var uniqueDeviceId: String

    private val viewModel: MobileDeviceViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceIdentityBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.checkSystemSettingsAdjustable()

        qrCodeWriter = QRCodeWriter()
        imageView = binding.qrCodeImage

        try {
            sharedPrefsUtil.setDeviceId()
            uniqueDeviceId = sharedPrefsUtil.getDeviceId()
            saveDevice(uniqueDeviceId)
            val bitMatrix: BitMatrix =
                qrCodeWriter.encode(uniqueDeviceId, BarcodeFormat.QR_CODE, 750, 750)
            val bitmap: Bitmap = createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Timber.e("Qr code error: ${e.message}")
            e.printStackTrace()
        }
    }

    /*private fun setUpAds() {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }*/

    private fun saveDevice(uniqueDeviceId: String) {
        Timber.d("Started to save device")
        viewModel.saveDeviceInitially(uniqueDeviceId)
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

