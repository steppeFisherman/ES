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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.es.R
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.databinding.FragmentMainBinding
import com.example.es.ui.model.MapDomainToUi
import com.example.es.utils.*
import com.example.es.utils.connectivity.ConnectivityManager
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.huawei.hms.api.HuaweiApiAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var snackTopBuilder: SnackBuilder

    @Inject
    lateinit var format: DateTimeFormat

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val vm by activityViewModels<MainFragmentViewModel>()
    private var phoneOperator = ""
    private var statusAnimation = false
    private var gpsEnabled = false
    private var userId = ""
    private var userPhoto = ""
    private val formatUiPhoneNumber = FormatUiPhoneNumber.Base()
    private val requestLocationUpdate = RequestLocationUpdate.Base()

    private val animation = Animation.Base()
    private val mapCloudToDomain = MapCloudToDomain.Base()
    private val mapDomainToUi = MapDomainToUi.Base()
    private var googleApi = false
    private var huaweiApi = false

    private lateinit var snack: Snackbar
    private lateinit var preferences: SharedPreferences
    private lateinit var googleFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var location: LocationHandle
    private lateinit var locationManager: LocationManager
    private lateinit var geoCoder: Geocoder

    private lateinit var huaweiFusedLocationProviderClient: com.huawei.hms.location.FusedLocationProviderClient
    private lateinit var huaweiSettingsClient: com.huawei.hms.location.SettingsClient

    //    private  var huaweiLocationCallback: com.huawei.hms.location.LocationCallback? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (checkGMS()) googleApi = true else if (checkHMS()) huaweiApi = true

        googleFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geoCoder = Geocoder(context, Locale.getDefault())
        huaweiFusedLocationProviderClient =
            com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(context)
        huaweiSettingsClient = com.huawei.hms.location.LocationServices.getSettingsClient(context)
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
            binding.txtPhone.text = formatUiPhoneNumber.modify(dataUi.phoneUser)

            if (userPhone != dataUi.phoneUser) {
                preferences.edit().clear().apply()
                findNavController().navigate(R.id.action_mainFragment_to_splashFragment)
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
            binding.progressBar.visible(true)
            postLocationUpdates()
        }

        binding.btnDial.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneOperator}")
            ContextCompat.startActivity(view.context, intent, null)
        }

        binding.ltAnimation.setOnClickListener {
            binding.progressBar.visible(true)
            postAlarmUpdates()
        }

        if (userId.isNotBlank()) {
            try {
                REF_DATABASE_ROOT.child(NODE_USERS).child(userId)
                    .addValueEventListener(SnapShotListener {
                        val dataCloud = it.getValue(DataCloud::class.java) ?: DataCloud()
                        val dataDomain = mapCloudToDomain.mapCloudToDomain(dataCloud)
                        val dataUi = mapDomainToUi.mapDomainToUi(dataDomain)
                        statusAnimation = dataUi.alarm

//                    lifecycleScope.launch(exceptionHandler) {
//                        while (statusAnimation) {
//                            delay(300)
//                                animation.animate(binding.imgAnimation1, binding.imgAnimation2)
//                        }
//                    }
                        /**
                        Using lottie instead of ViewPropertyAnimator
                         */
                        lifecycleScope.launch(exceptionHandler) {
                            if (statusAnimation) {
                                binding.ltAnimation.playAnimation()
                                binding.ltAnimation.repeatCount = LottieDrawable.INFINITE
                            } else {
                                binding.ltAnimation.repeatCount = 0
                            }
                        }
                    })
            } catch (e: Exception) {
                showToast(requireActivity(), e.message.toString())
            }
        }
    }

    private fun postLocationUpdates() {
        (requireActivity() as PermissionHandle).check()
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!gpsEnabled) dialogShow() else {

            if (googleApi) {
                location = LocationHandle.Google(googleFusedLocationProviderClient)
                location.handle(format, geoCoder) { map ->
                    map[CHILD_ALARM] = false
                    map[CHILD_LOCATION_FLAG_ONLY] = true
                    vm.postLocationUpdates(id = userId, map)
                }

            } else if (huaweiApi) {
                location = LocationHandle.Huawei(
                    huaweiFusedLocationProviderClient,
                    huaweiSettingsClient
                )
                location.handle(format, geoCoder) { map ->
                    map[CHILD_ALARM] = false
                    map[CHILD_LOCATION_FLAG_ONLY] = true
                    vm.postLocationUpdates(id = userId, map)
                }
            }
        }
    }

    private fun postAlarmUpdates() {
        (requireActivity() as PermissionHandle).check()
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!gpsEnabled) dialogShow() else {
            if (googleApi) {
                location = LocationHandle.Google(googleFusedLocationProviderClient)
                location.handle(format, geoCoder) { map ->
                    map[CHILD_ALARM] = true
                    map[CHILD_LOCATION_FLAG_ONLY] = false
                    map[CHILD_COMMENT] = ""
                    vm.postAlarmUpdates(id = userId, map)
                }

            } else if (huaweiApi) {
                location = LocationHandle.Huawei(
                    huaweiFusedLocationProviderClient,
                    huaweiSettingsClient,
                )

                location.handle(format, geoCoder) { map ->
                    map[CHILD_ALARM] = true
                    map[CHILD_LOCATION_FLAG_ONLY] = false
                    map[CHILD_COMMENT] = ""
                    vm.postAlarmUpdates(id = userId, map)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requestLocationUpdate.update(googleFusedLocationProviderClient)
        checkNetworks(connectivityManager) { isNetWorkAvailable ->
            when (isNetWorkAvailable) {
                false -> {
                    binding.btnLocation.isEnabled = false
                    binding.ltAnimation.visible(false)
                    snack.show()
                }
                true -> {
                    snack.dismiss()
                    binding.btnLocation.isEnabled = true
                    binding.ltAnimation.visible(true)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = preferences.getString(PREF_URI_VALUE, "")
        if (uri?.isBlank() == true) binding.imgPhoto
            .setImageResource(R.drawable.inset_holder_camera)
        else {
            binding.imgPhoto.setImageURI(uri?.toUri())
            binding.imgPhoto.setOnClickListener{
                val bundle = bundleOf(USER_PHOTO to uri)
                findNavController().navigate(R.id.action_mainFragment_to_userPhotoFragment, bundle)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        snack.dismiss()
        binding.imgPhoto.setOnClickListener(null)
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

    private fun checkGMS(): Boolean {
        val gApi = GoogleApiAvailability.getInstance()
        val resultCode = gApi.isGooglePlayServicesAvailable(requireContext())
        return resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS
    }

    private fun checkHMS(): Boolean {
        val hApi = HuaweiApiAvailability.getInstance()
        val resultCode = hApi.isHuaweiMobileServicesAvailable(requireContext())
        return resultCode == com.huawei.hms.api.ConnectionResult.SUCCESS
    }

    interface PermissionHandle {
        fun check()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val USER_PHOTO = "userPhoto"
        const val ARGS = "MAIN_FRAGMENT_ARGS"
        fun newInstance(message: String): MainFragment {
            val fragment = MainFragment()
            fragment.arguments?.putString(ARGS, message)
            return fragment
        }
    }
}

