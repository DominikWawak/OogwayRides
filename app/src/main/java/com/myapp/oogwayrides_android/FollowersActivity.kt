package com.myapp.oogwayrides_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.oogwayrides_android.controllers.FollowersRVAdapter
import com.myapp.oogwayrides_android.controllers.db
import com.myapp.oogwayrides_android.models.Adventure
import com.myapp.oogwayrides_android.models.User

/**
 * Followers Activity
 * Used to display currently all the users in the app.
 * TODO future improvement: make follow button
 */
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
        db.collection("users")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Load", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (doc in value!!) {

                     list.add(User(doc.data["name"].toString(),"",doc.data["profilePic"].toString().toUri(),"",
                         doc.data["tripsOrganised"] as ArrayList<Adventure>?,
                         doc.data["tripsGoing"] as ArrayList<Adventure>?
                     ))


                }
                adaptor.notifyDataSetChanged()

                Log.d("load", "Current advs : $list")
            }
        return list
    }


}