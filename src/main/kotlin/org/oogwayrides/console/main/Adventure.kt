package org.oogwayrides.console.main

data class Adventure(
    var organizer : User? = null,
    var vehicle : String? = null,
    var date : String?=null,
    var locaton : String? = null,
    var plan : String? = null,
    var passangers: Array<User> = arrayOf()

){

}