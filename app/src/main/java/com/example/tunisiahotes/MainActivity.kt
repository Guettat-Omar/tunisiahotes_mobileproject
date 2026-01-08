package com.tunisiahotes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tunisia.hotes.R
import com.tunisia.hotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuration de la navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configuration du toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Gestion des boutons du toolbar
        setupToolbarButtons()

        // Masquer/afficher le toolbar selon les fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showToolbar(true)
                R.id.detailFragment -> showToolbar(false)
                R.id.filterFragment -> showToolbar(false)
                R.id.reservationFragment -> showToolbar(false)
                else -> showToolbar(true)
            }
        }
    }

    private fun setupToolbarButtons() {
        binding.btnFilter.setOnClickListener {
            if (navController.currentDestination?.id == R.id.homeFragment) {
                navController.navigate(R.id.action_homeFragment_to_filterFragment)
            }
        }

        binding.btnDeveloper.setOnClickListener {
            if (navController.currentDestination?.id == R.id.homeFragment) {
                navController.navigate(R.id.action_homeFragment_to_developerAccessFragment)
            }
        }
    }

    private fun showToolbar(show: Boolean) {
        binding.toolbar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}