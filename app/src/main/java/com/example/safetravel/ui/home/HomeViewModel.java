package com.example.safetravel.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> textLiveData;

    public HomeViewModel() {
        textLiveData = new MutableLiveData<>();
        textLiveData.setValue("Default Text");
    }

    public LiveData<String> getText() {
        return textLiveData;
    }

    public void updateText(String newText) {
        textLiveData.setValue(newText);
    }
}
