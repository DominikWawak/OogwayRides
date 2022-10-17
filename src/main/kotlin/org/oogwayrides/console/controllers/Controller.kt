package org.oogwayrides.console.controllers

import com.mongodb.client.MongoCollection
import mu.KotlinLogging
import org.litote.kmongo.*

import org.oogwayrides.console.main.colAdventures
import org.oogwayrides.console.main.colAdventures_t
import org.oogwayrides.console.main.colUsers
import org.oogwayrides.console.models.Adventure
import org.oogwayrides.console.models.AdventureMemStore
import org.oogwayrides.console.models.User
import org.oogwayrides.console.views.OogwayRidesView
import java.lang.NumberFormatException
import kotlin.math.log

val memStore = AdventureMemStore()
val logger = KotlinLogging.logger {}
var oogwayRidesView = OogwayRidesView()
var user: User? = null

class Controller {
    /**
     * Start method that kicks off the whole application
     * Allows a user to log into the application
     */
    fun start() {
        println(
            "\n" +
                    "╭━━━╮╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╭━━━╮╱╱╭╮\n" +
                    "┃╭━╮┃╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱╱┃╭━╮┃╱╱┃┃\n" +
                    "┃┃╱┃┣━━┳━━┳╮╭╮╭┳━━┳╮╱╭┫╰━╯┣┳━╯┣━━┳━━╮\n" +
                    "┃┃╱┃┃╭╮┃╭╮┃╰╯╰╯┃╭╮┃┃╱┃┃╭╮╭╋┫╭╮┃┃━┫━━┫\n" +
                    "┃╰━╯┃╰╯┃╰╯┣╮╭╮╭┫╭╮┃╰━╯┃┃┃╰┫┃╰╯┃┃━╋━━┃\n" +
                    "╰━━━┻━━┻━╮┃╰╯╰╯╰╯╰┻━╮╭┻╯╰━┻┻━━┻━━┻━━╯\n" +
                    "╱╱╱╱╱╱╱╭━╯┃╱╱╱╱╱╱╱╭━╯┃\n" +
                    "╱╱╱╱╱╱╱╰━━╯╱╱╱╱╱╱╱╰━━╯"
        )


        var input: Int

        do {

            input = oogwayRidesView.loginMenu()

            when (input) {
                1 -> {
                    print("Enter User Name: ")
                    val username = readLine()!!
                    print("Write a short Bio: ")
                    val bio = readLine()!!
                    addUser(username,bio)}
                2 -> {

                    println("Enter username: ")
                    var loginUserName = readLine()!!

                    if (logIn(loginUserName)) {
                        oogwayRidesView.launchMenu()
                    }else{
                        logger.error { "User does not exist" }
                    }
                }

                -1 -> println("Exiting App")
                else -> println("Invalid Option")
            }
            println()
        } while (input != -1)
        logger.info { "Shutting Down OogwayRides Console App" }


    }


    /**
     * CRUD Functions for user to select, modify Objects
     */

    fun logIn(loginUserName:String): Boolean {
        if (colUsers.findOne(User::name eq loginUserName) != null) {
            user = colUsers.findOne(User::name eq loginUserName)
            return true
        }
        else{
            return false
        }
    }

    fun addUser(username:String,bio:String) {
        user = User(username, bio, "")
        if(colUsers.findOne(User::name eq user!!.name) == null) {
            colUsers.insertOne(User(username, bio, ""))
            logger.info { "user Added" }
        }
        else{
            logger.error { "user already exists" }
        }

    }

    fun addAdventure() {
        val id = (0..10000).random()
        print("Enter Location ")
        val location = readLine()!!
        print("Date and Time: ")
        val date = readLine()!!
        print("your trip plan: ")
        val plan = readLine()!!
        print("vehicle: ")
        val vehicle = readLine()!!
        print("number of passangers: ")
        val numOfPass = readLine()!!

        memStore.addAdventure(id,location,date,plan,vehicle,numOfPass, colAdventures)

    }




