package com.example.william.my.module.sample.model

import androidx.lifecycle.*
import com.example.william.my.module.sample.repo.KtArticleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Showcases different patterns using the liveData coroutines builder.
 */
class LiveDataViewModel(private val articleDataSource: KtArticleRepository) : ViewModel() {

    // Exposed LiveData from a function that returns a LiveData generated with a liveData builder
    val currentTime = articleDataSource.getCurrentTime()

    // Coroutines inside a transformation
    val currentTimeTransformed = currentTime.switchMap {
        // timeStampToTime is a suspend function so we need to call it from a coroutine.
        liveData { emit(timeStampToTime(it)) }
    }

    // Exposed cached value in the data source that can be updated later on
    val cachedValue = articleDataSource.cachedData

    // Called when the user clicks on the "FETCH NEW DATA" button. Updates value in data source.
    fun onRefresh() {
        // Launch a coroutine that reads from a remote data source and updates cache
        viewModelScope.launch {
            articleDataSource.fetchNewData()
        }
    }

    // Simulates a long-running computation in a background thread
    private suspend fun timeStampToTime(timestamp: Long): String {
        delay(500)  // Simulate long operation
        val date = Date(timestamp)
        return date.toString()
    }

    companion object {
        // Real apps would use a wrapper on the result type to handle this.
        const val LOADING_STRING = "Loading..."
    }
}


/**
 * 自定义实例，多参构造
 * Factory for [LiveDataViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {

    private val dataSource = KtArticleRepository()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LiveDataViewModel(dataSource) as T
    }
}
