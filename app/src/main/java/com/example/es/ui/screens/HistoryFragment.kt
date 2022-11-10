package com.example.es.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.es.R
import com.example.es.databinding.FragmentHistoryBinding
import com.example.es.ui.adapters.HistoryFragmentAdapter
import com.example.es.utils.LoadImage
import com.example.es.utils.snackLong
import com.example.es.utils.snowSnackIndefiniteTop
import com.example.es.utils.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val vm by viewModels<HistoryFragmentViewModel>()
    private lateinit var snack: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.fetchData()
        snack = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)

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
                1 -> view.snackLong(R.string.database_exception_message)
                2 -> view.snackLong(R.string.http_exception_message)
                3 -> view.snackLong(R.string.user_not_registered_exception_message)
                4 -> view.snowSnackIndefiniteTop(snack, R.string.database_exception_message)
                5 -> view.snackLong(R.string.generic_exception_message)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        snack.dismiss()
    }
}