package com.example.es.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.es.R
import com.example.es.databinding.FragmentMainBinding
import com.example.es.ui.BaseFragment
import com.example.es.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val vm by viewModels<MainFragmentViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnNewUser.setOnClickListener {
            createUser()
        }

        vm.allItems.observe(viewLifecycleOwner) {
            mBinding.mainTxt.text = it[0].full_name
        }

        vm.error.observe(viewLifecycleOwner) {
            when (it.ordinal) {
                0 -> view.showSnackLong(R.string.no_connection_message)
                1 -> view.showSnackLong(R.string.null_pointer_exception)
                2 -> view.showSnackLong(R.string.something_went_wrong)
                3 -> view.showSnackLong(R.string.service_unavailable_message)
            }
        }
    }

        private fun createUser() {
        val fullName = "Ivanov Iiiiiuuuu"
        val phone = "+7 916 800 00 20"
        val id = ""
        val time = ""
        val lat = "latitude"
        val lng = "latitude"

        val data = mutableMapOf<String, Any>()

        data[CHILD_ID] = id
        data[CHILD_TIME] = time
        data[CHILD_FULL_NAME] = fullName
        data[CHILD_PHONE] = phone
        data[CHILD_LATITUDE] = lat
        data[CHILD_LONGITUDE] = lng

        REF_DATABASE_ROOT.child(NODE_USERS).child(phone).updateChildren(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                } else {
                }
            }
    }
}