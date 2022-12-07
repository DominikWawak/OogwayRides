package com.myapp.oogwayrides_android.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.oogwayrides_android.R
import com.myapp.oogwayrides_android.models.Adventure

/**
 * My Adventures Recycler View Adapter
 */

class myAdvRVAdapter(private val items:ArrayList<String>): RecyclerView.Adapter<myAdvRVAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val advNameText: TextView =itemView.findViewById(R.id.adv_name_polar)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.myadv_item,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind the data to holder
        holder.advNameText.text=items[position]

    }

    override fun getItemCount(): Int {

        return items.size
    }


}