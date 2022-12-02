package com.myapp.oogwayrides_android.controllers

import android.content.res.Resources.NotFoundException
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.myapp.oogwayrides_android.fragments.ExploreAdvFragment
import com.myapp.oogwayrides_android.fragments.FollowersFragment
import com.myapp.oogwayrides_android.fragments.GoingAdvsFragment
import com.myapp.oogwayrides_android.fragments.MyAdvFragment

class PagerAdapter(fragmentActivity: FragmentActivity) :FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{ExploreAdvFragment()}
            1->{MyAdvFragment()}
            2->{GoingAdvsFragment()}
            else->{throw NotFoundException("Position not found")}

        }
    }
}