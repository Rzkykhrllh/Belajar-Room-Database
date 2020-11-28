package com.example.android.trackmysleepquality.sleeptracker

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepTrackerAdapter() : RecyclerView.Adapter<SleepTrackerAdapter.ViewHolder>(), Parcelable {

    var data = listOf<SleepNight>()
        set(value) {
            field = value //Untuk update data
            notifyDataSetChanged()
        }

    constructor(parcel: Parcel) : this() {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
        return  ViewHolder(view)
    }

    /** Telling adapter how to draw a data*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources

        holder.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
        holder.Quality.text = convertNumericQualityToString(item.sleepQuality, res)
        holder.Quality_Image.setImageResource(
                when (item.sleepQuality){
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    else -> R.drawable.ic_sleep_0
                }
        )
    }

    /** Telling adapter how many data we have*/
    override fun getItemCount(): Int = data.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sleepLength : TextView = itemView.findViewById(R.id.sleep_length)
        val Quality : TextView = itemView.findViewById(R.id.quality_string)
        val Quality_Image : ImageView = itemView.findViewById(R.id.Quality_Image)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SleepTrackerAdapter> {
        override fun createFromParcel(parcel: Parcel): SleepTrackerAdapter {
            return SleepTrackerAdapter(parcel)
        }

        override fun newArray(size: Int): Array<SleepTrackerAdapter?> {
            return arrayOfNulls(size)
        }
    }

}