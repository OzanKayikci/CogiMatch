package com.laivinieks.cogimatch.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.databinding.FragmentGameOverBinding
import com.laivinieks.cogimatch.utilities.CardUtility
import com.laivinieks.cogimatch.utilities.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GameOverFragment : Fragment() {
    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameOverBinding.inflate(inflater, container, false)

        val values = arguments?.getIntArray("values")
        val turns = values?.get(0) ?: 0
        val matches = values?.get(1) ?: 0
        val stages = values?.get(2) ?: 0
        Timber.d("$values")
        val score = CardUtility.scoreCalculator(turns, matches, stages)

        setTexts(turns, matches, score)

        buttonHandle()

        return binding.root
    }

    private fun setTexts(turns: Int, matches: Int, score: Int) {
        binding.tvTurns.text = "Turns : $turns "
        binding.tvMatches.text = "Matches : $matches "
        binding.tvScore.text =
            "Score : $score "
    }

    private fun buttonHandle() {
        binding.btnMainMenu.setOnClickListener {
            findNavController().navigate(R.id.action_gameOverFragment_to_mainMenuFragment)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_gameOverFragment_to_mainMenuFragment)
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