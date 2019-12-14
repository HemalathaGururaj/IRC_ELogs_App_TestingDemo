package com.shasratech.s_c_31;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.DBHelperUserData.*;
import static com.shasratech.s_c_31.Globals.*;
import com.shasratech.s_c_31.MySpinner;

public class Person_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Type = null;
    private static MySpinner CB_Person = null;
    private static MySpinner CB_SellerComp = null;
    private final String TAG = "Person_Modify";
    EditText PersonNameET = null;
    EditText AddressET = null;
    EditText PhoneNumET = null;
    EditText PrintDispNameET = null;
    EditText GSTNumET = null;

    Switch newAddition = null;
    Switch Disabled_CBx = null;


    boolean Add_New = false;
    String New_Person_Editor_Text = "";
    String Curr_Person = "";
    String Curr_Type = "";
    String Curr_SellerComp = "";
    String Address = "";
    String Print_Display_Name = "";
    String PhoneNumber = "";
    String GSTNum = "";

    boolean Disabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person__modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.Person_Modify_Add_New);

        PersonNameET = findViewById(R.id.Person_Name_TE);
        AddressET = findViewById(R.id.Person_Modify_Adderss);
        PhoneNumET = findViewById(R.id.Person_Modify_Phone) ;
        PrintDispNameET = findViewById(R.id.Person_Modify_Print_Disp_Name) ;
        GSTNumET = findViewById(R.id.Person_Modify_GST) ;


        Disabled_CBx = findViewById(R.id.Person_Modify_Disabled);

        PersonNameET.setVisibility(View.INVISIBLE);

        CB_Type = findViewById(R.id.Person_Modify_Type);
        CB_Type.setOnItemSelectedListener(this);
        String[] temp_type = {"CUSTOMER", "DRIVER", "EMPLOYEE", "OWNER", "SERVICE_CONTR", "STORAGE", "SUPPLIER", "TRANSPORTOR"};
        CB_Type.SetItems(temp_type);

        CB_Person = findViewById(R.id.Person_Modify_Name);
        CB_Person.setOnItemSelectedListener(this);
        CB_Person.UpdateCB(true, "Person", "Name", "", "DISTINCT");

        CB_SellerComp = findViewById(R.id.Person_Modify_SellerComp);
        CB_SellerComp.UpdateCB(true, "SellerComp", "CompanyName", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    PersonNameET.setVisibility(View.VISIBLE);
                } else {
                    PersonNameET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Person = parent.getItemAtPosition(position).toString();
                CurrentSelect_Person_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_Person_Changed();

    }

    private void CurrentSelect_Person_Changed () {
        if ((Curr_Person != null) && (!Curr_Person.isEmpty())) {
            String Sel = String.format("Name = '%s'", Curr_Person);
            Address = myDB_G.Get_Val_from_DB_UD("Person", "Address", "", Sel); AddressET.setText(Address);
            PhoneNumber = myDB_G.Get_Val_from_DB_UD("Person", "PhoneNum", "", Sel); PhoneNumET.setText(PhoneNumber);
            Print_Display_Name = myDB_G.Get_Val_from_DB_UD("Person", "Print_Display_Name", "", Sel); PrintDispNameET.setText(Print_Display_Name);
            GSTNum = myDB_G.Get_Val_from_DB_UD("Person", "GSTNum", "", Sel); GSTNumET.setText(GSTNum);
            Disabled = (myDB_G.Get_Val_from_DB_UD("Person", "Disabled", "", Sel)).equals("YES"); Disabled_CBx.setChecked(Disabled);
            Curr_SellerComp = myDB_G.Get_Val_from_DB_UD("Person", "SellerComp", "", Sel);
            if ((Curr_SellerComp != null) && (!Curr_SellerComp.isEmpty())) {
                CB_SellerComp.SetSelectedItem(Curr_SellerComp);
            }
        }
    }

    public void on_Person_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnPerson_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Person = PersonNameET.getText().toString();
        }
        if ((Curr_Person == null) || (Curr_Person.isEmpty())) {
            ErrMsgDialog("Person is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = "Person_Type = '" + Curr_Type +  "' and Name = '" + Curr_Person + "'";

        String Create_TS = myDB_G.Get_Val_from_DB_UD("Person", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }


        Address = AddressET.getText().toString();
        PhoneNumber = PhoneNumET.getText().toString();
        Print_Display_Name = PrintDispNameET.getText().toString();
        String GST = GSTNumET.getText().toString();
        Curr_SellerComp = (CB_SellerComp.getSelectedItem() != null)?CB_SellerComp.getSelectedItem().toString():"";
        String Disabled = Disabled_CBx.isChecked()?"YES":"NO";
        Curr_Type = CB_Type.getSelectedItemI();

        String UpdateStatement = Create_TS + "," + Modify_TS + "," + FBAppMBName + "," + Curr_Type + "," + Curr_Person + "," + Address + "," + PhoneNumber
                + "," + Print_Display_Name + "," + GST + "," + Curr_SellerComp +  ","  + Disabled;

        Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        // Need to add to Local DB
        //String[] Con_List = {Create_TS, Modify_TS, User, Curr_Person, Rate, Units, Disabled, OtherDetails, CFT_Rate};
        if (myDB_G.Insert_update_into_Person_Ordered(true, Create_TS, Modify_TS, FBAppMBName, Curr_Type, Curr_Person, Address, PhoneNumber, Print_Display_Name,
                GST, Curr_SellerComp, Disabled)) {
            // Upload to Could
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");
            //FB_N_Activity_2.Person_InUp2_FB(Curr_Type + "-" +  Curr_Person, UpdateStatement);
            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Type + "-" +  Curr_Person, Curr_Activity.Act_Person, UpdateStatement);
            UPload_Data_Msg_List.add(msg);

            //Upload_Data_InUp2_FB(Curr_Type + "-" +  Curr_Person, Curr_Activity.Act_Person, UpdateStatement );
        } else {
            ErrMsgDialog("Unable to Insert/Update Person into Local DB");
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
        CurrentSelect_Person_Changed();
    }
}
