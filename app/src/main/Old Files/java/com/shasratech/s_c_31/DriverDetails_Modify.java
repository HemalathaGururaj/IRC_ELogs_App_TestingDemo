package com.shasratech.s_c_31;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import static com.shasratech.s_c_31.Globals.ErrMsgDialog;
import static com.shasratech.s_c_31.Globals.FBAppCustomer;
import static com.shasratech.s_c_31.Globals.FBAppMBName;
import static com.shasratech.s_c_31.Globals.Get_Current_Date_Time;
import static com.shasratech.s_c_31.Globals.UPload_Data_Msg_List;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class DriverDetails_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Driver_UID = null;
    private final String TAG = "DriverDetails_Modify";
    EditText Driver_UIDET = null;
    EditText Designation_ET = null;
    EditText Address_ET = null;
    EditText FullName_ET = null;
    EditText PhNum_ET = null;
    EditText Zone_ET = null;
    Switch Active_CBx = null;



    boolean Add_New = false;
    String New_Driver_UID_Editor_Text = "";
    String Curr_Driver_UID = "";
    String Designation = "";
    String PhNum = "";
    String Address = "";
    String FullName = "";
    String Zone = "";
    boolean Active = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverdetails_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.Driver_UID_Add_New);
        Driver_UIDET = findViewById(R.id.Driver_UID_TE) ;
        Designation_ET = findViewById(R.id.DriverDetails_Modify_Designation);
        PhNum_ET = findViewById(R.id.DriverDetails_Modify_PhNum);
        Address_ET = findViewById(R.id.DriverDetails_Modify_Address);
        FullName_ET = findViewById(R.id.DriverDetails_Modify_FullName);
        Zone_ET = findViewById(R.id.DriverDetails_Modify_Zone);
        Active_CBx = findViewById(R.id.DriverDetails_Modify_Active);


        Driver_UIDET.setVisibility(View.INVISIBLE);

        CB_Driver_UID = findViewById(R.id.DriverDetails_Modify_Driver_UID);
        CB_Driver_UID.setOnItemSelectedListener(this);


        CB_Driver_UID.UpdateCB(true, "DriverDetails", "Driver_UID", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    Driver_UIDET.setVisibility(View.VISIBLE);
                } else {
                    Driver_UIDET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Driver_UID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Driver_UID = parent.getItemAtPosition(position).toString();
                CurrentSelect_DriverDetails_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_DriverDetails_Changed();

    }

    private void CurrentSelect_DriverDetails_Changed () {
        if ((Curr_Driver_UID != null) && (!Curr_Driver_UID.isEmpty())) {
            String Sel = String.format("Driver_UID= '%s'", Curr_Driver_UID);

            Designation = myDB_G.Get_Val_from_DB_UD("DriverDetails", "Designation", "", Sel); Designation_ET.setText(Designation);
            PhNum = myDB_G.Get_Val_from_DB_UD("DriverDetails", "PhNum", "", Sel); PhNum_ET.setText(PhNum);
            Address = myDB_G.Get_Val_from_DB_UD("DriverDetails", "Address", "", Sel); Address_ET.setText(Address);
            FullName = myDB_G.Get_Val_from_DB_UD("DriverDetails", "FullName", "", Sel); FullName_ET.setText(FullName);
            Zone = myDB_G.Get_Val_from_DB_UD("DriverDetails", "Zone", "", Sel); Zone_ET.setText(Zone);
            Active= (myDB_G.Get_Val_from_DB_UD("DriverDetails", "Active", "", Sel)).equals("YES"); Active_CBx.setChecked(Active);

        }
    }

    public void on_DriverDetails_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnDriverDetails_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Driver_UID = Driver_UIDET.getText().toString();
        }
        if ((Curr_Driver_UID == null) || (Curr_Driver_UID.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("Driver_UID = '%s'", Curr_Driver_UID);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("DriverDetails", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String Designation = Designation_ET.getText().toString();
        String PhNum = PhNum_ET.getText().toString();
        String Address = Address_ET.getText().toString();
        String FullName = FullName_ET.getText().toString();

        String Zone = Zone_ET.getText().toString();

        String Active = Active_CBx.isChecked()?"YES":"NO";


        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Driver_UID + "," + Zone + ","
                + Designation + "," + PhNum + "," + Address + ","  +FullName + ","    + "," + Active;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_DriverDetails_Ordered(true, Create_TS, Modify_TS, User, Curr_Driver_UID, Designation,PhNum, Address,FullName,Zone, Active)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Driver_UID, Curr_Activity.Act_DriverDetails, UpdateStatement);
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
        CurrentSelect_DriverDetails_Changed();
    }
}
