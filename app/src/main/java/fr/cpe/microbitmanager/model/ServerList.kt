package fr.cpe.microbitmanager.model

import android.content.Context

object ServerList {
    private var serverList = ArrayList<ServerInfo>()

    fun init(context: Context) {
        val savedList = ServerStorage.loadServerList(context)
        serverList = ArrayList(savedList)  // Replace dummy list
    }

    fun addNewServer(context :Context, newServer : ServerInfo)
    {
        var isUpdated = false;
        serverList.forEach { serv ->
            if (serv.ip_address == newServer.ip_address)
            {
                serv.status = newServer.status
                serv.name = newServer.name
                isUpdated = true
            }
        }
        if(!isUpdated){
            serverList.add(newServer)
        }
        ServerStorage.saveServerList(context, serverList)
    }

    fun updateServerStatus(context: Context, ipAddress : String, status : Boolean)
    {
        serverList.forEach {
            if(it.ip_address == ipAddress)
            {
                it.status = status
            }
        }
    }

    fun removeServer(context :Context, server: ServerInfo)
    {
        serverList.remove(server)
        ServerStorage.saveServerList(context, serverList)
    }

    fun asList() : List<ServerInfo>
    {
        return serverList.toList()
    }

    fun size() : Int
    {
        return serverList.size
    }
}