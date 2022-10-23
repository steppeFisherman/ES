package com.example.es.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.es.R
import com.example.es.databinding.ActivityMainBinding
import com.example.es.utils.*
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navControllerMain: NavController

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        initFirebase()
        initialise()
//        bottomNavIconSetup()
//        createUser()
    }


    private fun initialise() {
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControllerMain = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navControllerMain)
    }

    private fun bottomNavIconSetup() {
        binding.bottomNavigation.menu.clear()
        val view = LayoutInflater.from(this)
            .inflate(R.layout.bottom_nav_main_icon, binding.bottomNavigation, false)
        binding.bottomNavigation.addView(view)
        binding.bottomNavigation.inflateMenu(R.menu.menu_bottom_navigation)
    }


//    private fun createUser() {
//        val fullName = "Ivanov I"
//        val phone = "+7 916 800 00 20"
//        val id = ""
//        val time = ""
//        val lat = "latitude"
//        val lng = "latitude"
//
//        val data = mutableMapOf<String, Any>()
//
//        data[CHILD_ID] = id
//        data[CHILD_TIME] = time
//        data[CHILD_FULL_NAME] = fullName
//        data[CHILD_PHONE] = phone
//        data[CHILD_LATITUDE] = lat
//        data[CHILD_LONGITUDE] = lng
//
//        REF_DATABASE_ROOT.child(NODE_USERS).child(phone).updateChildren(data)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "isSuccessful", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(
//                        this,
//                        task.exception?.message.toString(), Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//    }
}
