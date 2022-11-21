package com.example.es.ui.screens

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.es.R
import com.example.es.databinding.FragmentSearchBinding
import com.example.es.map.MapsActivity
import com.example.es.ui.adapters.HistoryFragmentAdapter
import com.example.es.ui.model.DataUi
import com.example.es.utils.ResourceProvider
import com.example.es.utils.snackLong
import com.example.es.utils.snowSnackIndefiniteTop
import com.example.es.utils.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val vm by viewModels<SearchFragmentViewModel>()
    private var startDate: Long = 0
    private var endDate: Long = 0
    private var startDateString = ""
    private var endDateString = ""
    private val provider = ResourceProvider.Base()
    private lateinit var snack: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStartDate()
        setEndDate()

        snack = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)

        val adapter = HistoryFragmentAdapter(object : HistoryFragmentAdapter.Listener {
            override fun toLocation(user: DataUi) {
                val intent = Intent(view.context, MapsActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        })

        binding.rvSearchFragment.adapter = adapter

        vm.users.observe(viewLifecycleOwner) { listDataUi ->
            binding.progressBar.visible(false)

            if (listDataUi.isNullOrEmpty()){
                view.snackLong(R.string.no_data_for_selected_period)
            }else {
                adapter.submitList(listDataUi.asReversed())
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

        binding.btnSearch.setOnClickListener {
            binding.progressBar.visible(true)
            vm.fetchUsersByDate(startDate, endDate)
        }
    }

    private fun setStartDate() {
        binding.txtStartDate.setOnClickListener {
            val currentDateTime = Calendar.getInstance()
            val startYear = currentDateTime.get(Calendar.YEAR)
            val startMonth = currentDateTime.get(Calendar.MONTH)
            val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    val pickedDateTime = Calendar.getInstance()
                    pickedDateTime.set(Calendar.YEAR, year)
                    pickedDateTime.set(Calendar.MONTH, month)
                    pickedDateTime.set(Calendar.DAY_OF_MONTH, day)
                    pickedDateTime.set(Calendar.HOUR_OF_DAY, 0)
                    pickedDateTime.set(Calendar.MINUTE, 0)
                    pickedDateTime.set(Calendar.SECOND, 0)
                    pickedDateTime.set(Calendar.MILLISECOND, 0)

                    startDate = pickedDateTime.time.time
                    startDateString =
                        DateFormat.getDateInstance().format(pickedDateTime.time)

                    binding.txtStartDate.setTextColor(
                        provider.color(
                            requireContext(),
                            R.color.black_textView
                        )
                    )

                    binding.txtStartDate.text = startDateString

                    if (startDateString.isNotBlank() && endDateString.isNotBlank()) {
                        binding.btnSearch.isEnabled = true
                    }
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }
    }

    private fun setEndDate() {
        binding.txtEndDate.setOnClickListener {
            val currentDateTime = Calendar.getInstance()
            val startYear = currentDateTime.get(Calendar.YEAR)
            val startMonth = currentDateTime.get(Calendar.MONTH)
            val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    val pickedDateTime = Calendar.getInstance()
                    pickedDateTime.set(Calendar.YEAR, year)
                    pickedDateTime.set(Calendar.MONTH, month)
                    pickedDateTime.set(Calendar.DAY_OF_MONTH, day)
                    pickedDateTime.set(Calendar.HOUR_OF_DAY, 0)
                    pickedDateTime.set(Calendar.MINUTE, 0)
                    pickedDateTime.set(Calendar.SECOND, 0)
                    pickedDateTime.set(Calendar.MILLISECOND, 0)

                    endDate = pickedDateTime.time.time
                    endDateString =
                        DateFormat.getDateInstance().format(pickedDateTime.time)

                    binding.txtEndDate.setTextColor(
                        provider.color(
                            requireContext(),
                            R.color.black_textView
                        )
                    )

                    binding.txtEndDate.text = endDateString

                    if (startDateString.isNotBlank() && endDateString.isNotBlank()) {
                        binding.btnSearch.isEnabled = true
                    }
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}