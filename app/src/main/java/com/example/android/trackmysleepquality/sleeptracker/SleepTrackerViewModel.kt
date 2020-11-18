/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment
 * parameter ke-2 : aplication supaya kita bisa make value2 di res
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {


    private var viewModelJob = Job()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() //menghentikan semua coroutine yang berjalan saat viewmodel di destroys
    }

    //Scope itu (kalau gak salah) buat ngetrack coroutine
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    //database itu DAO, dari parameter
    private val nights = database.getAllNights()

    init {
        //make uiScope karena hasilnya akan ditampilkan di layar
        //tapi didalamnya, ngambil datanya gak make ui scope
        uiScope.launch {
            Log.i("ViewModel", "init")
            tonight.value = getToninghFromDatabase()
        }
    }

    private suspend fun getToninghFromDatabase(): SleepNight? {
        /**
        fungsi buat data terakhir
         */
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()

            // kalau start time sama end time gak sama, berarti data teratas bukan night baru/kosong
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            Log.i("ViewModel", "getTonight $night")
            night //returnya
        }
    }

    public fun onStartTracking(){
        /**
         * ketika tombol start dipencet
         * make uiScope karena hasilnya ditampilkan di ui
         * taoi dalamnya (insert data) nya dijalanin di IO Threads
         */
        uiScope.launch {
            val newNight = SleepNight() //buat baris baru
            insert(newNight) //insert newNight ke tabel
            Log.i("ViewModel", "onStart Pressed")
            tonight.value = getToninghFromDatabase()
        }
    }

    private suspend fun insert(night : SleepNight){
        //insert night baru ke database
        withContext(Dispatchers.IO){
            database.insert(night)
        }
    }

    fun onStopTracking(){
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()
            Log.i("ViewModel", "onStopPressed $oldNight")
            update(oldNight)
        }
    }

    private suspend fun update(night:SleepNight){
        withContext(Dispatchers.IO){
            Log.i("ViewModel", "Updating")
            database.update(night)
        }
    }

    fun onClear(){
        uiScope.launch {
            Log.i("ViewModel", "onClear Pressed")
            clear()
            tonight.value = null
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

}

