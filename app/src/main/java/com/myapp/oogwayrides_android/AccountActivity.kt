package com.myapp.oogwayrides_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myapp.oogwayrides_android.databinding.ActivityAccountBinding
import com.myapp.oogwayrides_android.databinding.AdvItemBinding

/**
 * Account Activity
 *
 * Used to dsplay the current logged in user and display their info as well as
 * log out and delete functionality.
 */
class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        binding=ActivityAccountBinding.inflate(layoutInflater)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
        var nameField = findViewById<TextView>(R.id.accName)
        var emailField = findViewById<TextView>(R.id.accMail)
        var pic = findViewById<ImageView>(R.id.accPic)
        var logOutButton = findViewById<Button>(R.id.accLogOut)


        var name: String? = intent.getStringExtra("userName")
        emailField.text=intent.getStringExtra("userEmail")

        //https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
        Glide.with(this).load(intent.getStringExtra("userPic")).into(pic);

        if (name != null) {
            Log.d("namena", name)
           nameField.text=name
        }

        logOutButton.setOnClickListener{
            auth.signOut();
            // Google sign out
            googleSignInClient.signOut().addOnCompleteListener(this) {
                if(it.isComplete){
                    val intent = Intent(this@AccountActivity, LogInActivity::class.java)
                    startActivity(intent)
                }

            };
        }

//        intent.getStringExtra("userName")?.let { Log.d("extra", it) }
    }


}
