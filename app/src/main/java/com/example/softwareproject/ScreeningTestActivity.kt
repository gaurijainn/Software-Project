package com.example.softwareproject

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit
import kotlin.random.Random

data class MCQ(val id: Int, val question: String, val options: List<String>, val correctIndex: Int)

class ScreeningTestActivity : AppCompatActivity() {

    private val CORRECT_PASSCODE = "HACK2026"
    private val TEST_DURATION_SECONDS = 30 * 60 // 30 minutes

    private var timeRemainingSeconds = TEST_DURATION_SECONDS
    private var countDownTimer: CountDownTimer? = null

    // MCQs (same as your React data)
    private val mcqQuestions = listOf(
        MCQ(1, "What does AI stand for in the context of computer science?",
            listOf("Automated Intelligence","Artificial Intelligence","Advanced Integration","Algorithmic Implementation"), 1),
        MCQ(2, "Which of the following is a supervised learning algorithm?",
            listOf("K-Means Clustering","Linear Regression","DBSCAN","Principal Component Analysis"), 1),
        MCQ(3, "What is the time complexity of binary search?",
            listOf("O(n)","O(n²)","O(log n)","O(n log n)"), 2)
    )

    // runtime state
    private val answers = mutableMapOf<Int, Int>() // questionId -> selectedIndex
    private var uploadedFileUri: Uri? = null
    private var testSubmitted = false

