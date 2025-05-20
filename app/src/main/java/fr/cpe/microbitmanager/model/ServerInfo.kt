package fr.cpe.microbitmanager.model

data class ServerInfo(
    var name : String,
    var ip_address : String,
    var status : Boolean,
) {
    fun updateStatus(status : Boolean)
    {
        this.status = status
    }
}