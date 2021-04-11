package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData

interface DataSource {
    fun getCurrentTime(): LiveData<Long>
    fun fetchWeather(): LiveData<String>
    val cachedData: LiveData<String>
    suspend fun fetchNewData()
}