    // Activity result for picking a file
    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadedFileUri = it
            // if current test screen present, update filename view
            val uploadedName = findViewById<TextView?>(R.id.uploadedFileName)
            uploadedName?.apply {
                text = uri.lastPathSegment ?: "selected_file"
                visibility = View.VISIBLE
            }
            updateProgressUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screening_test)

        // Start by showing passcode screen (function from previous message)
        showPasscodeScreen()
    }

    // ---------- PASSCODE SCREEN (already provided earlier) ----------
    private fun showPasscodeScreen() {
        val container = findViewById<FrameLayout>(R.id.screenContainer)
        container.removeAllViews()
        val view = layoutInflater.inflate(R.layout.screen_passcode, container, false)
        container.addView(view)

        val passInput = view.findViewById<EditText>(R.id.passcodeInput)
        val startBtn = view.findViewById<Button>(R.id.startTestBtn)
        val errorTxt = view.findViewById<TextView>(R.id.passcodeError)

        startBtn.setOnClickListener {
            if (passInput.text.toString() == CORRECT_PASSCODE) {
                errorTxt.visibility = View.GONE
                showTestScreen()
            } else {
                errorTxt.text = "Invalid passcode. Please try again."
                errorTxt.visibility = View.VISIBLE
            }
        }
    }

    // ---------- TEST SCREEN ----------
    private fun showTestScreen() {
        val container = findViewById<FrameLayout>(R.id.screenContainer)
        container.removeAllViews()
        val view = layoutInflater.inflate(R.layout.screen_test, container, false)
        container.addView(view)

        // Render MCQs
        renderMCQs(view)

        // File upload button
        val uploadBtn = view.findViewById<Button>(R.id.uploadFileBtn)
        val uploadedName = view.findViewById<TextView>(R.id.uploadedFileName)
        uploadedName.visibility = if (uploadedFileUri != null) View.VISIBLE else View.GONE
        uploadedName.text = uploadedFileUri?.lastPathSegment ?: ""

        uploadBtn.setOnClickListener {
            // open SAF file picker (allow text files)
            pickFileLauncher.launch("*/*")
        }

        // Submit button
        val submitBtn = view.findViewById<Button>(R.id.submitTestBtn)
        submitBtn.setOnClickListener {
            submitTest()
        }

        // Start timer
        startTimer(view.findViewById(R.id.timerText))
        updateProgressUI()
    }

    private fun renderMCQs(root: View) {
        val mcqContainer = root.findViewById<LinearLayout>(R.id.mcqContainer)
        mcqContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)

        mcqQuestions.forEachIndexed { idx, mcq ->
            val card = inflater.inflate(R.layout.item_mcq_card, mcqContainer, false)
            val qTitle = card.findViewById<TextView>(R.id.itemQuestion)
            val radioGroup = card.findViewById<RadioGroup>(R.id.itemRadioGroup)

            qTitle.text = "Question ${idx + 1}: ${mcq.question}"
            radioGroup.removeAllViews()

            mcq.options.forEachIndexed { optIdx, optionText ->
                val rb = RadioButton(this)
                rb.id = View.generateViewId()
                rb.text = optionText
                rb.tag = optIdx
                rb.setPadding(8, 10, 8, 10)
                radioGroup.addView(rb)
            }

            // preselect if answer exists
            answers[mcq.id]?.let { selected ->
                val child = radioGroup.getChildAt(selected)
                if (child is RadioButton) child.isChecked = true
            }

            // handle change
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedIndex = radioGroup.indexOfChild(radioGroup.findViewById(checkedId))
                if (selectedIndex >= 0) {
                    answers[mcq.id] = selectedIndex
                    updateProgressUI()
                }
            }

            mcqContainer.addView(card)
        }
    }

    private fun updateProgressUI() {
        val answeredCount = answers.size + (if (uploadedFileUri != null) 1 else 0)
        val totalQuestions = mcqQuestions.size + 1
        val percent = ((answeredCount.toDouble() / totalQuestions) * 100).toInt()

        findViewById<TextView?>(R.id.progressInfo)?.text = "Questions Answered: $answeredCount/$totalQuestions"
        findViewById<ProgressBar?>(R.id.progressBar)?.progress = percent
    }

    // ---------- TIMER ----------
    private fun startTimer(timerTextView: TextView) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(timeRemainingSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemainingSeconds = (millisUntilFinished / 1000).toInt()
                timerTextView.text = formatTime(timeRemainingSeconds)
            }

            override fun onFinish() {
                timeRemainingSeconds = 0
                timerTextView.text = formatTime(0)
                // auto submit
                if (!testSubmitted) submitTest()
            }
        }
        countDownTimer?.start()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
    }

    private fun formatTime(seconds: Int): String {
        val m = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val s = seconds - (m * 60)
        return String.format("%02d:%02d", m, s)
    }

    // ---------- SUBMIT & RESULTS ----------
    private fun submitTest() {
        // prevent multiple submits
        if (testSubmitted) return
        testSubmitted = true
        stopTimer()

        // calculate MCQ score
        var mcqScore = 0
        mcqQuestions.forEach { q ->
            val ans = answers[q.id]
            if (ans != null && ans == q.correctIndex) mcqScore += 1
        }

        // coding score: mimic React behavior (3-4 if file present, else 0)
        val codingScore = if (uploadedFileUri != null) Random.nextInt(4, 5) else 0

        val totalScore = mcqScore + codingScore

        // show results view
        showResults(totalScore, mcqScore, codingScore)
    }

    private fun showResults(totalScore: Int, mcqScore: Int, codingScore: Int) {
        val container = findViewById<FrameLayout>(R.id.screenContainer)
        container.removeAllViews()
        val view = layoutInflater.inflate(R.layout.screen_results, container, false)
        container.addView(view)

        val scoreBig = view.findViewById<TextView>(R.id.scoreBig)
        val scoreText = view.findViewById<TextView>(R.id.scoreText)
        val scoreProgress = view.findViewById<ProgressBar>(R.id.scoreProgress)
        val mcqBadge = view.findViewById<TextView>(R.id.mcqBadge)
        val codingBadge = view.findViewById<TextView>(R.id.codingBadge)
        val timeTaken = view.findViewById<TextView>(R.id.timeTaken)
        val closeBtn = view.findViewById<Button>(R.id.closeBtn)

        val maxPoints = mcqQuestions.size + 4 // as per React: mcqQuestions + (max coding 4)
        scoreBig.text = totalScore.toString()
        scoreText.text = "You scored $totalScore out of $maxPoints points"
        // --- Show acceptance message if full score ---
        val acceptanceText = view.findViewById<TextView>(R.id.acceptanceMessage)

        if (totalScore == maxPoints) {
            acceptanceText.visibility = View.VISIBLE
            acceptanceText.text = "🎉 Accepted for the Next Round!\nYour next-round passcode is: XO2026"
        } else {
            acceptanceText.visibility = View.VISIBLE
            acceptanceText.text = "❌ Not selected for the next round"
        }


        val percent = ((totalScore.toDouble() / maxPoints) * 100).toInt()
        scoreProgress.progress = percent

        mcqBadge.text = "$mcqScore/${mcqQuestions.size}"
        codingBadge.text = if (uploadedFileUri != null) "Submitted" else "Not Submitted"

        val taken = TEST_DURATION_SECONDS - timeRemainingSeconds
        timeTaken.text = formatTime(taken)

        closeBtn.setOnClickListener {
            // reset everything and finish activity
            resetState()
            finish()
        }
    }

    private fun resetState() {
        stopTimer()
        answers.clear()
        uploadedFileUri = null
        testSubmitted = false
        timeRemainingSeconds = TEST_DURATION_SECONDS
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }
}
