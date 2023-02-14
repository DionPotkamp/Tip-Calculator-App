package com.example.tipcalculatorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText priceField;

    SeekBar tipSeekBar;
    TextView tipSeekBarLabel;

    RadioGroup radioGroup;
    EditText splitOptionYesAmount;

    TextView tipAmountText;
    TextView totalCostText;
    TextView perPersonText;
    TextView perPersonTextView;

    Button clearButton;
    Button settingsButton;

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

        radioGroup = findViewById(R.id.radioGroup);
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
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this::SettingsButtonOnClick);

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

        splitOptionYesAmount.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            radioGroup.check(R.id.splitOptionYes);
            CalculatePrice();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            CalculatePrice();
        }
    };

    private void RadioGroupOnCheckedChanged(RadioGroup group, int checkedId) {
        CalculatePrice();
    }

    private int clearCounter = 0;

    private void ClearButtonOnClick(View view) {
        priceField.setText("");
        priceField.requestFocus();

        clearCounter++;
        if (clearCounter > 4) {
            // Show a dialog box to open settings
            new android.app.AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.clear_settings_dialog_message))
                    .setPositiveButton(getResources().getString(R.string.settings), (dialog, which) -> SettingsButtonOnClick(null))
                    .setNegativeButton(getResources().getString(R.string.cancel), null)
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setOnDismissListener(dialog -> clearCounter = 0)
                    .setCancelable(false)
                    .show();
        }

        onResume();
    }

    private void SettingsButtonOnClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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

        if (radioGroup.getCheckedRadioButtonId() == R.id.splitOptionYes) {
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
            int splitOptionYesAmountInt = 2;
            try {
                splitOptionYesAmountInt = Integer.parseInt(splitOptionYesAmountText);
            } catch (NumberFormatException e) {
                splitOptionYesAmount.setText("2");
                splitOptionYesAmount.setSelection(2);
            }
            perPerson = total / splitOptionYesAmountInt;
        }

        tipAmountText.setText(String.format("$%.2f", tip));
        totalCostText.setText(String.format("$%.2f", total));
        perPersonText.setText(String.format("$%.2f", perPerson));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("com.example.tipcalculatorapp", MODE_PRIVATE);

        // Remove the text watcher this prevents the wrong option to be selected.
        splitOptionYesAmount.removeTextChangedListener(textWatcher);

        tipSeekBar.setProgress(prefs.getInt("tipPercent", 15));
        tipSeekBarLabel.setText(prefs.getInt("tipPercent", 15) + "%");
        radioGroup.check(prefs.getInt("splitOption", R.id.splitOptionNo));
        splitOptionYesAmount.setText(String.valueOf(prefs.getInt("splitOptionYesAmount", 2)));

        splitOptionYesAmount.addTextChangedListener(textWatcher);
    }
}