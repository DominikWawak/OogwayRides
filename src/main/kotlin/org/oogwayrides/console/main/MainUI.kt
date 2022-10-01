package org.oogwayrides.console.main//package org.oogwayrides.console.main
import javafx.collections.ObservableArray
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import org.oogwayrides.console.controllers.memStore
import org.oogwayrides.console.controllers.user
import org.oogwayrides.console.models.Adventure
import tornadofx.*


var  adventures = colAdventures.find().toList().asObservable()
class LogIn : View() {
        override val root = gridpane() {
            var logIn: TextField? = null
            row {
                form {
                    fieldset("Log In") {
                         field("Name") {
                            logIn =textfield()
                        }
//                    field("Birthday") {
//                        datepicker()
//                    }
                    }

                }
            }
             row {
                 button("LogIn") {
                     action {
                            if(logIn?.let { controller.logIn(it.text) } == true)
                              replaceWith<MainView>()
                     }
                 }

                 button("Register") {
                     action {

                             replaceWith<Register>()
                     }
                 }
             }



                setPrefSize(900.0, 360.0)
            }
        }




    class MainView : View() {
        override val root = gridpane() {


            var search: TextField? = null
            row {
                user?.let { label("    user: "+it.name)
                    }
                button("Search") {
                    action {

                        adventures.setAll(search?.let { memStore.search(it.text,colAdventures.find().toList()).toList().asObservable() }!!)

                    }
                }

                form {
                fieldset("Search") {
                    field("") {
                        search = textfield()
                    }
                }


                }
                button("Log Out") {
                    action {
                        replaceWith<LogIn>()
                    }

                }
            }
                row {



                     tableview(adventures) {
                        readonlyColumn("ID",Adventure::_id)
                       // readonlyColumn("organizer", Adventure::organizer.name)
                        readonlyColumn("Location", Adventure::locaton)
                        readonlyColumn("Date",Adventure::date)
                         readonlyColumn("plan",Adventure::plan)
                         readonlyColumn("Space Left",Adventure::numOfPass)
                         useMaxWidth = true
                         gridpaneConstraints {
                             marginTop = 10.0
                             columnSpan = 20

                         }

                         onUserSelect {
                             adventure ->  println(adventure._id)
                         }
                    }

                }

            row {


                form {
                    hbox(20) {
                        fieldset("Add/update") {
                            hbox(20) {
                                vbox {
                                    field("Field l1a") { textfield() }
                                    field("Field l2a") { textfield() }
                                }
                                vbox {
                                    field("Field l1b") { textfield() }
                                    field("Field l2b") { textfield() }
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
                          bio=  textarea()
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