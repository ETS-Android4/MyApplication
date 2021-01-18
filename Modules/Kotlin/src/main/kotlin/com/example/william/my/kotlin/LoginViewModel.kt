package com.example.william.my.kotlin

import androidx.lifecycle.*
import com.example.william.my.module.bean.LoginBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val mutableLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    var loginBean: LiveData<Result<Any>> = Transformations.map(mutableLiveData) { it }

    fun login(username: String, password: String) {

        // 创建一个新的协程，然后在 I/O 线程上执行网络请求
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.IO) {
            val jsonBody = "{ username: \"$username\", password: \"$password\"}"

            // Make the network call and suspend execution until it finishes
            val result = try {
                loginRepository.makeLoginRequest(jsonBody)
            } catch (e: Exception) {
                Result.Error(Exception("Network request failed"))
            }

            // 向用户展示网络请求结果
            // Display result of the network request to the user
            when (result) {
                is Result.Success<LoginBean> -> {
                    mutableLiveData.postValue(result)
                }
                else -> {
                    mutableLiveData.postValue(result)
                }
            }
        }
    }
}