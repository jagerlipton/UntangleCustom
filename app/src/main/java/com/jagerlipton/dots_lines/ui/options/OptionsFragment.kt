package com.jagerlipton.dots_lines.ui.options

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.jagerlipton.dots_lines.R
import com.jagerlipton.dots_lines.databinding.FragmentOptionsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dotCountObserver: Observer<Int>
    private lateinit var maxLinksCountObserver: Observer<Int>
    private lateinit var radiusDotObserver: Observer<Int>
    private val optionsViewModel by viewModel<OptionsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNewGame.setOnClickListener { newGame() }

        dotCountObserver = Observer<Int> {
            if (binding.sliderDotCount.value != it.toFloat()) binding.sliderDotCount.value =
                it.toFloat()
        }
        maxLinksCountObserver = Observer<Int> {
            if (binding.sliderMaxLinksCount.value != it.toFloat()) binding.sliderMaxLinksCount.value =
                it.toFloat()
        }
        radiusDotObserver = Observer<Int> {
            if (binding.sliderRadius.value != it.toFloat()) {
                binding.sliderRadius.value = it.toFloat()
                binding.sliderRadiusView.thumbRadius = it
            }
        }

        binding.sliderDotCount.addOnChangeListener { slider, value, fromUser ->
            optionsViewModel.setDotCount(value.toInt())
        }

        binding.sliderMaxLinksCount.addOnChangeListener { slider, value, fromUser ->
            optionsViewModel.setMaxLinksCount(value.toInt())
        }

        binding.sliderRadius.addOnChangeListener { slider, value, fromUser ->
            optionsViewModel.setRadius(value.toInt())
            binding.sliderRadiusView.thumbRadius = value.toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun newGame() {
        optionsViewModel.savePrefs()
        optionsViewModel.newCustomGame()
        findNavController().navigate(R.id.action_navigation_options_to_navigation_game)
    }

    override fun onStart() {
        super.onStart()
        optionsViewModel.dotCount.observe(this, dotCountObserver)
        optionsViewModel.maxLinksCount.observe(this, maxLinksCountObserver)
        optionsViewModel.radiusDot.observe(this, radiusDotObserver)
    }

    override fun onStop() {
        super.onStop()
        optionsViewModel.dotCount.removeObserver(dotCountObserver)
        optionsViewModel.maxLinksCount.removeObserver(maxLinksCountObserver)
        optionsViewModel.radiusDot.removeObserver(radiusDotObserver)
    }

}

