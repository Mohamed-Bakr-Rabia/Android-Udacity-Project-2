package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDatabaseDao

class MainViewModelFactory(
    private val databaseDao: AsteroidDatabaseDao,
    private val application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(databaseDao,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}