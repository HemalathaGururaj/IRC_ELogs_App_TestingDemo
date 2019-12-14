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

public class HomeShed_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_HomeShed = null;
    private final String TAG = "HomeShed_Modify";
    EditText HomeShedET = null;

    EditText Zone_ET = null;
    EditText Active_ET = null;



    boolean Add_New = false;
    String New_HomeShed_Editor_Text = "";
    String Curr_HomeShed = "";
    String Zone = "";
    String Active = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeshed_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.HomeShed_Add_New);
        HomeShedET = findViewById(R.id.HomeShed_TE) ;
        Zone_ET = findViewById(R.id.HomeShed_Modify_Zone);
        Active_ET = findViewById(R.id.HomeShed_Modify_Active);


        HomeShedET.setVisibility(View.INVISIBLE);

        CB_HomeShed = findViewById(R.id.HomeShed_Modify_HomeShed);
        CB_HomeShed.setOnItemSelectedListener(this);


        CB_HomeShed.UpdateCB(true, "HomeShed", "HomeShed", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    HomeShedET.setVisibility(View.VISIBLE);
                } else {
                    HomeShedET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_HomeShed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_HomeShed = parent.getItemAtPosition(position).toString();
                CurrentSelect_HomeShed_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_HomeShed_Changed();

    }

    private void CurrentSelect_HomeShed_Changed () {
        if ((Curr_HomeShed != null) && (!Curr_HomeShed.isEmpty())) {
            String Sel = String.format("HomeShed = '%s'", Curr_HomeShed);
          Zone = myDB_G.Get_Val_from_DB_UD("HomeShed", "HomeShed", "", Sel); Zone_ET.setText(Zone);
            Active = myDB_G.Get_Val_from_DB_UD("HomeShed", "Active", "", Sel); Active_ET.setText(Active);

        }
    }

    public void on_HomeShed_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnHomeShed_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_HomeShed = HomeShedET.getText().toString();
        }
        if ((Curr_HomeShed == null) || (Curr_HomeShed.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("HomeShed = '%s'", Curr_HomeShed);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("HomeShed", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String Zone = Zone_ET.getText().toString();

        String Active = Active_ET.getText().toString();


        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_HomeShed + "," + Zone + ","
                + "," + Active;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_HomeSheds_Ordered(true, Create_TS, Modify_TS, User, Curr_HomeShed, Zone, Active)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_HomeShed, Curr_Activity.Act_HomeShed, UpdateStatement);
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
        CurrentSelect_HomeShed_Changed();
    }
}
