package com.shasratech.s_c_31;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.shasratech.s_c_31.Globals.*;

public class OpenningBal_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static MySpinner CB_Account = null;
    Calendar myCalendar;
    EditText date,openingBalance,additionalDetails,time;
    String Curr_OpeningBal = "";
    String openingBalance1,additionalDetails1,time1,date1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_modify_activity);

        CheckBox paidToCompany = (CheckBox) findViewById(R.id.paidToCompany);
        CheckBox receivedFromCompany = (CheckBox) findViewById(R.id.receivedFromCompany);

         openingBalance = (EditText)findViewById(R.id.openingBalance) ;
         additionalDetails = (EditText)findViewById(R.id.additionalDetails) ;
        date = (EditText)findViewById(R.id.date) ;
          time = (EditText)findViewById(R.id.time) ;

         CB_Account =  findViewById(R.id.accountSpinner);

        Button save = (Button)findViewById(R.id.save) ;

        myCalendar = Calendar.getInstance();

        CB_Account.setOnItemSelectedListener(this);
        CB_Account.UpdateCB(true, "Person", "Name", "", "DISTINCT");

        CB_Account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_OpeningBal = parent.getItemAtPosition(position).toString();
                CurrentSelect_OpeningBal_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_OpeningBal_Changed();

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
                new DatePickerDialog(OpenningBal_Modify.this, date1, myCalendar
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
                mTimePicker = new TimePickerDialog(OpenningBal_Modify.this, new TimePickerDialog.OnTimeSetListener() {
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
                if ((Curr_OpeningBal == null) || (Curr_OpeningBal.isEmpty())) {
                    ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
                    return;
                }

                String Sel = String.format("Amount = '%s'", Curr_OpeningBal);

                String Create_TS = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Create_TS", "", Sel);



                String Modify_TS = Get_Current_Date_Time();

                if ((Create_TS == null) || (Create_TS.isEmpty())) {
                    Create_TS = Modify_TS;
                }
                String User = FBAppMBName;

                String OpeningBalance = openingBalance.getText().toString();
                String AdditionalDetails = additionalDetails.getText().toString();
                String Date = date.getText().toString();
                String Time = time.getText().toString();

                String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_OpeningBal + "," + OpeningBalance + "," + AdditionalDetails
                        + "," + Date + "," + Time ;

                // Need to add to Local DB
                //String[] Con_List = {Create_TS, Modify_TS, User, Curr_Material, Rate, Units, Disabled, OtherDetails, CFT_Rate};
                if (myDB_G.Insert_update_into_MoneyTransaction_Ordered(true, Date+","+Time, User,"", "",Curr_OpeningBal,"","","","","OpeningBalance", "AdditionalDetails")) {
                    // Upload to Could
                    ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");
                    //FB_N_Activity_2.OpeningBal_InUp2_FB(Curr_OpeningBal, UpdateStatement);
                } else {
                    ErrMsgDialog("Unable to Insert/Update Material into Local DB");
                }
            }
        });
    }

    private void CurrentSelect_OpeningBal_Changed () {
        if ((Curr_OpeningBal != null) && (!Curr_OpeningBal.isEmpty())) {
            String Sel = String.format("Amount = '%s'", Curr_OpeningBal);
            openingBalance1 = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "", Sel); openingBalance.setText(openingBalance1);
            additionalDetails1 = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "OTherDetails", "", Sel); additionalDetails.setText(additionalDetails1);
            time1 = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Create_TS", "", Sel); time.setText(time1);
            date1 = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Create_TS", "", Sel); date.setText(date1);
        }
    }

    public void on_Material_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
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
