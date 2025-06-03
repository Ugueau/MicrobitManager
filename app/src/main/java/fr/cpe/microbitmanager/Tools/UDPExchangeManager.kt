package fr.cpe.microbitmanager.Tools

import android.util.Base64
import fr.cpe.microbitmanager.model.MicrobitInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private val key = "1234567890abcdef".toByteArray(Charsets.UTF_8)

class UDPExchangeManager(
    private val ip: String,
    private val port: Int,
) {
    suspend fun isUdpServerReachable(timeoutMs: Int = 2000): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val socket = DatagramSocket()
                socket.soTimeout = timeoutMs

                val sendData = encrypt("is_reachable")
                val serverAddress = InetAddress.getByName(ip)
                val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, port)
                socket.send(sendPacket)

                // Wait for a response
                val buffer = ByteArray(1024)
                val receivePacket = DatagramPacket(buffer, buffer.size)
                socket.receive(receivePacket)
                socket.close()

                val responseText = decrypt(receivePacket.data)
                responseText == "1"
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

                val sendData = encrypt("get_microbits")
                val serverAddress = InetAddress.getByName(ip)
                val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, port)
                socket.send(sendPacket)

                // Wait for a response
                val buffer = ByteArray(2048)
                val receivePacket = DatagramPacket(buffer, buffer.size)
                socket.receive(receivePacket)
                val jsonString = decrypt(receivePacket.data) ?: return@withContext emptyList()
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

    suspend fun updateMicrobitConfiguration(microbit: MicrobitInfo, timeoutMs: Int = 2000) : Boolean
    {
        return withContext(Dispatchers.IO) {
            try {
                val socket = DatagramSocket()
                socket.soTimeout = timeoutMs

                val microbitJson = Gson().toJson(microbit)

                val sendData = encrypt("configuration :$microbitJson")

                val serverAddress = InetAddress.getByName(ip)
                val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, port)
                socket.send(sendPacket)

                true
            } catch (e: SocketTimeoutException) {
                false
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun encrypt(message: String) : ByteArray {
        val secretKey = SecretKeySpec(key, "AES")

        val iv = Random.Default.nextBytes(16)
        val ivSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encrypted = cipher.doFinal(message.toByteArray(Charsets.UTF_8))

        val ivBase64 = Base64.encodeToString(iv, Base64.NO_WRAP)
        val dataBase64 = Base64.encodeToString(encrypted, Base64.NO_WRAP)

        val json = ("""{"iv":"$ivBase64","data":"$dataBase64"}""").toByteArray()
        return json
    }

    private fun decrypt(response: ByteArray): String? {
        return try {
            val jsonString = String(response, Charsets.UTF_8)
            val json = JSONObject(jsonString)

            val ivBase64 = json.getString("iv")
            val dataBase64 = json.getString("data")

            val iv = Base64.decode(ivBase64, Base64.NO_WRAP)
            val encrypted = Base64.decode(dataBase64, Base64.NO_WRAP)

            val secretKey = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))

            val decryptedBytes = cipher.doFinal(encrypted)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

