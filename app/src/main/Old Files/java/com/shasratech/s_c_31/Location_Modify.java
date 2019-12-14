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

public class Location_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Location = null;
    private final String TAG = "Location_Modify";
    EditText LocationET = null;

    EditText Zone_ET = null;
    Switch Active_CBx = null;



    boolean Add_New = false;
    String New_Location_Editor_Text = "";
    String Curr_Location = "";
    String Zone = "";
    boolean Active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.Location_Add_New);
        LocationET = findViewById(R.id.Location_TE) ;
        Zone_ET = findViewById(R.id.Location_Modify_Zone);
        Active_CBx = findViewById(R.id.Location_Modify_Active);


        LocationET.setVisibility(View.INVISIBLE);

        CB_Location = findViewById(R.id.Location_Modify_Location);
        CB_Location.setOnItemSelectedListener(this);


        CB_Location.UpdateCB(true, "Location", "Location", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    LocationET.setVisibility(View.VISIBLE);
                } else {
                    LocationET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Location = parent.getItemAtPosition(position).toString();
                CurrentSelect_Location_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_Location_Changed();

    }

    private void CurrentSelect_Location_Changed () {
        if ((Curr_Location != null) && (!Curr_Location.isEmpty())) {
            String Sel = String.format("Location = '%s'", Curr_Location);
            Zone = myDB_G.Get_Val_from_DB_UD("Location", "Zone", "", Sel); Zone_ET.setText(Zone);
            Active= (myDB_G.Get_Val_from_DB_UD("Location", "Active", "", Sel)).equals("YES"); Active_CBx.setChecked(Active);

        }
    }

    public void on_Location_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnLocation_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Location = LocationET.getText().toString();
        }
        if ((Curr_Location == null) || (Curr_Location.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("Location = '%s'", Curr_Location);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("Location", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String Zone = Zone_ET.getText().toString();

        String Active = Active_CBx.isChecked()?"YES":"NO";


        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Location + "," + Zone + ","
                 + "," + Active;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_Location_Ordered(true, Create_TS, Modify_TS, User, Curr_Location, Zone, Active)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Location, Curr_Activity.Act_Location, UpdateStatement);
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
        CurrentSelect_Location_Changed();
    }
}
