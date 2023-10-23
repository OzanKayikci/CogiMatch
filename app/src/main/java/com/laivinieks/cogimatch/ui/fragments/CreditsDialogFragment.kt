package com.laivinieks.cogimatch.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.laivinieks.cogimatch.R


class CreditsDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_credits_dialog, null)
        val negativeButton = view.findViewById<TextView>(R.id.btnCloseDialog)

        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(view).create()
        negativeButton.setOnClickListener {
            dialog.cancel()
        }
        return dialog
    }

    companion object {
        const val TAG = "CreditsDialog"
    }
}