package com.example.tipcalculatorapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    TextView tipSeekBarLabel;
    SeekBar tipSeekBar;
    RadioGroup radioGroup;
    EditText splitOptionYesAmount;
    Button saveAndBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {   // Hide the action bar
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_settings);

        tipSeekBar = findViewById(R.id.tipSeekBar);
        tipSeekBarLabel = findViewById(R.id.tipSeekBarLabel);
        tipSeekBarLabel.setText(tipSeekBar.getProgress() + "%");

        tipSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipSeekBarLabel.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        splitOptionYesAmount = findViewById(R.id.splitOptionYesAmount);

        saveAndBackButton = findViewById(R.id.saveAndBackButton);
        saveAndBackButton.setOnClickListener(this::SaveAndBackButtonOnClick);
    }

    private void SaveAndBackButtonOnClick(View view) {
        // onPause() will be called automatically
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("com.example.tipcalculatorapp", MODE_PRIVATE);

        tipSeekBar.setProgress(prefs.getInt("tipPercent", 15));
        radioGroup.check(prefs.getInt("splitOption", R.id.splitOptionNo));
        splitOptionYesAmount.setText(String.valueOf(prefs.getInt("splitOptionYesAmount", 2)));
    }

    @Override
    protected void onPause() {
        super.onPause();

        getSharedPreferences("com.example.tipcalculatorapp", MODE_PRIVATE)
                .edit()
                .putInt("tipPercent", tipSeekBar.getProgress())
                .putInt("splitOption", radioGroup.getCheckedRadioButtonId())
                .putInt("splitOptionYesAmount", Integer.parseInt(splitOptionYesAmount.getText().toString()))
                .apply();
    }
}