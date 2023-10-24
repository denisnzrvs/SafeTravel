package com.example.safetravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CreateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Button btnUserDataDone = findViewById(R.id.btn_user_data_done);
        btnUserDataDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfileJSON();
            }
        });


    }

    void createProfileJSON() {
        File externalDocumentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File profileJSON = new File(externalDocumentsDir, "profile.json");
        JSONObject buffer = new JSONObject();
        try {
            profileJSON.createNewFile();
        } catch (IOException e) {
            Toast.makeText(this, "Error: Could not create profile file.", Toast.LENGTH_SHORT).show();
        }

        EditText userNameInput = findViewById(R.id.user_name_input);
        EditText emailAddressInput = findViewById(R.id.email_address_input);
        EditText phoneNumberInput = findViewById(R.id.phone_number_input);

        String userName = userNameInput.getText().toString();
        String emailAddress = emailAddressInput.getText().toString();
        String phoneNumber = phoneNumberInput.getText().toString();

        try {
            buffer.put("name", userName);
            buffer.put("email", emailAddress);
            buffer.put("phone", phoneNumber);
            FileWriter f = new FileWriter(profileJSON);
            f.write(buffer.toString());
            f.close();
            Toast.makeText(this, "Profile created!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Error: Could not create profile file.", Toast.LENGTH_SHORT).show();
        }
    }
}