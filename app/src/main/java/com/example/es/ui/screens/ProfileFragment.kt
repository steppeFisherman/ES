package com.example.es.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.databinding.FragmentProfileBinding
import com.example.es.utils.APP_PREFERENCES
import com.example.es.utils.PREF_COMPANY_VALUE
import com.example.es.utils.PREF_HOME_ADDRESS_VALUE
import com.example.es.utils.PREF_ID_VALUE
import com.example.es.utils.PREF_URI_VALUE
import com.example.es.utils.dialogShow
import com.example.es.utils.snackLongTop
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ProfileFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = checkNotNull(_binding)
    private lateinit var preferences: SharedPreferences
    private var id: String = ""

//    private val permissionRequest = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions(),   // contract for requesting more than 1 permission
//        ::onGotLocationPermissionsResult
//    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.profileDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = preferences.getString(PREF_URI_VALUE, "")
        id = preferences.getString(PREF_ID_VALUE, "").toString()

        if (uri?.isBlank() == true) binding.profileImage
            .setImageResource(R.drawable.inset_holder_camera)
        else binding.profileImage.setImageURI(uri?.toUri())

        textViewSetUp()
//        checkPermissions()

        binding.btnLogout.setOnClickListener {
            preferences.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_splashFragment)
        }

        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val uriResult = result.data?.data
                        binding.profileImage.setImageURI(uriResult)
                        preferences.edit().putString(PREF_URI_VALUE, uriResult?.path).apply()
                        (requireActivity() as PhotoListener).photoListener(uriResult, id)
                    }

                    ImagePicker.RESULT_ERROR -> {
                        val message = ImagePicker.getError(result.data)
                        binding.root.snackLongTop(message)
                    }

                    else -> binding.root.snackLongTop(R.string.cancel)
                }
            }

        binding.profileImage.setOnClickListener {
            ImagePicker.with(this)
//                .galleryOnly()  //User can only select image from Gallery
//                .cameraOnly()   //User can only capture image using Camera
//                .cropSquare()   //Crop square image, its same as crop(1f, 1f)
                .crop(9f, 12f)    //Crop image with 9:12 aspect ratio
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }

    private fun onGotLocationPermissionsResult(grantResults: Map<String, Boolean>) {

        if (grantResults.entries.all {
                it.value
            }) {
//            showToast(this, R.string.location_permissions_granted)
        } else {

            val appSettingsIntent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireActivity().packageName, null)
            )

            dialogShow(
                requireActivity(),
                R.string.permissions_denied,
                R.string.photo_permission_denied_message
            ) {
                startActivity(appSettingsIntent)
            }
        }
    }

    private fun textViewSetUp() {
        val company = preferences.getString(PREF_COMPANY_VALUE, "")
        val homeAddress = getString(
            R.string.home_address, preferences.getString(PREF_HOME_ADDRESS_VALUE, "")
        )
        val id = getString(R.string.your_id, id)

        binding.txtCompanyName.text = company
        binding.txtHomeAddress.text = homeAddress
        binding.txtId.text = id
    }

//    private fun  checkPermissions(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            permissionRequest.launch(
//                arrayOf(
//                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.READ_MEDIA_VIDEO,
//                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
//                )
//            )
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            permissionRequest.launch(
//                arrayOf(
//                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.READ_MEDIA_VIDEO,
//                )
//            )
//        } else {
//            permissionRequest.launch(
//                arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                )
//            )
//        }
//    }

    interface PhotoListener {
        fun photoListener(uri: Uri?, id: String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





