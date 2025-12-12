package com.example.messengerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Lifecycle", "onCreate MainActivity")

        settingsViewModel.isDarkMode.observe(this) { isDark ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val fragmentContainer =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navigationController = fragmentContainer.navController

        val navigationBar = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navigationBar.setupWithNavController(navigationController)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy MainActivity")
    }
}
