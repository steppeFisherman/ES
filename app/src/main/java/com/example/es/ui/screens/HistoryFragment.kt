package com.example.es.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.es.R
import com.example.es.databinding.FragmentHistoryBinding
import com.example.es.ui.BaseFragment
import com.example.es.ui.adapters.HistoryFragmentAdapter
import com.example.es.utils.LoadImage
import com.example.es.utils.snackLong
import com.example.es.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private val vm by viewModels<HistoryFragmentViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHistoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryFragmentAdapter(LoadImage.Base())
        binding.historyFragmentRv.adapter = adapter

        vm.users.observe(viewLifecycleOwner) { listDataUi ->
            if (listDataUi.isNullOrEmpty()) binding.progressBar.visible(true)
            else {
                adapter.setData(listDataUi.asReversed())
                binding.progressBar.visible(false)
            }
        }

        vm.error.observe(viewLifecycleOwner) {
            when (it.ordinal) {
                0 -> view.snackLong(R.string.no_connection_exception_message)
                1 -> view.snackLong(R.string.firebase_exception_message)
                2 -> view.snackLong(R.string.http_exception_message)
                3 -> view.snackLong(R.string.user_not_registered_exception_message)
                4 -> view.snackLong(R.string.generic_exception_message)
            }
        }
    }
}