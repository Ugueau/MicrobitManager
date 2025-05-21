package fr.cpe.microbitmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import fr.cpe.microbitmanager.R
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.view.ServerAdapter
import fr.cpe.microbitmanager.viewmodel.MainViewModel

class ServerFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerViewAdapter: ServerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_server, container, false)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.restoreServerList(requireContext())
        recyclerViewAdapter = ServerAdapter(this::onServerRefreshStatus, this::openServerDetails, this::removeServer)

        val recyclerView: RecyclerView = view.findViewById(R.id.server_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.updateList(mainViewModel.getServerList())
        recyclerViewAdapter.notifyDataSetChanged()

        val addBtn = view.findViewById<Button>(R.id.sendButton)
        val textviewIp = view.findViewById<TextInputEditText>(R.id.ip_txt)

        addBtn.setOnClickListener {
            val ip = textviewIp.text.toString()
            mainViewModel.tryAccessToServer(requireContext(), ip).observe(viewLifecycleOwner) { isAccessible ->
                if (isAccessible) {
                    mainViewModel.addToServerList(requireContext(), ip, true)
                    recyclerViewAdapter.updateList(mainViewModel.getServerList())
                    recyclerViewAdapter.notifyDataSetChanged()
                    textviewIp.setText("")
                    Toast.makeText(requireContext(), "Server $ip added to list", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error: server $ip unreachable", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun onServerRefreshStatus(server: ServerInfo) {
        mainViewModel.tryAccessToServer(requireContext(), server.ip_address) // tryToAccessToServer auto update status
            .observe(viewLifecycleOwner) { status ->
                recyclerViewAdapter.updateList(mainViewModel.getServerList())
                recyclerViewAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Server status : $status", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openServerDetails(server : ServerInfo)
    {
        mainViewModel.tryAccessToServer(requireContext(), server.ip_address).observe(this)
        { status ->
            recyclerViewAdapter.updateList(mainViewModel.getServerList())
            recyclerViewAdapter.notifyDataSetChanged()
            if(status)
            {
                val fragment = MicrobitFragment(server)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    fun removeServer(server: ServerInfo)
    {
        mainViewModel.removeFromServerList(requireContext(), server)
        recyclerViewAdapter.updateList(mainViewModel.getServerList())
        recyclerViewAdapter.notifyDataSetChanged()
    }
}
