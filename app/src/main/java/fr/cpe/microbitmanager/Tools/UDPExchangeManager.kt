package fr.cpe.microbitmanager.Tools

import android.util.Log
import fr.cpe.microbitmanager.model.MicrobitInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val PORT = 10000 // Constante arbitraire du sujet

class UDPExchangeManager(
    private val ip: String,
    private val port: Int,
) {
    suspend fun isUdpServerReachable(timeoutMs: Int = 2000): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val socket = DatagramSocket()
                socket.soTimeout = timeoutMs

                val sendData = "is_reachable".toByteArray()
                val serverAddress = InetAddress.getByName(ip)
                val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, port)
                socket.send(sendPacket)

                // Wait for a response
                val buffer = ByteArray(1024)
                val receivePacket = DatagramPacket(buffer, buffer.size)
                socket.receive(receivePacket)
                socket.close()

                true
            } catch (e: SocketTimeoutException) {
                false // No response in time
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun getMicrobits(timeoutMs: Int = 2000) : List<MicrobitInfo>
    {
        return withContext(Dispatchers.IO) {
            try {
                val socket = DatagramSocket()
                socket.soTimeout = timeoutMs

                val sendData = "get_microbits".toByteArray()
                val serverAddress = InetAddress.getByName(ip)
                val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, port)
                socket.send(sendPacket)

                // Wait for a response
                val buffer = ByteArray(2048)
                val receivePacket = DatagramPacket(buffer, buffer.size)
                socket.receive(receivePacket)
                val jsonString = String(receivePacket.data, 0, receivePacket.length, Charsets.UTF_8)
                val listType = object : TypeToken<List<MicrobitInfo>>() {}.type
                val microbitList: List<MicrobitInfo> = Gson().fromJson(jsonString, listType)
                socket.close()

                microbitList
            } catch (e: SocketTimeoutException) {
                emptyList<MicrobitInfo>() // No response in time
            } catch (e: Exception) {
                emptyList<MicrobitInfo>()
            }
        }
    }
}

