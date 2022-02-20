package com.example.nammametromvvm.ui.homescreen.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.nammametromvvm.R
import com.example.nammametromvvm.R.string
import com.example.nammametromvvm.databinding.FragmentSettingsBinding
import com.example.nammametromvvm.ui.homescreen.viewModels.SettingsViewModel
import com.example.nammametromvvm.ui.homescreen.viewModels.SettingsViewModelFactory
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var settingsViewModel: SettingsViewModel

    @Inject
    lateinit var factory: SettingsViewModelFactory
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBar()
        setUpClick()
        handleLanguage()
        handleTheme()
    }

    private fun handleLanguage() {
        val chip = Chip(requireActivity())
        chip.text = getString(string.english)

        binding.chipGroup.addView(chip)
        val chip2 = Chip(requireActivity())
        chip2.text = "ಕನ್ನಡ"
        binding.chipGroup.addView(chip2)
        binding.chipGroup.isSingleSelection = true
        binding.chipGroup.childCount
        binding.chipGroup.check(chip.id)
    }

    private fun handleTheme() {
        val chip = Chip(requireActivity())
        chip.text = getString(string.light)
        binding.chipGroupTheme.addView(chip)
        val chip2 = Chip(requireActivity())
        chip2.text = getString(string.dark)
        binding.chipGroupTheme.addView(chip2)
        binding.chipGroupTheme.isSingleSelection = true
        val chip3 = Chip(requireActivity())
        chip3.text = getString(string.system)
        binding.chipGroupTheme.addView(chip3)
        binding.chipGroup.check(chip3.id)


    }


    private fun setUpActionBar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            if ((activity as AppCompatActivity).supportActionBar != null) {
                (activity as AppCompatActivity).supportActionBar!!.title =
                    getString(string.settings)
                (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(true)
                (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    private fun setUpClick() {
        binding.llLogOut.setOnClickListener {
            lifecycleScope.launch {
                settingsViewModel.logOut(requireContext())
                //action_homeFragment_to_splashScreenActivity
                navigateToSplashScreen()

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("test25", "onOptionsItemSelected: 2")
        if (item.itemId == R.id.home) {
            requireActivity().onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun navigateToSplashScreen() {
        navigateTo(
            SettingsFragmentDirections.actionSettingsFragmentToSplashScreenActivity(
            )
        )
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(
            navDirections
        )
    }
}