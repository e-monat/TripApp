package com.example.inventory.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.Trip
import java.text.SimpleDateFormat
import java.util.*

class TripAdapter(
    private val trips: List<Trip>,
    private val onClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trip_item, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount(): Int = trips.size

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tripTitle)
        private val date: TextView = itemView.findViewById(R.id.tripDate)

        fun bind(trip: Trip) {
            title.text = trip.title
            val formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(Date(trip.timestamp))
            date.text = formattedDate
            itemView.setOnClickListener { onClick(trip) }
        }
    }
}
