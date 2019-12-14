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

public class Trasnport_Modify extends AppCompatActivity {
/*
    private static Spinner TransportModifyItemSpinner;
    String CurrentTransport = "";
    private final String TAG = "Transport_Modify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_Transport__modify);
        Init_Variables();

    }

    private void Init_Variables() {
        TransportModifyItemSpinner = (Spinner) findViewById(R.id.Transport_Modify_Item);

        List<String> TransportModifyItemList = new ArrayList<>();

        myDB.Get_Val_from_DB_UserData("Transport", "Name", "", "", TransportModifyItemList);

        ArrayAdapter<String> TransportModifyItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, TransportModifyItemList);
        TransportModifyItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TransportModifyItemSpinner.setAdapter(TransportModifyItemAdapter);

        TransportModifyItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrentTransport = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void OnTransport_Modify_Save_Clicked (View view) {

        if ((CurrentTransport == null) || (CurrentTransport.isEmpty())) return;

        String Sel = "Name = '" + CurrentTransport + "'";

        String Create_TS = myDB.Get_Val_from_DB_UserData("Transport", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);
        String Modify_TS = Get_Current_Date_Time();
        String User = "Mobile";
        String Name = CurrentTransport;
        EditText Rate_ET = (EditText) findViewById(R.id.Transport_Modify_Rate);

        String Rate = Rate_ET.getText().toString();
        String Units = myDB.Get_Val_from_DB_UserData("Transport", "Units", "", Sel);
        CheckBox Disabled_CBx = (CheckBox) findViewById(R.id.Transport_Modify_Disabled);

        String Disabled = Disabled_CBx.isChecked()?"YES":"NO";
        String OtherDetails = "-";

        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Name + "," + Rate + "," + Units
                + "," + Disabled + "," + OtherDetails;

        Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        FB_N_Activity.Transport_InUp2_FB(CurrentTransport, UpdateStatement);
    }
    */
}
