package com.shasratech.s_c_31;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Inventory_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Calendar myCalendar;
    EditText date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_soft_crusher);


        Spinner material = (Spinner) findViewById(R.id.material);
        Spinner supplier = (Spinner) findViewById(R.id.supplier);
        Spinner invoice = (Spinner) findViewById(R.id.invoice);
        Spinner batchId = (Spinner) findViewById(R.id.batchId);
        Spinner consumer = (Spinner) findViewById(R.id.consumer);
        Spinner sgstSlab = (Spinner) findViewById(R.id.sgstSlab);
        Spinner cgstSlab = (Spinner) findViewById(R.id.cgstSlab);

        EditText rate = (EditText)findViewById(R.id.rate) ;
        date = (EditText)findViewById(R.id.date) ;
        final EditText time = (EditText)findViewById(R.id.time) ;
        final EditText totalItems = (EditText)findViewById(R.id.totalItems) ;
        EditText alertsTotal = (EditText)findViewById(R.id.alertsTotal) ;
        EditText remailQty = (EditText)findViewById(R.id.remailQty) ;
        EditText alert = (EditText)findViewById(R.id.alert) ;
        EditText issueItems = (EditText)findViewById(R.id.issueItems) ;
        EditText initQty = (EditText)findViewById(R.id.initQty) ;
        Button save = (Button)findViewById(R.id.save) ;

        material.setOnItemSelectedListener(this);
        supplier.setOnItemSelectedListener(this);
        invoice.setOnItemSelectedListener(this);
        batchId.setOnItemSelectedListener(this);
        consumer.setOnItemSelectedListener(this);
        sgstSlab.setOnItemSelectedListener(this);
        cgstSlab.setOnItemSelectedListener(this);

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
        material.setAdapter(dataAdapter);
        supplier.setAdapter(dataAdapter);
        invoice.setAdapter(dataAdapter);
        batchId.setAdapter(dataAdapter);
        consumer.setAdapter(dataAdapter);
        sgstSlab.setAdapter(dataAdapter);
        cgstSlab.setAdapter(dataAdapter);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Inventory_Modify.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Inventory_Modify.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


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

    public void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }
}
