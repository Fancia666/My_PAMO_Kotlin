package com.example.tipper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText; // for bill amount input
import android.widget.TextView; // for displaying text

import java.text.DecimalFormat;
import java.text.NumberFormat; // for currency formatting

public class MainActivity extends AppCompatActivity {

    private TextView bmiLabel;
    private TextView bmiButtonLabel;

    private EditText massEditText;

    private EditText heightEditText;

    private Button bmiButton;

    // wołane przy tworzeniu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmiLabel = (TextView) findViewById(R.id.bmiLabel);
        bmiButtonLabel = (TextView) findViewById(R.id.bmiCalculatedLabel);

        massEditText =
                (EditText) findViewById(R.id.editTextMass);

        bmiButton = (Button) findViewById(R.id.bmiButton);

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



                        // now assign the system
                        // service to InputMethodManager
                        InputMethodManager manager
                                = (InputMethodManager)
                                getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        manager
                                .hideSoftInputFromWindow(
                                        view.getWindowToken(), 0);


                    //wpisanie wyniku w pole na ekranie
                    bmiButtonLabel.setText("" + df.format(oo));

                }
            }
        });

        heightEditText = (EditText) findViewById(R.id.editTextHeight);
    }
}