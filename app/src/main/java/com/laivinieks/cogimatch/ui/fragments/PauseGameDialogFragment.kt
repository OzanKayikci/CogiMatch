package com.laivinieks.cogimatch.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.laivinieks.cogimatch.R

class PauseGameDialogFragment : DialogFragment() {

    private var onDialogCanceledCallback: (() -> Unit)? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.PauseGameDialogView).apply {
                setTitle("Stop Game")
                setMessage("Do you want to exit?")

                setPositiveButton("Exit") { dialog, id ->
                    findNavController().navigate(R.id.mainMenuFragment)

                }
                setNegativeButton("Cancel") { dialog, id ->
                    dialog.cancel()
                }

            }.create()


        return dialog
    }

    fun setOnDialogCanceledCallback(callback: () -> Unit) {
        onDialogCanceledCallback = callback
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDialogCanceledCallback?.invoke()
    }


    companion object {
        const val TAG = "PauseGameDialog"
    }
}

