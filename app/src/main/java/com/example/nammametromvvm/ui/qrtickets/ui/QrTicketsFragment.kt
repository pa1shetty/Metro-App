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
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentQrTicketsBinding
import com.example.nammametromvvm.ui.qrtickets.adapter.ViewPagerAdapter
import com.example.nammametromvvm.ui.qrtickets.ui.QrTicketsFragmentDirections.Companion.actionQrTicketsFragmentToStationSelectionScreen
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModel
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class QrTicketsFragment : Fragment() {
    private lateinit var binding: FragmentQrTicketsBinding
    private lateinit var viewModel: QrTicketsViewModel

    @Inject
    lateinit var factory: QrTicketsViewModelFactory
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Todo
        //fetchTicketList()
        getTicketListFromDb()
        setUpClick()
        setUpTabLayout()

    }

    private fun setUpTabLayout() {
        val animalsArray = arrayOf(
            getString(R.string.unused),
            getString(R.string.other)
        )
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.vpTickets.adapter = adapter

        TabLayoutMediator(binding.tlTickets, binding.vpTickets) { tab, position ->
            tab.text = animalsArray[position]
        }.attach()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[QrTicketsViewModel::class.java]
    }

    private fun setUpClick() {
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.srTickets.setOnRefreshListener {
            fetchTicketList()
        }
        binding.fabPurchaseQrTicket.setOnClickListener {
            navigateTo(actionQrTicketsFragmentToStationSelectionScreen())
        }
    }

    private fun getTicketListFromDb() {
        lifecycleScope.launch(IO) {
            viewModel.getTicketCount().collect { ticketCount ->
                withContext(Main) {
                    if (ticketCount > 0) {
                        if (binding.clTickets.visibility == GONE) {
                            binding.clTickets.visibility = VISIBLE
                        }
                        binding.clNoTickets.visibility = GONE
                    } else {
                        binding.clTickets.visibility = GONE
                        if (binding.clNoTickets.visibility == GONE) {
                            binding.clNoTickets.visibility = VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun fetchTicketList() {
        binding.srTickets.isRefreshing = true
        lifecycleScope.launch(IO) {
            viewModel.fetchTicketList()
            withContext(Main) {
                binding.srTickets.isRefreshing = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrTicketsBinding.inflate(layoutInflater)
        return (binding.root)
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(
            navDirections
        )
    }
}