package com.example.nammametromvvm.ui.qrtickets.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nammametromvvm.databinding.FragmentOtherTicketBinding
import com.example.nammametromvvm.ui.qrtickets.adapter.QrTicketListAdapter
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModel
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModelFactory
import com.example.nammametromvvm.utility.ui.GeneralUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class OtherTicketFragment : Fragment() {
    private lateinit var viewModel: QrTicketsViewModel

    @Inject
    lateinit var factory: QrTicketsViewModelFactory

    lateinit var binding: FragmentOtherTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[QrTicketsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherTicketBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
    }

    private fun setUpRV() {
        binding.rvOtherTickets.layoutManager = LinearLayoutManager(requireContext())
        val ticketAdapter =
            QrTicketListAdapter(requireContext(), viewModel.getDateMethods()) { qrTicket ->
                navigateTo(
                    QrTicketsFragmentDirections.actionQrTicketsFragmentToQrTicketDetailsFragment(
                        qrTicket.txnID
                    )
                )
            }
        binding.rvOtherTickets.adapter = ticketAdapter
        lifecycleScope.launch {
            viewModel.getOtherTickets().collect { ticketList ->
                ticketAdapter.submitList(ticketList)
                withContext(Dispatchers.Main) {
                    if (ticketList.isEmpty()) {
                        binding.rvOtherTickets.visibility = GONE
                        binding.clNoTickets.visibility = VISIBLE
                        GeneralUi.fadingAnimation(
                            binding.clNoTickets,
                            android.R.anim.fade_in,
                            requireContext()
                        )
                    } else {
                        binding.clNoTickets.visibility = GONE
                        if (binding.rvOtherTickets.visibility == GONE) {
                            binding.rvOtherTickets.visibility = VISIBLE
                            GeneralUi.fadingAnimation(
                                binding.rvOtherTickets,
                                android.R.anim.fade_in,
                                requireContext()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(
            navDirections
        )
    }
}