    fun editAdventure(advList: List<Adventure>) {
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
        print("new number of passangers?: ")
        val numOfPass = readLine()!!


        if (input.toInt() < advList.size && input.toInt() >= 0) {
            //
            memStore.editAdventure(advList[input.toInt()],location,date,plan,vehicle,numOfPass, colAdventures)
        }
    }


    /**
     * Searching for trips around a location.
     */
    fun tripsNearMe() {
        print("Search Location: ")
        var searchLocation = readLine()!!
        val list: List<Adventure> = colAdventures.find().toList()
        var searchList = memStore.search(searchLocation, list)

        if (searchList.isNotEmpty()) {
            println("")
            println("OPTIONS:")
            println(
                "1. Join Trip \n" +
                        "-1. Go Back"
            )

            do {
                println("choose Option")
                var input = readLine()!!
                try{
                when (input.toInt()) {
                    1 -> {
                        println("Enter index of Adventure to join")
                        var index = readLine()!!

                        if (searchList[index.toInt()] != null) {
                            user?.let { memStore.addPassenger(searchList[index.toInt()], it, colAdventures) }
                        }
                        oogwayRidesView.launchMenu()

                    }

                    -1 -> oogwayRidesView.launchMenu()

                    else -> println("Invalid Option")
                }
                println()
            }catch (e:NumberFormatException){
                logger.error { e }
            }
            } while (input.toInt() != -1)


        }else{
            logger.error { "No search found" }
        }
    }


    /**
     * Listing trips you created, Allows you to modify and delete Adventures.
     */
    fun showMyTrips() {
        var drivingToList: List<Adventure> = colAdventures.find(Adventure::organizer eq user).toList()

        println("You are driving for these events :")
        for ((index, value) in drivingToList.withIndex()) {
            println("$index : $value")
        }
        println("")
        println("OPTIONS:")
        println(
            "1. Edit \n" +
                    "2. Delete\n" +
                    "3. Filter by Location\n" +
                    "-1. Go Back"
        )

        do {
            println("choose Option")
            var input = readLine()!!
            when (input.toInt()) {
                1 -> {
                    editAdventure(drivingToList)
                    oogwayRidesView.launchMenu()
                }

                2 -> {
                    println("Enter index of Adventure you want to remove ")
                    var input = readLine()!!
                    memStore.deleteAdventure(drivingToList, input.toInt(), colAdventures)
                    oogwayRidesView.launchMenu()
                }

                3 -> {
                    memStore.filterByLocation(drivingToList)
                    showMyTrips()
                }

                -1 -> oogwayRidesView.launchMenu()
                else -> println("Invalid Option")
            }
            println()
        } while (input.toInt() != -1)

    }


    /**
     * Trips you are signed up for, Option to leave the trip
     */
    fun goingToTrips(){
        var signedUpToList: List<Adventure> = colAdventures.find(Adventure::passangers.contains(user)).toList()
        println("You are signed up for these events :")
        for ((index, value) in signedUpToList.withIndex()) {
            println("$index : $value")
        }
        println("")
        println("OPTIONS:")
        println(
                    "1. Leave \n" +

                    "-1. Go Back"
        )

        do {
            println("choose Option")
            var input = readLine()!!
            when (input.toInt()) {
                1 -> {
                    println("Enter index of Adventure to leave")
                    var index = readLine()!!

                    if (signedUpToList[index.toInt()] != null) {
                        user?.let { memStore.removePassenger(colAdventures,signedUpToList[index.toInt()]._id, it) }
                        logger.info { "Passenger removed" }
                    }
                    oogwayRidesView.launchMenu()
                }

                -1 -> oogwayRidesView.launchMenu()
                else -> println("Invalid Option")
            }
            println()
        } while (input.toInt() != -1)

    }

}