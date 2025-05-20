package fr.cpe.microbitmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun tryAccessToServer(ipAddress : String): LiveData<Boolean>
    {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val data = true
            liveData.postValue(data)
        }
        return liveData
    }
}