package com.example.nammametromvvm.ui.homescreen

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.nammametromvvm.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view: View = binding.root
        binding.tvUserName.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        setContentView(view)
    }
}