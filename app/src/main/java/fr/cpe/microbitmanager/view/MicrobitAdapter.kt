package fr.cpe.microbitmanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitmanager.R
import fr.cpe.microbitmanager.model.MicrobitInfo
import fr.cpe.microbitmanager.model.ServerInfo

class MicrobitAdapter(private val opendMicrobitConfig: (MicrobitInfo) -> Unit) : RecyclerView.Adapter<MicrobitAdapter.MicrobitViewHolder>() {
    var microbitList = emptyList<MicrobitInfo>()
    class MicrobitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById<TextView>(R.id.microbit_name)
        val config: TextView = view.findViewById<TextView>(R.id.microbit_config)
        val temperature: TextView = view.findViewById<TextView>(R.id.temperature)
        val luminosity: TextView = view.findViewById<TextView>(R.id.luminosity)
        val humidity: TextView = view.findViewById<TextView>(R.id.humidity)
        val card : ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.microbit_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MicrobitViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_microbit   , parent, false)
        return MicrobitViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return microbitList.size
    }

    override fun onBindViewHolder(holder: MicrobitViewHolder, position: Int) {
        val microbit = microbitList[position]
        holder.name.text = "${microbit.name}:${microbit.id}"
        holder.config.text = microbit.formatConfig()
        holder.temperature.text = "T : ${microbit.temperature}°C"
        holder.luminosity.text = "L : ${microbit.luminosity} lux"
        holder.humidity.text = "H : ${microbit.humidity} g.m-3"
        holder.card.setOnClickListener {
            opendMicrobitConfig(microbit)
        }

    }

    fun setNewMicrobitList(newMicrobitList : List<MicrobitInfo>)
    {
        this.microbitList = newMicrobitList
    }

}