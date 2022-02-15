package com.example.nammametromvvm.ui.homescreen.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.ActivityHomeBinding
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeActivityViewModel
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeActivityViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityViewModel

    @Inject
    lateinit var factory: HomeActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view: View = binding.root
        viewModel = ViewModelProvider(this, factory)[HomeActivityViewModel::class.java]
        setContentView(view)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            onBackPressed()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

}