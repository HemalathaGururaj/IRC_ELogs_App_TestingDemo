package com.shasratech.s_c_31;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;

public class CustomerDetails_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static MySpinner CB_customerSpinner = null;
    private static MySpinner CB_sellerCompanySpinner = null;
    boolean Add_New = false;
    String Curr_CustomerSpinner = "";
    String Curr_SellerCompanySpinner = "";
    private final String TAG = "CustDet_Modify";

    CheckBox tpPrintCash = null;
    CheckBox tpPrintCompanyDetails = null;


    EditText poNumber,creditLimit,CreditLimitAlarm,permitPct,sgstPct,cgstPct,isgstPct,numPrints,taxInvoiceRemarks;
    String poNumber1,creditLimit1,CreditLimitAlarm1,permitPct1,sgstPct1,cgstPct1,isgstPct1,numPrints1,taxInvoiceRemarks1,CB_sellerCompanySpinner1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_details_modify_activity);

        CB_customerSpinner =  findViewById(R.id.customer);
        CB_sellerCompanySpinner =  findViewById(R.id.sellerCompany);
         poNumber = (EditText)findViewById(R.id.poNumber) ;
         creditLimit = (EditText)findViewById(R.id.creditLimit) ;
         CreditLimitAlarm = (EditText)findViewById(R.id.creditLimitAlarm) ;

         permitPct = (EditText)findViewById(R.id.permitPct) ;
         sgstPct = (EditText)findViewById(R.id.sgstPct) ;
         cgstPct = (EditText)findViewById(R.id.cgstPct) ;
         isgstPct = (EditText)findViewById(R.id.isgstPct) ;
         numPrints = (EditText)findViewById(R.id.numPrints) ;
         taxInvoiceRemarks = (EditText)findViewById(R.id.taxInvoiceRemarks) ;


         tpPrintCash = (CheckBox) findViewById(R.id.tpPrintCash);
         tpPrintCompanyDetails = (CheckBox) findViewById(R.id.tpPrintCompanyDetails);

        Button save = (Button)findViewById(R.id.save) ;


        CB_customerSpinner.setOnItemSelectedListener(this);
        CB_sellerCompanySpinner.setOnItemSelectedListener(this);

        CB_sellerCompanySpinner.UpdateCB(true, "SellerComp", "CompanyName", "", "DISTINCT");
        CB_customerSpinner.UpdateCB(true, "Person", "Name", "Person_Type = 'CUSTOMER'", "DISTINCT");

        CB_customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_CustomerSpinner = parent.getItemAtPosition(position).toString();
                CurrentSelect_customerSpinner_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((Curr_CustomerSpinner == null) || (Curr_CustomerSpinner.isEmpty())) {
                    ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
                    return;
                }

                String Sel = String.format("CustomerName = '%s'", Curr_CustomerSpinner);

                String Create_TS = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "Create_TS", "", Sel);


                String Modify_TS = Get_Current_Date_Time();

                if ((Create_TS == null) || (Create_TS.isEmpty())) {
                    Create_TS = Modify_TS;
                }
                String User = FBAppMBName;

                String CreditLimit = creditLimit.getText().toString();
                String CreditLimitAlarm1 = CreditLimitAlarm.getText().toString();
                String SellerComName = CB_sellerCompanySpinner.getSelectedItemI();
                String Permit_Pct = permitPct.getText().toString();
                String CGST_Pct = cgstPct.getText().toString();
                String SGST_Pct = sgstPct.getText().toString();
                String Num_Prints = numPrints.getText().toString();
                String TP_Print_Cash = tpPrintCash.isChecked()?"YES":"NO";
                String TP_Print_CompD = tpPrintCompanyDetails.isChecked()?"YES":"NO";
                String PO_Num = poNumber.getText().toString();
                String Tax_Invoice_Remarks = (taxInvoiceRemarks.getText().toString()).replace('\n', '\05').replace(',', '\04');
                String ISGST_Pct = isgstPct.getText().toString();
                ISGST_Pct = ISGST_Pct.isEmpty()?"0":ISGST_Pct;
                CreditLimitAlarm1 = CreditLimitAlarm1.isEmpty()?"0":CreditLimitAlarm1;

                String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_CustomerSpinner + "," + CreditLimit +
                         "," + SellerComName + "," + Permit_Pct + "," + CGST_Pct+ "," + SGST_Pct + "," + Num_Prints + "," + TP_Print_Cash
                        + "," + TP_Print_CompD + "," + PO_Num + "," + Tax_Invoice_Remarks + "," + ISGST_Pct + "," + CreditLimitAlarm1;

                Log.i (TAG, "save.setOnClickListener UpdateStatement = " + UpdateStatement );

                Log.i (TAG, "save.setOnClickListener CreditLimitAlarm1 = " + CreditLimitAlarm1 );

                // Need to add to Local DB
                //String[] Con_List = {Create_TS, Modify_TS, User, Curr_Material, Rate, Units, Disabled, OtherDetails, CFT_Rate};
                if (myDB_G.Insert_update_into_Customer_Details_Ordered(true, Create_TS, Modify_TS, User, Curr_CustomerSpinner, CreditLimit, CreditLimitAlarm1, SellerComName, Permit_Pct, CGST_Pct,SGST_Pct,Num_Prints,TP_Print_Cash,TP_Print_CompD,PO_Num,Tax_Invoice_Remarks,ISGST_Pct)) {

                    ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");
                    UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_CustomerSpinner, Curr_Activity.Act_CustomerDetails, UpdateStatement );
                    UPload_Data_Msg_List.add(msg);

                } else {
                    ErrMsgDialog("Unable to Insert/Update Material into Local DB");
                }
            }
        });
    }

    private void CurrentSelect_customerSpinner_Changed () {
        if ((Curr_CustomerSpinner != null) && (!Curr_CustomerSpinner.isEmpty())) {
            String Sel = String.format("CustomerName = '%s'", Curr_CustomerSpinner);
            poNumber1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "PO_Num", "", Sel); poNumber.setText(poNumber1);
            creditLimit1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "CreditLimit", "", Sel); creditLimit.setText(creditLimit1);
            CreditLimitAlarm1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "CreditLimitAlarm", "", Sel); CreditLimitAlarm.setText(CreditLimitAlarm1);
            permitPct1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "Permit_Pct", "", Sel); permitPct.setText(permitPct1);
            sgstPct1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "SGST_Pct", "", Sel); sgstPct.setText(sgstPct1);
            cgstPct1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "CGST_Pct", "", Sel); cgstPct.setText(cgstPct1);
            isgstPct1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "ISGST_Pct", "", Sel); isgstPct.setText(isgstPct1);
            numPrints1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "Num_Prints", "", Sel); numPrints.setText(numPrints1);
            taxInvoiceRemarks1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "Tax_Invoice_Remarks", "", Sel); taxInvoiceRemarks.setText(taxInvoiceRemarks1);
            CB_sellerCompanySpinner1 = myDB_G.Get_Val_from_DB_UD("CustomerDetails", "SellerComName", "", Sel); CB_sellerCompanySpinner.getSelectedItem();

            boolean tp_PrintCash_B = (myDB_G.Get_Val_from_DB_UD("CustomerDetails", "TP_Print_Cash", "", Sel)).equals("YES"); tpPrintCash.setChecked(tp_PrintCash_B);
            boolean tp_PrintCompD_B = (myDB_G.Get_Val_from_DB_UD("CustomerDetails", "TP_Print_CompD", "", Sel)).equals("YES"); tpPrintCompanyDetails.setChecked(tp_PrintCompD_B);
        }
    }

    public void on_Material_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
