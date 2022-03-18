package com.example.nammametromvvm.ui.qrtickets.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nammametromvvm.databinding.FragmentQrTicketDetailsBinding
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModel
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QrTicketDetailsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    private fun setUpClick() {
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }

    }

}