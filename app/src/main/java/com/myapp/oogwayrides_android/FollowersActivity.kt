package com.myapp.oogwayrides_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.oogwayrides_android.controllers.FollowersRVAdapter
import com.myapp.oogwayrides_android.models.User

private lateinit var adaptor: FollowersRVAdapter
class FollowersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)
        val recyclerView: RecyclerView = findViewById(R.id.followRecyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true)
        val items= getDataForRecyclerView()
        adaptor = FollowersRVAdapter(items)
        recyclerView.adapter= adaptor

    }

    private fun getDataForRecyclerView(): ArrayList<User> {
        var list = arrayListOf<User>()

        list.add(User("Dominik","hi","ddd".toUri(),"dd@dd.com",null,null))
        return list
    }


}