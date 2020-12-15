package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight

class SleepDetailViewModelFactory(
        private val sleepNightKey : Long,
        private val datascource : SleepDatabaseDao) : ViewModelProvider.Factory
{
    @Suppress("uncheked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)){
            return SleepDetailViewModel(sleepNightKey, datascource) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel Class")
    }
}