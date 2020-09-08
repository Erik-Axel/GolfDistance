package com.example.navigationmap.ui.clubselector;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClubSelecorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClubSelecorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}