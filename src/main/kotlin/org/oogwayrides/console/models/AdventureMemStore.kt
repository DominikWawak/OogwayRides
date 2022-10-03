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
    ): Adventure? {
        try {
            var newAdv= Adventure(id, numOfPass.toInt(), user, vehicle, date, location, plan)
            colAdventures.insertOne(newAdv)
            return newAdv
        } catch (e: Exception) {

        }
        return null
    }

    override fun deleteAdventure(advList: List<Adventure>, input: Int, colAdventures: MongoCollection<Adventure>) {
        if (input < advList.size && input >= 0) {
            colAdventures.deleteOne(advList[input].json)
        }
    }

    override fun deleteAdventure( colAdventures: MongoCollection<Adventure>, id:Int) {

            colAdventures.deleteOne(Adventure::_id eq id)

    }

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

    override fun removePassenger(colAdventures: MongoCollection<Adventure>, id: Int, user:User) {

        colAdventures.updateOne(Adventure::_id eq id, pull(Adventure::passangers, user))

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


    //make general 1 taks per function

    override fun search(searchLocation: String, list:List<Adventure>): ArrayList<Adventure> {

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