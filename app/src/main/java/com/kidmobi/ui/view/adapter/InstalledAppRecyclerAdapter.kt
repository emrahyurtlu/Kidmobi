package com.kidmobi.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kidmobi.R
import com.kidmobi.data.model.InstalledApp
import com.kidmobi.ui.view.fragment.tabs.devicemanagement.InstalledAppFragment

class InstalledAppRecyclerAdapter(
    private var apps: MutableList<InstalledApp>,
    private val listener: InstalledAppFragment
) :
    RecyclerView.Adapter<InstalledAppRecyclerAdapter.ViewHolder>() {

    interface OnListItemClickListener {
        fun onItemClick(app: InstalledApp)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val appName: TextView = view.findViewById(R.id.appName)
        private val packageName: TextView = view.findViewById(R.id.packageName)

        //private val deviceImage: ImageView = view.findViewById(R.id.my_device_item_image)
        private val container: CardView = view.findViewById(R.id.installed_app_cv)

        fun bind(app: InstalledApp, listener: OnListItemClickListener) {
            container.setOnClickListener {
                listener.onItemClick(app)
            }
            /*Picasso.get()
                .load(R.drawable.iphone)
                .placeholder(R.drawable.ic_baseline_phone_iphone_64)
                .into(deviceImage)*/
            appName.text = app.appName
            packageName.text = app.packageName
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_installed_app_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app: InstalledApp = apps[position]
        holder.bind(app, listener)
    }

    override fun getItemCount() = apps.size
}

