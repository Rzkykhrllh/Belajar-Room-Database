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

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

//Dao adalah tempat dimana fungsi untuk mengakses database dibuat

@Dao
interface SleepDatabaseDao{

    @Insert
    fun insert(night: SleepNight)

    @Update //otomatis
    fun update(night: SleepNight)

    @Query("SELECT * from daily_sleep_table where nightID = :key") //manual, tulis sql
    fun get (key : Long) : SleepNight

    @Query("Delete from daily_sleep_table")
    fun clear()

    @Query("select * from daily_sleep_table order by nightID desc")
    fun getAllNights() : LiveData<List<SleepNight>>

    @Query("select * from daily_sleep_table order by nightID desc limit 1")
    fun getTonight() : SleepNight?

    @Query("select * from daily_sleep_table where nightID= :key")
    fun getNightWithId(key: Long) : LiveData<SleepNight>
}
