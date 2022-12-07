package com.myapp.oogwayrides_android.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myapp.oogwayrides_android.R
import com.myapp.oogwayrides_android.controllers.RecyclerViewAdaptor
import com.myapp.oogwayrides_android.controllers.db
import com.myapp.oogwayrides_android.controllers.myAdvRVAdapter
import com.myapp.oogwayrides_android.models.Adventure


/**
 * Fragment used to display all adventures
 * the current user is going to
 */

class MyAdvFragment : Fragment() {

    private lateinit var adaptor:myAdvRVAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View=inflater.inflate(R.layout.fragment_all_adv, container, false)

        auth = Firebase.auth


        val recyclerView= view.findViewById<RecyclerView>(R.id.recyclermy)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        val items= getDataForRecyclerView()
        adaptor= myAdvRVAdapter(items)
        recyclerView.adapter=adaptor

        return view
    }

    fun getDataForRecyclerView():ArrayList<String> {
        var list = arrayListOf<String>()
        db.collection("adventures")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Load", "Listen failed.", e)
                    return@addSnapshotListener
                }
                for (doc in value!!) {

                    if(auth.currentUser?.uid?.let {
                            arrayOf(doc.data["passangers"]).joinToString(" ").contains(
                                it
                            )
                        } == true || doc.data["organizer"].toString() == auth.currentUser?.uid){

                        list.add(doc.data["name"].toString())
                    }
                }
                adaptor.notifyDataSetChanged()

                Log.d("load", "Current advs : $list")
            }
        return list
    }



}