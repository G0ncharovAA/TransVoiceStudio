package com.gmail.sasha.inverse.transvoicestudio.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gmail.sasha.inverse.transvoicestudio.R
import com.gmail.sasha.inverse.transvoicestudio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.navView.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }
}
