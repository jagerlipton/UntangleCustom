package com.jagerlipton.dots_lines.ui.home

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jagerlipton.dots_lines.R
import com.jagerlipton.dots_lines.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var gameOverObserver: Observer<Boolean>
    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNewGame.setOnClickListener { newCustomGame() }
        binding.buttonReturnGame.setOnClickListener { returnToGame() }
        gameOverObserver = Observer<Boolean> {
            when (it) {
                true -> binding.buttonReturnGame.visibility = View.GONE
                false -> binding.buttonReturnGame.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun newCustomGame() {
        findNavController().navigate(R.id.action_navigation_home_to_navigation_options)
    }

    private fun returnToGame() {
        findNavController().navigate(R.id.action_navigation_home_to_navigation_game2)
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.gameOver.observe(this, gameOverObserver)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadPrefs()
    }

    override fun onStop() {
        super.onStop()
        homeViewModel.gameOver.removeObserver(gameOverObserver)
    }
}