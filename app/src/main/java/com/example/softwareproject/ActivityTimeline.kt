package com.example.softwareproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class ActivityTimeline : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        val btnProblemStatement = findViewById<MaterialButton>(R.id.btn_problem_statement)
        btnProblemStatement.setOnClickListener {
            val intent = Intent(this, PdfViewerActivity::class.java)
            startActivity(intent)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_timeline

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_timeline -> true // current
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
