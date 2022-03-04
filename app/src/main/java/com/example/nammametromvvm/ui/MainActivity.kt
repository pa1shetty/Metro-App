package com.example.nammametromvvm.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.ActivityMainBinding
import com.example.nammametromvvm.ui.homescreen.viewModels.MainViewModel
import com.example.nammametromvvm.ui.homescreen.viewModels.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private lateinit var binding: ActivityMainBinding
lateinit var mainViewModel: MainViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        setUpTheme()

    }

    override fun onBackPressed() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            onBackPressed()
            return false
        }
        return true
    }
    private fun setUpTheme() = mainViewModel.setUpTheme()
}