package com.myapp.oogwayrides_android.models

import com.google.firebase.auth.FirebaseUser


/**
 * Adventure data class
 */
data class Adventure(
    var organizer: String? = null,
    var location: ArrayList<String>,
    var passangers: ArrayList<FirebaseUser>? = arrayListOf<FirebaseUser>(),

    var name: String? = null,
    var vehicle: String? = null,
    var date: String?=null,
    var numOfPass: Int? =0,
    var plan: String? = null,


    ){

}