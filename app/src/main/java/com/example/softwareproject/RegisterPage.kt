package com.example.softwareproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        // ✅ GOOGLE FORM BUTTON CLICK
        val registerBtn = findViewById<Button>(R.id.registerButton)
        registerBtn.setOnClickListener {
            val url = "https://docs.google.com/forms/d/e/1FAIpQLSeg15NcERxNGxaHmXf3yPLGmBKkDz0izFII3IQbfVnIfXtbjg/viewform"
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
            startActivity(intent)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_register

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_timeline -> {
                    startActivity(Intent(this, ActivityTimeline::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_prizes -> {
                    startActivity(Intent(this, PrizesActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_register -> true
                else -> false
            }
        }
    }
}
