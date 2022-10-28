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
import com.example.es.utils.APP_PREFERENCES
import com.example.es.utils.PREF_BOOLEAN_VALUE
import com.example.es.utils.REF_DATABASE_ROOT
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navControllerMain: NavController
    private lateinit var destinationChangedListener:
            NavController.OnDestinationChangedListener
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var preferences: SharedPreferences
    private var firstTimeUser = false


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
//        createUser(binding.root)

    }

    private fun initialise() {
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControllerMain = navHostFragment.navController
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navControllerMain)
    }

    private fun checkUserLoggedIn() {
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        firstTimeUser = preferences.getBoolean(PREF_BOOLEAN_VALUE, false)
        if (!firstTimeUser) {
            navControllerMain.navigate(R.id.action_mainFragment_to_splashFragment)
        } else return
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

//    override fun onDestroy() {
//        super.onDestroy()
//        usersService.removeListener(usersListener)
//    }
//
//    private fun createUser(view: View) {
//        REF_DATABASE_ROOT.child(NODE_USERS).updateChildren(mapDataCloud)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) view.showSnackLong(mapDataCloud.size)
//                else view.showSnackLong(R.string.failure_update)
//            }
//    }
//
//
//    private val usersListener: UsersListener = {
//       it.forEach { dataCloud ->
//           val data = mutableMapOf<String, Any>()
//        data[CHILD_ID] = dataCloud.id
//        data[CHILD_TIME] = dataCloud.time
//        data[CHILD_FULL_NAME] = dataCloud.full_name
//        data[CHILD_PHONE] = dataCloud.phone
//        data[CHILD_LATITUDE] = dataCloud.latitude
//        data[CHILD_LONGITUDE] = dataCloud.longitude
//
//                   REF_DATABASE_ROOT.child(NODE_USERS).child(dataCloud.id.toString()).updateChildren(data)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) view.showSnackLong(mapDataCloud.size)
//                else view.showSnackLong(R.string.failure_update)
//            }
//       }
//    }
}
