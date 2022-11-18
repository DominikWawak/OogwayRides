package com.myapp.oogwayrides_android.models

import android.net.Uri

data class User(
    var name:String,
    var bio:String,
    var profilePic: Uri?,
    var email:String,
    var tripsOrganised: ArrayList<Adventure>?,
    var tripsGoing: ArrayList<Adventure>?
    ) {

}