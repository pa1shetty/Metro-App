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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nammametromvvm.databinding.UnusedTicketFragmentBinding
import com.example.nammametromvvm.ui.qrtickets.adapter.TicketAdapter
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModel
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
        val ticketAdapter = TicketAdapter(requireContext(), viewModel.getDateMethods()) {}
        binding.rvUnusedTickets.adapter = ticketAdapter
        lifecycleScope.launch {
            viewModel.getUnusedTickets().collect { ticketList ->
                ticketAdapter.submitList(ticketList)
                withContext(Dispatchers.Main) {
                    if (ticketList.isEmpty()) {
                        if (binding.rvUnusedTickets.visibility == GONE) {
                            binding.rvUnusedTickets.visibility = VISIBLE
                        }
                        binding.clNoTickets.visibility = VISIBLE
                    } else {
                        if (binding.rvUnusedTickets.visibility == GONE) {
                            binding.rvUnusedTickets.visibility = VISIBLE
                        }
                        binding.clNoTickets.visibility = GONE
                    }
                }
            }
        }
    }
}