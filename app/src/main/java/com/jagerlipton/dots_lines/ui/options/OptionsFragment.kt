package com.jagerlipton.dots_lines.ui.options

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
            if (binding.editTextDotCount.editText?.text.toString() != it.toString()) binding.editTextDotCount.editText?.setText(
                it.toString()
            )
        }
        maxLinksCountObserver = Observer<Int> {
            if (binding.editTextMaxLinks.editText?.text.toString() != it.toString()) binding.editTextMaxLinks.editText?.setText(
                it.toString()
            )
        }
        radiusDotObserver = Observer<Int> {
            if (binding.editTextRadius.editText?.text.toString() != it.toString()) binding.editTextRadius.editText?.setText(
                it.toString()
            )
        }

        binding.editTextDotCount.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!binding.editTextDotCount.editText?.text.isNullOrEmpty() && binding.editTextDotCount.editText?.text.toString()
                        .toInt() != optionsViewModel.getDotCount().value
                )
                    optionsViewModel.setDotCount(
                        binding.editTextDotCount.editText?.text.toString().toInt()
                    )
                visibleButton(isNotEmptyEditTexts() && isValidNumbers())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.editTextMaxLinks.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!binding.editTextMaxLinks.editText?.text.isNullOrEmpty() && binding.editTextMaxLinks.editText?.text.toString()
                        .toInt() != optionsViewModel.getMaxLinksCount().value
                )
                    optionsViewModel.setMaxLinksCount(
                        binding.editTextMaxLinks.editText?.text.toString().toInt()
                    )
                visibleButton(isNotEmptyEditTexts() && isValidNumbers())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.editTextRadius.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!binding.editTextRadius.editText?.text.isNullOrEmpty() && binding.editTextRadius.editText?.text.toString()
                        .toInt() != optionsViewModel.getRadius().value
                )
                    optionsViewModel.setRadius(
                        binding.editTextRadius.editText?.text.toString().toInt()
                    )
                visibleButton(isNotEmptyEditTexts() && isValidNumbers())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun visibleButton(flag: Boolean) {
        when (flag) {
            true -> binding.buttonNewGame.visibility = View.VISIBLE
            false -> binding.buttonNewGame.visibility = View.INVISIBLE
        }
    }

    private fun isNotEmptyEditTexts(): Boolean {
        return binding.editTextDotCount.editText!!.text.isNotEmpty() && binding.editTextMaxLinks.editText!!.text.isNotEmpty() && binding.editTextRadius.editText!!.text.isNotEmpty()
    }

    private fun isValidNumbers(): Boolean {
        if (binding.editTextDotCount.editText!!.text.toString().toInt() > 0 &&
            binding.editTextMaxLinks.editText!!.text.toString().toInt() > 0 &&
            binding.editTextRadius.editText!!.text.toString().toInt() > 0
        ) return true
        return false
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
        optionsViewModel.getDotCount().observe(this, dotCountObserver)
        optionsViewModel.getMaxLinksCount().observe(this, maxLinksCountObserver)
        optionsViewModel.getRadius().observe(this, radiusDotObserver)
    }

    override fun onStop() {
        super.onStop()
        optionsViewModel.getDotCount().removeObserver(dotCountObserver)
        optionsViewModel.getMaxLinksCount().removeObserver(maxLinksCountObserver)
        optionsViewModel.getRadius().removeObserver(radiusDotObserver)
    }

}