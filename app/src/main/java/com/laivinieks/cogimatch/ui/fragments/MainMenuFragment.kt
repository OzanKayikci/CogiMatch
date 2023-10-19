package com.laivinieks.cogimatch.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.databinding.FragmentMainMenuBinding
import com.laivinieks.cogimatch.utilities.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMainMenuBinding.inflate(inflater)
        loadFieldsFromSharedPref()
        buttonOperations()
        return binding.root
    }

    private fun buttonOperations() {
        binding.btnArcade.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_gameBoardFragment)
        }
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_settingsFragment)
        }
    }

    private fun loadFieldsFromSharedPref() {

        val score = sharedPreferences.getInt(Constants.ARCADE_SCORE, 0)
        if (score > 0) {
            binding.tvArcadeScore.isVisible = true
        }
        binding.tvArcadeScore.text = "${getString(R.string.score)}: $score "
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, callback
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}