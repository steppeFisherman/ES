package com.example.es.ui.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.es.R
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.databinding.FragmentMainBinding
import com.example.es.ui.BaseFragment
import com.example.es.ui.model.DataUi
import com.example.es.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private lateinit var preferences: SharedPreferences
    private val vm by activityViewModels<MainFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AAA", "onCreate")
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AAA", "onViewCreated")

        initialise(view)

        val userId = preferences.getString(PREF_ID_VALUE, "").toString()
        val userPhone = preferences.getString(PREF_PHONE_VALUE, "").toString()
        val dataUi = DataUi()
        if (userId.isNotBlank()){
            vm.fetchExistedUser(id = userId)
            REF_DATABASE_ROOT.child(NODE_USERS).child(userId)
                .addValueEventListener(SnapShotListener{ snapShot ->
                        val dataCloud = snapShot.getValue(DataCloud::class.java)
                                ?: DataCloud()
                        if (userPhone == dataCloud.phone_user) {
                            binding.txtLocation.text = dataCloud.latitude
                            binding.txtTime.text = dataCloud.time_location
                        }else {
                            findNavController()
                                .navigate(R.id.action_mainFragment_to_splashFragment)
                            preferences.edit().clear().apply()
                        }
                })
        }

        vm.user.observe(viewLifecycleOwner) { data ->
            binding.progressBar.visible(false)
            dataUi.id = data.id
            dataUi.full_name = data.full_name
            dataUi.phone_user = data.phone_user
            dataUi.phone_operator = data.phone_operator
            dataUi.photo = data.photo
            dataUi.time_location = data.time_location
            dataUi.latitude = data.latitude
            dataUi.longitude = data.longitude
            dataUi.alarm = data.alarm
            dataUi.notify = data.notify

            binding.txtPhone.text = data.phone_user
            binding.txtName.text = data.full_name
//            binding.txtLocation.text = data.latitude
//            binding.txtTime.text = data.time_location
        }

        vm.error.observe(viewLifecycleOwner) { errorType ->
            binding.progressBar.visible(false)
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
            dataUi.time_location = dateString
            vm.postLocation(id = userId, dataUi = dataUi)
        }

        binding.btnDial.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${dataUi.phone_operator}")
            ContextCompat.startActivity(view.context, intent, null)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("AAA", "onResume")

        val uri = preferences.getString(PREF_URI_VALUE, "")
        if (uri?.isBlank() == true) binding.imgPhoto
            .setImageResource(R.drawable.inset_holder_camera)
        else binding.imgPhoto.setImageURI(uri?.toUri())
    }

    private fun initialise(view: View) {
        preferences = view.context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        binding.progressBar.visible(true)
        val args: DataUi? = arguments?.getParcelable(ARGS)
        if (args != null) {
            binding.progressBar.visible(false)
            binding.txtPhone.text = args.phone_user
            binding.txtLocation.text = args.full_name
        }
    }

    companion object {
        const val ARGS = "MAIN_FRAGMENT_ARGS"
        fun newInstance(message: String): MainFragment {
            val fragment = MainFragment()
            fragment.arguments?.putString(ARGS, message)
            return fragment
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("AAA", "onStart")

    }

    override fun onPause() {
        super.onPause()
        Log.d("AAA", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("AAA", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AAA", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AAA", "onDestroy")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("AAA", "onAttach")

    }

    override fun onDetach() {
        super.onDetach()
        Log.d("AAA", "onDetach")

    }

}

