package com.example.nammametromvvm.ui.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentHomeBinding
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeFragmentViewModel
import com.example.nammametromvvm.utility.AppConstants
import com.example.nammametromvvm.utility.interfaces.BottomSheetDialogueCallBackListener
import com.example.nammametromvvm.utility.ui.BottomSheet
import com.example.nammametromvvm.utility.ui.BottomSheet.BottomSheetCalledFrom.*
import com.example.nammametromvvm.utility.ui.GeneralUi.fadingAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
) : Fragment(), BottomSheetDialogueCallBackListener {
    private val homeFragmentViewModel by viewModels<HomeFragmentViewModel>()


    private lateinit var binding: FragmentHomeBinding
    private val bottomSheet: BottomSheet = BottomSheet(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserName()
        setUpClick()
        fadingAnimation(binding.root, android.R.anim.fade_in, requireContext())
    }

    private fun setUpClick() {
        binding.ivSettings.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment()) }
        binding.ivProfile.setOnClickListener {
            lifecycleScope.launch {
                if (homeFragmentViewModel.isUserLoggedIn()) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
                } else {
                    bottomSheet.bottomSheetLoginRequired(
                        requireActivity(),
                        HOME_PROFILE
                    )
                }
            }
        }
        binding.cvCardTopUp.setOnClickListener {
            lifecycleScope.launch {
                if (homeFragmentViewModel.isUserLoggedIn()) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToCardTopUpFragment()
                    )
                } else {
                    bottomSheet.bottomSheetLoginRequired(
                        requireActivity(),
                        HOME_TOP_UP
                    )
                }
            }
        }
        binding.cvQrTickets.setOnClickListener {
            lifecycleScope.launch {
                if (homeFragmentViewModel.isUserLoggedIn()) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToQrTicketsFragment())
                } else {
                    bottomSheet.bottomSheetLoginRequired(
                        requireActivity(),
                        HOME_QR_TICKET
                    )
                }
            }
        }
    }


    private fun setUpUserName() {
        lifecycleScope.launch {
            homeFragmentViewModel.getUserName().collect { userName ->
                if (userName == AppConstants.dataStoreDefaultValue) {
                    binding.tvUserName.visibility = View.INVISIBLE
                } else {
                    binding.tvUserName.text = getString(R.string.home_screen_welcome, userName)
                }
            }
        }

    }

    fun test(requestedFrom: BottomSheet.BottomSheetCalledFrom) {

    }

    override fun onNegativeButtonClick(requestedFrom: BottomSheet.BottomSheetCalledFrom) {

    }

    override fun onPositiveButtonClick(requestedFrom: BottomSheet.BottomSheetCalledFrom) {
        when (requestedFrom) {
            HOME_PROFILE -> navigateLoginScreen(
                getString(R.string.navigated_from_homescreen_profile)
            )
            HOME_TOP_UP -> navigateLoginScreen(
                getString(R.string.navigated_from_homescreen_top_up)
            )
            HOME_QR_TICKET -> navigateLoginScreen(
                getString(R.string.navigated_from_homescreen_qr_tickets)
            )
        }
    }

    private fun navigateLoginScreen(navigatedFrom: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToLoginUserDetailsFragment(
                navigatedFrom
            )
        )
    }
}