package com.example.messengerapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean>(false)
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    init {
        Log.d("Lifecycle", "SettingsViewModel created")
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        Log.d("SettingsViewModel", "Dark mode: $enabled")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("Lifecycle", "SettingsViewModel cleared")
    }
}
