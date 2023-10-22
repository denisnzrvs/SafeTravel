package com.example.safetravel;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.safetravel.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        checkProfile();
    }

    void checkProfile() {
        File internalDir = getFilesDir();
        File profileJSON = new File(internalDir, "profile.json");

        Button createProfile = findViewById(R.id.createProfileButton);
        View fragmentContainer = findViewById(R.id.textHome); // The container for your fragment
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if (!profileJSON.exists()) {
            // Profile doesn't exist, show the button and hide navigation
            fragmentContainer.setVisibility(View.GONE);
            navView.setVisibility(View.GONE);
            createProfile.setVisibility(View.VISIBLE);

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
            navView.setVisibility(View.VISIBLE);

        }
    }
}
