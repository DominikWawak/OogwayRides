package org.oogwayrides.console.main//package org.oogwayrides.console.main
import javafx.scene.control.DatePicker
import javafx.scene.paint.Color
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import org.litote.kmongo.contains
import org.litote.kmongo.eq
import org.litote.kmongo.json
import org.litote.kmongo.updateOne
import org.oogwayrides.console.controllers.memStore
import org.oogwayrides.console.controllers.user
import org.oogwayrides.console.models.Adventure
import tornadofx.*
import java.io.File
import java.time.LocalDate



var adventures = colAdventures.find().toList().asObservable()

class LogIn : View() {
    override val root = gridpane() {

        var logIn: TextField? = null
        row {
            form {
                fieldset("Log In") {
                    field("Name") {
                        logIn = textfield()
                    }

                }

            }
        }
        row {
            button("LogIn") {
                action {
                    if (logIn?.let { controller.logIn(it.text) } == true)
                        replaceWith<MainView>()
                }
                style{
                    backgroundColor = multi(Color.RED, Color.BLUE, Color.CYAN)
                }
            }

            button("Register") {
                action {

                    replaceWith<Register>()
                }
            }
        }



        setPrefSize(900.0, 360.0)

       style{
           backgroundImage+= File("src/tfxbgog.png").toURI()


       }
    }



}


class MainView : View() {
    override val root = gridpane() {


        var searchT: TextField? = null

        var chosenAdventure: Adventure? = null

        row {
            hbox {
                user?.let {
                    label("    user: " + it.name)
                }
                button("Log Out") {
                    action {
                        replaceWith<LogIn>()
                    }

                }


                form {
                    fieldset("Search") {
                        field("") {
                            searchT = textfield()
                        }
                        button("Search") {
                            action {

                                adventures.setAll(searchT?.let {
                                    memStore.search(it.text, colAdventures.find().toList()).toList().asObservable()
                                }!!)

                            }
                        }

                    }


                }
                button("Refresh") {
                    action {

                        adventures.setAll(colAdventures.find().toList().asObservable())

                    }
                }

            }
        }
        row {


            tableview(adventures) {
                readonlyColumn("ID", Adventure::_id)
                // readonlyColumn("organizer", Adventure::organizer.name)
                readonlyColumn("Location", Adventure::locaton)
                readonlyColumn("Date", Adventure::date)
                readonlyColumn("plan", Adventure::plan)
                readonlyColumn("Space Left", Adventure::numOfPass)

                useMaxWidth = true
                //                         gridpaneConstraints {
//                             marginTop = 10.0
//                             columnSpan = 20
//
//                         }


                onUserSelect { adventure ->
                    chosenAdventure = adventure
                    println(adventure._id)
//                    adventure.locaton?.let {
//                        adventure.plan?.let { it1 ->
//                            adventure.date?.let { it2 ->
//                                adventure.vehicle?.let { it3 ->
//                                    populateTextfields(
//                                        it,
//                                        it1, it2, it3, adventure.numOfPass
//                                    )
//                                }
//                            }
//                        }
//                    }


                }
            }

        }

        row {


            button("Join") {
                action {
                    user?.let { chosenAdventure?.let { it1 -> memStore.addPassenger(it1, it, colAdventures) } }

                }
            }
            button("My Adventures") {
                action {
                    replaceWith<UserView>()

                }
            }
        }

    }
}


class UserView : View() {

