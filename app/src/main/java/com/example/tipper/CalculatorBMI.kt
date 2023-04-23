package com.example.tipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.myapplication.databinding.FragmentBmiBinding;

import java.text.DecimalFormat;

public class CalculatorBMI extends Fragment {

    // wywołane przy tworzeniu
    private FragmentBmiBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentBmiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText massEditText = binding.editTextMass;
        EditText heightEditText = binding.editTextHeight;

        //guzik do wykonania akcji przeliczenia bmi
        Button bmiButton = binding.bmiButton;

        // odnalezienie pola, w które wpisywana będzie przeliczona wartość
        TextView bmiButtonLabel = binding.bmiCalculatedLabel;

        //dodanie akcji dla guzika
        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String massText = massEditText.getText().toString();
                String heightText = heightEditText.getText().toString();

                if (massText.length() == 0 || heightText.length() == 0) {

                } else {
                    //przeliczenie funkcji
                    float oo = ((Float.parseFloat(massText)) /
                            (Float.parseFloat(heightText) * Float.parseFloat(heightText))) * 10000;

                    //obiekt potrzebny do formatowania wyniku
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);

                    //wpisanie wyniku w pole na ekranie
                    bmiButtonLabel.setText("" + df.format(oo));

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