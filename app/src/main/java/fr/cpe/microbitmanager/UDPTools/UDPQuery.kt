package fr.cpe.microbitmanager.UDPTools

import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

private const val PORT = 10000 // Constante arbitraire du sujet

class UDPQuery(msg : String, ipAddress : String) : Thread() {
    private var address: InetAddress? = null // Structure Java décrivant une adresse résolue

    private var UDPSocket: DatagramSocket? =
        null // Structure Java permettant d'accéder au réseau (UDP)

    private var msg: String = msg

    init {
        try {
            UDPSocket = DatagramSocket()
            Log.e("addr", ipAddress)
            address = InetAddress.getByName(ipAddress)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        sendMessage()
    }

    private fun sendMessage() {
        try {
            val data = msg.toByteArray()
            Log.e("multidata", data.size.toString())
            if (data.size >= 60000) {
                val dataFragments = data.toList().chunked(10).map { it.toByteArray() }
                dataFragments.forEach {
                    val packet = DatagramPacket(it, it.size, address, PORT)
                    UDPSocket!!.send(packet)
                }
            } else {
                val packet = DatagramPacket(data, data.size, address, PORT)
                Log.e("addr2", address.toString())
                UDPSocket!!.send(packet)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    public fun endUDP() {
        UDPSocket?.close()
        UDPSocket = null
    }

    public fun startUDP(msg: String, ipAddress: String) {
        this.msg = msg
        address = InetAddress.getByName(ipAddress)
        UDPSocket = DatagramSocket()
    }
}
