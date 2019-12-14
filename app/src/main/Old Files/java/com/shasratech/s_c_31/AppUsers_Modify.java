package com.shasratech.s_c_31;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AppUsers_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_users_soft_crusher);

        Spinner userName = (Spinner) findViewById(R.id.userName);

        EditText password = (EditText)findViewById(R.id.password) ;

        CheckBox disabled = (CheckBox) findViewById(R.id.disabled);
        CheckBox admin = (CheckBox) findViewById(R.id.admin);
        CheckBox managers = (CheckBox) findViewById(R.id.managers);
        CheckBox cash = (CheckBox) findViewById(R.id.cash);
        CheckBox report = (CheckBox) findViewById(R.id.report);
        Button save = (Button)findViewById(R.id.save) ;

        userName.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Item 1");
        categories.add("Item 2");
        categories.add("Item 3");
        categories.add("Item 4");
        categories.add("Item 5");
        categories.add("Item 6");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        userName.setAdapter(dataAdapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
