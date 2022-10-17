package org.oogwayrides.console.models

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*
import org.oogwayrides.console.controllers.logger
import org.oogwayrides.console.controllers.user
import org.oogwayrides.console.main.colAdventures

class AdventureMemStore : AdventureStore {

    /**
     * addAdventure()
     * Adds a new adventure to mongo db
     */
    override fun addAdventure(
        id: Int,
        location: String,
        date: String,
        plan: String,
        vehicle: String,
        numOfPass: String,
        colAdventures: MongoCollection<Adventure>
    ): Adventure? {
        try {
            var newAdv = Adventure(id, numOfPass.toInt(), user, vehicle, date, location, plan)
            colAdventures.insertOne(newAdv)
            return newAdv
        } catch (e: Exception) {

        }
        return null
    }

    override fun addAdventure(adv: Adventure, colAdventures: MongoCollection<Adventure>): Boolean {
        return try {
            colAdventures.insertOne(adv)
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * deleteAdventure()
     * deletes adventure if the input is valid,
     * Accepts a list and a index allowing for different searches.
     */
    override fun deleteAdventure(advList: List<Adventure>, input: Int, colAdventures: MongoCollection<Adventure>) {
        if (input < advList.size && input >= 0) {
            colAdventures.deleteOne(advList[input].json)
        }
    }

    /**
     * Delete adventure for testing
     */
    override fun deleteAdventure(colAdventures: MongoCollection<Adventure>, id: Int) {

        colAdventures.deleteOne(Adventure::_id eq id)

    }

    /**
     * Adding a passenger to an Adventure,
     * Error checking for logged in user and number of passengers,
     * Updating the number of passengers
     */
    override fun addPassenger(
        selectedAdv: Adventure,
        user: User,
        colAdventures: MongoCollection<Adventure>
    ): Boolean {

        if (selectedAdv != null && selectedAdv.numOfPass != 0 && !selectedAdv.organizer?.equals(user)!!) {

            user?.let { selectedAdv.passangers.add(it) }
            colAdventures.updateOne(
                Adventure::_id eq selectedAdv._id,
                setValue(Adventure::numOfPass, selectedAdv.numOfPass - 1)
            )
            println(selectedAdv)
            //colAdventures.updateOne(searchList[index.toInt()].json, newAdv).
            //colAdventures.findOne(searchList[index.toInt()].json)
            colAdventures.updateOne(Adventure::_id eq selectedAdv._id, push(Adventure::passangers, user))
            return true

        } else {
            logger.error { "number of passangers exceeded or adventure is null or you can add yourself to your adventure" }
            return false
        }

    }


    /**
     * Simple removing of passenger, incrementing the number of passengers by 1
     */
    override fun removePassenger(colAdventures: MongoCollection<Adventure>, id: Int, user: User) {

        colAdventures.updateOne(Adventure::_id eq id, pull(Adventure::passangers, user))
        colAdventures.updateOne(Adventure::_id eq id, inc(Adventure::numOfPass,   1))

    }


    /**
     * Edit a adventure
     */
    override fun editAdventure(
        chosenAdv: Adventure,
        location: String,
        date: String,
        plan: String,
        vehicle: String,
        numOfPass: String,
        colAdventures: MongoCollection<Adventure>
    ) {


        var updatedAdv: Adventure =
            Adventure(chosenAdv._id, numOfPass.toInt(), user, vehicle, date, location, plan, (chosenAdv.passangers));

        colAdventures.updateOne(chosenAdv.json, updatedAdv)

    }


    /**
     * Filtering by location
     */
    override fun filterByLocation(advList: List<Adventure>) {
        var sortedList: List<Adventure> = advList.sortedBy { adventure -> adventure.locaton }
        for (item in sortedList) {
            println(item)
        }
    }


    /**
     * Searching by location
     * Ignores the case and matches strings
     */
    override fun search(searchLocation: String, list: List<Adventure>): ArrayList<Adventure> {

        val searchList: ArrayList<Adventure> = arrayListOf()
        for (item in list) {
            if (item.locaton?.contains(searchLocation, true) == true) {
                println(item)
                searchList.add(item)
            }
        }
        return searchList
    }


}