package com.myapp.oogwayrides_android

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.myapp.oogwayrides_android.controllers.PagerAdapter
import com.myapp.oogwayrides_android.controllers.RecyclerViewAdaptor

import com.myapp.oogwayrides_android.fragments.FollowersFragment
import com.myapp.oogwayrides_android.fragments.MyAdvFragment


private lateinit var tabLayout:TabLayout
private lateinit var viewPager:ViewPager2

class TripsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        tabLayout=findViewById(R.id.tabLayout)
        viewPager=findViewById(R.id.viewPager)
        viewPager.adapter=PagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager){tab,index->
            tab.text=when(index) {
                0 -> {
                    "Explore"
                }
                1 -> {
                    "My Adventures"
                }
                2 -> {
                    "Going"
                }

                else->{
            throw Resources.NotFoundException("Position not found")
        }
        }}.attach()





    }




}