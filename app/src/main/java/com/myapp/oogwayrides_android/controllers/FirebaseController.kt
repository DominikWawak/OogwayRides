package com.myapp.oogwayrides_android.controllers

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.Fraction
import com.myapp.oogwayrides_android.models.Adventure
import com.myapp.oogwayrides_android.models.User


val db = Firebase.firestore
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
                if (document != null) {
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


    fun getAdventures(){
        var adventures = arrayListOf<Adventure>()
        db.collection("adventures")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

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
}