package org.oogwayrides.console.views

import mu.KotlinLogging
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.oogwayrides.console.controllers.Controller
import org.oogwayrides.console.main.*
import org.oogwayrides.console.models.Adventure
import org.oogwayrides.console.models.User

private val logger = KotlinLogging.logger {}
val controller = Controller()
var box = "\u001B[36m";

class OogwayRidesView {


    fun launchMenu(){
        var input: Int
        do {
            input = mainMenu()
            when(input) {
                1 ->  controller.tripsNearMe()
                2 ->  controller.addAdventure()
                3 ->  controller.showMyTrips()
                4->  controller.goingToTrips()
                -1 -> println("Exiting App")
                else -> println("Invalid Option")
            }
            println()
        } while (input != -1)
    }


    fun loginMenu() : Int {

        var option : Int
        var input: String? = null

        println(box+"Sign Up/ log In")
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
        println("4. Trips youre going to")
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


}