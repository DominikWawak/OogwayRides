package com.myapp.oogwayrides_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.myapp.oogwayrides_android.controllers.RecyclerViewAdaptor


private lateinit var tabLayout:TabLayout
private lateinit var viewPager:ViewPager2

class TripsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        tabLayout=findViewById(R.id.tabLayout)
        viewPager=findViewById(R.id.viewPager)


//        val recyclerView= findViewById<RecyclerView>(R.id.recycler)
//        recyclerView.layoutManager=LinearLayoutManager(this)
//        val items= getDataForRecyclerView()
//        val adaptor= RecyclerViewAdaptor(items)
//        recyclerView.adapter=adaptor;

    }



    fun getDataForRecyclerView():ArrayList<String>{
        val list=ArrayList<String>()
        for(i in 0 until 10){
            list.add("item $i")
        }
        return list
    }


}