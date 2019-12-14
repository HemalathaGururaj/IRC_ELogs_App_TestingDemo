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



public class Site_Modify_HSM extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "Site_Modify_HSM";
    MySpinner CB_Type =  null;
    MySpinner CB_Owner =  null;
    MySpinner CB_Name =  null;
    MySpinner CB_Material =  null;

    EditText siteNameET = null;
    EditText Distance_ET = null;

    EditText Rate_Local_ET = null;
    EditText Rate_Remote_ET = null;

    EditText Latitude_ET = null;
    EditText Longitude_ET = null;

    EditText CFT_Rate_ET = null;
    Switch Sw_GST_Included = null;
    Switch Sw_Disabled = null;

    EditText Extra_ET = null;

    boolean Add_New = false;
    String New_Site_Editor_Text = "";

    String Curr_Type = "";
    String Curr_Owner = "";
    String Curr_Name = "";
    String Curr_Material = "";

    String Distance = "";
    String Rate_Local = "";
    String Rate_Remote = "";

    String Latitude = "";
    String Longitude = "";

    String CFT_Rate = "";
    String GST_Included = "";

    boolean Disabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_modify);

        CB_Type =  findViewById(R.id.siteType);
        CB_Owner =  findViewById(R.id.CB_siteOwner);
        CB_Name =  findViewById(R.id.siteName);
        CB_Material =  findViewById(R.id.Site_Modify_Material);

        siteNameET = (EditText)findViewById(R.id.siteNameET) ;
        Distance_ET = findViewById(R.id.Site_Modify_Distance) ;
        Rate_Local_ET = (EditText)findViewById(R.id.Site_Modify_Rate_Local) ;
        Rate_Remote_ET = (EditText)findViewById(R.id.Site_Modify_Rate_Remote) ;
        CFT_Rate_ET = (EditText)findViewById(R.id.Site_Modify_CFT_Rate) ;
        Extra_ET = (EditText)findViewById(R.id.Site_Modify_Extra_KG) ;

        final Switch newAddition = findViewById(R.id.newAddition_Site);
        Sw_Disabled =  findViewById(R.id.Site_Modify_disabled);
        Sw_GST_Included = findViewById(R.id.GST_Included);

        Button PB_Save = (Button)findViewById(R.id.Site_Modify_Save) ;

        CB_Type.setOnItemSelectedListener(this);
        CB_Owner.setOnItemSelectedListener(this);
        CB_Name.setOnItemSelectedListener(this);
        CB_Material.setOnItemSelectedListener(this);

        siteNameET.setVisibility(View.INVISIBLE);

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    siteNameET.setVisibility(View.VISIBLE);
                } else {
                    siteNameET.setVisibility(View.INVISIBLE);
                }
            }

        });


        String[] temp_type = {"CUSTOMER", "STORAGE", "SUPPLIER"};

        CB_Type.SetItems(temp_type);
        //siteOwner.UpdateCB(true, "Site", "Owner", String.format("Type = '%s'", siteType.getSelectedItemI()), "DISTINCT");
        CB_Owner.UpdateCB(true, "Person", "Name", "Disabled = 'NO' and Person_Type = '" + CB_Type.getSelectedItemI() + "'", "DISTINCT");
        CB_Material.UpdateCB(true, "Material", "Name", "Disabled = 'NO'", "DISTINCT");


        CB_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i (TAG, "In siteType onItemSelected listyner");
                Curr_Type = parent.getItemAtPosition(position).toString();
                CurrentSelect_Type_Changed (Curr_Type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Curr_Type = parent.getItemAtPosition(position).toString();
                //CurrentSelect_Type_Changed (Curr_Type);
            }

        });

        CB_Owner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Owner = parent.getItemAtPosition(position).toString();
                CurrentSelect_Owner_Changed (Curr_Owner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Curr_Type = parent.getItemAtPosition(position).toString();
                //CurrentSelect_Type_Changed (Curr_Type);
            }

        });



        Update_Values_2_Curr_Combination();

        PB_Save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Add_New) {
                    Curr_Name = siteNameET.getText().toString();
                } else {
                    Curr_Name = CB_Name.getSelectedItemI();
                }
                if ((Curr_Name == null) || (Curr_Name.isEmpty())) {
                    ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
                    return;
                }

                String Sel = String.format("Type = '%s' and Owner = '%s' and Name = '%s' and Material = '%s'", Curr_Type, Curr_Owner, Curr_Name, Curr_Material);

                String Create_TS = myDB_G.Get_Val_from_DB_UD("Site", "Create_TS", "", Sel);

                Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

                String Modify_TS = Get_Current_Date_Time();

                if ((Create_TS == null) || (Create_TS.isEmpty())) {
                    Create_TS = Modify_TS;
                }
                String User = FBAppMBName;

                Curr_Type = CB_Type.getSelectedItemI();
                Curr_Owner = CB_Owner.getSelectedItemI();
                Curr_Material = CB_Material.getSelectedItemI();

                Distance = Distance_ET.getText().toString();
                String Extra = Extra_ET.getText().toString();

                Rate_Local = Rate_Local_ET.getText().toString();
                Rate_Remote = Rate_Remote_ET.getText().toString();

                Latitude = "0"; /* Latitude_ET.getText().toString(); */
                Longitude = "0"; /* Longitude_ET.getText().toString(); */


                String Disabled = Sw_Disabled.isChecked()?"YES":"NO";
                CFT_Rate = CFT_Rate_ET.getText().toString();

                String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," +
                        Curr_Type + "," + Curr_Owner + "," + Curr_Name + "," + Curr_Material + "," +
                        Distance + "," + Latitude + "," + Longitude + "," + Rate_Local + "," + Rate_Remote +
                        CFT_Rate + "," + GST_Included + "," + Disabled + "," + Extra;

                //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

                if (myDB_G.Insert_update_into_Site_Ordered(true, Create_TS, Modify_TS, User,
                        Curr_Type, Curr_Owner, Curr_Name, Curr_Material, Distance, Latitude, Longitude, Rate_Local, Rate_Remote,
                        CFT_Rate, GST_Included, Disabled, Extra, "")) {
                    ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");
                    //FB_N_Activity_2.Material_InUp2_FB(Curr_Type + "-" + Curr_Material, UpdateStatement);
                    UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Type + "-"  + Curr_Owner + "-" + Curr_Name + "-" +  Curr_Material, Curr_Activity.Act_Site, UpdateStatement);
                    UPload_Data_Msg_List.add(msg);
                    //Upload_Data_InUp2_FB(Curr_Type + "-"  + Curr_Owner + "-" + Curr_Name + "-" +  Curr_Material, Curr_Activity.Act_Site, UpdateStatement );
                } else {
                    ErrMsgDialog("Unable to Insert/Update SIte into Local DB");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a MySpinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    void CurrentSelect_Type_Changed(String curr_type)
    {
        Log.i (TAG, "In CurrentSelect_Type_Changed calliung SiteOwner.updateCB");
        CB_Owner.UpdateCB(true, "Person", "Name", "Disabled = 'NO' and Person_Type = '" + CB_Type.getSelectedItemI() + "'", "DISTINCT");
        Update_Values_2_Curr_Combination();
		
    }

    void CurrentSelect_Owner_Changed(String curr_Owner)
    {
        CB_Name.UpdateCB(true, "Site", "Name", String.format("Type = '%s' and Owner = '%s'", CB_Type.getSelectedItemI(), CB_Owner.getSelectedItemI()), "DISTINCT");
        Update_Values_2_Curr_Combination();
    }

    void Update_Values_2_Curr_Combination () {
        CB_Name.UpdateCB(true, "Site", "Name", String.format("Type = '%s' and Owner = '%s'", CB_Type.getSelectedItemI(), CB_Owner.getSelectedItemI()), "DISTINCT");
        Curr_Type = CB_Type.getSelectedItemI();
        Curr_Owner = CB_Owner.getSelectedItemI();
        Curr_Name = CB_Name.getSelectedItemI();
        Curr_Material = CB_Material.getSelectedItemI();

        String Sel = String.format("Type = '%s' and Owner = '%s' and Name = '%s' and Material = '%s'", Curr_Type, Curr_Owner, Curr_Name, Curr_Material);

        Log.i (TAG, "Sel = " + Sel);
        if (
                (Curr_Type != null) && (!Curr_Type.isEmpty()) &&
                        (Curr_Owner != null) && (!Curr_Owner.isEmpty()) &&
                        (Curr_Name != null) && (!Curr_Name.isEmpty()) &&
                        (Curr_Material != null) && (!Curr_Material.isEmpty())
                ) {

            Distance = myDB_G.Get_Val_from_DB_UD("Site", "Distance", "", Sel); Distance_ET.setText(Distance);

            Log.i (TAG, "Distance -= " + Distance);
            Rate_Local = myDB_G.Get_Val_from_DB_UD("Site", "Rate_Local", "", Sel); Rate_Local_ET.setText(Rate_Local);
            Rate_Remote = myDB_G.Get_Val_from_DB_UD("Site", "Rate_Remote", "", Sel); Rate_Remote_ET.setText(Rate_Remote);
            CFT_Rate = myDB_G.Get_Val_from_DB_UD("Site", "CFT_Rate", "", Sel); CFT_Rate_ET.setText(CFT_Rate);

            String Extra = myDB_G.Get_Val_from_DB_UD("Site", "Extra", "", Sel); Extra_ET.setText(Extra);


            Disabled = (myDB_G.Get_Val_from_DB_UD("Site", "Disabled", "", Sel)).equals("YES"); Sw_Disabled.setChecked(Disabled);
            boolean GST_Included_B = (myDB_G.Get_Val_from_DB_UD("Site", "GST_Included", "", Sel)).equals("YES"); Sw_GST_Included.setChecked(GST_Included_B);
        }
    }

}
