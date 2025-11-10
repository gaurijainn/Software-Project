//package com.example.softwareproject
//
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.softwareproject.databinding.ActivityMainBinding
//import com.google.android.material.navigation.NavigationBarView
//import java.util.concurrent.TimeUnit
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//    private val totalDays = 57L // total days until deadline
//    private val deadlineMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(totalDays)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupCountdown()
//        setupBottomNavigation()
//    }
//
//    /** Countdown logic for registration progress **/
//    private fun setupCountdown() {
//        val millisLeft = deadlineMillis - System.currentTimeMillis()
//
//        object : CountDownTimer(millisLeft, TimeUnit.DAYS.toMillis(1)) {
//            override fun onTick(millisUntilFinished: Long) {
//                val daysRemaining = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
//                val progressPercent =
//                    ((daysRemaining.toFloat() / totalDays) * 100).toInt().coerceAtLeast(0)
//
//                binding.progressBar.progress = progressPercent
//                binding.tvDaysRemaining.text = "$daysRemaining days remaining"
//            }
//
//            override fun onFinish() {
//                binding.progressBar.progress = 0
//                binding.tvDaysRemaining.text = "Registration Closed"
//            }
//        }.start()
//    }
//
//    /** Bottom Navigation setup **/
//    private fun setupBottomNavigation() {
//        binding.bottomNavigationView.setOnItemSelectedListener(
//            NavigationBarView.OnItemSelectedListener { item ->
//                when (item.itemId) {
//                    R.id.nav_home -> {
//                        Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.nav_events -> {
//                        Toast.makeText(this, "Events clicked", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.nav_profile -> {
//                        Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    else -> false
//                }
//            })
//    }
//}

package com.example.softwareproject

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.softwareproject.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val totalDays = 54L // total days until deadline
    private val deadlineMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(totalDays)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCountdown()
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
}
