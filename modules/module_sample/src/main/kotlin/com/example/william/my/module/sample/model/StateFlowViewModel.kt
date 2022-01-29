package com.example.william.my.module.sample.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.module.sample.repo.FlowRepository
import com.example.william.my.module.sample.repo.SuspendRepository
import com.example.william.my.module.sample.state.ArticleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 存放和管理State，同时接受Intent并进行数据请求
 */
class StateFlowViewModel(private val dataSource: SuspendRepository) : ViewModel() {

    private val _state = MutableStateFlow<ArticleUiState>(ArticleUiState.Idle)
    val state: StateFlow<ArticleUiState> = _state.asStateFlow()

    fun getArticle(page: Int) {
        viewModelScope.launch {
            _state.value = ArticleUiState.Loading
            _state.value = try {
                ArticleUiState.Users(dataSource.getArticle(page).data.datas)
            } catch (e: Exception) {
                ArticleUiState.Error(e.localizedMessage)
            }
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [DataBindingViewModel].
 */
object StateVMFactory : ViewModelProvider.Factory {

    private val dataSource = SuspendRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return StateFlowViewModel(dataSource) as T
    }
}
