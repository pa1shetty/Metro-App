package com.example.nammametromvvm.ui.qrtickets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nammametromvvm.data.repositaries.network.responses.stationLists.Station
import com.example.nammametromvvm.databinding.StationItemBinding
import com.example.nammametromvvm.utility.date.DateMethods

class StationListAdapter(
    private val context: Context,
    private val dateMethods: DateMethods,
    private val onItemClicked: (Station) -> Unit
) : ListAdapter<Station, StationListAdapter.BusStopViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Station>() {
            override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
                return oldItem.stationID == newItem.stationID
            }

            override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusStopViewHolder {
        val viewHolder = BusStopViewHolder(
            StationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), dateMethods
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }


    class BusStopViewHolder(
        private var binding: StationItemBinding,
        private val dateMethods: DateMethods
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station, context: Context) {
            binding.tvStationName.text = station.englishName
        }

        private fun convertDateFormat(
            originalDate: String,
            originalDateFormat: String = DateMethods.DateConstants.date_format_from_server
        ): String = dateMethods.convertDateFormatDynamic(originalDate, originalDateFormat)

    }

    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}
