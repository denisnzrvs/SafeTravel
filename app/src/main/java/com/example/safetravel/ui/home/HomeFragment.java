package com.example.safetravel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
    private RelativeLayout dialogContainer;
    private EditText dialogFolderNameEditText;
    private Button dialogSaveButton;
    private Button dialogCancelButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button addButton = binding.addButton;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateFolderDialog().show(getChildFragmentManager(), "CreateFolderDialog");
            }
        });

        return root;
    }

    private void toggleDialogVisibility(boolean show) {
        if (show) {
            dialogContainer.setVisibility(View.VISIBLE);
        } else {
            dialogContainer.setVisibility(View.GONE);
            dialogFolderNameEditText.setText(""); // Clear the input field
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}