package com.myapp.oogwayrides_android.models

import android.net.Uri

/**
 * User data class
 */
data class User(
    var name:String,
    var bio:String,
    var profilePic: Uri?,
    var email:String,
    var tripsOrganised: ArrayList<Adventure>?,
    var tripsGoing: ArrayList<Adventure>?
    ) {

}