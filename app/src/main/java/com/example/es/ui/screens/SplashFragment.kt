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
import com.example.es.utils.connectivity.ConnectivityManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

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
            val idFetched = dataUi.id
            val phoneFetched = dataUi.phoneUser

            val userExists = (idFetched == idEntered && phoneFetched == phoneEntered)
            preferences.edit().putBoolean(PREF_BOOLEAN_VALUE, userExists).apply()

            if (userExists) {
                preferences.edit().putString(PREF_ID_VALUE, idEntered).apply()
                preferences.edit().putString(PREF_PHONE_VALUE, phoneEntered).apply()
                preferences.edit().putString(PREF_COMPANY_VALUE, dataUi.company).apply()
                preferences.edit().putString(PREF_HOME_ADDRESS_VALUE, dataUi.homeAddress).apply()
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
                1 -> view.snackLongTop(R.string.database_exception_message)
                2 -> view.snackLongTop(R.string.http_exception_message)
                3 -> view.snackLongTop(R.string.user_not_registered_exception_message)
                4 -> view.snackLong(R.string.database_exception_message)
                5 -> view.snackLongTop(R.string.generic_exception_message)
            }
        }

        connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner) { isNetworkAvailable ->
            if (isNetworkAvailable == false) {
                binding.btnLogin.isEnabled = false
                snack.show()
            } else {
                snack.dismiss()
                binding.btnLogin.isEnabled = true
                binding.btnLogin.setOnClickListener {
                    val phone = binding.editTextPhone.text.toString().trim()
                    val splitPhone = phone.split(" ")
                    phoneEntered = splitPhone[0] + extracted
                    idEntered = binding.editTextPassword.text.toString().trim()

                    if (phoneEntered.isBlank() || idEntered.isBlank()) it.snackLongTop(R.string.fill_all_fields)
                    else {
                        vm.fetchData(id = idEntered, phone = phoneEntered)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initialise() {
        preferences = binding.root.context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        snack = snackTopBuilder.buildSnackTopIndefinite(binding.root)
    }

    private fun inputMaskSetUp() {
        val listener = InputMaskHandle(binding.editTextPhone,
            ValueListener { extractedValue -> extracted = extractedValue })

        binding.editTextPhone.addTextChangedListener(listener)
        binding.editTextPhone.onFocusChangeListener = listener
    }
}


