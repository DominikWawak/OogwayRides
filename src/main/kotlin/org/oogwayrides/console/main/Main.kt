package org.oogwayrides.console.main

import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}
var user : String? =null
fun main(args: Array<String>) {
    logger.info { "Launching Oogway Rides Console App" } 
    println("Oogway Rides")


    var input: Int

    do {
        input = loginMenu()
        when(input) {
            1 ->  println("Registering...")
            2 -> { 
                    val scanner = Scanner(System.`in`)
                    println("Enter username: ")
                    user = scanner.nextLine()
                do {
                    input = mainMenu()
                    when(input) {
                        1 ->  println("1. Trips near you")
                        2 ->  println("2. Add Adventure")
                        3 ->  println("3. Your Trips")
// Search (current location) for location/city   find rides near you   (filter by date,amount of places,)
// Add adventure
// your trips - going(remove) - driving(edit,remove)
                        -1 -> println("Exiting App")
                        else -> println("Invalid Option")
                    }
                    println()
                } while (input != -1)  }
            -1 -> println("Exiting App")
            else -> println("Invalid Option")
        }
        println()
    } while (input != -1)
    logger.info { "Shutting Down OogwayRides Console App" }
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

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
    println("Hi $user You have sucesfully logged in... ")
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


fun addAdventure(){

}

fun showMyTrips(){

}