package com.laivinieks.cogimatch.ui.fragments


import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.databinding.FragmentSplashScreenBinding
import com.laivinieks.cogimatch.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.Contexts


@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        val videoPath = R.raw.cogi_match_splash
        val video =
            Uri.parse("android.resource://" + requireContext().packageName + "/" + videoPath)
        binding.videoView.setVideoURI(video)

        binding.videoView.setOnCompletionListener {
            Handler(Looper.getMainLooper()).postDelayed(
                { findNavController().navigate(R.id.action_splashScreenFragment_to_mainMenuFragment) },
                500
            )

        }

        binding.videoView.start()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}