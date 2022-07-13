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
import com.example.nammametromvvm.databinding.UnusedTicketFragmentBinding
import com.example.nammametromvvm.ui.qrtickets.adapter.QrTicketListAdapter
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModel
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModelFactory
import com.example.nammametromvvm.utility.ui.GeneralUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class UnusedTicketFragment : Fragment() {
    lateinit var binding: UnusedTicketFragmentBinding

    companion object {
        fun newInstance() = UnusedTicketFragment()
    }

    private lateinit var viewModel: QrTicketsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UnusedTicketFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @Inject
    lateinit var factory: QrTicketsViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[QrTicketsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
    }

    private fun setUpRV() {
        binding.rvUnusedTickets.layoutManager = LinearLayoutManager(requireContext())
        val ticketAdapter =
            QrTicketListAdapter(requireContext(), viewModel.getDateMethods()) { qrTicket ->
                navigateTo(
                    QrTicketsFragmentDirections.actionQrTicketsFragmentToQrTicketDetailsFragment(
                        qrTicket.txnID
                    )
                )
            }
        binding.rvUnusedTickets.adapter = ticketAdapter
        lifecycleScope.launch {
            viewModel.getUnusedTickets().collect { ticketList ->
                ticketAdapter.submitList(ticketList)
                withContext(Dispatchers.Main) {
                    if (ticketList.isEmpty()) {
                        binding.rvUnusedTickets.visibility = GONE
                        binding.clNoTickets.visibility = VISIBLE
                        GeneralUi.fadingAnimation(
                            binding.clNoTickets,
                            android.R.anim.fade_in,
                            requireContext()
                        )
                    } else {
                        binding.clNoTickets.visibility = GONE
                        if (binding.rvUnusedTickets.visibility == GONE) {
                            binding.rvUnusedTickets.visibility = VISIBLE
                            GeneralUi.fadingAnimation(
                                binding.rvUnusedTickets,
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