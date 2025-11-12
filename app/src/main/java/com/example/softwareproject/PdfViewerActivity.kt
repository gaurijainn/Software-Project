package com.example.softwareproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView


class PdfViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfView = findViewById<PDFView>(R.id.pdfView)
        pdfView.fromAsset("problem_statement.pdf")
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .load()
    }
}
