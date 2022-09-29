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

val memStore = AdventureMemStore()
val logger = KotlinLogging.logger {}
var oogwayRidesView = OogwayRidesView()
var user: User? = null

class Controller {


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
                    }
                }

                -1 -> println("Exiting App")
                else -> println("Invalid Option")
            }
            println()
        } while (input != -1)
        logger.info { "Shutting Down OogwayRides Console App" }


    }



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
        colUsers.insertOne(User(username, bio, ""))

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

        memStore.addAdventure(id,location,date,plan,vehicle,numOfPass)

    }

    fun addAdventure(adv: Adventure): Boolean {
        return try {
            colAdventures_t.insertOne(adv)
            true
        } catch (e: Exception) {
            false
        }
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

       memStore.editAdventure(advList,input,location,date,plan,vehicle,numOfPass)
    }



    fun tripsNearMe() {
        print("Search Location: ")
        var searchLocation = readLine()!!
        //println(colAdventures.find().toList())
//    val list : List<Adventure> = colAdventures.find(Adventure::locaton eq searchLocation).toList()

        //Search
        var  searchList = memStore.search(searchLocation)
        println("")
        println("OPTIONS:")
        println(
            "1. Join Trip \n" +
                    "-1. Go Back"
        )

        do {
            println("choose Option")
            var input = readLine()!!
            when (input.toInt()) {
                1 -> {
                    println("Enter index of Adventure to join")
                    var index = readLine()!!
                    var newAdv: Adventure? = searchList[index.toInt()]
                    if (newAdv != null) {
                        user?.let { memStore.addPassenger(newAdv, searchList[index.toInt()], it, colAdventures) }
                    }
                    oogwayRidesView.launchMenu()

                }

                -1 -> oogwayRidesView.launchMenu()
                else -> println("Invalid Option")
            }
            println()
        } while (input.toInt() != -1)


    }






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

}