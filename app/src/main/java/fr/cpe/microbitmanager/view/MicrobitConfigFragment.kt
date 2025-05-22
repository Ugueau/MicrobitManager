package fr.cpe.microbitmanager.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitmanager.R
import androidx.recyclerview.widget.ItemTouchHelper
import fr.cpe.microbitmanager.model.MicrobitInfo
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.viewmodel.MainViewModel
import android.widget.Toast

class MicrobitConfigFragment(
    private val microbit : MicrobitInfo,
    private val server: ServerInfo
)
    : Fragment() {
        private lateinit var mainViewModel : MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_microbit_config, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.microbit_config_recyclerview)
        val recyclerViewAdapter = MicrobitConfigAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerViewAdapter
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val microbit_name: TextView = view.findViewById(R.id.microbit_config_name)
        microbit_name.text = "${microbit.name}:${microbit.id}"
        val microbit_order: TextView = view.findViewById(R.id.microbit_config_order)
        microbit_order.text = "Current configuration : ${microbit.formatConfig()}"

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                recyclerViewAdapter.onItemMove(fromPosition, toPosition)
                return true
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1f

                val configOrder = recyclerViewAdapter.getCurrentOrder()

                microbit.temperatureConfigIndex = configOrder.indexOf("temperature")
                microbit.humidityConfigIndex = configOrder.indexOf("humidity")
                microbit.luminosityConfigIndex = configOrder.indexOf("luminosity")

                val microbit_order: TextView = view.findViewById(R.id.microbit_config_order)
                microbit_order.text = "Current configuration : ${microbit.formatConfig()}"
                viewModel.updateMicrobitConfiguration(server, microbit)
                    .observe(viewLifecycleOwner) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Configuration envoyée en UDP", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "e: Échec de l'envoi UDP", Toast.LENGTH_SHORT).show()
                        }
                    }
            }


            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.8f
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
