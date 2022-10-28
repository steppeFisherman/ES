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

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val scope = CoroutineScope(Job() + exceptionHandler)
    private val dispatchers: ToDispatch = ToDispatch.Base()
    private var firstTimeUser = false
    private var extracted = String()
    private lateinit var preferences: SharedPreferences

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = view.context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        inputMaskSetUp()

        mBinding.btnLogin.setOnClickListener {
            val phoneNumber = mBinding.editTextPhone.text.toString().trim()
            val id = mBinding.editTextPassword.text.toString().trim()

            val splitPhoneNumber = phoneNumber.split(" ")
            val phoneNumberToSave = splitPhoneNumber[0] + extracted

            if (phoneNumber.isBlank() || id.isBlank()) it.snackLongTop(R.string.fill_all_fields)
            else checkIfUserExists(phone = phoneNumberToSave, id = id, view = view)
        }
    }

    private fun inputMaskSetUp() {
        val listener = InputMaskHandle(mBinding.editTextPhone,
            ValueListener { extractedValue -> extracted = extractedValue })

        mBinding.editTextPhone.addTextChangedListener(listener)
        mBinding.editTextPhone.onFocusChangeListener = listener
    }

    private fun checkIfUserExists(phone: String, id: String, view: View) {

        dispatchers.launchIO(scope = scope) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener() {
                    val data = it.result.getValue(DataCloud::class.java) ?: DataCloud()
                    val idExists = it.result.exists()
                    val phoneExists = phone == data.phone

                    firstTimeUser = idExists && phoneExists
                    preferences.edit().putBoolean(PREF_BOOLEAN_VALUE, firstTimeUser).apply()
                }.await()

            dispatchers.launchUI(scope) {
                if (firstTimeUser) findNavController()
                    .navigate(R.id.action_splashFragment_to_mainFragment)
                else view.snackLongTop(R.string.enter_correct_phone_password)
            }
        }
    }
}