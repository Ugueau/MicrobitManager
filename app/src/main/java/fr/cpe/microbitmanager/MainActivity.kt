package fr.cpe.microbitmanager

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import fr.cpe.microbitmanager.model.ServerInfo
import fr.cpe.microbitmanager.model.ServerList
import fr.cpe.microbitmanager.view.MicrobitFragment
import fr.cpe.microbitmanager.view.ServerAdapter
import fr.cpe.microbitmanager.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }

    val mainViewModel by viewModels<MainViewModel>()

    private val recyclerViewAdapter = ServerAdapter(this, this::onServerRefreshStatus)

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ServerList.init(this)
        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.server_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()

        val addBtn = findViewById<Button>(R.id.sendButton)
        val textviewIp = findViewById<TextInputEditText>(R.id.ip_txt)
        addBtn.setOnClickListener {
            mainViewModel.tryAccessToServer(this, textviewIp.text.toString()).observe(this){ isAccessible ->
                if(isAccessible)
                {
                    mainViewModel.addToServerList(this, textviewIp.text.toString(), true)
                    recyclerViewAdapter.updateList(mainViewModel.getServerList())
                    recyclerViewAdapter.notifyDataSetChanged()
                    textviewIp.setText("")
                    Toast.makeText(this, "Server ${textviewIp.text.toString()} added to list", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this, "Error server ${textviewIp.text.toString()} unreachable", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onServerRefreshStatus(server : ServerInfo)
    {
        mainViewModel.tryAccessToServer(this, server.ip_address).observe(this){status->
            server.status = status
            mainViewModel.updateServer(this, server)
            recyclerViewAdapter.updateList(mainViewModel.getServerList())
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    fun openServerDetails(server : ServerInfo)
    {
        val fragment = MicrobitFragment()
        val args = Bundle()
        args.putString("serverIp", server.ip_address)
        fragment.arguments = args

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_microbit, fragment)
            .addToBackStack(null)
            .commit()

    }
}