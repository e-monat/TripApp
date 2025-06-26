package com.example.inventory.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.inventory.R
import com.example.inventory.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

//        binding.navigation.setupWithNavController(navController)

        // Intercept navigation
        binding.navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mapFragment -> {
                    // Do something before navigating
                    Log.d("NAV", "Map clicked")
                    navController.navigate(R.id.mapFragment)
                    true
                }
                R.id.tripsFragment -> {
                    Log.d("NAV", "Trips clicked")
                    navController.navigate(R.id.tripsFragment)
                    true
                }
                R.id.setingsFragment -> {
                    Log.d("NAV", "Settings clicked")
                    navController.navigate(R.id.setingsFragment)
                    true
                }
                else -> false
            }
        }

        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all {
                    it == PackageManager.PERMISSION_GRANTED }
            ) {
                Log.d("MainActivity", "Toutes les permissions accordées")
            } else {
                Log.e("MainActivity", "Permissions refusées, certaines fonctionnalités peuvent ne pas marcher.")
            }
        }
    }
}
