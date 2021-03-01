package com.example.william.my.module.jetpack.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class LiveDataViewModel extends ViewModel {

    private final MutableLiveData<Integer> _likes;

    private final LiveData<String> likes;

    public LiveDataViewModel() {
        this._likes = new MutableLiveData<>(0);
        this.likes = Transformations.map(_likes, new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        });
    }

    public LiveData<String> getLikes() {
        return likes;
    }

    public void onLike() {
        _likes.setValue(_likes.getValue() + 1);
    }
}
