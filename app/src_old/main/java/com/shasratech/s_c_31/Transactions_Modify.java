package com.shasratech.s_c_31;

import android.content.Intent;
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
import java.util.logging.ErrorManager;

import static com.shasratech.s_c_31.Globals.Get_Current_Date_Time;
import static com.shasratech.s_c_31.Globals.*;

public class Transactions_Modify extends AppCompatActivity  implements AdapterView.OnItemSelectedListener  {
    private static Spinner TransactionsModifyItemSpinner;
    String CurrentTransactions = "";
    private final String TAG = "Transactions_Modify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions__modify);
        Init_Variables();

        /*
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
		*/
    }

    private void Init_Variables() {
        /*TransactionsModifyItemSpinner = (Spinner) findViewById(R.id.Transactions_Modify_Item);

        List<String> TransactionsModifyItemList = new ArrayList<>();

        myDB.Get_Val_from_DB_UD("Transactions", "Name", "", "", TransactionsModifyItemList);

        ArrayAdapter<String> TransactionsModifyItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, TransactionsModifyItemList);
        TransactionsModifyItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TransactionsModifyItemSpinner.setAdapter(TransactionsModifyItemAdapter);

        TransactionsModifyItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrentTransactions = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
    }
    private void CurrentSelect_Trans_ID_Changed () {
        /*if ((Curr_Material != null) && (!Curr_Material.isEmpty())) {
            String Sel = String.format("Name = '%s'", Curr_Material);
            Ton_Rate = myDB.Get_Val_from_DB_UD("Material", "Price", "", Sel); Rate_ET.setText(Ton_Rate);
            CFT_Rate = myDB.Get_Val_from_DB_UD("Material", "CFT_Rate", "", Sel); CFT_Rate_ET.setText(CFT_Rate);
            Other_Details = myDB.Get_Val_from_DB_UD("Material", "OtherDetails", "", Sel); OtherDetails_ET.setText(Other_Details);
            Disabled = (myDB.Get_Val_from_DB_UD("Material", "Disabled", "", Sel)).equals("YES"); Disabled_CBx.setChecked(Disabled);
        } */
    }

    public void on_Material_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnTransactions_Modify_Save_Clicked (View view) {
/*
     	if (Add_New) {
            Curr_Material = MaterialNameET.getText().toString();
        }
        if ((Curr_Material == null) || (Curr_Material.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }
		

        if ((CurrentTransactions == null) || (CurrentTransactions.isEmpty())) return;

        String Sel = "Name = '" + CurrentTransactions + "'";

        String Create_TS = myDB.Get_Val_from_DB_UD("Transactions", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);
        String Modify_TS = Get_Current_Date_Time();
        String User = "Mobile";
        String Name = CurrentTransactions;
        EditText Rate_ET = (EditText) findViewById(R.id.Transactions_Modify_Rate);

        String Rate = Rate_ET.getText().toString();
        String Units = myDB.Get_Val_from_DB_UD("Transactions", "Units", "", Sel);
        CheckBox Disabled_CBx = (CheckBox) findViewById(R.id.Transactions_Modify_Disabled);

        String Disabled = Disabled_CBx.isChecked()?"YES":"NO";
        String OtherDetails = "-";

        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Name + "," + Rate + "," + Units
                + "," + Disabled + "," + OtherDetails;

        Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        //FB_N_Activity.Transactions_InUp2_FB(CurrentTransactions, UpdateStatement);
        */
		/*
		if (myDB.Insert_update_into_Material_Ordered(true, Create_TS, Modify_TS, User, Curr_Material, Rate, Units, Disabled, OtherDetails, CFT_Rate)) {
            // Upload to Could
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");
            FB_N_Activity_2.Material_InUp2_FB(Curr_Material, UpdateStatement);
        } else {
            ErrMsgDialog("Unable to Insert/Update Material into Local DB");
        } */

        ErrMsgDialog("This feature is not implemented on Mobile App as of now\n\nContact SHASRaTech for further details");
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
        //CurrentSelect_Material_Changed();
    }
}
