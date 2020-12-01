package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepTrackerAdapter() : androidx.recyclerview.widget.ListAdapter<SleepNight, SleepTrackerAdapter.ViewHolder>(SleepNightDiffCallback()) {
// Adapter lebih bertanggung jawab dengan DATA

        /* gak dibutuhin lagi karena kita make ListAdapter
        var data = listOf<SleepNight>()
        set(value) {
            field = value //Untuk update data
            notifyDataSetChanged()
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /** Telling adapter how to draw a data*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = data[position] recycler view way, gak dipake karena make listadapter

        val item = getItem(position)
        holder.bind(item)
    }

    /* gak dibutuhin karena kita ganti recyclerview jadi listadapter
    *//** Telling adapter how many data we have*//*
    override fun getItemCount(): Int = data.size*/

    // ViewHolder adalah class yang bertanggung jawab terhadap TAMPILAN
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){

        /** Refactor buat onBindViewHolder*/
        fun bind(item: SleepNight) {
            /** menyambungkan databinding adapter dengan viewmodel recyclerview*/
            binding.sleep = item
            binding.executePendingBindings()
        }

        /** Refactor buat onCreateViewHolder*/
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false) //Databinding  x ViewHolder

                return ViewHolder(binding)
            }
        }
    }

}

/** Class for comparing old and new list in faster way*/
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {

    /** membandingkan apakah item (id doang) nya sama*/
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return  oldItem.nightID == newItem.nightID
    }

    /** membandingkan apakah item (id dan isinya) nya sama*/
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}