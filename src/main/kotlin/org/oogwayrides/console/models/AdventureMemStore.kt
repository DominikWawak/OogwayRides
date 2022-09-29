package org.oogwayrides.console.models

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*
import org.oogwayrides.console.controllers.logger
import org.oogwayrides.console.controllers.user
import org.oogwayrides.console.main.colAdventures

class AdventureMemStore:AdventureStore {
    override fun addAdventure(
        id: Int,
        location: String,
        date: String,
        plan: String,
        vehicle: String,
        numOfPass: String
    ) {
        try {
            colAdventures.insertOne(Adventure(id, numOfPass.toInt(), user, vehicle, date, location, plan))
        } catch (e: Exception) {

        }
    }

    override fun deleteAdventure(advList: List<Adventure>, input: Int, colAdventures: MongoCollection<Adventure>) {
        if (input < advList.size && input >= 0) {
            colAdventures.deleteOne(advList[input].json)
        }
    }

    override fun addPassenger(
        newAdv: Adventure,
        selectedAdv: Adventure,
        user: User,
        colAdventures: MongoCollection<Adventure>
    ): Boolean {

            if (newAdv != null && newAdv.numOfPass != 0 && !selectedAdv.organizer?.equals(user)!!) {

                user?.let { newAdv.passangers.add(it) }
                colAdventures.updateOne(
                    Adventure::_id eq selectedAdv._id,
                    setValue(Adventure::numOfPass, selectedAdv.numOfPass - 1)
                )
                println(newAdv)
                //colAdventures.updateOne(searchList[index.toInt()].json, newAdv).
                //colAdventures.findOne(searchList[index.toInt()].json)

                colAdventures.updateOne(Adventure::_id eq selectedAdv._id, push(Adventure::passangers, user))
                return true

            } else {
                logger.error { "number of passangers exceeded or adventure is null or you can add yourself to your adventure" }
                return false
            }

    }

    override fun editAdventure(advList: List<Adventure>,input:String,location: String,date: String,plan: String,vehicle: String,numOfPass: String) {


        var updatedAdv: Adventure =
            Adventure(advList[input.toInt()]._id, numOfPass.toInt(), user, vehicle, date, location, plan)
        if (input.toInt() < advList.size && input.toInt() >= 0) {
            // update
            colAdventures.updateOne(advList[input.toInt()].json, updatedAdv)
        }
    }

    override fun filterByLocation(advList: List<Adventure>)  {
        var sortedList: List<Adventure> = advList.sortedBy { adventure -> adventure.locaton }
        for (item in sortedList) {
            println(item)
        }
    }

    override fun search(searchLocation: String): ArrayList<Adventure> {
        val list: List<Adventure> = colAdventures.find().toList()

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