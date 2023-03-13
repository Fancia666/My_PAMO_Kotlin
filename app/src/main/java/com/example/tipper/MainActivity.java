package com.example.tipper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView bmiButtonLabel;
    private EditText massEditText;
    private EditText heightEditText;
    private Button bmiButton;

    // wywołane przy tworzeniu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //pola w ktore wpisywane są wartości
        massEditText = (EditText) findViewById(R.id.editTextMass);
        heightEditText = (EditText) findViewById(R.id.editTextHeight);

        //guzik do wykonania akcji przeliczenia bmi
        bmiButton = (Button) findViewById(R.id.bmiButton);

        // odnalezienie pola, w które wpisywana będzie przeliczona wartość
        bmiButtonLabel = (TextView) findViewById(R.id.bmiCalculatedLabel);

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
                            ( Float.parseFloat(heightText) * Float.parseFloat(heightText) )) * 10000;

                    //obiekt potrzebny do formatowania wyniku
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);

                    //wpisanie wyniku w pole na ekranie
                    bmiButtonLabel.setText("" + df.format(oo));

                }
            }
        });
    }
}