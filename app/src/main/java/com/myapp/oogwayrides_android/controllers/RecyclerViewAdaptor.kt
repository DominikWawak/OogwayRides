package com.myapp.oogwayrides_android.controllers

import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.oogwayrides_android.R
import com.myapp.oogwayrides_android.models.Adventure
import java.util.*
import kotlin.math.log


/**
 * Adventures Recycler View Adapter
 * Used on Recycler views displaying adventure details
 */
class RecyclerViewAdaptor(private val items:ArrayList<Adventure>): RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder>() {
    lateinit var view:View
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val advNameText:TextView=itemView.findViewById(R.id.advRVName)
        val advPlanText:TextView=itemView.findViewById(R.id.planItem)
        val advlocationText:TextView=itemView.findViewById(R.id.locationItem)
        val advOrganizerText:TextView=itemView.findViewById(R.id.organizerItem)
        val advvehText:TextView=itemView.findViewById(R.id.vehItem)
        val advDateText:TextView=itemView.findViewById(R.id.dateItem)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view= LayoutInflater.from(parent.context).inflate(R.layout.adv_item,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind the data to holder
        holder.advNameText.text=items[position].name
        holder.advPlanText.text=items[position].plan

        val geocoder = Geocoder(view.context, Locale.getDefault())

        var addresses: MutableList<Address?>? = geocoder.getFromLocation(items[position].location[0].toDouble(), items[position].location[1].toDouble() , 1)
        val cityName: String = addresses!![0]?.getAddressLine(0) ?: ""
        holder.advlocationText.text=cityName
        holder.advOrganizerText.text=items[position].organizer
        holder.advvehText.text=items[position].vehicle
        holder.advDateText.text=items[position].date
    }

    override fun getItemCount(): Int {

        return items.size
    }


}