package com.example.safetravel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.safetravel.databinding.FragmentHomeBinding;

import java.io.File;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText folderNameEditText;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button addButton = binding.addButton;
        folderNameEditText = binding.folderNameEditText;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String folderName = folderNameEditText.getText().toString();

                if (!folderName.isEmpty()) {
                    createFolderAndFiles(folderName);
                } else {
                    Toast.makeText(requireContext(), "Please enter a folder name.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }

    private void createFolderAndFiles(String folderName) {
        // Create a new trip folder
        File tripFolder = new File(requireContext().getExternalFilesDir(null), folderName);

        if (!tripFolder.exists() && tripFolder.mkdirs()) {
            // Folder created successfully
            // You can also create and save files here if needed
            Toast.makeText(requireContext(), "Folder created: " + folderName, Toast.LENGTH_SHORT).show();
        } else {
            // Folder creation failed or folder already exists
            Toast.makeText(requireContext(), "Folder creation failed or folder already exists.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}