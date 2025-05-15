package fr.cpe.microbitmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    init {
        loadUser()
    }

    private fun loadUser() {
        // Simulate fetching user name
        _userName.value = "John Doe"
    }
}