    override val root = gridpane() {

        var dateT: DatePicker? = null
        var locationT: TextField? = null
        var planT: TextField? = null
        var vehicleT: TextField? = null
        var numofPassT: TextField? = null
        var chosenAdventure: Adventure? = null

        adventures.setAll(colAdventures.find(Adventure::organizer eq user).toList().asObservable())

hbox {
    user?.let {
        label("    user: " + it.name)

    }
    button("Go Back") {
        action {
            replaceWith<MainView>()
            adventures.setAll(colAdventures.find().toList().asObservable())
        }

    }
    radiobutton("Include signed up to events") {
        action {
            if(isSelected){
                adventures.setAll(colAdventures.find(Adventure::passangers.contains(user)).toList().asObservable())
            }
            else{
                adventures.setAll(colAdventures.find(Adventure::organizer eq user).toList().asObservable())
            }
        }
    }
    spacing=5.0

}
row{}
        row{


            tableview(adventures) {

                readonlyColumn("ID", Adventure::_id)
                // readonlyColumn("organizer", Adventure::organizer.name)
                readonlyColumn("Location", Adventure::locaton)
                readonlyColumn("Date", Adventure::date)
                readonlyColumn("plan", Adventure::plan)
                readonlyColumn("Space Left", Adventure::numOfPass)
                readonlyColumn("passengers",Adventure::passangers)
                useMaxWidth = true
                //                         gridpaneConstraints {
//                             marginTop = 10.0
//                             columnSpan = 20
//
//                         }
                fun populateTextfields(
                    location: String,
                    plan: String,
                    date: String,
                    vehicle: String,
                    numOfPass: Int
                ) {
                    locationT?.text = location
                    planT?.text = plan
                    dateT?.value = LocalDate.parse(date);
                    vehicleT?.text = vehicle
                    numofPassT?.text = numOfPass.toString()


                }

                onUserSelect { adventure ->
                    chosenAdventure = adventure
                    println(adventure._id)
                    adventure.locaton?.let {
                        adventure.plan?.let { it1 ->
                            adventure.date?.let { it2 ->
                                adventure.vehicle?.let { it3 ->
                                    populateTextfields(
                                        it,
                                        it1, it2, it3, adventure.numOfPass
                                    )
                                }
                            }
                        }
                    }


                }
            }

        }

        row {


            form {
                hbox(20) {
                    fieldset("Add/update") {
                        hbox(20) {
                            vbox {
                                field("Location") { locationT = textfield() }
                                field("Date") { dateT = datepicker() }
                            }
                            vbox {
                                field("plan") { planT = textfield() }
                                field("vehicle") { vehicleT = textfield() }
                            }
                            vbox {
                                field("Number of Passengers") { numofPassT = textfield() }
                                hbox {
                                    button("Add") {
                                        action {
                                            locationT?.let {
                                                dateT?.let { it1 ->
                                                    vehicleT?.let { it2 ->
                                                        numofPassT?.let { it3 ->
                                                            planT?.let { it4 ->
                                                                memStore.addAdventure(
                                                                    (0..10000).random(),
                                                                    it.text,
                                                                    it1.value.toString(),
                                                                    it4.text,
                                                                    it2.text,
                                                                    it3.text,
                                                                    colAdventures
                                                                )

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            adventures.setAll(colAdventures.find(Adventure::organizer eq user).toList().asObservable())
                                        }
                                    }
                                    button("Update") {
                                        action {
                                            chosenAdventure?.let { locationT?.let { it1 ->
                                                planT?.let { it2 ->
                                                    vehicleT?.let { it3 ->
                                                        numofPassT?.let { it4 ->
                                                            memStore.editAdventure(it,
                                                                it1.text,
                                                                dateT?.value.toString(), it2.text, it3.text, it4.text,
                                                                colAdventures)
                                                        }
                                                    }
                                                }
                                            } }

                                            adventures.setAll(colAdventures.find(Adventure::organizer eq user).toList().asObservable())
                                        }

                                    }
                                    button("Delete") {
                                        action {
                                            chosenAdventure?.let { memStore.deleteAdventure(colAdventures, it._id) }
                                            // Refresh table view
                                            adventures.remove(chosenAdventure)

                                        }
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }

    }
}

class Register : View() {
    override val root = gridpane() {
        var name: TextField? = null
        var bio: TextArea? = null
        row {
            form {
                fieldset("Register") {
                    field("Name") {
                        name = textfield()
                    }
                    field("Bio") {
                        bio = textarea()
                    }


                }
            }
        }

        row {
            button("Back to LogIn") {
                action {
                    replaceWith<LogIn>()
                }
            }
            button("Register") {
                action {
                    name?.let { bio?.let { it1 -> controller.addUser(it.text, it1.text) } }
                    replaceWith<LogIn>()
                }
            }
        }


    }
}


class MainUI : App(LogIn::class)

fun main(args: Array<String>) {
    launch<MainUI>(args)
}