package fr.cpe.microbitmanager.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import fr.cpe.microbitmanager.databinding.FragmentMicrobitBinding
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.viewmodel.MainViewModel

class MicrobitFragment(private val server : ServerInfo) : Fragment() {
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMicrobitBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val recyclerView: RecyclerView = view.findViewById(R.id.server_recycler_view)
        //val recyclerViewAdapter = ServerAdapter(requireContext())
        //recyclerView.adapter = recyclerViewAdapter


    }

}