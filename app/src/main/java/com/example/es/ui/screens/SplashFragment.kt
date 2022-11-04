package com.example.es.ui.screens

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.databinding.FragmentSplashBinding
import com.example.es.ui.BaseFragment
import com.example.es.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData

    @Inject
    lateinit var snackTopBuilder: SnackBuilder

    private var extracted = ""
    private var idEntered = ""
    private var phoneEntered = ""
    private lateinit var preferences: SharedPreferences
    private lateinit var snack: Snackbar

    private val vm by activityViewModels<SplashFragmentViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        inputMaskSetUp()

        vm.userAuth.observe(viewLifecycleOwner) { dataUi ->
            binding.progressBar.visibility = View.GONE
            val idFetched = dataUi.id.toString()
            val phoneFetched = dataUi.phone_user

            val userExists = (idFetched == idEntered && phoneFetched == phoneEntered)
            preferences.edit().putBoolean(PREF_BOOLEAN_VALUE, userExists).apply()

            if (userExists) {
                preferences.edit().putString(PREF_ID_VALUE, idEntered).apply()
                val bundle = Bundle()
                bundle.putParcelable(MainFragment.ARGS, dataUi)
                findNavController()
                    .navigate(R.id.action_splashFragment_to_mainFragment, bundle)
            } else {
                view.snackLongTop(R.string.enter_correct_phone_password)
            }
        }

        vm.error.observe(viewLifecycleOwner) { errorType ->
            binding.progressBar.visibility = View.GONE
            when (errorType.ordinal) {
                0 -> view.snackLongTop(R.string.no_connection_exception_message)
                1 -> view.snackLongTop(R.string.firebase_exception_message)
                2 -> view.snackLongTop(R.string.http_exception_message)
                3 -> view.snackLongTop(R.string.user_not_registered_exception_message)
                4 -> view.snackLongTop(R.string.generic_exception_message)
            }
        }

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val phone = binding.editTextPhone.text.toString().trim()
            val splitPhone = phone.split(" ")
            phoneEntered = splitPhone[0] + extracted
            idEntered = binding.editTextPassword.text.toString().trim()

            if (phoneEntered.isBlank() || idEntered.isBlank()) it.snackLongTop(R.string.fill_all_fields)
            else {
                checkNetworks(connectionLiveData) { isNetWorkAvailable ->
                    when (isNetWorkAvailable) {
                        false -> {
                            it.isEnabled = false
                            snack.show()
                        }
                        true -> {
                            it.isEnabled = true
                            vm.fetchData(id = idEntered, phone = phoneEntered)
                            snack.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun initialise() {
        connectionLiveData = ConnectionLiveData(binding.root.context)
        preferences = binding.root.context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        snack = snackTopBuilder.buildSnackTopIndefinite(binding.root)
    }

    private fun inputMaskSetUp() {
        val listener = InputMaskHandle(binding.editTextPhone,
            ValueListener { extractedValue -> extracted = extractedValue })

        binding.editTextPhone.addTextChangedListener(listener)
        binding.editTextPhone.onFocusChangeListener = listener
    }

    private fun checkNetworks(
        connection: ConnectionLiveData,
        connected: (Boolean) -> Unit
    ): Boolean {
        var isNetWorkAvailable = true
        connection.checkValidNetworks()
        connection.observe(viewLifecycleOwner) {
            isNetWorkAvailable = it
            connected(isNetWorkAvailable)
        }
        return isNetWorkAvailable
    }
}


