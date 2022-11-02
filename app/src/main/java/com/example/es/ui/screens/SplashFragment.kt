package com.example.es.ui.screens

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.data.repository.ToDispatch
import com.example.es.databinding.FragmentSplashBinding
import com.example.es.ui.BaseFragment
import com.example.es.ui.MainActivityViewModel
import com.example.es.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData

    @Inject
    lateinit var snackTopBuilder: SnackBuilder

    private lateinit var snack: Snackbar
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val scope = CoroutineScope(Job() + exceptionHandler)
    private val dispatchers: ToDispatch = ToDispatch.Base()

    private var userExists = false
    private var extracted = ""

    //    private var phoneNumberToSave = ""
    private var idEntered = ""
    private var phoneEntered = ""
    private lateinit var preferences: SharedPreferences

    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()

        activityViewModel.user.observe(viewLifecycleOwner) { dataUi ->
            val idFetched = dataUi.id.toString()
            val phoneFetched = dataUi.phone_user
            val nameFetched = dataUi.full_name

            userExists = (idFetched == idEntered && phoneFetched == phoneEntered)
            preferences.edit().putBoolean(PREF_BOOLEAN_VALUE, userExists).apply()
//            preferences.edit().putString(PREF_ID_VALUE, idExists).apply()
//            preferences.edit().putString(PREF_PHONE_VALUE, phoneExists).apply()
//
            if (userExists) {
                preferences.edit().putString(PREF_ID_VALUE, idFetched).apply()
                preferences.edit().putString(PREF_PHONE_VALUE, phoneFetched).apply()
                preferences.edit().putString(PREF_FULL_NAME_VALUE, nameFetched).apply()

                findNavController()
                    .navigate(R.id.action_splashFragment_to_mainFragment)
            } else {
                view.snackLongTop(R.string.enter_correct_phone_password)
            }
        }

        activityViewModel.error.observe(viewLifecycleOwner) { errorType ->
            when (errorType.ordinal) {
                0 -> view.snackLongTop(R.string.no_connection_exception_message)
                1 -> view.snackLongTop(R.string.firebase_exception_message)
                2 -> view.snackLongTop(R.string.http_exception_message)
                3 -> view.snackLongTop(R.string.user_not_registered_exception_message)
                4 -> view.snackLongTop(R.string.generic_exception_message)
            }
        }

        inputMaskSetUp()

        binding.btnLogin.setOnClickListener {
            val phone = binding.editTextPhone.text.toString().trim()
            idEntered = binding.editTextPassword.text.toString().trim()

            val splitPhone = phone.split(" ")
            phoneEntered = splitPhone[0] + extracted

            if (phoneEntered.isBlank() || idEntered.isBlank()) it.snackLongTop(R.string.fill_all_fields)
            else {
                checkNetworks(connectionLiveData) { isNetWorkAvailable ->
                    when (isNetWorkAvailable) {
                        false -> snack.show()
                        true -> {
                            activityViewModel.fetchData(id = idEntered, phone = phoneEntered)
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
        var isNetWorkAvailable: Boolean = true
        connection.checkValidNetworks()
        connection.observe(viewLifecycleOwner) {
            isNetWorkAvailable = it
            connected(isNetWorkAvailable)
        }
        return isNetWorkAvailable
    }
}
