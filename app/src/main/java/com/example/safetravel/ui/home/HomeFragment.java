package com.example.safetravel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.safetravel.databinding.FragmentHomeBinding;
import com.example.safetravel.FileHandler;

import java.io.File;
import java.io.FileFilter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button addButton = binding.addButton;

        FileHandler fh = new FileHandler(getContext());
        String currentTrip = fh.getCurrentTripName();
        File tripFolder = fh.getCurrentTripDir();
        if (currentTrip != null) {
            addButton.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            TextView countryTitle = binding.countryTitle;
            countryTitle.setVisibility(View.VISIBLE);
            countryTitle.setText(currentTrip);
            LinearLayout folders = binding.foldersList;


                File[] foldersList = tripFolder.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                });

            int count = 1;
            LinearLayout currentColumnLayout = null; // Initialize the current column layout
            for (File folder : foldersList) {
                if (count % 3 == 1) {
                    // Create a new vertical LinearLayout for a new column
                    currentColumnLayout = new LinearLayout(getContext());
                    currentColumnLayout.setOrientation(LinearLayout.VERTICAL);
                    folders.addView(currentColumnLayout); // Add the column layout to the parent HorizontalScrollView
                }

                // Create a new button for the folder and add it to the current column layout
                Button button = new Button(getContext());
                button.setText(folder.getName());
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Match the width of the column
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                currentColumnLayout.addView(button); // Add the button to the current column layout
                count++;
            }
                Button addFolder = new Button(getContext());
                addFolder.setText("Add Folder");
                addFolder.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                addFolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CreateCategory().show(getChildFragmentManager(), "CreateCategoryDialog");
                    }
                });
                folders.addView(addFolder);

        } else {
            Toast.makeText(getContext(), "No current trip", Toast.LENGTH_LONG).show();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateTripFolder().show(getChildFragmentManager(), "CreateFolderDialog");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
