package fr.cpe.microbitmanager.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitmanager.R
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.model.ServerList

class ServerAdapter(val context : Context): RecyclerView.Adapter<ServerAdapter.ServerViewHolder>() {
    private var serverList = emptyList<ServerInfo>()
    class ServerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serverStatus: TextView = view.findViewById(R.id.status)
        val serverIp: TextView = view.findViewById(R.id.ip_address)
        val refreshBtn: Button = view.findViewById(R.id.refresh)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_server   , parent, false)
        return ServerViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return this.serverList.size
    }

    // TODO add refresh button action
    override fun onBindViewHolder(holder: ServerViewHolder, position: Int) {
        val server = serverList[position]
        holder.serverIp.text = server.ip_address
        holder.refreshBtn.setOnClickListener{
            Toast.makeText(
                this.context,
                "Not Implemented yet !",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    fun updateList(newServList : List<ServerInfo>)
    {
        this.serverList = newServList
    }
}