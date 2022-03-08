package com.example.nammametromvvm.ui.qrtickets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nammametromvvm.R
import com.example.nammametromvvm.data.repositaries.entites.QrTicket
import com.example.nammametromvvm.databinding.TicketItemBinding
import com.example.nammametromvvm.utility.TicketType
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.ui.GeneralUi

class TicketAdapter(
    private val context: Context,
    private val dateMethods: DateMethods,
    private val onItemClicked: (QrTicket) -> Unit
) : ListAdapter<QrTicket, TicketAdapter.BusStopViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<QrTicket>() {
            override fun areItemsTheSame(oldItem: QrTicket, newItem: QrTicket): Boolean {
                return oldItem.txnID == newItem.txnID
            }

            override fun areContentsTheSame(oldItem: QrTicket, newItem: QrTicket): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        val viewHolder = BusStopViewHolder(
            TicketItemBinding.inflate(
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

    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }

    class BusStopViewHolder(
        private var binding: TicketItemBinding,
        private val dateMethods: DateMethods
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(qrTicket: QrTicket, context: Context) {
            binding.tvFromStation.text = qrTicket.fromStop
            binding.tvToStation.text = qrTicket.toStop
            val fare = context.getString(R.string.rs_symbol) + " " + qrTicket.totalFare
            binding.tvFare.text = fare
            when (qrTicket.txnStatus.toInt()) {
                TicketType.UNUSED.ticketType -> {
                    binding.tvStatus.text =
                        context.getString(
                            R.string.purchased_on,
                            convertDateFormat(qrTicket.txnDateTime)
                        )
                    GeneralUi.setImageDrawable(
                        binding.ivStatus,
                        context,
                        R.drawable.ic_round_check_circle_24
                    )
                    GeneralUi.setDrawableColorGreen(binding.ivStatus, context)
                }
                TicketType.USED.ticketType -> {
                    binding.tvStatus.text =
                        context.getString(R.string.used_on, convertDateFormat(qrTicket.txnDateTime))
                    GeneralUi.setImageDrawable(
                        binding.ivStatus,
                        context,
                        R.drawable.ic_round_check_circle_24
                    )
                    GeneralUi.setDrawableColorGreen(binding.ivStatus, context)
                    GeneralUi.setDrawableColorGreen(binding.ivFromStation, context)
                    GeneralUi.setDrawableColorGreen(binding.ivToStation, context)
                    GeneralUi.setBackgroundColorGreen(binding.vLine, context)

                }
                TicketType.CANCELLED.ticketType -> {
                    binding.tvStatus.text =
                        context.getString(
                            R.string.cancelled_on,
                            convertDateFormat(qrTicket.txnDateTime)
                        )
                    binding.ivStatus.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_baseline_cancel_24
                        )
                    )
                    GeneralUi.setDrawableColorRed(binding.ivStatus, context)
                    GeneralUi.setDrawableColorRed(binding.ivFromStation, context)
                    GeneralUi.setDrawableColorRed(binding.ivToStation, context)
                    GeneralUi.setBackgroundColorRed(binding.vLine, context)
                }
                TicketType.EXPIRED.ticketType -> {
                    binding.tvStatus.text =
                        context.getString(
                            R.string.expired_on,
                            convertDateFormat(qrTicket.expiredOn)
                        )
                    binding.ivStatus.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_baseline_error_24
                        )
                    )
                    GeneralUi.setDrawableColorRed(binding.ivStatus, context)
                    GeneralUi.setDrawableColorRed(binding.ivFromStation, context)
                    GeneralUi.setDrawableColorRed(binding.ivToStation, context)
                    GeneralUi.setBackgroundColorRed(binding.vLine, context)
                }

                TicketType.FAILED.ticketType -> {
                    binding.tvStatus.text =
                        context.getString(
                            R.string.purchase_initiated_on,
                            convertDateFormat(qrTicket.txnDateTime)
                        )
                    binding.ivStatus.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_baseline_error_24
                        )
                    )
                    GeneralUi.setDrawableColorRed(binding.ivStatus, context)
                    GeneralUi.setDrawableColorRed(binding.ivFromStation, context)
                    GeneralUi.setDrawableColorRed(binding.ivToStation, context)
                    GeneralUi.setBackgroundColorRed(binding.vLine, context)
                }
                TicketType.PENDING.ticketType -> {
                    binding.tvStatus.text =
                        context.getString(
                            R.string.purchase_initiated_on,
                            convertDateFormat(qrTicket.expiredOn)
                        )
                    binding.ivStatus.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_baseline_pending_24
                        )
                    )
                    GeneralUi.setDrawableColorOrange(binding.ivStatus, context)
                }

            }
        }

        private fun convertDateFormat(
            originalDate: String,
            originalDateFormat: String = DateMethods.DateConstants.date_format_from_server
        ): String = dateMethods.convertDateFormatDynamic(originalDate, originalDateFormat)

    }
}
