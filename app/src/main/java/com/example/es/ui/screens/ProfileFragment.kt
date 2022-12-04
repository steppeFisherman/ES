package com.example.es.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.databinding.FragmentProfileBinding
import com.example.es.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = checkNotNull(_binding)
    private lateinit var preferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.profileDialogTheme)

//        parentFragmentManager.
//        setFragmentResultListener("KEY_PROFILE", viewLifecycleOwner) { key, bundle ->
//            binding.txtCompanyName.text = bundle.getString("KEY_BUNDLE")
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = preferences.getString(PREF_URI_VALUE, "")
        if (uri?.isBlank() == true) binding.profileImage
            .setImageResource(R.drawable.inset_holder_camera)
        else binding.profileImage.setImageURI(uri?.toUri())

        textViewSetUp()

        binding.btnLogout.setOnClickListener {
            preferences.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_splashFragment)
        }

        binding.profileImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
    }

    private fun textViewSetUp() {
        val company = preferences.getString(PREF_COMPANY_VALUE, "")
        val homeAddress = getString(
            R.string.home_address, preferences.getString(PREF_HOME_ADDRESS_VALUE, "")
        )
        val id = getString(
            R.string.your_id, preferences.getString(PREF_ID_VALUE, "")
        )

        binding.txtCompanyName.text = company
        binding.txtHomeAddress.text = homeAddress
        binding.txtId.text = id
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri = data?.data?.path
                binding.profileImage.setImageURI(uri?.toUri())
                preferences.edit().putString(PREF_URI_VALUE, uri).apply()
                (requireActivity() as PhotoListener).photoListener(uri.toString())
            }
            ImagePicker.RESULT_ERROR -> binding.root.snackLongTop(ImagePicker.getError(data))
            else -> binding.root.snackLongTop(R.string.cancel)
        }
    }

    interface PhotoListener {
        fun photoListener(photo: String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





