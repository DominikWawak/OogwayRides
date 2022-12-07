package com.myapp.oogwayrides_android.controllers

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myapp.oogwayrides_android.models.Adventure
import com.myapp.oogwayrides_android.models.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await


val db = Firebase.firestore

/**
 * Firebase Controller Class
 * Used for CRUD methods for firebase firestore.
 */
class FirebaseController {
    var TAG = "firebase controller"


    fun addAdventure(adventure: Adventure){


        db.collection("adventures")
            .add(adventure)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore add", "DocumentSnapshot added with ID: ${documentReference.id}")
                adventure.organizer?.let {
                    db.collection("users").document(it)
                        .update("tripsOrganised", FieldValue.arrayUnion(documentReference.id))
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore add ", "Error adding document", e)
            }









    }

    fun addUser(user: User,userUID:String){
        val docRef = db.collection("users").document(userUID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document, adding")
                    db.collection("users").document(userUID)
                        .set(user, SetOptions.merge())
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }



    }


     suspend fun getAdventures(): ArrayList<String> {
        var adventures = arrayListOf<String>()
        db.collection("adventures")
            .get()

            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    adventures.add(document.data["name"] as String)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


        return adventures

    }

    fun joinAdv(userUID:String,advID:String){


        db.collection("users").document(userUID)
            .update("tripsGoing",  FieldValue.arrayUnion(advID))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }


        db.collection("adventures").document(advID)
            .update("passangers",  FieldValue.arrayUnion(userUID))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

    }


    fun deleteAdv(userUID:String,advID:String){


        db.collection("users").document(userUID)
            .update("tripsOrganised",  FieldValue.arrayRemove(advID))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }


        db.collection("adventures").document(advID)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }


    fun newGetAdv(): ArrayList<Adventure> {
    var adventures = arrayListOf<Adventure>()
    db.collection("adventures")
        .addSnapshotListener { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            for (doc in value!!) {
                doc.data["name"]
                adventures.add(Adventure(doc.data["organizer"].toString(),
                    doc.data["location"] as ArrayList<String>,null,doc.data["name"].toString(),
                    doc.data["vehicle"] as String?, doc.data["date"] as String?,null, doc.data["plan"] as String?

                ))
//                doc.getString("name")?.let {
//                    adventures.add(it)
//                }
            }

            Log.d(TAG, "Current advs : $adventures")
        }


    return adventures

}


    suspend fun getUser(userId:String): Map<String, Any>? =coroutineScope{
        var foundUser: Map<String, Any>? =null
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    foundUser=document.data
                    Log.d(TAG, "getUser: "+document.data)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
            .await()
        return@coroutineScope foundUser
    }

}