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
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.safetravel.R;
import com.example.safetravel.databinding.FragmentHomeBinding;
import com.example.safetravel.FileHandler;

import java.io.File;
import java.util.Objects;

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
        String currentTrip = fh.getCurrentTrip();
        if (currentTrip != null) {
            addButton.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            TextView countryTitle = binding.countryTitle;
            countryTitle.setVisibility(View.VISIBLE);
            countryTitle.setText(currentTrip);
        } else {
            Toast.makeText(getContext(), "No current trip", Toast.LENGTH_LONG).show();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateFolderDialog().show(getChildFragmentManager(), "CreateFolderDialog");
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
