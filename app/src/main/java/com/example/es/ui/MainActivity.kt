package com.example.es.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.es.R
import com.example.es.databinding.ActivityMainBinding
import com.example.es.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var snack: Snackbar
    private lateinit var navControllerMain: NavController
    private lateinit var destinationChangedListener:
            NavController.OnDestinationChangedListener
    private lateinit var bottomNavigationView: BottomNavigationView

    //    private lateinit var preferences: SharedPreferences
//    private var userExists = false
//    private var userId = ""
    private val vm by viewModels<MainActivityViewModel>()
    private val snackTopBuilder = SnackBuilder.Base()


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
        connectionLiveData = ConnectionLiveData(this)
        snack = snackTopBuilder.buildSnackTopIndefinite(binding.root)

//        snack = Snackbar
//            .make(
//                binding.root,
//                R.string.check_internet_connection,
//                Snackbar.LENGTH_INDEFINITE
//            )
//        val layoutParams = FrameLayout.LayoutParams(snack.view.layoutParams)
//
//        layoutParams.gravity = Gravity.TOP
//        layoutParams.marginStart = 40
//        layoutParams.marginEnd = 40
//        snack.view.setPadding(0, 8, 0, 8)
//        snack.view.layoutParams = layoutParams
//        snack.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE


//        mSnack = Snackbar
//            .make(binding.root, "Проверьте наличие интернета", Snackbar.LENGTH_INDEFINITE)
//
//        val view: View = mSnack.view
//        val txt =
//            view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
//        txt.textAlignment = View.TEXT_ALIGNMENT_CENTER


        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControllerMain = navHostFragment.navController
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navControllerMain)
    }

    private fun checkUserLoggedIn() {
        val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val userLoggedIn = preferences.getBoolean(PREF_BOOLEAN_VALUE, false)
//        val userId = preferences.getString(PREF_ID_VALUE, "").toString()
//        val userPhone = preferences.getString(PREF_PHONE_VALUE, "").toString()
        if (!userLoggedIn) {
            navControllerMain.navigate(R.id.action_mainFragment_to_splashFragment)
        }
//        else {
//            vm.fetchData(id = userId, phone = userPhone)
//        }
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

    private fun checkNetWorkConnection() {
        connectionLiveData.checkValidNetworks()
        connectionLiveData.observe(this) { isNetWorkAvailable ->
            when (isNetWorkAvailable) {
                false -> snack.show()
                true -> snack.dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
//          checkNetWorkConnection()

    }

    override fun onResume() {
        super.onResume()
        navControllerMain.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navControllerMain.removeOnDestinationChangedListener(destinationChangedListener)
    }


//
//    override fun checkConnection() {
//        connectionLiveData.checkValidNetworks()
//        connectionLiveData.observe(this) { isNetWorkAvailable ->
//            when (isNetWorkAvailable) {
//                false -> binding.root
//                    .snackIndefiniteTop(R.string.check_internet_connection)
//                true -> binding.root
//                    .snackIndefiniteTop(R.string.check_internet_connection,false)
//            }
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        usersService.removeListener(usersListener)
//    }


//    private val usersListener: UsersListener = {
//        it.forEach { dataCloud ->
//            mapDataCloud[CHILD_ID] = dataCloud.id
//            mapDataCloud[CHILD_FULL_NAME] = dataCloud.full_name
//            mapDataCloud[CHILD_PHONE_USER] = dataCloud.phone_user
//            mapDataCloud[CHILD_PHONE_OPERATOR] = dataCloud.phone_operator
//            mapDataCloud[CHILD_PHOTO] = dataCloud.photo
//            mapDataCloud[CHILD_TIME] = dataCloud.time
//            mapDataCloud[CHILD_LATITUDE] = dataCloud.latitude
//            mapDataCloud[CHILD_LONGITUDE] = dataCloud.longitude
//            mapDataCloud[CHILD_ALARM] = dataCloud.alarm
//            mapDataCloud[CHILD_NOTIFY] = dataCloud.notify
//
//            REF_DATABASE_ROOT.child(NODE_USERS).child(dataCloud.id.toString())
//                .updateChildren(mapDataCloud)
//                .addOnCompleteListener { task -> }
//        }
//    }

//    override fun listen() = checkNetWorkConnection()
}
