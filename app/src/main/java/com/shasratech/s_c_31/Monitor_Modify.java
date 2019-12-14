package com.shasratech.s_c_31;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import static com.shasratech.s_c_31.Globals.ErrMsgDialog;
import static com.shasratech.s_c_31.Globals.FBAppCustomer;
import static com.shasratech.s_c_31.Globals.FBAppMBName;
import static com.shasratech.s_c_31.Globals.Get_Current_Date_Time;
import static com.shasratech.s_c_31.Globals.UPload_Data_Msg_List;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class Monitor_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Trip_No = null;
    private final String TAG = "Monitor_Modify";
    EditText Trip_No_ET = null;
    EditText TimeET= null;
    EditText KMET= null;
    EditText Monitoring_BreakingET= null;
    EditText NotchET= null;
    EditText Load_Meter_ReadingET= null;
    EditText Battery_Ammeter_ReadingET= null;

   // EditText Zone_ET = null;




    boolean Add_New = false;
    //String New_Monitor_Editor_Text = "";
    String Curr_Trip_No = "";
    String Time = "";
    String KM= "";
    String Monitoring_Breaking= "";
    String Notch= "";
    String Load_Meter_Reading= "";
    String Battery_Ammeter_Reading= "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeshed_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.Monitor_Add_New);
        Trip_No_ET = findViewById(R.id.Monitor_Trip_No) ;
        TimeET = findViewById(R.id.Monitor_Modify_Monitor_Time);
        KMET = findViewById(R.id.Monitor_Modify_KM);
        Monitoring_BreakingET = findViewById(R.id.Monitor_Modify_Monitor_Breaking);
        NotchET= findViewById(R.id.Monitor_Modify_Notch);
        Load_Meter_ReadingET = findViewById(R.id.Monitor_Modify_Load_Meter_Reading);
        Battery_Ammeter_ReadingET = findViewById(R.id.Monitor_Modify_Battery_Ammeter_Reading);


        Trip_No_ET.setVisibility(View.INVISIBLE);


        CB_Trip_No = findViewById(R.id.Monitor_Modify_Trip_No);
        CB_Trip_No.setOnItemSelectedListener(this);


        CB_Trip_No.UpdateCB(true, "Monitor", "Trip_No", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    Trip_No_ET.setVisibility(View.VISIBLE);
                } else {
                    Trip_No_ET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Trip_No.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Trip_No = parent.getItemAtPosition(position).toString();
                CurrentSelect_Trip_NO_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_Trip_NO_Changed();

    }

    private void CurrentSelect_Trip_NO_Changed () {
        if ((Curr_Trip_No != null) || (!Curr_Trip_No.isEmpty())) {
            String Sel = String.format("Trip_No = '%s'", Curr_Trip_No);
            Time = myDB_G.Get_Val_from_DB_UD("Monitor", "Monitor_Time", "", Sel); TimeET.setText(Time);
            KM = myDB_G.Get_Val_from_DB_UD("Monitor", "KM", "", Sel); KMET.setText(KM);
            Notch = myDB_G.Get_Val_from_DB_UD("Monitor", "Notch", "", Sel); NotchET.setText(Notch);
            Monitoring_Breaking = myDB_G.Get_Val_from_DB_UD("Monitor", "Monitor_Breaking", "", Sel); Monitoring_BreakingET.setText(Monitoring_Breaking);
            Load_Meter_Reading = myDB_G.Get_Val_from_DB_UD("Monitor", "Load_Meter_Reading", "", Sel); Load_Meter_ReadingET.setText(Load_Meter_Reading);
            Battery_Ammeter_Reading = myDB_G.Get_Val_from_DB_UD("Monitor", "Battery_Ammeter_Reading", "", Sel); Battery_Ammeter_ReadingET.setText(Battery_Ammeter_Reading);
          //  Active = myDB_G.Get_Val_from_DB_UD("HomeShed", "Active", "", Sel); Active_ET.setText(Active);

        }
    }

    public void on_Monitor_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnMonitor_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Trip_No = Trip_No_ET.getText().toString();
        }
        if ((Curr_Trip_No == null) || (Curr_Trip_No.isEmpty())) {
            ErrMsgDialog("Monitor is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("Trip_No = '%s'", Curr_Trip_No);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("Monitor", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String Time = TimeET.getText().toString();
        String KM = KMET.getText().toString();
        String Monitoring_Breaking = Monitoring_BreakingET.getText().toString();
        String Notch = NotchET.getText().toString();
        String Battery_Ammeter_Reading = Battery_Ammeter_ReadingET.getText().toString();
        String Load_meter_Reading = Load_Meter_ReadingET.getText().toString();






        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Trip_No + "," + Time + ","+ KM + ","+ Monitoring_Breaking + ","+ Notch + ","+ Battery_Ammeter_Reading + ","
                + "," + Load_meter_Reading;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_Monitor_Ordered(true, Create_TS, Modify_TS, User,Time, Curr_Trip_No, KM, Monitoring_Breaking, Notch, Load_meter_Reading, "")) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Trip_No, Curr_Activity.Act_Monitor, UpdateStatement);
            UPload_Data_Msg_List.add(msg);
        } else {
            ErrMsgDialog("Unable to Insert/Update Material into Local DB");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume () {
        super.onResume();
        CurrentSelect_Trip_NO_Changed();
    }
}
