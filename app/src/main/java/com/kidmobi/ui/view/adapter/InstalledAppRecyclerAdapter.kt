package com.kidmobi.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kidmobi.R
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.ui.view.fragment.tabs.devicemanagement.InstalledAppFragment
import com.squareup.picasso.Picasso
import java.util.*

class InstalledAppRecyclerAdapter(
    private var devices: MutableList<MobileDevice>,
    private val listener: InstalledAppFragment
) :
    RecyclerView.Adapter<InstalledAppRecyclerAdapter.MobileDeviceViewHolder>() {
    interface OnMyDeviceItemClickListener {
        fun onItemClick(device: MobileDevice)
    }

    inner class MobileDeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ownersTitle: TextView = view.findViewById(R.id.my_device_item_owners_title)
        private val ownersSubtitle: TextView = view.findViewById(R.id.my_device_item_owners_subtitle)
        private val deviceImage: ImageView = view.findViewById(R.id.my_device_item_image)
        private val container: CardView = view.findViewById(R.id.my_device_item_cv)

        fun bind(device: MobileDevice, listener: OnMyDeviceItemClickListener) {
            container.setOnClickListener {
                listener.onItemClick(device)
            }
            Picasso.get()
                .load(R.drawable.iphone)
                .placeholder(R.drawable.ic_baseline_phone_iphone_64)
                .into(deviceImage)
            ownersTitle.text = device.deviceOwnerName
            ownersSubtitle.text =
                String.format("${device.info.brand.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} ${device.info.model}")
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

