package com.example.es.ui.screens

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.data.repository.ToDispatch
import com.example.es.databinding.FragmentSplashBinding
import com.example.es.ui.BaseFragment
import com.example.es.utils.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.tasks.await

class SplashFragment() : BaseFragment<FragmentSplashBinding>() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val scope = CoroutineScope(Job() + exceptionHandler)
    private val dispatchers: ToDispatch = ToDispatch.Base()
    private var firstTimeUser = false
    private lateinit var preferences: SharedPreferences

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = view.context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        firstTimeUser = preferences.getBoolean(PREF_BOOLEAN_VALUE, false)

        if (firstTimeUser) {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        } else {
            mBinding.btnLogin.setOnClickListener {
                val phone = mBinding.editTextPhone.text.toString().trim()
                val id = mBinding.editTextPassword.text.toString().trim().lowercase()
                if (phone.isBlank() || id.isBlank()) it.showSnackLong(R.string.fill_all_fields)
                else checkIfUserIsLoggedIn(phone = phone, id = id, view = view)

            }
        }
    }

    private fun checkIfUserIsLoggedIn(phone: String, id: String, view: View) {

        var data = DataCloud()

        dispatchers.launchIO(scope = scope) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener() {
                    data = it.result.getValue(DataCloud::class.java) ?: DataCloud()
                    val idExists = it.result.exists()
                    val phoneExists = phone == data.phone
                    firstTimeUser = idExists && phoneExists
                    preferences.edit().putBoolean(PREF_BOOLEAN_VALUE, firstTimeUser).apply()
                }.await()
            dispatchers.launchUI(scope) {
                if (firstTimeUser) {

                    preferences.edit().putString(PREF_PHONE_VALUE, phone).apply()
                    preferences.edit().putInt(PREF_ID_VALUE, id.toInt()).apply()

                    val bundle = Bundle()
                    bundle.putString("phone", data.phone)
                    bundle.putInt("id_number", data.id)
                    findNavController()
                        .navigate(R.id.action_splashFragment_to_mainFragment, bundle)
                } else view.showSnackLong(R.string.enter_correct_phone_password)
            }
        }
    }
}