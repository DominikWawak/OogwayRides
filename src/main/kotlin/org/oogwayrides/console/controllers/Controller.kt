package org.oogwayrides.console.controllers

import mu.KotlinLogging
import org.litote.kmongo.*

import org.oogwayrides.console.main.colAdventures
import org.oogwayrides.console.main.colUsers
import org.oogwayrides.console.models.Adventure
import org.oogwayrides.console.models.User
import org.oogwayrides.console.views.OogwayRidesView
import org.oogwayrides.console.views.controller


private val logger = KotlinLogging.logger {}
var oogwayRidesView = OogwayRidesView()
var user : User? =null

class Controller {


    fun start(){
        println("\n" +
                "╭━━━╮╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╭━━━╮╱╱╭╮\n" +
                "┃╭━╮┃╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱┃╭━╮┃╱╱┃┃\n" +
                "┃┃╱┃┣━━┳━━┳╮╭╮╭┳━━┳╮╱╭┫╰━╯┣┳━╯┣━━┳━━╮\n" +
                "┃┃╱┃┃╭╮┃╭╮┃╰╯╰╯┃╭╮┃┃╱┃┃╭╮╭╋┫╭╮┃┃━┫━━┫\n" +
                "┃╰━╯┃╰╯┃╰╯┣╮╭╮╭┫╭╮┃╰━╯┃┃┃╰┫┃╰╯┃┃━╋━━┃\n" +
                "╰━━━┻━━┻━╮┃╰╯╰╯╰╯╰┻━╮╭┻╯╰━┻┻━━┻━━┻━━╯\n" +
                "╱╱╱╱╱╱╱╭━╯┃╱╱╱╱╱╱╱╭━╯┃\n" +
                "╱╱╱╱╱╱╱╰━━╯╱╱╱╱╱╱╱╰━━╯")


        var input: Int

        do {

            input = oogwayRidesView.loginMenu()

            when(input) {
                1 ->  addUser()
                2 -> {

                    println("Enter username: ")
                    var loginUserName = readLine()!!

                    if(colUsers.findOne(User::name eq loginUserName)!=null) {
                        user = colUsers.findOne(User::name eq loginUserName)
                        oogwayRidesView.launchMenu()
                    }}
                -1 -> println("Exiting App")
                else -> println("Invalid Option")
            }
            println()
        } while (input != -1)
        logger.info { "Shutting Down OogwayRides Console App" }


    }


    fun addUser(){
        print("Enter User Name: ")
        val username = readLine()!!
        print("Write a short Bio: ")
        val bio = readLine()!!
        user= User(username,bio,"") // toRemove
        colUsers.insertOne(User(username,bio,""))

    }
    fun addAdventure(){
        print("Enter Location ")
        val location = readLine()!!
        print("Date and Time: ")
        val date = readLine()!!
        print("your trip plan: ")
        val plan = readLine()!!
        print("vehicle: ")
        val vehicle = readLine()!!

        colAdventures.insertOne(Adventure(user,vehicle,date,location,plan))
    }

    fun tripsNearMe(){
        print("Search Location: ")
        var searchLocation = readLine()!!
        //println(colAdventures.find().toList())
//    val list : List<Adventure> = colAdventures.find(Adventure::locaton eq searchLocation).toList()
        val list : List<Adventure> = colAdventures.find().toList()

        for (item in list){
            if(item.locaton?.contains(searchLocation,true) == true)
                println(item)
        }
    }


    fun deleteAdventure(advList :List<Adventure>){
        println("Enter index of Adventure you want to remove ")
        var input = readLine()!!
        if( input.toInt() < advList.size && input.toInt()>=0){
            colAdventures.deleteOne(advList[input.toInt()].json)
        }
    }

    fun editAdventure(advList :List<Adventure>){
        println("Enter index of Adventure you want to edit ")
        var input = readLine()!!
        print("Enter new Location ")
        val location = readLine()!!
        print("new Date and Time: ")
        val date = readLine()!!
        print("your new trip plan: ")
        val plan = readLine()!!
        print("new vehicle: ")
        val vehicle = readLine()!!

        var updatedAdv: Adventure = Adventure(user,vehicle,date,location,plan)
        if( input.toInt() < advList.size && input.toInt()>=0){
            // update
            colAdventures.updateOne(advList[input.toInt()].json,updatedAdv)
        }
    }

    fun filterByLocation(advList :List<Adventure>){
        var sortedList : List<Adventure> =advList.sortedBy { adventure -> adventure.locaton }
        for (item in sortedList){
            println(item)
        }
    }


    fun showMyTrips(){
        var drivingToList : List<Adventure> = colAdventures.find(Adventure::organizer eq user).toList()

        println("You are driving for these events :")
        for ((index, value) in drivingToList.withIndex()) {
            println("$index : $value")
        }
        println("")
        println("OPTIONS:")
        println("1. Edit \n" +
                "2. Delete\n" +
                "3. Filter by Location\n"+
                "-1. Go Back")

        do {
            println("choose Option")
            var input = readLine()!!
            when(input.toInt()) {
                1 -> {
                    editAdventure(drivingToList)
                    oogwayRidesView.launchMenu()
                }
                2 ->  {
                    deleteAdventure(drivingToList)
                    oogwayRidesView.launchMenu()
                }
                3 -> {
                    filterByLocation(drivingToList)
                    showMyTrips()
                }
                -1 -> oogwayRidesView.launchMenu()
                else -> println("Invalid Option")
            }
            println()
        } while (input.toInt() != -1)

    }

}