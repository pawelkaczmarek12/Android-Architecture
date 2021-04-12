package pl.edu.wat.androidarchitecture.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.wat.androidarchitecture.R
import pl.edu.wat.androidarchitecture.data.service.ConnectionManager
import pl.edu.wat.androidarchitecture.databinding.ActivityMainBinding
import pl.edu.wat.androidarchitecture.model.internal.Status
import pl.edu.wat.androidarchitecture.ui.custom.Show
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var connectionManager: ConnectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))

        connectionManager.registerNetworkCallback(applicationContext, {
            runOnUiThread {
                binding.connectionStatus.visibility = View.VISIBLE
                binding.connectionStatus.setText(R.string.no_connection)
            }
        }, {
            runOnUiThread {
                binding.connectionStatus.visibility = View.GONE
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sync, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_sync) {
            viewModel.sync().observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        Show.info(binding.mainContent, "Syncing complete!")
                    }
                    Status.ERROR -> {
                        Show.error(binding.mainContent, it.message)
                    }
                    Status.LOADING -> {
                        Show.info(binding.mainContent, "Synchronizing...")
                    }
                }
            })
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
