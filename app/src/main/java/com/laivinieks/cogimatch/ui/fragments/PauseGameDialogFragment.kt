package com.laivinieks.cogimatch.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.laivinieks.cogimatch.R

class PauseGameDialogFragment : DialogFragment() {

    private var onDialogCanceledCallback: (() -> Unit)? = null
    private var onPositiveButtonCallback:(()->Unit)? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.PauseGameDialogView).apply {
                setTitle("Stop Game")
                setMessage("Do you want to exit?")

                setPositiveButton("Exit") { dialog, id ->
                   onPositiveButtonCallback?.invoke()

                }
                setNegativeButton("Cancel") { dialog, id ->
                    dialog.cancel()
                }

            }.create()


        return dialog
    }

    fun setOnPositiveButtonCallback(callback: () -> Unit){
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

