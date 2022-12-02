package com.myapp.oogwayrides_android.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.oogwayrides_android.R
import com.myapp.oogwayrides_android.models.Adventure


class RecyclerViewAdaptor(private val items:ArrayList<Adventure>): RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val advNameText:TextView=itemView.findViewById(R.id.advRVName)
        val advPlanText:TextView=itemView.findViewById(R.id.planItem)
        val advlocationText:TextView=itemView.findViewById(R.id.locationItem)
        val advOrganizerText:TextView=itemView.findViewById(R.id.organizerItem)
        val advvehText:TextView=itemView.findViewById(R.id.vehItem)
        val advDateText:TextView=itemView.findViewById(R.id.dateItem)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.adv_item,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind the data to holder
        holder.advNameText.text=items[position].name
        holder.advPlanText.text=items[position].plan
        holder.advlocationText.text=items[position].location[0]
        holder.advOrganizerText.text=items[position].organizer
        holder.advvehText.text=items[position].vehicle
        holder.advDateText.text=items[position].date
    }

    override fun getItemCount(): Int {

        return items.size
    }


}