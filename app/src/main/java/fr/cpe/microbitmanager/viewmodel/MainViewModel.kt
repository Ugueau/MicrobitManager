package fr.cpe.microbitmanager.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cpe.microbitmanager.Tools.ConnectionManager
import fr.cpe.microbitmanager.Tools.UDPExchangeManager
import fr.cpe.microbitmanager.model.MicrobitInfo
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.model.ServerList
import kotlinx.coroutines.launch

const val PORT = 10000

class MainViewModel : ViewModel() {

    lateinit var watchedServer : ServerInfo

    fun restoreServerList(context: Context)
    {
        ServerList.init(context)
    }

    fun tryAccessToServer(context : Context, ipAddress : String): LiveData<Boolean>
    {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            if (ConnectionManager.checkInternetConnection(context))
            {
                val udpManager = UDPExchangeManager(ipAddress, PORT)
                val data = udpManager.isUdpServerReachable()
                ServerList.updateServerStatus(context, ipAddress, data)
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

    fun updateServerStatus(context: Context, server : ServerInfo)
    {
        ServerList.updateServerStatus(context, server.ip_address, server.status)
    }

    fun  getServerList() : List<ServerInfo>
    {
        return ServerList.asList()
    }

    fun getMicrobitList(ipAddress : String): LiveData<List<MicrobitInfo>>
    {
        watchedServer = ServerList.getServer(ipAddress)!!
        val liveData = MutableLiveData<List<MicrobitInfo>>()
        viewModelScope.launch {
            val udpManager = UDPExchangeManager(ipAddress, PORT)
            if (udpManager.isUdpServerReachable())
            {
                val data = udpManager.getMicrobits()
                liveData.postValue(data)
            }
        }
        return liveData
    }

    fun removeFromServerList(context: Context, server: ServerInfo)
    {
        ServerList.removeServer(context, server)
    }
}