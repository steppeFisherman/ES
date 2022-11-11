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
import com.example.es.utils.APP_PREFERENCES
import com.example.es.utils.Navigator
import com.example.es.utils.PREF_URI_VALUE
import com.example.es.utils.snackLongTop
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = checkNotNull(_binding)
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.profileDialogTheme)
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

        preferences = view.context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        val uri = preferences.getString(PREF_URI_VALUE, "")
        if (uri?.isBlank() == true) binding.profileImage
            .setImageResource(R.drawable.inset_holder_camera)
        else binding.profileImage.setImageURI(uri?.toUri())

        binding.btnLogout.setOnClickListener {
            preferences.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_splashFragment)
        }

        binding.profileImage.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
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
}





