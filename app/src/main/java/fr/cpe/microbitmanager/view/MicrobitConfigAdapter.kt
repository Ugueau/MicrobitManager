package fr.cpe.microbitmanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitmanager.R

data class MicrobitConfigItem(
    val type: String // "temperature", "humidity", or "luminosity"
)

class MicrobitConfigAdapter(
    private val configList: MutableList<MicrobitConfigItem>
) : RecyclerView.Adapter<MicrobitConfigAdapter.ConfigViewHolder>() {

    inner class ConfigViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.config_item_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_microbit_config, parent, false)
        return ConfigViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConfigViewHolder, position: Int) {
        val item = configList[position]
        holder.label.text = item.type.replaceFirstChar { it.uppercaseChar() }
    }

    override fun getItemCount(): Int = configList.size

    fun onItemMove(from: Int, to: Int) {
        val movedItem = configList.removeAt(from)
        configList.add(to, movedItem)
        notifyItemMoved(from, to)
    }

    fun getCurrentOrder(): List<String> {
        return configList.map { it.type }
    }
}
