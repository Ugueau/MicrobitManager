package fr.cpe.microbitmanager.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ServerStorage {
    private const val PREF_NAME = "server_prefs"
    private const val KEY_LIST = "server_list"

    fun saveServerList(context: Context, list: List<ServerInfo>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(list)
        prefs.edit().putString(KEY_LIST, json).apply()
    }

    fun loadServerList(context: Context): List<ServerInfo> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<ServerInfo>>() {}.type
        return Gson().fromJson(json, type)
    }
}
