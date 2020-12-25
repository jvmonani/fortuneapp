package com.mydomain.yyjp.myfortuneapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {
    private final Context applicationContext;

    public MainActivityViewModelFactory(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(applicationContext);
    }
}
