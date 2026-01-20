package com.example.messengerapp.ui.feed

import android.util.Log
import androidx.lifecycle.*
import com.example.messengerapp.data.local.MessageEntity
import com.example.messengerapp.data.repository.MessageRepository
import com.example.messengerapp.data.repository.Resource
import kotlinx.coroutines.launch

class FeedViewModel(private val repository: MessageRepository) : ViewModel() {

    val messages: LiveData<List<MessageEntity>> = repository.messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        Log.d("Lifecycle", "FeedViewModel created")
        refreshMessages()
    }

    fun refreshMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.refreshMessages()) {
                is Resource.Success -> {
                    Log.d("FeedViewModel", "Refresh successful")
                }
                is Resource.Error -> {
                    Log.e("FeedViewModel", "Error: ${result.message}")
                    _errorMessage.value = result.message

                    if (!repository.hasCachedData()) {
                        _errorMessage.value = "No internet and no cached data"
                    }
                }
                is Resource.Loading -> {}
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("Lifecycle", "FeedViewModel cleared")
    }
}

class FeedViewModelFactory(
    private val repository: MessageRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
