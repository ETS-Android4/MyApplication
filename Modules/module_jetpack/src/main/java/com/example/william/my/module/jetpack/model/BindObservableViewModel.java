package com.example.william.my.module.jetpack.model;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.william.my.module.jetpack.BR;

public class BindObservableViewModel extends BaseObservable {

    private final ObservableField<Integer> likes;

    public BindObservableViewModel() {
        likes = new ObservableField<>(0);
    }

    @Bindable
    public String getLikes() {
        return String.valueOf(likes.get());
    }

    public void onLike() {
        likes.set(likes.get() + 1);
        // You control when the @Bindable properties are updated using `notifyPropertyChanged()`.
        notifyPropertyChanged(BR.likes);
    }
}
