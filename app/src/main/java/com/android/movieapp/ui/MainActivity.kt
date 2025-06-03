package com.android.movieapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.android.movieapp.R
import com.android.movieapp.utils.ConnectivityMonitor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityMonitor: ConnectivityMonitor

    private var noInternetDialog: NoInternetDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                connectivityMonitor.isConnected.collect { connected ->
                    if (!connected && noInternetDialog == null) {
                        noInternetDialog = NoInternetDialogFragment()
                        noInternetDialog!!.show(
                            supportFragmentManager,
                            NoInternetDialogFragment.TAG
                        )
                    } else if (connected) {
                        noInternetDialog?.dismissAllowingStateLoss()
                        noInternetDialog = null

                        val currentFragment = supportFragmentManager
                            .primaryNavigationFragment
                            ?.childFragmentManager
                            ?.fragments
                            ?.firstOrNull()

                        if (currentFragment is ConnectivityRestoredListener) {
                            currentFragment.onConnectivityRestored()
                        }
                    }
                }
            }
        }
    }
}
