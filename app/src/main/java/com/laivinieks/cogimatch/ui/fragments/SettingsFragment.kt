package com.laivinieks.cogimatch.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laivinieks.cogimatch.adapter.LanguageAdapter
import com.laivinieks.cogimatch.databinding.FragmentSettingsBinding
import com.laivinieks.cogimatch.utilities.SoundSettings
import com.laivinieks.cogimatch.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var soundSettings: SoundSettings

    @Inject
    lateinit var sharedPref: SharedPreferences


    private val settingsViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        soundSettings = SoundSettings(view, settingsViewModel, sharedPref)
        initRecycleView()
        buttonHandle()
        return view
    }

    private fun buttonHandle() {
        binding.credits.setOnClickListener {
            val dialogFragment = CreditsDialogFragment()
            dialogFragment.show(childFragmentManager, CreditsDialogFragment.TAG)
        }
    }

    private fun initRecycleView() {
        val horizontalLM =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLanguage.layoutManager = horizontalLM
        val adapter = LanguageAdapter(requireContext(), settingsViewModel, requireActivity())
        binding.rvLanguage.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}