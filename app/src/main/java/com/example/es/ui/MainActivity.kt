package com.example.es.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.es.R
import com.example.es.databinding.ActivityMainBinding
import com.example.es.ui.screens.MainFragment
import com.example.es.ui.screens.ProfileFragment
import com.example.es.utils.APP_PREFERENCES
import com.example.es.utils.Navigator
import com.example.es.utils.PREF_BOOLEAN_VALUE
import com.example.es.utils.REF_DATABASE_ROOT
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProfileFragment.PhotoListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var navControllerMain: NavController
    private lateinit var destinationChangedListener:
            NavController.OnDestinationChangedListener
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var preferences: SharedPreferences

//    private val usersService: UsersService
//        get() = (applicationContext as App).usersService
//    private val mapDataCloud = mutableMapOf<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ES)
        binding = ActivityMainBinding.inflate(layoutInflater)
        /**
        Changes statusBar text/icons color, when the statusBar background is white or transparent
        (this works with API level 21 and higher)
         */
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = true
        setContentView(binding.root)

        initialise()
        checkUserLoggedIn()
        displayBottomNav()
//        usersService.addListener(usersListener)
    }

    private fun initialise() {
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControllerMain = navHostFragment.navController
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navControllerMain)
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun checkUserLoggedIn() {
        val userLoggedIn = preferences.getBoolean(PREF_BOOLEAN_VALUE, false)
        if (!userLoggedIn) {
            navControllerMain.navigate(R.id.action_mainFragment_to_splashFragment)
        }
    }

    private fun displayBottomNav() {
        destinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.mainFragment -> bottomNavigationView.visibility = View.VISIBLE
                    R.id.splashFragment -> bottomNavigationView.visibility = View.GONE
                }
            }
    }

    override fun onResume() {
        super.onResume()
        navControllerMain.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navControllerMain.removeOnDestinationChangedListener(destinationChangedListener)
    }

    override fun photoListener(photo: String) {
        MainFragment.newInstance(photo)
    }
}

