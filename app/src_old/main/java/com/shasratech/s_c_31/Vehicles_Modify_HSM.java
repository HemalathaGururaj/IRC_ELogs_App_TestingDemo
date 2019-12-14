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

public class Vehicles_Modify_HSM extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_soft_crusher);

        Spinner vehicle = (Spinner) findViewById(R.id.vehicle);
        Spinner customer = (Spinner) findViewById(R.id.customer);
        Spinner transport = (Spinner) findViewById(R.id.transport);

        EditText tareWeight = (EditText)findViewById(R.id.tareWeight) ;
        EditText cft = (EditText)findViewById(R.id.cft) ;
        EditText otherDetails = (EditText)findViewById(R.id.otherDetails) ;

        CheckBox newVehicle = (CheckBox) findViewById(R.id.newVehicle);
        CheckBox disabled = (CheckBox) findViewById(R.id.disabled);

        Button save = (Button)findViewById(R.id.save) ;

        vehicle.setOnItemSelectedListener(this);
        customer.setOnItemSelectedListener(this);
        transport.setOnItemSelectedListener(this);

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
        vehicle.setAdapter(dataAdapter);
        customer.setAdapter(dataAdapter);
        transport.setAdapter(dataAdapter);

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
