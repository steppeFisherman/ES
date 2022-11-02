package com.example.es.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.databinding.FragmentProfileBinding
import com.example.es.ui.BaseBottomSheetDialogFragment
import com.example.es.utils.*

class ProfileFragment : BaseBottomSheetDialogFragment<FragmentProfileBinding>() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.profileDialogTheme)
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = view.context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        binding.btnLogout.setOnClickListener {
            preferences.edit().putBoolean(PREF_BOOLEAN_VALUE, false).apply()
            preferences.edit().putString(PREF_ID_VALUE, "").apply()
            preferences.edit().putString(PREF_PHONE_VALUE, "").apply()
            findNavController()
                .navigate(R.id.action_profileFragment_to_splashFragment)
        }
    }
}