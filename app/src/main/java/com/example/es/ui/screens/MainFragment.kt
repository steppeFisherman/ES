package com.example.es.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.es.R
import com.example.es.databinding.FragmentMainBinding
import com.example.es.ui.BaseFragment
import com.example.es.ui.MainActivityViewModel
import com.example.es.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private lateinit var preferences: SharedPreferences
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = view.context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        binding.txtPhone.text = preferences.getString(PREF_PHONE_VALUE, "").toString()
        binding.txtLocation.text = preferences.getString(PREF_FULL_NAME_VALUE, "").toString()


        activityViewModel.user.observe(viewLifecycleOwner) { dataUi ->
            binding.txtPhone.text = dataUi.phone_user
            binding.txtLocation.text = dataUi.full_name
        }

        activityViewModel.error.observe(viewLifecycleOwner) { errorType ->
            when (errorType.ordinal) {
                0 -> view.snackLong(R.string.no_connection_exception_message)
                1 -> view.snackLong(R.string.firebase_exception_message)
                2 -> view.snackLong(R.string.http_exception_message)
                3 -> view.snackLong(R.string.user_not_registered_exception_message)
                4 -> view.snackLong(R.string.generic_exception_message)
            }
        }

        binding.btnLocation.setOnClickListener {
            (requireActivity() as NetWorkListener).listen()
        }
    }
}