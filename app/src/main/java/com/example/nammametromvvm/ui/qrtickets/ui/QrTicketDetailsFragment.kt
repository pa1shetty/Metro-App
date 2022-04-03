package com.example.nammametromvvm.ui.qrtickets.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.nammametromvvm.R
import com.example.nammametromvvm.data.repositaries.database.module.QrTicket
import com.example.nammametromvvm.databinding.FragmentQrTicketDetailsBinding
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModel
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModelFactory
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.TicketType
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.ui.GeneralUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class QrTicketDetailsFragment : Fragment() {
    private val safeArgs: QrTicketDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var genericMethods: GenericMethods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[QrTicketsViewModel::class.java]

    }

    private lateinit var viewModel: QrTicketsViewModel

    @Inject
    lateinit var factory: QrTicketsViewModelFactory

    lateinit var binding: FragmentQrTicketDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrTicketDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClick()
        getTicketDetails()
    }

    private fun getTicketDetails() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getTicketDetails(safeArgs.ticketId).collectLatest { qrTicket ->
                withContext(Dispatchers.Main) {
                    showTripDetails(qrTicket)
                    showPaymentDetails(qrTicket)
                    setUpCancelUI(qrTicket.txnStatus)
                }
            }
        }
    }

    private fun setUpCancelUI(txnStatus: String) {
        when (txnStatus.toInt()) {
            TicketType.UNUSED.ticketType -> {

            }
        }

    }

    private fun showPaymentDetails(qrTicket: QrTicket) {
        binding.tvTransactionIdValue.text = qrTicket.txnID
        binding.tvPaymentIdValue.text = qrTicket.paymentID
        binding.tvPaymentModeValue.text = qrTicket.paymentMode
        binding.tvTransactionOnValue.text = qrTicket.txnDateTime
        val fare = requireContext().getString(R.string.rs_symbol) + " " + qrTicket.totalFare
        binding.tvFareTotalAmount.text = fare
    }

    private fun showTripDetails(qrTicket: QrTicket) {
        binding.tvQrTicketId.text = qrTicket.qrSerialNumber
        binding.tvFromStation.text = qrTicket.fromStop
        binding.tvToStation.text = qrTicket.toStop
        binding.tvPassengerCount.text = qrTicket.nmbrOfPssngrs
        binding.tvFromStation.text = qrTicket.fromStop
        binding.tvToStation.text = qrTicket.toStop
        GeneralUi.textToImageEncode(
            requireContext(),
            qrTicket.qrData
        )?.let { binding.ivQrTicket.setImageBitmap(it) }


        val fare =
            requireContext().getString(R.string.rs_symbol) + " " + qrTicket.totalFare

        binding.tvFare.text = fare
        when (qrTicket.txnStatus.toInt()) {
            TicketType.UNUSED.ticketType -> {
                binding.tvStatus.text =
                    requireContext().getString(
                        R.string.purchased_on,
                        convertDateFormat(qrTicket.txnDateTime)
                    )
                GeneralUi.setImageDrawable(
                    binding.ivStatus,
                    requireContext(),
                    R.drawable.ic_round_check_circle_24
                )
                GeneralUi.setDrawableColorGreen(binding.ivStatus, requireContext())
            }
            TicketType.USED.ticketType -> {
                binding.tvStatus.text =
                    requireContext().getString(
                        R.string.used_on,
                        convertDateFormat(qrTicket.txnDateTime)
                    )
                GeneralUi.setImageDrawable(
                    binding.ivStatus,
                    requireContext(),
                    R.drawable.ic_round_check_circle_24
                )
                GeneralUi.setDrawableColorGreen(binding.ivStatus, requireContext())
                GeneralUi.setDrawableColorGreen(binding.ivFromStation, requireContext())
                GeneralUi.setDrawableColorGreen(binding.ivToStation, requireContext())
                GeneralUi.setBackgroundColorGreen(binding.vLine, requireContext())

            }
            TicketType.CANCELLED.ticketType -> {
                binding.tvStatus.text =
                    requireContext().getString(
                        R.string.cancelled_on,
                        convertDateFormat(qrTicket.txnDateTime)
                    )
                binding.ivStatus.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_cancel_24
                    )
                )
                GeneralUi.setDrawableColorRed(binding.ivStatus, requireContext())
                GeneralUi.setDrawableColorRed(binding.ivFromStation, requireContext())
                GeneralUi.setDrawableColorRed(binding.ivToStation, requireContext())
                GeneralUi.setBackgroundColorRed(binding.vLine, requireContext())
            }
            TicketType.EXPIRED.ticketType -> {
                binding.tvStatus.text =
                    requireContext().getString(
                        R.string.expired_on,
                        convertDateFormat(qrTicket.expiredOn)
                    )
                binding.ivStatus.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_error_24
                    )
                )
                GeneralUi.setDrawableColorRed(binding.ivStatus, requireContext())
                GeneralUi.setDrawableColorRed(binding.ivFromStation, requireContext())
                GeneralUi.setDrawableColorRed(binding.ivToStation, requireContext())
                GeneralUi.setBackgroundColorRed(binding.vLine, requireContext())
            }

            TicketType.FAILED.ticketType -> {
                binding.tvStatus.text =
                    requireContext().getString(
                        R.string.purchase_initiated_on,
                        convertDateFormat(qrTicket.txnDateTime)
                    )
                binding.ivStatus.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_error_24
                    )
                )
                GeneralUi.setDrawableColorRed(binding.ivStatus, requireContext())
                GeneralUi.setDrawableColorRed(binding.ivFromStation, requireContext())
                GeneralUi.setDrawableColorRed(binding.ivToStation, requireContext())
                GeneralUi.setBackgroundColorRed(binding.vLine, requireContext())
            }
            TicketType.PENDING.ticketType -> {
                binding.tvStatus.text =
                    requireContext().getString(
                        R.string.purchase_initiated_on,
                        convertDateFormat(qrTicket.expiredOn)
                    )
                binding.ivStatus.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_pending_24
                    )
                )
                GeneralUi.setDrawableColorOrange(binding.ivStatus, requireContext())
            }

        }

    }

    private fun setUpClick() {
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }

    }

    private fun convertDateFormat(
        originalDate: String,
        originalDateFormat: String = DateMethods.DateConstants.date_format_from_server
    ): String =
        viewModel.getDateMethods().convertDateFormatDynamic(originalDate, originalDateFormat)

}