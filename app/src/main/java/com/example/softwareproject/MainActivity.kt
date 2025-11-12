package com.example.softwareproject

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.softwareproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val totalDays = 54L // total days until deadline
    private val deadlineMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(totalDays)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start countdown
        startCountdown()

        // Setup bottom navigation
        setupBottomNavigation()

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }

    private fun startCountdown() {
        val millisLeft = deadlineMillis - System.currentTimeMillis()

        object : CountDownTimer(millisLeft, TimeUnit.DAYS.toMillis(1)) {
            override fun onTick(millisUntilFinished: Long) {
                val daysRemaining = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                val progressPercent = ((daysRemaining.toFloat() / totalDays) * 100).toInt()
                binding.progressBar.progress = progressPercent
                binding.tvDaysRemaining.text = "$daysRemaining days remaining"
            }

            override fun onFinish() {
                binding.progressBar.progress = 0
                binding.tvDaysRemaining.text = "Registration Closed"
            }
        }.start()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_home  // highlight home icon

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true // already on MainActivity
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
                R.id.nav_register -> {
                    startActivity(Intent(this, RegisterPage::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}
