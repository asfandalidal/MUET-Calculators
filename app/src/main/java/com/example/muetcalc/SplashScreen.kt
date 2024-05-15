package com.example.muetcalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    private val SPLASH_DURATION: Long = 3000 // Splash screen duration in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Delayed execution to show the splash screen for a specific duration
        Handler().postDelayed({
            // Start your main activity after the splash screen duration
            startActivity(Intent(this@SplashScreen, MainActivity2::class.java))
            finish() // Close the splash activity to prevent going back to it using the back button
        }, SPLASH_DURATION)
    }
}