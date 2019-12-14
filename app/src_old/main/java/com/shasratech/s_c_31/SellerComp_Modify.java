package com.shasratech.s_c_31;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import static com.shasratech.s_c_31.Globals.*;

public class SellerComp_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static MySpinner CB_Sales_Company = null;
    String Curr_SalesCompanyl = "";
    EditText address,gst,companyLogo;
    Switch CBx_Disabled,newAddition;
    boolean Add_New = false;
    String address1,gst1,companyLogo1;
    Boolean disabled1,newAddition1;
    EditText SalesNameET = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_comp);

        CB_Sales_Company =  findViewById(R.id.sellerCompanyName);

         address = (EditText)findViewById(R.id.address) ;
         gst = (EditText)findViewById(R.id.gst) ;
         companyLogo = (EditText)findViewById(R.id.companyLogo) ;
         SalesNameET = findViewById(R.id.Sales_Name_TE) ;
         CBx_Disabled =  findViewById(R.id.disabled);

        Button save = (Button)findViewById(R.id.save) ;
        Switch newAddition = findViewById(R.id.SalesComp_Add_New);
        CB_Sales_Company.setOnItemSelectedListener(this);
        CB_Sales_Company.UpdateCB(true, "SellerComp", "CompanyName", "", "DISTINCT");

        SalesNameET.setVisibility(View.INVISIBLE);

        CB_Sales_Company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_SalesCompanyl = parent.getItemAtPosition(position).toString();
                CurrentSelect_SalesCompanyl_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    SalesNameET.setVisibility(View.VISIBLE);
                } else {
                    SalesNameET.setVisibility(View.INVISIBLE);
                }
            }

        });

        CurrentSelect_SalesCompanyl_Changed();
    }

    private void CurrentSelect_SalesCompanyl_Changed () {
        if ((Curr_SalesCompanyl != null) && (!Curr_SalesCompanyl.isEmpty())) {
            String Sel = String.format("CompanyName = '%s'", Curr_SalesCompanyl);
            address1 = myDB_G.Get_Val_from_DB_UD("SellerComp", "Address", "", Sel); address.setText(address1);
            gst1 = myDB_G.Get_Val_from_DB_UD("SellerComp", "GST", "", Sel); gst.setText(gst1);
            companyLogo1 = myDB_G.Get_Val_from_DB_UD("SellerComp", "CompLogo", "", Sel); companyLogo.setText(companyLogo1);
            disabled1 = (myDB_G.Get_Val_from_DB_UD("SellerComp", "Disabled", "", Sel)).equals("YES"); CBx_Disabled.setChecked(disabled1);
            //newAddition1 = (myDB_G.Get_Val_from_DB_UD("SellerComp", "Disabled", "", Sel)).equals("YES"); newAddition.setChecked(newAddition1);

        }
    }

    public void on_Material_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }



    public void OnSalesComp_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_SalesCompanyl = SalesNameET.getText().toString();
        }
        if ((Curr_SalesCompanyl == null) || (Curr_SalesCompanyl.isEmpty())) {
            ErrMsgDialog("Sales Company is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("CompanyName = '%s'", Curr_SalesCompanyl);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("SellerComp", "Create_TS", "", Sel);

        //Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;


        String Disabled = CBx_Disabled.isChecked()?"YES":"NO";
        String Address = address.getText().toString();
        String GST = gst.getText().toString();
        String CompLogo = companyLogo.getText().toString();


        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_SalesCompanyl  + "," + Address
                + ","   + GST + "," + CompLogo + "," + Disabled;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        // Need to add to Local DB
        //String[] Con_List = {Create_TS, Modify_TS, User, Curr_SalesCompanyl, Rate, Units, Disabled, OtherDetails, CFT_Rate};
        if (myDB_G.Insert_update_into_SalesCompany_Ordered(true, Create_TS, Modify_TS, User, Curr_SalesCompanyl, Address, GST, CompLogo, Disabled)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_SalesCompanyl, Curr_Activity.Act_SellerComp, UpdateStatement);
            UPload_Data_Msg_List.add(msg);

            //Upload_Data_InUp2_FB(Curr_SalesCompanyl, Curr_Activity.Act_Seller_Comp, UpdateStatement );
        } else {
            ErrMsgDialog("Unable to Insert/Update SalesCompany into Local DB");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onResume () {
        super.onResume();
        CurrentSelect_SalesCompanyl_Changed();
    }
}
