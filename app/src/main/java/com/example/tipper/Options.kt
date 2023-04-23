package com.example.tipper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentOptionsBinding

class Options : Fragment() {
    private var binding: FragmentOptionsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.buttonBMI.setOnClickListener {
            NavHostFragment.findNavController(this@Options)
                .navigate(R.id.action_options_to_calculator_bmi)
        }
        binding!!.buttonPpm.setOnClickListener {
            NavHostFragment.findNavController(this@Options)
                .navigate(R.id.action_options_to_form_ppm)
        }
        binding!!.buttonRecipes.setOnClickListener {
            NavHostFragment.findNavController(this@Options)
                .navigate(R.id.action_options_to_recipes)
        }
        binding!!.buttonQuiz.setOnClickListener {
            NavHostFragment.findNavController(this@Options)
                .navigate(R.id.action_options_to_quiz)
        }
        binding!!.buttonCannon.setOnClickListener {
            NavHostFragment.findNavController(this@Options)
                .navigate(R.id.action_options_to_cannon)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}