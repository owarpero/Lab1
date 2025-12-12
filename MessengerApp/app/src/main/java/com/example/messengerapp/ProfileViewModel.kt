package com.example.messengerapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _name = MutableLiveData<String>("")
    val name: LiveData<String> = _name

    private val _status = MutableLiveData<String>("")
    val status: LiveData<String> = _status

    init {
        Log.d("Lifecycle", "ProfileViewModel created")
    }

    fun updateName(newName: String) {
        _name.value = newName
        Log.d("ProfileViewModel", "Name updated: $newName")
    }

    fun updateStatus(newStatus: String) {
        _status.value = newStatus
        Log.d("ProfileViewModel", "Status updated: $newStatus")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("Lifecycle", "ProfileViewModel cleared")
    }
}
