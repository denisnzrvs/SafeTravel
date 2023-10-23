package com.example.safetravel.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.safetravel.FileHandler;

import java.io.File;

public class CreateCategory extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Create a documents category");

        // Create the EditText and set a hint
        EditText dialogFolderNameEditText = new EditText(requireContext());
        dialogFolderNameEditText.setHint("Name your category"); // Set the hint text

        builder.setView(dialogFolderNameEditText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String folderName = dialogFolderNameEditText.getText().toString();
                        if (!folderName.isEmpty()) {
                            FileHandler fh = new FileHandler(getContext());
                            File tripFolder = new File(fh.getCurrentTripDir(), folderName);
                            if (!tripFolder.exists() && tripFolder.mkdirs()) {
                                Toast.makeText(requireContext(), "Category created: " + folderName, Toast.LENGTH_SHORT).show();

                                // Restart the activity
                                Intent intent = requireActivity().getIntent();
                                requireActivity().finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "Folder creation failed or folder already exists.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Please enter a category name.", Toast.LENGTH_SHORT).show();
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
