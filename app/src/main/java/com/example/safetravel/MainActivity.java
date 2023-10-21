package com.example.safetravel;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.safetravel.databinding.ActivityMainBinding;
import com.example.safetravel.ui.home.HomeFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkProfile();
    }

    void checkProfile() {
        File internalDir = getFilesDir();
        File profileJSON = new File(internalDir, "profile.json");

        Button createProfile = findViewById(R.id.createProfileButton);
        View fragmentContainer = findViewById(R.id.text_home); // The container for your fragment

        if (!profileJSON.exists()) {
            // Profile doesn't exist, show the button and hide the fragment container
            createProfile.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);
            Toast.makeText(this, "No profile found! Please, create a new one!", Toast.LENGTH_SHORT).show();
            //Navigate to CreateProfileActivity if profile doesn't exist.
            createProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, CreateProfile.class));
                }
            });
        } else {
            // Profile exists, hide the button and show the fragment container
            createProfile.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);
        }
    }
}
