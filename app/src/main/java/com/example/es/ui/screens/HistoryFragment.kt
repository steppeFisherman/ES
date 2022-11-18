package com.example.es.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.es.R
import com.example.es.databinding.FragmentHistoryBinding
import com.example.es.map.MapsActivity
import com.example.es.ui.adapters.HistoryFragmentAdapter
import com.example.es.ui.model.DataUi
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
        snack = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)

        val adapter = HistoryFragmentAdapter(object : HistoryFragmentAdapter.Listener {
            override fun toLocation(user: DataUi) {
                val intent = Intent(view.context, MapsActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        })

        binding.historyFragmentRv.adapter = adapter

        vm.users.observe(viewLifecycleOwner) { listDataUi ->
            if (listDataUi.isNullOrEmpty()) binding.progressBar.visible(true)
            else {
                adapter.submitList(listDataUi.asReversed())
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