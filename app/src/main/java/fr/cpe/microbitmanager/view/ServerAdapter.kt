package fr.cpe.microbitmanager.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitmanager.R
import fr.cpe.microbitmanager.model.ServerInfo
import androidx.constraintlayout.widget.ConstraintLayout

class ServerAdapter(val context : Context, private val onRefreshClicked: (ServerInfo) -> Unit): RecyclerView.Adapter<ServerAdapter.ServerViewHolder>() {
    private var serverList = emptyList<ServerInfo>()
    class ServerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serverStatus: TextView = view.findViewById(R.id.status)
        val serverIp: TextView = view.findViewById(R.id.ip_address)
        val refreshBtn: Button = view.findViewById(R.id.refresh)
        val serverCard : ConstraintLayout = view.findViewById(R.id.server_card)
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
        holder.serverStatus.text = if(server.status) "✅" else "❌"
        holder.refreshBtn.setOnClickListener{
            onRefreshClicked(server)
        }
        holder.serverCard.setOnClickListener{
            // TODO open fragment with microbits
        }
    }

    fun updateList(newServList : List<ServerInfo>)
    {
        this.serverList = newServList
    }
}