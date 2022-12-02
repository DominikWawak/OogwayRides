package com.myapp.oogwayrides_android.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.myapp.oogwayrides_android.R
import com.myapp.oogwayrides_android.models.Adventure
import com.myapp.oogwayrides_android.models.User

class FollowersRVAdapter(private val items:ArrayList<User>): RecyclerView.Adapter<FollowersRVAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name= itemView.findViewById<TextView>(R.id.fnameItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.follower_item,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind the data to holder
        holder.name.text=items[position].name
    }

    override fun getItemCount(): Int {

        return items.size
    }

}



