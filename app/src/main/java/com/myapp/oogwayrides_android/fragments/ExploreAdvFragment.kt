package com.myapp.oogwayrides_android.fragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.oogwayrides_android.R
import com.myapp.oogwayrides_android.controllers.FirebaseController
import com.myapp.oogwayrides_android.controllers.RecyclerViewAdaptor
import com.myapp.oogwayrides_android.controllers.db
import com.myapp.oogwayrides_android.models.Adventure
import java.util.*
import kotlin.collections.ArrayList

/**
 * Fragment used to display all adventures
 */
val firebaseController=FirebaseController()
private lateinit var adaptor:RecyclerViewAdaptor
class ExploreAdvFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View = inflater.inflate(R.layout.fragment_my_adv, container, false)
        // Inflate the layout for this fragment


        val recyclerView= view.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        val items= getDataForRecyclerView()
        adaptor= RecyclerViewAdaptor(items)
        recyclerView.adapter=adaptor

        return view


    }



    fun getDataForRecyclerView():ArrayList<Adventure> {

        var list = arrayListOf<Adventure>()
        db.collection("adventures")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Load", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    doc.data["name"]
                    list.add(Adventure(doc.data["organizer"].toString(),
                        doc.data["location"] as ArrayList<String>,null,doc.data["name"].toString(),
                        doc.data["vehicle"] as String?, doc.data["date"] as String?,null, doc.data["plan"] as String?

                    ))

                }
                adaptor.notifyDataSetChanged()

                Log.d("load", "Current advs : $list")
            }
        return list
    }



}