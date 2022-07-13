package com.example.nammametromvvm.ui.homescreen.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.nammametromvvm.BuildConfig
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentSettingsBinding
import com.example.nammametromvvm.ui.homescreen.viewModels.SettingsViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val settingsViewModel by viewModels<SettingsViewModel>()

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
        setUpClick()
        handleLanguage()
        handleTheme()
        setUpVersionData()
        setUpLoggedInUI()
    }

    private fun setUpLoggedInUI() {
        binding.btnLogOut.visibility = View.GONE
        lifecycleScope.launch {
            settingsViewModel.isUserLoggedIn().collect { isUserLoggedIn ->
                if (isUserLoggedIn) {
                    binding.btnLogOut.visibility = View.VISIBLE
                } else {
                    binding.btnLogOut.visibility = View.GONE
                }
            }
        }
    }

    private fun setUpVersionData() {
        binding.tvVersion.text = getString(
            R.string.version_build,
            BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE.toString()
        )
    }

    private fun handleLanguage() {
        val languages = settingsViewModel.getLanguages()
        lifecycleScope.launch {
            val currentLanguage = settingsViewModel.getCurrentLanguage()
            languages.forEachIndexed { index, language ->
                val chip = Chip(requireActivity())
                chip.text = language.name
                chip.id = index + 100
                language.id = chip.id
                if (language.code == currentLanguage) {
                    chip.isChecked = true
                }
                binding.cgLanguage.addView(chip)
            }
        }
        binding.cgLanguage.setOnCheckedChangeListener { _, checkedId ->
            languages.forEach { language ->
                if (language.id == checkedId) {
                    setNewLocale(requireActivity(), language.code)
                }
            }
        }
    }


    @SuppressLint("ResourceType")
    private fun handleTheme() {
        val themes = settingsViewModel.getThemes()
        lifecycleScope.launch {
            val currentTheme = settingsViewModel.getCurrentTheme()
            themes.forEachIndexed { index, theme ->
                val chip = Chip(requireActivity())
                chip.text = theme.name
                chip.id = index
                theme.id = chip.id
                if (theme.name == currentTheme) {
                    chip.isChecked = true
                }
                binding.chipGroupTheme.addView(chip)
            }
        }

        binding.chipGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            themes.forEach { theme ->
                if (theme.id == checkedId) {
                    settingsViewModel.saveCurrentTheme(theme.name)
                }
            }
        }
    }


    private fun setUpClick() {
        binding.btnLogOut.setOnClickListener {
            lifecycleScope.launch {
                settingsViewModel.logOut()
                navigateToSplashScreen()
            }
        }
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            requireActivity().onBackPressed()
            return true
        }
        return false
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

    private fun setNewLocale(mContext: Activity, language: String) {
        settingsViewModel.setNewLocale(requireActivity(), language)
        val intent = mContext.intent
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}