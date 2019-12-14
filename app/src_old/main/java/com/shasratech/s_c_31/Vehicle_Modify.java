package com.shasratech.s_c_31;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;

public class Vehicle_Modify extends AppCompatActivity {

    private static Spinner VehicleModifyItemSpinner;
    String CurrentVehicle = "";
    private final String TAG = "Vehicle_Modify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle__modify);
        Init_Variables();

    }

    private void Init_Variables() {
        VehicleModifyItemSpinner = (Spinner) findViewById(R.id.Vehicle_Modify_Item);

        List<String> VehicleModifyItemList = new ArrayList<>();

        myDB_G.Get_Val_from_DB_UD("Vehicle", "ID_Number", "", "", VehicleModifyItemList);

        ArrayAdapter<String> VehicleModifyItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, VehicleModifyItemList);
        VehicleModifyItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VehicleModifyItemSpinner.setAdapter(VehicleModifyItemAdapter);

        VehicleModifyItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrentVehicle = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void OnVehicle_Modify_Save_Clicked (View view) {

        if ((CurrentVehicle == null) || (CurrentVehicle.isEmpty())) return;

        String Sel = "ID_Number = '" + CurrentVehicle + "'";

        String Create_TS = myDB_G.Get_Val_from_DB_UD("Vehicle", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);
        String Modify_TS = Get_Current_Date_Time();
        String User = "Mobile";
        String Name = CurrentVehicle;
        EditText Rate_ET = (EditText) findViewById(R.id.Vehicle_Modify_CFT);

        String Rate = Rate_ET.getText().toString();
       // String Units = myDB_G.Get_Val_from_DB_UD("Vehicle", "Units", "", Sel);
        CheckBox Disabled_CBx = (CheckBox) findViewById(R.id.Vehicle_Modify_Disabled);

        String Disabled = Disabled_CBx.isChecked()?"YES":"NO";
        String OtherDetails = "-";

        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Name + "," + Rate + "," +  "," + Disabled + "," + OtherDetails;

        Log.i (TAG, "UpdateStatement = " + UpdateStatement);
        ErrMsgDialog("This Feature is not Implemented as of now.\n\nIt will be implemented in coming Releases");
    }

}
