package org.oogwayrides.console.models

data class Adventure(
    //add id
    var _id : Int = 0,
    var numOfPass:Int=0,
    var organizer : User? = null,
    var vehicle : String? = null,
    var date : String?=null,
    var locaton : String? = null,
    var plan : String? = null,
    var passangers: ArrayList<User> = arrayListOf<User>()

){

}