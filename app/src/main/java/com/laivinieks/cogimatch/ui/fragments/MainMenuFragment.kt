package com.laivinieks.cogimatch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.databinding.FragmentMainMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMainMenuBinding.inflate(inflater)
        buttonOperations()
        return binding.root
    }

    private fun buttonOperations() {
        binding.btnArcade.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_gameBoardFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}