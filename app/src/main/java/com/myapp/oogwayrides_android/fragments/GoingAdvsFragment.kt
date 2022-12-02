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
import com.myapp.oogwayrides_android.models.Adventure
import kotlin.math.log


class GoingAdvsFragment : Fragment() {
    private lateinit var adaptor:RecyclerViewAdaptor
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View=inflater.inflate(R.layout.fragment_going_advs, container, false)
        auth = Firebase.auth


        val recyclerView= view.findViewById<RecyclerView>(R.id.recyclerGoing)
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

                    if(auth.currentUser?.uid?.let {
                            arrayOf(doc.data["passangers"]).joinToString(" ").contains(
                                it
                            )
                        } == true){

                    list.add(Adventure(doc.data["organizer"].toString(),
                        doc.data["location"] as ArrayList<String>,null,doc.data["name"].toString(),
                        doc.data["vehicle"] as String?, doc.data["date"] as String?,null, doc.data["plan"] as String?

                    ))

                }
                }
                adaptor.notifyDataSetChanged()

                Log.d("load", "Current advs : $list")
            }
        return list
    }



}