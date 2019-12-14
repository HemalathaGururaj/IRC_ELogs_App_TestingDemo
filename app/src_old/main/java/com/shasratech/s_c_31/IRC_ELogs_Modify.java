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

public class IRC_ELogs_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Material = null;
    private final String TAG = "Material_Modify";
    EditText MaterialNameET = null;

    EditText Rate_ET = null;
    Switch Disabled_CBx = null;
    EditText OtherDetails_ET = null;
    EditText CFT_Rate_ET = null;


    boolean Add_New = false;
    String New_Material_Editor_Text = "";
    String Curr_Material = "";
    String Ton_Rate = "";
    String CFT_Rate = "";
    String Other_Details = "";
    boolean Disabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.Material_Add_New);
        MaterialNameET = findViewById(R.id.Material_Name_TE) ;
        Rate_ET = findViewById(R.id.Material_Modify_Rate);
        Disabled_CBx = findViewById(R.id.Material_Modify_Disabled);
        OtherDetails_ET = findViewById(R.id.Material_Modify_Other_Details);
        CFT_Rate_ET = findViewById(R.id.Material_Modify_CFT_Rate);

        MaterialNameET.setVisibility(View.INVISIBLE);

        CB_Material = findViewById(R.id.Material_Modify_Item);
        CB_Material.setOnItemSelectedListener(this);


        CB_Material.UpdateCB(true, "Material", "Name", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    MaterialNameET.setVisibility(View.VISIBLE);
                } else {
                    MaterialNameET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Material = parent.getItemAtPosition(position).toString();
                CurrentSelect_Material_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_Material_Changed();

    }

    private void CurrentSelect_Material_Changed () {
        if ((Curr_Material != null) && (!Curr_Material.isEmpty())) {
            String Sel = String.format("Name = '%s'", Curr_Material);
            Ton_Rate = myDB_G.Get_Val_from_DB_UD("Material", "Price", "", Sel); Rate_ET.setText(Ton_Rate);
            CFT_Rate = myDB_G.Get_Val_from_DB_UD("Material", "CFT_Rate", "", Sel); CFT_Rate_ET.setText(CFT_Rate);
            Other_Details = myDB_G.Get_Val_from_DB_UD("Material", "OtherDetails", "", Sel); OtherDetails_ET.setText(Other_Details);
            Disabled = (myDB_G.Get_Val_from_DB_UD("Material", "Disabled", "", Sel)).equals("YES"); Disabled_CBx.setChecked(Disabled);
        }
    }

    public void on_Material_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnMaterial_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Material = MaterialNameET.getText().toString();
        }
        if ((Curr_Material == null) || (Curr_Material.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("Name = '%s'", Curr_Material);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("Material", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String Rate = Rate_ET.getText().toString();
        String Units = myDB_G.Get_Val_from_DB_UD("Material", "Units", "", Sel);

        if ((Units == null) || (Units.isEmpty())) {
            Units = "1000";
        }


        String Disabled = Disabled_CBx.isChecked()?"YES":"NO";
        String OtherDetails = OtherDetails_ET.getText().toString();
        CFT_Rate = CFT_Rate_ET.getText().toString();

        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Material + "," + Rate + "," + Units
                + "," + Disabled + "," + OtherDetails + "," + CFT_Rate;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_Material_Ordered(true, Create_TS, Modify_TS, User, Curr_Material, Rate, Units, Disabled, OtherDetails, CFT_Rate, "")) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Material, Curr_Activity.Act_Material, UpdateStatement);
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
        CurrentSelect_Material_Changed();
    }
}
