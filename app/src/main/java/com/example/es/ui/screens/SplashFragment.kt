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
    private lateinit var mSnack: Snackbar
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val scope = CoroutineScope(Job() + exceptionHandler)
    private val dispatchers: ToDispatch = ToDispatch.Base()
    private var userExists = false
    private var extracted = String()
    private var phoneNumberToSave = String()
    private var id = String()
    private lateinit var preferences: SharedPreferences

    private val activityViewModel by activityViewModels<MainActivityViewModel>()


    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = view.context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        activityViewModel.user.observe(viewLifecycleOwner) { dataUi ->
            val idExists = dataUi.id.toString()
            val phoneExists = dataUi.phone_user

            userExists = (idExists == id && phoneExists == phoneNumberToSave)
            preferences.edit().putBoolean(PREF_BOOLEAN_VALUE, userExists).apply()
            preferences.edit().putString(PREF_ID_VALUE, idExists).apply()

            if (userExists) {
                findNavController()
                    .navigate(R.id.action_splashFragment_to_mainFragment)
            } else {
                view.snackLongTop(R.string.enter_correct_phone_password)
            }
        }

        activityViewModel.error.observe(viewLifecycleOwner) { errorType ->
            when (errorType.ordinal) {
                0 -> view.snackLong(R.string.no_connection_exception_message)
                1 -> view.snackLong(R.string.firebase_exception_message)
                2 -> view.snackLong(R.string.generic_exception_message)
                3 -> view.snackLong(R.string.http_exception_message)
            }
        }

        mSnack = Snackbar
            .make(view, R.string.check_internet_connection, Snackbar.LENGTH_INDEFINITE)

        inputMaskSetUp()

        mBinding.btnLogin.setOnClickListener {
            val phoneNumber = mBinding.editTextPhone.text.toString().trim()
            id = mBinding.editTextPassword.text.toString().trim()

            val splitPhoneNumber = phoneNumber.split(" ")
            phoneNumberToSave = splitPhoneNumber[0] + extracted

            if (phoneNumber.isBlank() || id.isBlank()) it.snackLongTop(R.string.fill_all_fields)
            else {
                checkIfUserExists(phone = phoneNumberToSave, id = id, view = view)
            }
        }
    }

    private fun inputMaskSetUp() {
        val listener = InputMaskHandle(mBinding.editTextPhone,
            ValueListener { extractedValue -> extracted = extractedValue })

        mBinding.editTextPhone.addTextChangedListener(listener)
        mBinding.editTextPhone.onFocusChangeListener = listener
    }

    private fun checkIfUserExists(phone: String, id: String, view: View) =
        activityViewModel.fetchData(id = id)
}