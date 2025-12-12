package com.example.messengerapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "onCreate SettingsFragment")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themeSwitcher = view.findViewById<MaterialSwitch>(R.id.theme_switch)

        viewModel.isDarkMode.observe(viewLifecycleOwner) { isDark ->
            if (themeSwitcher.isChecked != isDark) {
                themeSwitcher.isChecked = isDark
            }
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy SettingsFragment")
    }
}
