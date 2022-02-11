package com.example.nammametromvvm.ui.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentHomeBinding
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeActivityViewModel
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeActivityViewModelFactory
import com.example.nammametromvvm.utility.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var homeActivityViewModel: HomeActivityViewModel

    @Inject
    lateinit var factory: HomeActivityViewModelFactory
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityViewModel = ViewModelProvider(this, factory)[HomeActivityViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return (binding.root)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserName()
        setUpClick()
    }

    private fun setUpClick() {
        binding.ivSettings.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment()) }
        binding.ivProfile.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment()) }
    }


    private fun setUpUserName() {
        lifecycleScope.launch {
            homeActivityViewModel.getUserName().collect { userName ->
                if (userName == AppConstants.dataStoreDefaultValue) {
                    binding.tvUserName.visibility = View.INVISIBLE
                } else {
                    binding.tvUserName.text = getString(R.string.home_screen_welcome, userName)
                    binding.tvUserName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.fade_in
                        )
                    )
                }
            }
        }

    }

}