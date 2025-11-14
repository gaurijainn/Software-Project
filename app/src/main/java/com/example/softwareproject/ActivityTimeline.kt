package com.example.softwareproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import kotlin.toString

class ActivityTimeline : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        val btnProblemStatement = findViewById<MaterialButton>(R.id.btn_problem_statement)
        btnProblemStatement.setOnClickListener {
            val intent = Intent(this, PdfViewerActivity::class.java)
            startActivity(intent)
        }

        // Attempt Screening Test
        val btnAttemptTest = findViewById<MaterialButton>(R.id.btn_attempt_test)
        btnAttemptTest.setOnClickListener {
            startActivity(Intent(this, ScreeningTestActivity::class.java))
        }

        // Submit Solution (Passcode + PDF Upload)
        val btnSubmitSolution = findViewById<MaterialButton>(R.id.btn_submit_solution)
        btnSubmitSolution.setOnClickListener {
            showPasscodeDialog()
        }

        val btnViewLocation = findViewById<MaterialButton>(R.id.btn_view_location)

        btnViewLocation.setOnClickListener {
            val url = "https://www.google.com/maps/place/National+Institute+of+Technology,+Silchar/@24.758427,92.794378,16z/"
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
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

    private fun showPasscodeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_passcode, null)
        val etPasscode = dialogView.findViewById<android.widget.EditText>(R.id.et_passcode)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Submit") { dlg, _ ->

                val passcode = etPasscode.text.toString().trim()

                if (passcode == "XO2026") {
                    dlg.dismiss()
                    pickFileForUpload()
                } else {
                    etPasscode.error = "Incorrect Passcode"
                }
            }
            .setNegativeButton("Cancel") { dlg, _ ->
                dlg.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun pickFileForUpload() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf"))
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            val fileUri = data?.data

            if (fileUri != null) {
                // TODO: Upload to server if needed
                showSuccessMessage()
            }
        }
    }

    private fun showSuccessMessage() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Submission Successful")
            .setMessage("Your PPT has been submitted successfully.\n\nPlease wait for results, which will be sent to your Team Leader’s email.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


}



