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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerViewModel

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        /** masih bingung ini apaan
         * applcation as context, buat di lempar ke viewmodel*/
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

        /** mengubah layoutmanager menjadi gridlayout*/
        val manager = GridLayoutManager(activity, 3)
        binding.sleepList.layoutManager = manager

        /** Init Adapter for recycler view*/
        val adapter = SleepTrackerAdapter(SleepNightListener { nightId ->
            Toast.makeText(context, "$nightId", Toast.LENGTH_SHORT).show()
            viewModel.onSleepNightClicked(nightId)

        }) //init adapter

        binding.sleepList.adapter = adapter //Assign adapter to view
        viewModel.nights.observe(viewLifecycleOwner,  //Updating data
                Observer {
                    it?.let {
                        //adapter.data = it //Assign data to adapter
                        // gak dipake karena sekarang make list adapter

                        adapter.submitList(it) //listadapter way
                    }
        })


        /** Observer buat pindah ke quality fragment*/
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

        /** Observer buat pindah ke detail fragment*/
        viewModel.navigateToSleepDataQuality.observe(viewLifecycleOwner,
                Observer { night->

                Toast.makeText(context, "$night", Toast.LENGTH_LONG).show()

                //apabila sleepNight gak kosong
                    night?.let {

                        Log.i("Navigation", "$night clicked")

                        this.findNavController().navigate(SleepTrackerFragmentDirections
                                .actionSleepTrackerFragmentToSleepDetailFragment(night)
                        )

                        viewModel.onSleepDataQualityNavigated()
                    }
                }
        )
        return binding.root
    }
}
