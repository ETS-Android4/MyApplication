package com.example.william.my.module.sample.viewmodel

import androidx.lifecycle.*
import com.example.william.my.module.sample.repo.FlowRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map

class LiveDataViewModel(private val dataSource: FlowRepository) : ViewModel() {

    /**
     * 使用 Flow 流构造方法 -> asLiveData()
     */
    fun loginByFLow(username: String, password: String): LiveData<String> {
        return dataSource.loginByFlow(username, password)
            .map {
                Gson().toJson(it)
            }
            .asLiveData()//返回一个不可变的 LiveData
    }

    /**
     * 使用 Coroutine 协程构造方法 -> liveData{}
     */
    fun loginByCoroutine(username: String, password: String): LiveData<String> {
        return liveData {
            dataSource.loginByFlow(username, password)
                .collect {
                    emit(Gson().toJson(it))
                }
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [LiveDataViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {

    private val dataSource = FlowRepository(Dispatchers.IO)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LiveDataViewModel(dataSource) as T
    }
}
