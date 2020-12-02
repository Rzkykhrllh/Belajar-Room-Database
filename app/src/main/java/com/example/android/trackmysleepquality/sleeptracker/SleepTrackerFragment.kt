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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        /** masih bingung ini apaan*/
        // applcation as context, buat di lempat ke viewmodel
        val application = requireNotNull(this.activity).application

        /**database init */
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        /** viewModelFactory init*/
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        /** viwModel init*/
        val viewModel = ViewModelProviders.of(
                this, viewModelFactory)
                .get(SleepTrackerViewModel::class.java)

        /** databinding x viewmodel*/
        binding.sleepViewModel = viewModel
        binding.setLifecycleOwner(this)

        /** layout manager for recyclerview */
        val manager = GridLayoutManager(activity, 3)
        binding.sleepList.layoutManager = manager //Assign manager to layout of recylcerview in fragment

        /** Init Adapter for recycler view*/
        val adapter = SleepTrackerAdapter() //init adapter
        binding.sleepList.adapter = adapter //Assign adapter to view
        viewModel.nights.observe(viewLifecycleOwner,  //Updating data
                Observer {
                    it?.let {
                        //adapter.data = it //Assign data to adapter
                        // gak dipake karena sekarang make list adapter

                        adapter.submitList(it) //listadapter way
                    }
        })

        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner,
                Observer {
                    Log.i("navigation","$it")

                    if (it != null){
                        this.findNavController().navigate(
                                SleepTrackerFragmentDirections
                                        .actionSleepTrackerFragmentToSleepQualityFragment(it!!.nightID))

                        viewModel.doneNavigate()
                    }

                }
        )

        return binding.root
    }
}
