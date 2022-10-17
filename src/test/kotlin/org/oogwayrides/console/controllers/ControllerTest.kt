package org.oogwayrides.console.controllers

import com.mongodb.client.FindIterable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.litote.kmongo.*
import org.oogwayrides.console.main.colAdventures
import org.oogwayrides.console.main.colAdventures_t
import org.oogwayrides.console.main.colUsers_t
import org.oogwayrides.console.models.Adventure
import org.oogwayrides.console.models.User

internal class ControllerTest {

    val user1 = User("Dominik","Hi, Im open to Adventures","url....")
    val user2 = User("Joe","Hi, Im looking to hitchhike","url....")
    var adv1 = Adventure(111,0,user1,"van","20/02/22","Waterford","go surfing")
    var adv2 = Adventure(112,0,user1,"van","20/02/22","France","go surfing")
    var adv2_same_id = Adventure(111,4,user1,"car","20/02/22","Madrid","City Tour")
    var list :List<Adventure> = arrayListOf(adv1,adv2)
    @BeforeEach
    fun setUp() {



    }

    @AfterEach
    fun tearDown() {
        colAdventures_t.drop()
        colUsers_t.drop()

    }

    @Test
    fun addAdventure() {
        // different id
        assertTrue(memStore.addAdventure(adv1, colAdventures_t))
        // cant be added because id is the same
        assertFalse( memStore.addAdventure(adv2_same_id, colAdventures_t))

    }

    @Test
    fun editAdventure() {
        memStore.addAdventure(adv1, colAdventures_t)
        memStore.editAdventure(adv1,"Palma","2023-01-01","Go party","plane","3", colAdventures_t)

        assertEquals("Palma", colAdventures_t.findOne(Adventure::locaton eq "Palma")?.locaton)

    }


    @Test
    fun addPassenger() {

        // check that passanger cant be added when no seats are available
        memStore.addAdventure(adv1, colAdventures_t)
        assertFalse(memStore.addPassenger(adv1,user1, colAdventures_t)) // No passengers left

        // check that passenger gets added
        memStore.addAdventure(adv2_same_id, colAdventures_t)
        assertTrue(memStore.addPassenger(adv2_same_id,user2, colAdventures_t))
        assertEquals(user2,adv2_same_id.passangers[0])

        //check that the size got decrimented
        var adv = colAdventures_t.findOne(Adventure::_id eq adv2_same_id._id)
        if (adv != null) {
            assertEquals(3,adv.numOfPass)
        }
    }



    @Test
    fun deleteAdventure() {
        //insert into db
        colAdventures_t.insertOne(adv1)
        colAdventures_t.insertOne(adv2)
        var drivingToList : List<Adventure> = colAdventures_t.find(Adventure::organizer eq user1).toList()

        //Assert before delete
        assertEquals(2,drivingToList.size)
        memStore.deleteAdventure(drivingToList,1, colAdventures_t)
        // delete and assert it got deleted
        drivingToList = colAdventures_t.find(Adventure::organizer eq user1).toList()
        assertEquals(1,drivingToList.size)
    }

    @Test
    fun searchAdventures(){

        // Searching with a section of a string and caps
        assertEquals("France",memStore.search("FR", list).get(0).locaton)

    }



}