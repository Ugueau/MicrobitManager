package fr.cpe.microbitmanager.model

object ServerList {
    private var serverList = ArrayList<ServerInfo>()

    fun addNewServer(newServer : ServerInfo)
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
    }

    fun asList() : List<ServerInfo>
    {
        return serverList.toList()
    }
}