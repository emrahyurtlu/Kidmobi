package com.kidmobi.assets.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kidmobi.R
import com.kidmobi.mvvm.model.MobileDevice
import com.mobicon.android.mvvm.view.fragment.MobileDevicesFragment
import com.squareup.picasso.Picasso
import java.util.*

class MobileDeviceRecyclerAdapter(
    private var devices: MutableList<MobileDevice>,
    private val listener: MobileDevicesFragment
) :
    RecyclerView.Adapter<MobileDeviceRecyclerAdapter.MobileDeviceViewHolder>() {
    interface OnMyDeviceItemClickListener {
        fun onItemClick(device: MobileDevice)
    }

    inner class MobileDeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ownersTitle: TextView = view.findViewById(R.id.my_device_item_owners_title)
        val ownersSubtitle: TextView = view.findViewById(R.id.my_device_item_owners_subtitle)
        val deviceImage: ImageView = view.findViewById(R.id.my_device_item_image)
        val container: CardView = view.findViewById(R.id.my_device_item_cv)

        fun bind(device: MobileDevice, listener: OnMyDeviceItemClickListener) {
            container.setOnClickListener {
                listener.onItemClick(device)
            }
            Picasso.get()
                .load(device.deviceImageUrl)
                .placeholder(R.drawable.ic_baseline_phone_iphone_64)
                .into(deviceImage)
            ownersTitle.text = device.deviceOwnerName
            ownersSubtitle.text =
                String.format("${device.info.brand.capitalize(Locale.ROOT)} ${device.info.model}")
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MobileDeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mydevices_layout, parent, false)
        return MobileDeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MobileDeviceViewHolder, position: Int) {
        val device: MobileDevice = devices[position]
        holder.bind(device, listener)
    }

    override fun getItemCount() = devices.size
}

