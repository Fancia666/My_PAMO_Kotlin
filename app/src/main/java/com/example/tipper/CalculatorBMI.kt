package com.example.tipper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentBmiBinding
import java.text.DecimalFormat

class CalculatorBMI : Fragment() {
    // wywołane przy tworzeniu
    private var binding: FragmentBmiBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val massEditText = binding!!.editTextMass
        val heightEditText = binding!!.editTextHeight

        //guzik do wykonania akcji przeliczenia bmi
        val bmiButton = binding!!.bmiButton

        // odnalezienie pola, w które wpisywana będzie przeliczona wartość
        val bmiButtonLabel = binding!!.bmiCalculatedLabel

        //dodanie akcji dla guzika
        bmiButton.setOnClickListener {
            val massText = massEditText.text.toString()
            val heightText = heightEditText.text.toString()
            if (massText.length == 0 || heightText.length == 0) {
            } else {
                //przeliczenie funkcji
                val oo = massText.toFloat() /
                        (heightText.toFloat() * heightText.toFloat()) * 10000

                //obiekt potrzebny do formatowania wyniku
                val df = DecimalFormat()
                df.maximumFractionDigits = 2

                //wpisanie wyniku w pole na ekranie
                bmiButtonLabel.text = df.format(oo.toDouble())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}