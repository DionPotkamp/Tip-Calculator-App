package com.example.tipcalculatorapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final int DEFAULT_TIP_PERCENT = 15;
    EditText priceField;

    SeekBar tipSeekBar;
    TextView tipSeekBarLabel;

    RadioGroup radioGroup;
    RadioButton splitOptionNo;
    RadioButton splitOptionYes;
    EditText splitOptionYesAmount;

    TextView tipAmountText;
    TextView totalCostText;
    TextView perPersonText;
    TextView perPersonTextView;

    Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {   // Hide the action bar
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_main);

        priceField = findViewById(R.id.priceField);
        priceField.requestFocus();

        tipSeekBar = findViewById(R.id.tipSeekBar);
        tipSeekBarLabel = findViewById(R.id.tipSeekBarLabel);
        tipSeekBar.setProgress(DEFAULT_TIP_PERCENT);
        tipSeekBarLabel.setText(DEFAULT_TIP_PERCENT + "%");

        radioGroup = findViewById(R.id.radioGroup);
        splitOptionNo = findViewById(R.id.splitOptionNo);
        splitOptionYes = findViewById(R.id.splitOptionYes);
        splitOptionYesAmount = findViewById(R.id.splitOptionYesAmount);
        radioGroup.setOnCheckedChangeListener(this::RadioGroupOnCheckedChanged);

        tipAmountText = findViewById(R.id.tipAmountText);
        totalCostText = findViewById(R.id.totalCostText);
        perPersonText = findViewById(R.id.perPersonText);
        perPersonTextView = findViewById(R.id.perPersonTextView);
        perPersonText.setEnabled(false);
        perPersonTextView.setEnabled(false);

        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this::ClearButtonOnClick);

        priceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                CalculatePrice();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });

        tipSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipSeekBarLabel.setText(progress + "%");
                CalculatePrice();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        splitOptionYesAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                splitOptionNo.setChecked(false);
                splitOptionYes.setChecked(true);
                CalculatePrice();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });
    }

    private void RadioGroupOnCheckedChanged(RadioGroup group, int checkedId) {
        CalculatePrice();
    }

    private void ClearButtonOnClick(View view) {
        priceField.setText("");
        priceField.requestFocus();

        tipSeekBar.setProgress(DEFAULT_TIP_PERCENT);
        tipSeekBarLabel.setText(DEFAULT_TIP_PERCENT + "%");

        splitOptionYesAmount.setText("");
        splitOptionNo.setChecked(true);
    }

    private void CalculatePrice() {
        String priceFieldText = priceField.getText().toString();

        if (priceFieldText.isEmpty()) {
            priceFieldText = "0";
        }

        double price = Double.parseDouble(priceFieldText);
        double tip = price * tipSeekBar.getProgress() / 100;
        double total = price + tip;
        double perPerson = total;

        if (splitOptionYes.isChecked()) {
            String splitOptionYesAmountText = splitOptionYesAmount.getText().toString();

            if (splitOptionYesAmountText.isEmpty()) {
                splitOptionYesAmountText = "1";
                perPersonText.setEnabled(false);
                perPersonTextView.setEnabled(false);
            } else {
                perPersonText.setEnabled(true);
                perPersonTextView.setEnabled(true);
            }

            // If the user enters an integer that is too large, the app will crash.
            int splitOptionYesAmountInt = 1;
            try {
                splitOptionYesAmountInt = Integer.parseInt(splitOptionYesAmountText);
            } catch (NumberFormatException e) {
                splitOptionYesAmount.setText("1");
                splitOptionYesAmount.setSelection(1);
            }
            perPerson = total / splitOptionYesAmountInt;
        }

        tipAmountText.setText(String.format("$%.2f", tip));
        totalCostText.setText(String.format("$%.2f", total));
        perPersonText.setText(String.format("$%.2f", perPerson));
    }
}