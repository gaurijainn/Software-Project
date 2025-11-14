package com.example.softwareproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val logoIcon = findViewById<ImageView>(R.id.logoIcon)
        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
        logoIcon.startAnimation(bounceAnim)

        val root = findViewById<RelativeLayout>(R.id.centerContainer)

        // Fade-out animation
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 500
        fadeOut.startOffset = 2500 // starts after 2.5s

        root.startAnimation(fadeOut)

        // Move to main screen after fade
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}
