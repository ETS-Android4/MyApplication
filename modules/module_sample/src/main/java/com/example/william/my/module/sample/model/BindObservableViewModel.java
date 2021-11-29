package com.example.william.my.module.sample.model;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.william.my.module.sample.BR;

/**
 * Observable
 */
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
        Integer like = likes.get();
        if (like != null) {
            likes.set(like + 1);
            // You control when the @Bindable properties are updated using `notifyPropertyChanged()`.
            notifyPropertyChanged(BR.likes);
        }
    }
}
