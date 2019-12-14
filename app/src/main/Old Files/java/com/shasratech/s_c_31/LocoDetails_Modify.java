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

public class LocoDetails_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Loco_num = null;
    private final String TAG = "LocoDetails_Modify";
    EditText Loco_numET = null;

    EditText Loco_type_ET = null;
    Switch Active_CBx = null;



    boolean Add_New = false;
    String New_Loco_num_Editor_Text = "";
    String Curr_Loco_num = "";
    String Loco_type = "";
    boolean Active = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locodetails_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.Loco_num_Add_New);
        Loco_numET = findViewById(R.id.Loco_num_TE) ;
        Loco_type_ET = findViewById(R.id.LocoDetails_Modify_Loco_type);
        Active_CBx = findViewById(R.id.HomeShed_Modify_Active);


        Loco_numET.setVisibility(View.INVISIBLE);

        CB_Loco_num = findViewById(R.id.LocoDetails_Modify_Loco_num);
        CB_Loco_num.setOnItemSelectedListener(this);


        CB_Loco_num.UpdateCB(true, "LocoDetails", "Loco_num", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    Loco_numET.setVisibility(View.VISIBLE);
                } else {
                    Loco_numET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Loco_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Loco_num = parent.getItemAtPosition(position).toString();
                CurrentSelect_LocoDetails_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_LocoDetails_Changed();

    }

    private void CurrentSelect_LocoDetails_Changed () {
        if ((Curr_Loco_num != null) && (!Curr_Loco_num.isEmpty())) {
            String Sel = String.format("Loco_num = '%s'", Curr_Loco_num);
            Loco_type = myDB_G.Get_Val_from_DB_UD("LocoDetails", "Loco_type", "", Sel); Loco_type_ET.setText(Loco_type);
            Active= (myDB_G.Get_Val_from_DB_UD("LocoDetails", "Active", "", Sel)).equals("YES"); Active_CBx.setChecked(Active);

        }
    }

    public void on_LocoDetails_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnLocoDetails_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Loco_num = Loco_numET.getText().toString();
        }
        if ((Curr_Loco_num == null) || (Curr_Loco_num.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("Loco_num = '%s'", Curr_Loco_num);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("LocoDetails", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String Loco_type = Loco_type_ET.getText().toString();

        String Active = Active_CBx.isChecked()?"YES":"NO";


        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Loco_num + "," + Loco_type + ","
                + "," + Active;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_LocoDetails_Ordered(true, Create_TS, Modify_TS, User, Curr_Loco_num, Loco_type, Active)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Loco_num, Curr_Activity.Act_LocoDetails, UpdateStatement);
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
        CurrentSelect_LocoDetails_Changed();
    }
}
