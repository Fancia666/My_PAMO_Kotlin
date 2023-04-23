package com.example.tipper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentPpmBinding
import java.text.DecimalFormat

class FormPPM : Fragment() {
    // wywołane przy tworzeniu
    private var binding: FragmentPpmBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPpmBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val massEditText = binding!!.editTextMass
        val heightEditText = binding!!.editTextHeight
        val ageEditText = binding!!.editTextAge

        //guzik do wykonania akcji przeliczenia ppm
        val ppmButton = binding!!.ppmButton

        // odnalezienie pola, w które wpisywana będzie przeliczona wartość
        val ppmButtonLabel = binding!!.ppmCalculatedLabel
        val ppmRadioWoman = binding!!.radioWoman
        val ppmRadioMan = binding!!.radioMan

        //dodanie akcji dla guzika
        ppmButton.setOnClickListener {
            val massText = massEditText.text.toString()
            val heightText = heightEditText.text.toString()
            val ageText = ageEditText.text.toString()
            if (massText.isNotEmpty() && heightText.isNotEmpty() && ageText.isNotEmpty()) {
                if (ppmRadioWoman.isChecked) {
                    //dla kobiet:
                    val output =
                        (655.0955 + 9.5634 * massText.toFloat() + 1.8496 * heightText.toFloat() - 4.6756 * ageText.toFloat()).toFloat()

                    //obiekt potrzebny do formatowania wyniku
                    val df = DecimalFormat()
                    df.maximumFractionDigits = 3

                    //wpisanie wyniku w pole na ekranie
                    ppmButtonLabel.text = df.format(output.toDouble())
                } else {
                    //dla mężczyzn:
                    val output =
                        (66.4730 + 13.7516 * massText.toFloat() + 5.0033 * heightText.toFloat() - 6.7550 * ageText.toFloat()).toFloat()

                    //obiekt potrzebny do formatowania wyniku
                    val df = DecimalFormat()
                    df.maximumFractionDigits = 3

                    //wpisanie wyniku w pole na ekranie
                    ppmButtonLabel.text = df.format(output.toDouble())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}