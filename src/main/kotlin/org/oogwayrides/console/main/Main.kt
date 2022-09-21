package org.oogwayrides.console.main

import mu.KotlinLogging
import org.litote.kmongo.*
import org.oogwayrides.console.controllers.Controller
import org.oogwayrides.console.models.Adventure
import org.oogwayrides.console.models.User
import org.oogwayrides.console.views.OogwayRidesView


var controller =Controller()
private val logger = KotlinLogging.logger {}
val client = KMongo.createClient()
val database = client.getDatabase("OogwayRides")
val colUsers = database.getCollection<User>()
val colAdventures = database.getCollection<Adventure>()

fun main() {

    logger.info { "Launching Oogway Rides Console App" }
    controller.start()

}






