package org.oogwayrides.console.main

import mu.KotlinLogging
import java.util.*
import org.litote.kmongo.*
import org.litote.kmongo.util.idValue
import kotlin.collections.ArrayList

val scanner = Scanner(System.`in`)
private val logger = KotlinLogging.logger {}
var user : User? =null

val client = KMongo.createClient()
val database = client.getDatabase("OogwayRides")
val colUsers = database.getCollection<User>()
val colAdventures = database.getCollection<Adventure>()

fun main() {

    logger.info { "Launching Oogway Rides Console App" } 
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

            input = loginMenu()

        when(input) {
            1 ->  addUser()
            2 -> { 

                    println("Enter username: ")
                    var loginUserName = scanner.nextLine()

                if(colUsers.findOne(User::name eq loginUserName)!=null) {
                    user = colUsers.findOne(User::name eq loginUserName)
                    launchMenu()
                }}
            -1 -> println("Exiting App")
            else -> println("Invalid Option")
        }
        println()
    } while (input != -1)
    logger.info { "Shutting Down OogwayRides Console App" }
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

}

fun launchMenu(){
    var input: Int
    do {
        input = mainMenu()
        when(input) {
            1 ->  tripsNearMe()
            2 ->  addAdventure()
            3 ->  showMyTrips()
            -1 -> println("Exiting App")
            else -> println("Invalid Option")
        }
        println()
    } while (input != -1)
}


fun loginMenu() : Int {

    var option : Int
    var input: String? = null

    println("Sign Up/ log In")
    println(" 1. Register")
    println(" 2. logIn")
    println("-1. Exit")
    println()
    print("Enter an integer : ")
    input = readLine()!!
    option = if (input.toIntOrNull() != null && !input.isEmpty())
        input.toInt()
    else
        -9
    return option
}

fun mainMenu() : Int {

    println("")
    var option : Int
    var input: String? = null

    println("Main Menu")
    println("1. Trips near you")
    println("2. Add Adventure")
    println("3. Your Trips")
    println("-1. Exit")
    println()
    print("Enter an integer : ")
    input = readLine()!!
    option = if (input.toIntOrNull() != null && !input.isEmpty())
        input.toInt()
    else
        -9
    return option
}


fun addUser(){
    print("Enter User Name: ")
    val username = scanner.nextLine()
    print("Write a short Bio: ")
    val bio = scanner.nextLine()
    user=User(username,bio,"") // toRemove
    colUsers.insertOne(User(username,bio,""))

}
fun addAdventure(){
    print("Enter Location ")
    val location = scanner.nextLine()
    print("Date and Time: ")
    val date = scanner.nextLine()
    print("your trip plan: ")
    val plan = scanner.nextLine()
    print("vehicle: ")
    val vehicle = scanner.nextLine()

    colAdventures.insertOne(Adventure(user,vehicle,date,location,plan))
}

fun tripsNearMe(){
    print("Search Location: ")
    var searchLocation = scanner.nextLine()
    //println(colAdventures.find().toList())
    val list : List<Adventure> = colAdventures.find(Adventure::locaton eq searchLocation).toList()
    for (item in list){
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
            "-1. Go Back")

    do {
        println("choose Option")
        var input = scanner.nextInt()
        when(input) {
            1 -> {
                editAdventure(drivingToList)
                launchMenu()
                 }
            2 ->  {deleteAdventure(drivingToList)
                    launchMenu()
                    }
            -1 -> launchMenu()
            else -> println("Invalid Option")
        }
        println()
    } while (input != -1)

}

fun deleteAdventure(advList :List<Adventure>){
    println("Enter index of Adventure you want to remove ")
    var input = scanner.nextInt()
    if( input < advList.size && input>=0){
        colAdventures.deleteOne(advList[input].json)
    }
}

fun editAdventure(advList :List<Adventure>){
    println("Enter index of Adventure you want to edit ")
    var input = scanner.nextInt()
    print("Enter new Location ")
    val location = scanner.nextLine()
    print("new Date and Time: ")
    val date = scanner.nextLine()
    print("your new trip plan: ")
    val plan = scanner.nextLine()
    print("new vehicle: ")
    val vehicle = scanner.nextLine()

    var updatedAdv:Adventure = Adventure(user,vehicle,date,location,plan)
    if( input < advList.size && input>=0){
        // update
        colAdventures.updateOne(advList[input].json,updatedAdv)
    }
}



