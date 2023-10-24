package com.example.safetravel;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        File f = new File(getIntent().getStringExtra("file"));

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromUri(Uri.fromFile(f)).load();
    }
}