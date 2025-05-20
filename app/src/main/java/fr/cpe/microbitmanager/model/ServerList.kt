package fr.cpe.microbitmanager.model

import android.content.Context

object ServerList {
    private var serverList = ArrayList<ServerInfo>()

    init {
        serverList.add(ServerInfo("example", "example IP", false))
    }

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

    fun asList() : List<ServerInfo>
    {
        return serverList.toList()
    }

    fun size() : Int
    {
        return serverList.size
    }
}