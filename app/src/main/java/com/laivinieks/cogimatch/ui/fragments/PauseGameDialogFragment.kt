package com.laivinieks.cogimatch.ui.fragments


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.utilities.Constants
import com.laivinieks.cogimatch.utilities.SoundManager
import com.laivinieks.cogimatch.utilities.SoundSettings
import com.laivinieks.cogimatch.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PauseGameDialogFragment : DialogFragment() {

    private var onDialogCanceledCallback: (() -> Unit)? = null
    private var onPositiveButtonCallback: (() -> Unit)? = null

    private lateinit var soundSettings: SoundSettings

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val settingsViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[SettingsViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Timber.d("create dialog")
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_pause_menu_dialog, null)
        val negativeButton = view.findViewById<MaterialButton>(R.id.negativeButton)
        val positiveButton = view.findViewById<MaterialButton>(R.id.positiveButton)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(view).create()

        soundSettings = SoundSettings(view, settingsViewModel, sharedPref)


        negativeButton.setOnClickListener {
            dialog.cancel()
        }

        positiveButton.setOnClickListener {
            onPositiveButtonCallback?.invoke()
        }



        return dialog
    }

    fun setOnPositiveButtonCallback(callback: () -> Unit) {
        onPositiveButtonCallback = callback
    }

    fun setOnDialogCanceledCallback(callback: () -> Unit) {
        onDialogCanceledCallback = callback
    }



    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDialogCanceledCallback?.invoke()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onDialogCanceledCallback?.invoke()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, callback
        )
    }

    companion object {
        const val TAG = "PauseGameDialog"
    }
}

/*
*
*  MaterialAlertDialogBuilder(requireContext(), R.style.PauseGameDialogView).apply {
                setTitle("Pause ")
                setMessage("Do you want to exit? ")

                setPositiveButton("Exit") { dialog, id ->
                   onPositiveButtonCallback?.invoke()

                }
                setNegativeButton("Cancel ") { dialog, id ->
                    dialog.cancel()
                }

            }.create()
* */