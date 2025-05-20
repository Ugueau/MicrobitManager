package fr.cpe.microbitmanager.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cpe.microbitmanager.Tools.ConnectionManager
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.model.ServerList
import kotlinx.coroutines.launch

const val PORT = 10000

class MainViewModel : ViewModel() {

    fun tryAccessToServer(context : Context, ipAddress : String): LiveData<Boolean>
    {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            if (ConnectionManager.checkInternetConnection(context))
            {
                val data = ConnectionManager.isUdpServerReachable(ipAddress, PORT)
                liveData.postValue(data)
            }
        }
        return liveData
    }

    fun addToServerList(context :Context, ipAddress : String, status : Boolean)
    {
        val name = "Server" + ServerList.size() + 1
        ServerList.addNewServer(context, ServerInfo(name, ipAddress, status))
    }

    fun updateServer(context: Context, server : ServerInfo)
    {
        ServerList.addNewServer(context, server)
    }

    fun  getServerList() : List<ServerInfo>
    {
        return ServerList.asList()
    }
}