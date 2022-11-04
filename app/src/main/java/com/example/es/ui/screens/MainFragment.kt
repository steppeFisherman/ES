package com.example.es.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import com.example.es.R
import com.example.es.databinding.FragmentMainBinding
import com.example.es.ui.BaseFragment
import com.example.es.ui.model.DataUi
import com.example.es.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private lateinit var preferences: SharedPreferences
    private val vm by activityViewModels<MainFragmentViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise(view)

        val userId = preferences.getString(PREF_ID_VALUE, "").toString()
        if (userId.isNotBlank()) vm.fetchExistedUser(id = userId)

        vm.user.observe(viewLifecycleOwner) { dataUi ->
            binding.progressBar.visible(false)
            binding.txtPhone.text = dataUi.phone_user
            binding.txtLocation.text = dataUi.full_name
        }

        vm.error.observe(viewLifecycleOwner) { errorType ->
            binding.progressBar.visible(false)
            when (errorType.ordinal) {
                0 -> view.snackLong(R.string.no_connection_exception_message)
                1 -> view.snackLong(R.string.firebase_exception_message)
                2 -> view.snackLong(R.string.http_exception_message)
                3 -> view.snackLong(R.string.user_not_registered_exception_message)
                4 -> view.snackLong(R.string.generic_exception_message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = preferences.getString(PREF_URI_VALUE, "")
        if (uri?.isBlank() == true) binding.imgPhoto
            .setImageResource(R.drawable.inset_holder_camera)
        else binding.imgPhoto.setImageURI(uri?.toUri())
    }

    private fun initialise(view: View) {
        binding.progressBar.visible(true)

        val args: DataUi? = arguments?.getParcelable(ARGS)
        if (args != null) {
            binding.progressBar.visible(false)
            binding.txtPhone.text = args.phone_user
            binding.txtLocation.text = args.full_name
        }
        preferences = view.context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    companion object {
        const val ARGS = "MAIN_FRAGMENT_ARGS"
        fun newInstance(message: String): MainFragment {
            val fragment = MainFragment()
            fragment.arguments?.putString(ARGS, message)
            return fragment
        }
    }
}

