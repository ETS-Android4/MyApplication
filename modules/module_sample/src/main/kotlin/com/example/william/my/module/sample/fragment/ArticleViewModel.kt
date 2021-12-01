package com.example.william.my.module.sample.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.william.my.bean.repo.DataRepository

class ArticleViewModel : ViewModel() {

    private val mMutableLiveData = MutableLiveData<Int>()

    val article = Transformations.switchMap(mMutableLiveData) { input ->
        DataRepository.getInstance().getArticle(input)
    }

    fun queryArticle(page: Int) {
        mMutableLiveData.postValue(page)
    }
}
