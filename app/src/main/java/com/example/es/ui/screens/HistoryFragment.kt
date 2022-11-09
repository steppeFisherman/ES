package com.example.es.ui.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.example.es.R
import com.example.es.databinding.FragmentHistoryBinding
import com.example.es.ui.BaseFragment
import com.example.es.ui.adapters.HistoryFragmentAdapter
import com.example.es.utils.LoadImage
import com.example.es.utils.snackLong
import com.example.es.utils.snowSnackIndefiniteTop
import com.example.es.utils.visible
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private val vm by viewModels<HistoryFragmentViewModel>()
    private lateinit var snack: Snackbar

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHistoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snack = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
//        val layoutParams = FrameLayout.LayoutParams(snack.view.layoutParams)
//
//        layoutParams.gravity = Gravity.TOP
//        layoutParams.marginStart = 40
//        layoutParams.marginEnd = 40
//        snack.view.setPadding(0, 8, 0, 8)
//        snack.view.layoutParams = layoutParams
//        snack.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
//        snack.show()


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
//                4 -> showSnack(snack)
                5 -> view.snackLong(R.string.generic_exception_message)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        snack.dismiss()
        Log.d("AAA", " HistoryFragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("AAA", " HistoryFragment onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AAA", " HistoryFragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AAA", "HistoryFragment onDestroy")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("AAA", "HistoryFragment onAttach")

    }

    override fun onDetach() {
        super.onDetach()
        Log.d("AAA", "HistoryFragment onDetach")

    }

    private fun showSnack(snack: Snackbar) {
        val layoutParams = FrameLayout.LayoutParams(snack.view.layoutParams)
        snack.setText(R.string.database_exception_message)
        layoutParams.gravity = Gravity.TOP
        layoutParams.marginStart = 40
        layoutParams.marginEnd = 40
        snack.view.setPadding(0, 8, 0, 8)
        snack.view.layoutParams = layoutParams
        snack.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snack.show()
    }
}