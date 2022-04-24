package com.example.otpreader

import android.Manifest
import android.Manifest.permission.READ_SMS
import android.Manifest.permission.RECEIVE_SMS
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import android.provider.Telephony
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.otpreader.databinding.OtpListFragmentBinding
import com.google.android.material.snackbar.Snackbar

class OtpListFragment : Fragment() {

    companion object {
        const val TAG = "projectlog"
    }

    private val viewModel by viewModels<OtpListViewModel>()

    private lateinit var binding: OtpListFragmentBinding

    @RequiresApi(Build.VERSION_CODES.N)
    private val smsPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(READ_SMS, false) -> {
                // Read sms granted.]
                Log.i(TAG, "Read sms granted")
                viewModel.getSmsList(requireContext())
            }
            permissions.getOrDefault(RECEIVE_SMS, false) -> {
                // RECEIVE_SMS granted.
                Log.i(TAG, "RECEIVE_SMS granted")
            }
            else -> {
                // No sms permissions granted.
                Log.i(TAG, "No sms permissions granted")
                Snackbar.make(
                    binding.root,
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = OtpListFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.otpRecyclerView.adapter = SmsAdapter(SmsAdapter.OnClickListener {
            Log.i(TAG, "sms clicked")
        })

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isPermissionGranted()) {
            Log.i(TAG, "sms permission granted")
            viewModel.getSmsList(requireContext())
        } else {
            Log.i(TAG, "Requesting sms permissions")
            smsPermissionRequest.launch(
                arrayOf(
                    READ_SMS,
                    RECEIVE_SMS
                )
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        val smsPermissionApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(),
                            READ_SMS
                        )) && (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(),
                            RECEIVE_SMS
                        ))

        return smsPermissionApproved
    }
}