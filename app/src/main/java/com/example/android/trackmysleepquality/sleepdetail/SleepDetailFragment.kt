package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.SleepDetailFragmentBinding
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerViewModelFactory

class SleepDetailFragment : Fragment() {

    private lateinit var viewModel: SleepDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: SleepDetailFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.sleep_detail_fragment, container, false
        )

        val application = requireNotNull(this.activity).application

        /** Mengambil argumen / data dari fragment sebelumnya*/
        val arguments = SleepDetailFragmentArgs.fromBundle(arguments!!)

        /** menyambungkan dengan database*/
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        /** menyambungkan dengan viewmodelfactory*/
        val viewmodelFactory = SleepDetailViewModelFactory(arguments.sleepNightKey, dataSource)

        /** Menyambungkan dengan viewmodel*/
        val viewModel = ViewModelProviders.of(
                this, viewmodelFactory).get(SleepDetailViewModel::class.java)

        binding.sleepDetailViewModel = viewModel
        binding.lifecycleOwner = this

        /**observer, masih belumt tau buat apa*/
        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner,
                Observer {
                    if (it == true) {
                        this.findNavController().navigate(
                            SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
                        )

                        viewModel.doneNavigating()
                    }
                })


        return binding.root
    }

}