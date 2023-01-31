package com.example.tipcalculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        priceField = findViewById(R.id.priceField);

        tipSeekBar      = findViewById(R.id.tipSeekBar);
        tipSeekBarLabel = findViewById(R.id.tipSeekBarLabel);
        tipSeekBarLabel.setText(tipSeekBar.getProgress() + "%");

        radioGroup           = findViewById(R.id.radioGroup);
        splitOptionNo        = findViewById(R.id.splitOptionNo);
        splitOptionYes       = findViewById(R.id.splitOptionYes);
        splitOptionYesAmount = findViewById(R.id.splitOptionYesAmount);

        tipAmountText = findViewById(R.id.tipAmountText);
        totalCostText = findViewById(R.id.totalCostText);
        perPersonText = findViewById(R.id.perPersonText);

        tipSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipSeekBarLabel.setText(progress + "%");
                calculatePrice();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            calculatePrice();
        });

        priceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                calculatePrice();
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        splitOptionYesAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                splitOptionNo.setChecked(false);
                splitOptionYes.setChecked(true);
                calculatePrice();
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });
    }

    private void calculatePrice() {
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
            }

            int splitOptionYesAmountInt = Integer.parseInt(splitOptionYesAmountText);
            perPerson = total / splitOptionYesAmountInt;
        }

        tipAmountText.setText(String.format("$%.2f", tip));
        totalCostText.setText(String.format("$%.2f", total));
        perPersonText.setText(String.format("$%.2f", perPerson));
    }
}