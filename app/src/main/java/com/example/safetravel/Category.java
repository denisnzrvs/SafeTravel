package com.example.safetravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        RecyclerView recyclerView = findViewById(R.id.categoryFIlesList);

        FileHandler fh = new FileHandler(this);



File tripDir = fh.getCurrentTripDir();
File categoryDir = new File(tripDir, getIntent().getStringExtra("folderName"));

ArrayList<Category_File> files = new ArrayList<Category_File>();

for(File f:categoryDir.listFiles()){
    files.add(new Category_File(f.getName(), f.getName()));
    Toast.makeText(this, f.getName(), Toast.LENGTH_SHORT);
}
Category_RecyclerViewAdapter adapter = new Category_RecyclerViewAdapter(this, files);
recyclerView.setAdapter(adapter);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}