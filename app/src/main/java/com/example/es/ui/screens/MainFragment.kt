package com.example.es.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.databinding.FragmentMainBinding
import com.example.es.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var snackTopBuilder: SnackBuilder

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val vm by activityViewModels<MainFragmentViewModel>()
    private var phoneOperator = ""
    private var statusAnimation = false
    private var gpsStatus = false
    private var userId = ""
    private val formatUiPhoneNumber = FormatUiPhoneNumber.Base()
    private val requestLocationUpdate = RequestLocationUpdate.Base()
    private val fusedLocationResult = FusedLocationResult.Base()
    private val animation = Animation.Base()
    private lateinit var preferences: SharedPreferences

    private lateinit var snack: Snackbar
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var geoCoder: Geocoder

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geoCoder = Geocoder(context, Locale.getDefault())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise(view)

        userId = preferences.getString(PREF_ID_VALUE, "").toString()
        val userPhone = preferences.getString(PREF_PHONE_VALUE, "").toString()

        if (userId.isNotBlank()) vm.fetchExistedUser(id = userId)

        vm.user.observe(viewLifecycleOwner) { dataUi ->
            phoneOperator = dataUi.phoneOperator
            binding.txtName.text = dataUi.fullName
            binding.txtLocationAddress.text = dataUi.locationAddress
            binding.txtTime.text = dataUi.time
            binding.txtPhone.text = formatUiPhoneNumber
                .modify(dataUi.phoneUser)

            if (userPhone != dataUi.phoneUser) {
                preferences.edit().clear().apply()
                findNavController()
                    .navigate(R.id.action_mainFragment_to_splashFragment)
            }
        }

        vm.error.observe(viewLifecycleOwner) { errorType ->
            when (errorType.ordinal) {
                0 -> view.snackLong(R.string.no_connection_exception_message)
                1 -> view.snackLong(R.string.database_exception_message)
                2 -> view.snackLong(R.string.http_exception_message)
                3 -> view.snackLong(R.string.user_not_registered_exception_message)
                4 -> view.snackLong(R.string.database_exception_message)
                5 -> view.snackLong(R.string.generic_exception_message)
            }
        }

        vm.loading.observe(viewLifecycleOwner) {
            if (it) binding.progressBar.visible(true)
            else binding.progressBar.visible(false)
        }

        binding.btnLocation.setOnClickListener {
            (requireActivity() as PermissionHandle).check()
            gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (!gpsStatus) dialogShow() else
                fusedLocationResult.result(fusedLocationClient, geoCoder) {
                    vm.postLocation(id = userId, it)
                }
        }

        binding.btnDial.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneOperator}")
            ContextCompat.startActivity(view.context, intent, null)
        }

        binding.btnPanic.setOnClickListener {
            statusAnimation = !statusAnimation
            lifecycleScope.launch {
                while (statusAnimation) {
                    delay(300)
                    animation.animate(binding.imgAnimation1, binding.imgAnimation2)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        requestLocationUpdate.update(fusedLocationClient)
        checkNetworks(connectivityManager) { isNetWorkAvailable ->
            when (isNetWorkAvailable) {
                false -> {
                    binding.btnLocation.isEnabled = false
                    binding.btnPanic.isEnabled = false
                    snack.show()
                }
                true -> {
                    snack.dismiss()
                    binding.btnLocation.isEnabled = true
                    binding.btnPanic.isEnabled = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = preferences.getString(PREF_URI_VALUE, "")
        if (uri?.isBlank() == true) binding.imgPhoto
            .setImageResource(R.drawable.inset_holder_camera)
        else binding.imgPhoto.setImageURI(uri?.toUri())
    }

    override fun onPause() {
        super.onPause()
        snack.dismiss()
    }

    private fun initialise(view: View) {
        preferences = view.context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        snack = snackTopBuilder.buildSnackTopIndefinite(view)
    }

    private fun checkNetworks(
        connectivityManager: ConnectivityManager,
        connected: (Boolean) -> Unit
    ): Boolean {
        var isNetWorkAvailable = true
        connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner) {
            isNetWorkAvailable = it
            connected(isNetWorkAvailable)
        }
        return isNetWorkAvailable
    }

    private fun dialogShow() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.location_mode)
            .setMessage(R.string.location_mode_denied_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                val intent =
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .create()
            .show()
    }

    companion object {
        const val ARGS = "MAIN_FRAGMENT_ARGS"
        fun newInstance(message: String): MainFragment {
            val fragment = MainFragment()
            fragment.arguments?.putString(ARGS, message)
            return fragment
        }
    }

    interface PermissionHandle {
        fun check()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

