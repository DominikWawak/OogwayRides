package com.myapp.oogwayrides_android.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.myapp.oogwayrides_android.R
import com.myapp.oogwayrides_android.models.Adventure
import com.myapp.oogwayrides_android.models.User

/**
 * Followers Recycler View Adapter
 */
class FollowersRVAdapter(private val items:ArrayList<User>): RecyclerView.Adapter<FollowersRVAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name= itemView.findViewById<TextView>(R.id.fnameItem)
        val pic= itemView.findViewById<ImageView>(R.id.profile_follower)
        val org= itemView.findViewById<TextView>(R.id.tripsOrg)
        val going= itemView.findViewById<TextView>(R.id.tripsGoing)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.follower_item,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind the data to holder
        holder.name.text=items[position].name
        Glide.with(holder.itemView).load(items[position].profilePic).into(holder.pic);
        holder.org.text="Organized: "+ (items[position].tripsOrganised?.size ?: "")
        holder.going.text="Going: "+ (items[position].tripsGoing?.size ?: "")

    }

    override fun getItemCount(): Int {

        return items.size
    }

}



