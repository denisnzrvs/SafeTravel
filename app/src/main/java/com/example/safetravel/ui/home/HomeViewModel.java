package com.example.safetravel.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> text;

    public HomeViewModel() {
        text = new MutableLiveData<>();
        text.setValue("Welcome to SafeTravel!");
    }

    public LiveData<String> getText() {
        return text;
    }

    public void updateText(String newText) {
        text.setValue(newText);
    }
}



