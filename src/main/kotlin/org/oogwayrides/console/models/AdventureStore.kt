package org.oogwayrides.console.models

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*

import org.oogwayrides.console.controllers.user
import org.oogwayrides.console.main.colAdventures

interface AdventureStore {

    fun addAdventure(id:Int,location:String,date:String,plan:String,vehicle:String,numOfPass:String): Adventure?

    fun deleteAdventure(advList: List<Adventure>, input: Int, colAdventures: MongoCollection<Adventure>)
    fun deleteAdventure( colAdventures: MongoCollection<Adventure>, id:Int)

    fun addPassenger(
        selectedAdv: Adventure,
        user: User,
        colAdventures: MongoCollection<Adventure>
    ): Boolean

    fun removePassenger(colAdventures: MongoCollection<Adventure>,id: Int,user:User)
    fun editAdventure(choseAdventure:Adventure,location: String,date: String,plan: String,vehicle: String,numOfPass: String)


    fun filterByLocation(advList: List<Adventure>)

    fun search(searchLocation:String,list:List<Adventure>): ArrayList<Adventure>


}