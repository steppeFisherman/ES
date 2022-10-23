package com.example.es.ui.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.es.databinding.FragmentHistoryBinding
import com.example.es.ui.BaseFragment

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHistoryBinding.inflate(inflater, container, false)
}