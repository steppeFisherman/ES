package com.example.es.ui.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.es.databinding.FragmentProfileBinding
import com.example.es.ui.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)
}