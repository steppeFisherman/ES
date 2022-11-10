package com.example.es.ui.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.es.R
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.databinding.FragmentMainBinding
import com.example.es.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding)
    private var phoneOperator = ""
    private var isAttachedToActivity: Boolean = false
    private val vm by activityViewModels<MainFragmentViewModel>()
    private lateinit var preferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isAttachedToActivity = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = view.context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val userId = preferences.getString(PREF_ID_VALUE, "").toString()
        val userPhone = preferences.getString(PREF_PHONE_VALUE, "").toString()

        if (userId.isNotBlank()) {

            vm.fetchExistedUser(id = userId)

            REF_DATABASE_ROOT.child(NODE_USERS).child(userId)
                .addValueEventListener(SnapShotListener { snapShot ->
                    if (snapShot.exists()) {
                        val dataCloud = snapShot.getValue(DataCloud::class.java) ?: DataCloud()
                        val ddd = dataCloud.phone_user
                        if (userPhone == ddd) {
                            phoneOperator = dataCloud.phone_operator
                            binding.txtPhone.text = dataCloud.phone_user
                            binding.txtName.text = dataCloud.full_name
                            binding.txtLocation.text = dataCloud.latitude
                            binding.txtTime.text = dataCloud.time_location
                        } else if (isAttachedToActivity) {
                            (requireActivity() as Navigator)
                                .navigateAndPrefClear(
                                    R.id.action_mainFragment_to_splashFragment,
                                    true
                                )
                        }
                    }
                })
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

        binding.btnLocation.setOnClickListener {
            val dateDate = Calendar.getInstance(Locale.getDefault()).time
            val dateString = DateFormat.getDateTimeInstance().format(dateDate)
            val map = mutableMapOf<String, Any>()
            map[CHILD_TIME] = dateString
            map[CHILD_LATITUDE] = dateString
            vm.postLocation(id = userId, map)
        }

        binding.btnDial.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneOperator}")
            ContextCompat.startActivity(view.context, intent, null)
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = preferences.getString(PREF_URI_VALUE, "")
        if (uri?.isBlank() == true) binding.imgPhoto
            .setImageResource(R.drawable.inset_holder_camera)
        else binding.imgPhoto.setImageURI(uri?.toUri())
    }

    override fun onDetach() {
        super.onDetach()
        isAttachedToActivity = false
    }

    companion object {
        const val ARGS = "MAIN_FRAGMENT_ARGS"
        fun newInstance(message: String): MainFragment {
            val fragment = MainFragment()
            fragment.arguments?.putString(ARGS, message)
            return fragment
        }
    }
}

