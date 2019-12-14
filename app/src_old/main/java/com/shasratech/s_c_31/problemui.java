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

public class problemui extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static MySpinner CB_TripNo = null;
    private final String TAG = "problemui";
    EditText TripNoET = null;

    EditText DriverName_ET = null;
    EditText ProblemCategory_ET = null;
    EditText ProblemID_ET = null;
    EditText ProblemDescription_ET = null;

    boolean Add_New = false;
    String New_TripNo_Editor_Text = "";
    String Curr_TripNo = "";
    String DriverName = "";
    String ProblemCategory = "";
    String ProblemID = "";
    String ProblemDescription = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problemui_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.problemui_Add_New);
        TripNoET = findViewById(R.id.TripNo_TE) ;
        DriverName_ET = findViewById(R.id.problemui_drivername);
        ProblemCategory_ET = findViewById(R.id.problemui_ProblemCategory);
        ProblemID_ET = findViewById(R.id.problemui_ProblemID);
        ProblemDescription_ET = findViewById(R.id.problemui_ProblemDescription);


        TripNoET.setVisibility(View.INVISIBLE);

        CB_TripNo = findViewById(R.id.problemui_tripno);
        CB_TripNo.setOnItemSelectedListener(this);


        CB_TripNo.UpdateCB(true, "Problem", "TripNo", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    TripNoET.setVisibility(View.VISIBLE);
                } else {
                    TripNoET.setVisibility(View.INVISIBLE);
                }
            }

        });

        CB_TripNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_TripNo = parent.getItemAtPosition(position).toString();
                CurrentSelect_TripNo_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_TripNo_Changed();
    }
    private void CurrentSelect_TripNo_Changed () {
        if ((Curr_TripNo != null) && (!Curr_TripNo.isEmpty())) {
            String Sel = String.format("TripNo = '%s'", Curr_TripNo);
            DriverName = myDB_G.Get_Val_from_DB_UD("Problem", "TripNo", "", Sel); DriverName_ET.setText(DriverName);
            ProblemCategory = myDB_G.Get_Val_from_DB_UD("Problem", "TripNo", "", Sel); ProblemCategory_ET.setText(ProblemCategory);
            ProblemID = myDB_G.Get_Val_from_DB_UD("Problem", "TripNo", "", Sel); ProblemID_ET.setText(ProblemID);
            ProblemDescription = myDB_G.Get_Val_from_DB_UD("Problem", "TripNo", "", Sel); ProblemDescription_ET.setText(ProblemDescription);

        }
    }

    public void problemui_Save_Clicked (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnTripNo_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_TripNo = TripNoET.getText().toString();
        }
        if ((Curr_TripNo == null) || (Curr_TripNo.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("HomeShed = '%s'", Curr_TripNo);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("Problem", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String DriverName = DriverName_ET.getText().toString();

        String ProblemCategory = ProblemCategory_ET.getText().toString();

        String ProblemID = ProblemID_ET.getText().toString();

        String ProblemDescription = ProblemDescription_ET.getText().toString();

        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_TripNo + "," + DriverName + ","
                + "," + ProblemCategory + "," + ProblemID + "," + ProblemDescription;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_Problem(true, Create_TS, Modify_TS, User, Curr_TripNo, DriverName, ProblemCategory,ProblemID,ProblemDescription)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_TripNo, Curr_Activity.Act_TripNo, UpdateStatement);
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
        CurrentSelect_TripNo_Changed();
    }
}
