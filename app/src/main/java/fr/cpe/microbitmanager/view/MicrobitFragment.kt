package fr.cpe.microbitmanager.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitmanager.R
import fr.cpe.microbitmanager.databinding.FragmentMicrobitBinding
import fr.cpe.microbitmanager.model.MicrobitInfo
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.viewmodel.MainViewModel

class MicrobitFragment(private val server : ServerInfo) : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_microbit, container, false)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.microbit_recycler_view)
        val recyclerViewAdapter = MicrobitAdapter(this::openMicrobitConfig)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerViewAdapter

        mainViewModel.getMicrobitList(server.ip_address).observe(viewLifecycleOwner){ microbitsList ->
            recyclerViewAdapter.setNewMicrobitList(microbitsList)
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun openMicrobitConfig(microbit : MicrobitInfo)
    {
        val fragment = MicrobitConfigFragment(microbit)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}