package com.example.safetravel.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.io.File;

public class CreateTripFolder extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Create a trip!");

        // Create the EditText and set a hint
        EditText dialogFolderNameEditText = new EditText(requireContext());
        dialogFolderNameEditText.setHint("Name your trip"); // Set the hint text

        builder.setView(dialogFolderNameEditText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String folderName = dialogFolderNameEditText.getText().toString();
                        if (!folderName.isEmpty()) {
                            File tripFolder = new File(requireContext().getFilesDir(), folderName);
                            if (!tripFolder.exists() && tripFolder.mkdirs()) {
                                Toast.makeText(requireContext(), "Folder created: " + folderName, Toast.LENGTH_SHORT).show();

                                // Restart the activity
                                Intent intent = requireActivity().getIntent();
                                requireActivity().finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "Folder creation failed or folder already exists.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Please enter a folder name.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        // Create the AlertDialog object and return it.
        return builder.create();
    }
}
