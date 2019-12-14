
package com.shasratech.s_c_31;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;
import static com.shasratech.s_c_31.DBHelperUserData.*;

public class CustomerDetails extends AppCompatActivity {

    Context mycontext = this;
    private String TAG = "CustomerDetails";
	private Handler Table_Display_Handler = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Table_Display_Handler = new Handler();

        Populate_Items_into_Table();

    }

    @Override
    public  void onResume () {
        super.onResume();
        Populate_Items_into_Table();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.material, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void on_Home_PB_Click(View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void on_Refresh_PB_Click(View view) {
        Populate_Items_into_Table();
    }

    public void on_Select_PB_Click(View view) {
        ErrMsgDialog("Noting to Select or This feature is not Implemented");
        //Intent Person_Modify = new Intent(this, Person_Modify.class);
        //startActivity(Person_Modify);
    }

    public void on_Modify_PB_Click(View view) {
        Intent CustomerDetails_Modify = new Intent(this, CustomerDetails_Modify.class);
        startActivity(CustomerDetails_Modify);
    }

    public void on_DownLoad_PB_Click(View view) {
        // Need to Add Code to Download the data present in the Cloud

        DownLoad_Complete_Table_from_Cloud_Act_CustomerDetails = true;
        //myGContext_Global = this;
        //DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_CustomerDetails);
    }
	
    public void on_Delete_Data_PB_Click(View view) {
        final String Table = "CustomerDetails";
        // Need to Add Code to Download the data present in the Cloud

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        myDB_G.db_Handle_G_UD.execSQL("DELETE FROM " + Table + ";");
                        ErrMsgDialog("Cleanin "+ Table + " table Complete.\n");
                        Populate_Items_into_Table();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Delete data from table \"" + Table + "\" in Current Mobile?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
	
   public void on_Delete_Table_PB_Click(View view) {
        // Need to Add Code to Download the data present in the Cloud
        final String Table = "CustomerDetails";

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        myDB_G.db_Handle_G_UD.execSQL("DROP TABLE IF EXISTS " + Table + ";");
                        System.exit(0);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Delete \"\" + Table + \"\" Table in Current Mobile\n\nYou will need to restart the Application after this.?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void Populate_Items_into_Table () {
     		new Thread() {
            public void run() {
                Table_Display_Handler.post(new Runnable() {
                    @Override
                    public void run() {

        String Table = "CustomerDetails";

        myDB_G.Get_Val_from_DB_UD(Table);
        Cursor OutputC = myDB_G.ReadData_from_DB_Table(Table, "_id, CustomerName, Reference, CreditLimit,  CreditLimitAlarm, SellerComName, Permit_Pct, CGST_Pct,SGST_Pct,ISGST_Pct,Num_Prints,TP_Print_Cash,TP_Print_CompD,PO_Num,Tax_Invoice_Remarks, Create_TS, Modify_TS, User", "", "", "CustomerName");

        if (OutputC != null) {
            String[] fromFieldNames1 = {"_id", "CustomerName", "Reference", "CreditLimit", "CreditLimitAlarm", "SellerComName", "Permit_Pct", "CGST_Pct","SGST_Pct", "ISGST_Pct", "Num_Prints", "TP_Print_Cash","TP_Print_CompD","PO_Num","Tax_Invoice_Remarks", "Create_TS", "Modify_TS", "User"};


            ArrayList<HashMap<String, String>> val1 = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> val = new HashMap<String, String>();
            int a1 = 0;
            int a2 = fromFieldNames1.length;
            for (; a1 < a2; a1++) {
                val.put(fromFieldNames1[a1], fromFieldNames1[a1]);
            }
            val1.add(val);

            int[] ToViewIDs = new int[]{R.id.CustomerDetails__L_id, R.id.CustomerDetails_CustomerName, R.id.CustomerDetails_Reference,  R.id.CustomerDetails_CreditLimit,  R.id.CustomerDetails_CreditLimitAlarm,
                    R.id.CustomerDetails__SellerComName, R.id.CustomerDetails__Permit_Pct,
                    R.id.CustomerDetails__CGST_Pct,R.id.CustomerDetails__SGST_Pct, R.id.CustomerDetails__ISGST_Pct,
                    R.id.CustomerDetails__Num_Prints, R.id.CustomerDetails__TP_Print_Cash, R.id.CustomerDetails__TP_Print_CompD,
                    R.id.CustomerDetails__PO_Num,R.id.CustomerDetails__Tax_Invoice_Remarks, R.id.CustomerDetails__Create_TS, R.id.CustomerDetails__Modify_TS, R.id.CustomerDetails__User };

            SimpleAdapter k1 = new SimpleAdapter(getBaseContext(), val1, R.layout.customer_details_inner, fromFieldNames1, ToViewIDs);

            ListView myCustomerDetails_Items1 = (ListView) findViewById(R.id.Material_Header);
            myCustomerDetails_Items1.setAdapter(k1);

            SimpleCursorAdapter myCursorAdapter;
            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.customer_details_inner, OutputC, fromFieldNames1, ToViewIDs, 0);


            //ListView myCustomerDetails_Items = (ListView) findViewById(R.id.CustomerDetails_Items_List);
            GridView myCustomerDetails_Items = (GridView) findViewById(R.id.Material_Items_List);
            myCustomerDetails_Items.setAdapter(myCursorAdapter);

        }
                    }
                });
            }
        }.start();
    }

}
