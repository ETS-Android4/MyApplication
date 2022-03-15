package com.example.william.my.module.sample.flow

import androidx.lifecycle.ViewModel
import com.example.william.my.bean.data.LoginBean
import com.example.william.my.module.sample.repo.FlowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LatestNewsViewModel(private val flowRepository: FlowRepository) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LatestNewsUiState.Success(LoginBean()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LatestNewsUiState> = _uiState

    init {
//        viewModelScope.launch {
//            newsRepository.favoriteLatestNews
//                .catch { exception ->
//                    //notifyError(exception)
//                }
//                .collect { favoriteNews ->
//                    _login.value = LatestNewsUiState.Success(favoriteNews)
//                }
//        }
    }
}

sealed class LatestNewsUiState {
    data class Success(val news: LoginBean) : LatestNewsUiState()
    data class Error(val exception: Throwable) : LatestNewsUiState()
}