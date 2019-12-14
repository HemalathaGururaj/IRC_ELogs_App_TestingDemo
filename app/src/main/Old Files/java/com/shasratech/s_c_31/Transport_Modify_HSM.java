package com.shasratech.s_c_31;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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



public class Transport_Modify_HSM extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "Transport_Modify_HSM";
    MySpinner CB_Customer =  null;
    MySpinner CB_Site =  null;
    MySpinner CB_Name =  null;
    EditText TransportNameET = null;

    EditText Rate_ET = null;
    EditText Addition_ET = null;

    Switch Sw_Disabled = null;

    boolean Add_New = false;
    String New_Transport_Editor_Text = "";

    String Curr_Customer = "";
    String Curr_Site = "";
    String Curr_Name = "";

    String Rate = "";
    String Addition = "";

    boolean Disabled = false;

    Switch Sw_newAddition = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_modify);

        CB_Customer =  findViewById(R.id.transport_Modify_Customer);
        CB_Site =  findViewById(R.id.transport_Modify_Site);
        CB_Name =  findViewById(R.id.transport_Modify_Name);

        Rate_ET = (EditText)findViewById(R.id.transport_Modify_Rate) ;
        Addition_ET = (EditText)findViewById(R.id.transport_Modify_Addition) ;

        TransportNameET = (EditText)findViewById(R.id.Transport_NewName) ;

        Sw_newAddition =  findViewById(R.id.transport_Modify_newAddition);
        Sw_Disabled =  findViewById(R.id.transport_Modify_Disabled);

        Button PB_Save = (Button)findViewById(R.id.transport_Modify_PB_Save) ;

        CB_Customer.setOnItemSelectedListener(this);
        CB_Site.setOnItemSelectedListener(this);
        CB_Name.setOnItemSelectedListener(this);

        TransportNameET.setVisibility(View.INVISIBLE);

        Sw_newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    TransportNameET.setVisibility(View.VISIBLE);
                } else {
                    TransportNameET.setVisibility(View.INVISIBLE);
                }
            }

        });
        CB_Name.UpdateCB(true, "Transport", "Name", "", "DISTINCT");
        CB_Customer.UpdateCB(true, "Person", "Name", "Disabled = 'NO' ", "DISTINCT");
        CB_Site.UpdateCB(true, "Site", "Name", "Disabled = 'NO' and Owner = '" + CB_Customer.getSelectedItemI() + "'", "DISTINCT");

        CB_Name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i (TAG, "In siteType onItemSelected listyner");
                Curr_Customer = parent.getItemAtPosition(position).toString();
                //Current_Name_Changed();
                Update_Values_2_Curr_Combination();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Curr_Type = parent.getItemAtPosition(position).toString();
                //CurrentSelect_Type_Changed (Curr_Type);
            }

        });

        CB_Customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i (TAG, "In siteType onItemSelected listyner");
                Curr_Customer = parent.getItemAtPosition(position).toString();
                Current_Customer_Changed();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Curr_Type = parent.getItemAtPosition(position).toString();
                //CurrentSelect_Type_Changed (Curr_Type);
            }

        });


        CB_Site.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i (TAG, "In siteType onItemSelected listyner");
                Curr_Customer = parent.getItemAtPosition(position).toString();
                Update_Values_2_Curr_Combination();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Curr_Type = parent.getItemAtPosition(position).toString();
                //CurrentSelect_Type_Changed (Curr_Type);
            }

        });

        PB_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Add_New) {
                    Curr_Name = TransportNameET.getText().toString();
                } else {
                    Curr_Name = CB_Name.getSelectedItemI();
                }
                if ((Curr_Name == null) || (Curr_Name.isEmpty())) {
                    ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
                    return;
                }

                String Sel = String.format(" Customer = '%s' and Name = '%s' and Site = '%s' ", Curr_Customer, Curr_Name, Curr_Site);

                String Create_TS = myDB_G.Get_Val_from_DB_UD("Transport", "Create_TS", "", Sel);


                String Modify_TS = Get_Current_Date_Time();

                if ((Create_TS == null) || (Create_TS.isEmpty())) {
                    Create_TS = Modify_TS;
                }
                String User = FBAppMBName;

                Curr_Customer = CB_Customer.getSelectedItemI();
                Curr_Name = CB_Name.getSelectedItemI();
                Curr_Site = CB_Site.getSelectedItemI();


                Rate = Rate_ET.getText().toString();
                Addition = Addition_ET.getText().toString();

                String Disabled = Sw_Disabled.isChecked()?"YES":"NO";

                String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Name + "," + Curr_Customer + "," +
                        Curr_Site + "," + Rate + "," + Addition + "," + Disabled ;

                //Log.i (TAG, "UpdateStatement = " + UpdateStatement);


                if (myDB_G.Insert_update_into_Transport_Ordered(true, Create_TS, Modify_TS, User,
                        Curr_Name, Curr_Customer, Curr_Site, Rate, Addition,
                         Disabled)) {
                    ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");
                    //FB_N_Activity_2.Material_InUp2_FB(Curr_Type + "-" + Curr_Material, UpdateStatement);
                    UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Name + "-"  + Curr_Customer + "-" + Curr_Site, Curr_Activity.Act_Transport, UpdateStatement);
                    UPload_Data_Msg_List.add(msg);
                } else {
                    ErrMsgDialog("Unable to Insert/Update SIte into Local DB");
                }
            }
        });

        Update_Values_2_Curr_Combination();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    void Current_Customer_Changed () {
        CB_Site.UpdateCB(true, "Site", "Name", "Disabled = 'NO' and Owner = '" + CB_Customer.getSelectedItemI() + "'", "DISTINCT", Curr_Site);
        Update_Values_2_Curr_Combination();
    }

    void Update_Values_2_Curr_Combination () {
        Curr_Name = CB_Name.getSelectedItemI();
        Curr_Customer = CB_Customer.getSelectedItemI();
        Curr_Site = CB_Site.getSelectedItemI();

        String Sel = String.format(" Customer = '%s' and Name = '%s' and Site = '%s' ", Curr_Customer, Curr_Name, Curr_Site);

        Log.i (TAG, "Sel = " + Sel);
        if (
                (Curr_Customer != null) && (!Curr_Customer.isEmpty()) &&
                        (Curr_Site != null) && (!Curr_Site.isEmpty()) &&
                        (Curr_Name != null) && (!Curr_Name.isEmpty())
                ) {

            Rate = myDB_G.Get_Val_from_DB_UD("Transport", "Rate", "", Sel); Rate_ET.setText(Rate);
            Addition = myDB_G.Get_Val_from_DB_UD("Transport", "Addition", "", Sel); Addition_ET.setText(Addition);
            Disabled = (myDB_G.Get_Val_from_DB_UD("Transport", "Disabled", "", Sel)).equals("YES"); Sw_Disabled.setChecked(Disabled);
        }
    }
}
