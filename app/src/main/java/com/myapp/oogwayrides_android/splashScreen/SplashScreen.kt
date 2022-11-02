package com.myapp.oogwayrides_android.splashScreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.myapp.oogwayrides_android.LogInActivity
import com.myapp.oogwayrides_android.MapsActivity
import com.myapp.oogwayrides_android.R


// youtube
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            //val intent = Intent(this@SplashScreen,MapsActivity::class.java)
            val intent = Intent(this@SplashScreen,LogInActivity::class.java)

            startActivity(intent)
            finish()
        },3000)
    }
}