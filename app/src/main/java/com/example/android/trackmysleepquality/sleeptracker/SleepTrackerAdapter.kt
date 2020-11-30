package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

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
    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sleepLength : TextView = itemView.findViewById(R.id.sleep_length)
        val Quality : TextView = itemView.findViewById(R.id.quality_string)
        val Quality_Image : ImageView = itemView.findViewById(R.id.Quality_Image)

        fun bind(item: SleepNight) {

            val res = itemView.context.resources

            sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            Quality.text = convertNumericQualityToString(item.sleepQuality, res)
            Quality_Image.setImageResource(
                    when (item.sleepQuality) {
                        0 -> R.drawable.ic_sleep_0
                        1 -> R.drawable.ic_sleep_1
                        2 -> R.drawable.ic_sleep_2
                        3 -> R.drawable.ic_sleep_3
                        4 -> R.drawable.ic_sleep_4
                        5 -> R.drawable.ic_sleep_5
                        else -> R.drawable.ic_sleep_0
                    }
            )
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                        .inflate(R.layout.list_item_sleep_night, parent, false)

                return ViewHolder(view)
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