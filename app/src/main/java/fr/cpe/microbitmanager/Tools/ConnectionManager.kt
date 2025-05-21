package fr.cpe.microbitmanager.Tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException

class ConnectionManager {
    companion object {

        suspend fun checkInternetConnection(context: Context): Boolean{
            if (Build.DEVICE.contains("emulator")){
                return true
            }

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)

            if (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
                var success = false
                isInternetReachable().collect{
                    success = it
                }
                return success
            }
            return false
        }

        private fun isInternetReachable():  Flow<Boolean> = flow {
            try {
                // Use a reliable server, such as Google's DNS server (8.8.8.8), for the connectivity check
                val address = InetAddress.getByName("8.8.8.8")
                val success = address.isReachable(3000)  // 3 seconds timeout
                emit(success)
            } catch (e: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }
}