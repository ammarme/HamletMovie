package com.android.movieapp.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.movieapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoInternetDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.no_internet_title)
            .setMessage(R.string.no_internet_message)
            .setIcon(R.drawable.wifi_off)
            .setCancelable(false)
            .setPositiveButton(
                R.string.action_retry,
                null
            )
            .create().apply {
                setOnShowListener {
                    val button = getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                    button.setOnClickListener {
                        startActivity(android.content.Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
                    }
                }
            }
    }

    companion object {
        const val TAG = "NoInternetDialog"
    }
}
