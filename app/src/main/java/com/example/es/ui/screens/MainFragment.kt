package com.example.es.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.es.R
import com.example.es.databinding.FragmentMainBinding
import com.example.es.ui.BaseFragment
import com.example.es.ui.MainActivityViewModel
import com.example.es.utils.snackLong
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.user.observe(viewLifecycleOwner) { dataUi ->
            mBinding.txtPhone.text = dataUi.phone_user
            mBinding.txtLocation.text = dataUi.full_name
        }

        activityViewModel.error.observe(viewLifecycleOwner) { errorType ->
            when (errorType.ordinal) {
                0 -> view.snackLong(R.string.no_connection_exception_message)
                1 -> view.snackLong(R.string.firebase_exception_message)
                2 -> view.snackLong(R.string.generic_exception_message)
                3 -> view.snackLong(R.string.http_exception_message)
            }
        }
    }
}