package com.example.es.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.es.R
import com.example.es.databinding.FragmentUserPhotoBinding
import com.example.es.ui.model.DataUi
import com.example.es.utils.LoadImage

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserPhotoFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUserPhotoBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val loadImage = LoadImage.Base()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.profileDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPhotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPhoto = arguments?.getString(MainFragment.USER_PHOTO).toString()
        loadImage.load(requireContext(), binding.personImgLarge, userPhoto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}