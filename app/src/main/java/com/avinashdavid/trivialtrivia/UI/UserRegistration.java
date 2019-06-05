package com.avinashdavid.trivialtrivia.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.avinashdavid.trivialtrivia.R;

public class UserRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Spinner coursesSpinner = (Spinner)findViewById(R.id.reg_course);
        ArrayAdapter<CharSequence> coursesAdapter = ArrayAdapter.createFromResource(this, R.array.courses, R.layout.spinner_text);
        coursesAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        coursesSpinner.setAdapter(coursesAdapter);

        Spinner genderSpinner = (Spinner)findViewById(R.id.reg_gender);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, R.layout.spinner_text);
        genderAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        genderSpinner.setAdapter(genderAdapter);

        Spinner gradeSpinner = (Spinner)findViewById(R.id.reg_gradeLevel);
        ArrayAdapter<CharSequence> gradeAdapter = ArrayAdapter.createFromResource(this, R.array.gradeLevel, R.layout.spinner_text);
        gradeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setOnItemSelectedListener(this);

        ((Button)findViewById(R.id.button_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistration.this, UserLogin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
