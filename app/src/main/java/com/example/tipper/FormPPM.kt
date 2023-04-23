package com.example.tipper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.databinding.FragmentPpmBinding;
import java.text.DecimalFormat;

public class FormPPM extends Fragment {

    // wywołane przy tworzeniu
    private FragmentPpmBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPpmBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText massEditText = binding.editTextMass;
        EditText heightEditText = binding.editTextHeight;
        EditText ageEditText = binding.editTextAge;

        //guzik do wykonania akcji przeliczenia ppm
        Button ppmButton = binding.ppmButton;

        // odnalezienie pola, w które wpisywana będzie przeliczona wartość
        TextView ppmButtonLabel = binding.ppmCalculatedLabel;

        RadioButton ppmRadioWoman = binding.radioWoman;
        RadioButton ppmRadioMan = binding.radioMan;

        //dodanie akcji dla guzika
        ppmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String massText = massEditText.getText().toString();
                String heightText = heightEditText.getText().toString();
                String ageText = ageEditText.getText().toString();

                if (massText.length() != 0 &&
                        heightText.length() != 0 &&
                        ageText.length() != 0) {
                    if (ppmRadioWoman.isChecked()) {
                        //dla kobiet:
                        float output = new Float(655.0955) +
                                (new Float(9.5634) * Float.parseFloat(massText)) +
                                (new Float(1.8496) * Float.parseFloat(heightText)) -
                                (new Float(4.6756) * Float.parseFloat(ageText));

                        //obiekt potrzebny do formatowania wyniku
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(3);

                        //wpisanie wyniku w pole na ekranie
                        ppmButtonLabel.setText("" + df.format(output));
                    } else {
                        //dla mężczyzn:
                        float output = new Float(66.4730) +
                                (new Float(13.7516) * Float.parseFloat(massText)) +
                                (new Float(5.0033) * Float.parseFloat(heightText)) -
                                (new Float(6.7550) * Float.parseFloat(ageText));

                        //obiekt potrzebny do formatowania wyniku
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(3);

                        //wpisanie wyniku w pole na ekranie
                        ppmButtonLabel.setText("" + df.format(output));
                    }
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}