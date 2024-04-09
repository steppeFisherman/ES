package com.example.es.ui.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.es.R
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.databinding.FragmentMainBinding
import com.example.es.ui.model.MapDomainToUi
import com.example.es.utils.API_TYPE
import com.example.es.utils.APP_PREFERENCES
import com.example.es.utils.Animation
import com.example.es.utils.ApiType
import com.example.es.utils.CHILD_ALARM
import com.example.es.utils.CHILD_COMMENT
import com.example.es.utils.CHILD_LOCATION_FLAG_ONLY
import com.example.es.utils.DateTimeFormat
import com.example.es.utils.FormatUiPhoneNumber
import com.example.es.utils.LocationHandle
import com.example.es.utils.NODE_USERS
import com.example.es.utils.PREF_ID_VALUE
import com.example.es.utils.PREF_PHONE_VALUE
import com.example.es.utils.PREF_URI_VALUE
import com.example.es.utils.REF_DATABASE_ROOT
import com.example.es.utils.SnackBuilder
import com.example.es.utils.SnapShotListener
import com.example.es.utils.connectivity.NetworkProvider
import com.example.es.utils.dialogLocationShow
import com.example.es.utils.showToast
import com.example.es.utils.snackLong
import com.example.es.utils.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.SettingsClient
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var googleLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var googleSettingsLocationClient: SettingsClient

    @Inject
    lateinit var huaweiLocationClient: com.huawei.hms.location.FusedLocationProviderClient

    @Inject
    lateinit var huaweiSettingsClient: com.huawei.hms.location.SettingsClient

    @Inject
    lateinit var networkProvider: NetworkProvider

    @Inject
    lateinit var format: DateTimeFormat.Base

    @Inject
    lateinit var geoCoder: Geocoder

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val vm by activityViewModels<MainFragmentViewModel>()
    private val snackBuilder = SnackBuilder.SnackIndefiniteFadeMode()

    private var phoneOperator = ""
    private var statusAnimation = false
    private var gpsEnabled = false
    private var networkEnabled = false
    private var isBtnPressed = false
    private var userId = ""
    private val formatUiPhoneNumber = FormatUiPhoneNumber.Base()

    private val animation = Animation.Base()
    private val mapCloudToDomain = MapCloudToDomain.Base()
    private val mapDomainToUi = MapDomainToUi.Base()

    private lateinit var snack: Snackbar
    private lateinit var observer: Observer<Boolean>
    private lateinit var preferences: SharedPreferences
    private lateinit var location: LocationHandle

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

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
//            postLocationUpdates()
        }

        binding.btnDial.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneOperator}")
            ContextCompat.startActivity(view.context, intent, null)
        }

        binding.ltAnimation.customLongClickListener(duration = 2000L) {
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
                                binding.ltAnimation.apply {
                                    playAnimation()
                                    repeatCount = LottieDrawable.INFINITE
                                    isClickable = false
                                }
                            } else {
                                isBtnPressed = false
                                binding.ltAnimation.apply {
                                    repeatCount = 0
                                    isClickable = true
                                }
                            }
                        }
                    })
            } catch (e: Exception) {
                showToast(requireActivity(), e.message.toString())
            }
        }
    }

//    private fun postLocationUpdates() {
//        (requireActivity() as PermissionHandle).check()
//        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//
//        if (!gpsEnabled) dialogShow() else {
//
//            if (googleApi) {
//                location = LocationHandle.Google(googleLocationClient)
//                location.handle(format, geoCoder) { map ->
//                    map[CHILD_ALARM] = false
//                    map[CHILD_LOCATION_FLAG_ONLY] = true
//                    vm.postLocationUpdates(id = userId, map)
//                }
//
//            } else if (huaweiApi) {
//                location = LocationHandle.Huawei(
//                    huaweiLocationClient,
//                    huaweiSettingsClient
//                )
//                location.handle(format, geoCoder) { map ->
//                    map[CHILD_ALARM] = false
//                    map[CHILD_LOCATION_FLAG_ONLY] = true
//                    vm.postLocationUpdates(id = userId, map)
//                }
//            }
//        }
//    }

    private fun postAlarmUpdates() {
        when (API_TYPE) {
            ApiType.GOOGLE -> {
                location = LocationHandle.Google(googleLocationClient)
                sendAlarm()
            }

            ApiType.HUAWEI -> {
                location = LocationHandle.Huawei(huaweiLocationClient, huaweiSettingsClient)
                sendAlarm()
            }

            ApiType.OTHERS -> {}
        }
    }

    private fun sendAlarm() {
        location.handle(requireContext(), format, geoCoder) { map ->
            map[CHILD_ALARM] = true
            map[CHILD_LOCATION_FLAG_ONLY] = false
            map[CHILD_COMMENT] = ""
            vm.postAlarmUpdates(id = userId, map)
        }
    }

    private fun com.airbnb.lottie.LottieAnimationView.customLongClickListener(
        duration: Long,
        listener: () -> Unit
    ) {
        setOnTouchListener(object : View.OnTouchListener {

            private val handler = Handler(Looper.getMainLooper())

            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                view?.performClick()
                if (event?.action == MotionEvent.ACTION_DOWN) {

                    view?.isPressed = true

                    (requireActivity() as PermissionHandle).check()

                    gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    networkEnabled =
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    isBtnPressed = true

                    if (!gpsEnabled || !networkEnabled) {
                        dialogLocationShow(requireActivity())
                    } else {
                        handler.postDelayed({ listener.invoke() }, duration)
                    }

                } else if (event?.action == MotionEvent.ACTION_UP) {
                    view?.isPressed = false
                    handler.removeCallbacksAndMessages(null)
                }
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        observer = Observer { isConnected ->
            if (isConnected) {
                snack.dismiss()
            } else {
                snack.show()
            }
        }
        networkProvider.liveData.observe(this, observer)

        val settings = com.google.android.gms.location.LocationSettingsRequest.Builder()
            .build()
        val task = googleSettingsLocationClient.checkLocationSettings(settings)

        task.addOnSuccessListener { locationSettingsResponse ->
            val state = locationSettingsResponse.locationSettingsStates
            val isGpsUsable = state?.isGpsUsable
            val isNetworkLocationUsable = state?.isNetworkLocationUsable
            val allSourcesLocation = isGpsUsable == true && isNetworkLocationUsable == true

            if (isBtnPressed && allSourcesLocation) {
                location = LocationHandle.Google(googleLocationClient)
                sendAlarm()
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
            binding.imgPhoto.setOnClickListener {
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
        snack = snackBuilder.snackBarWithPadding(
            binding.root.rootView,
            R.string.check_internet_connection
        )
    }

    interface PermissionHandle {
        fun check()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_PHOTO = "userPhoto"
        const val ARGS = "MAIN_FRAGMENT_ARGS"

        @JvmStatic
        fun newInstance(message: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGS, message)
                }
            }
    }
}

