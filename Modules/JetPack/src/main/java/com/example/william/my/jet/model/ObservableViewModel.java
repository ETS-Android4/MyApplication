package com.example.william.my.jet.model;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.william.my.jet.BR;

public class ObservableViewModel extends BaseObservable {

    private final ObservableField<Integer> likes;

    public ObservableViewModel() {
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
