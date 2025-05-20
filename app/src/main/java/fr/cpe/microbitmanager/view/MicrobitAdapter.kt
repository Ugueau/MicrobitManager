package fr.cpe.microbitmanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitmanager.R
import fr.cpe.microbitmanager.model.MicrobitInfo
import fr.cpe.microbitmanager.model.ServerInfo

class MicrobitAdapter : RecyclerView.Adapter<MicrobitAdapter.MicrobitViewHolder>() {
    var microbitList = emptyList<MicrobitInfo>()
    class MicrobitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

    }

}