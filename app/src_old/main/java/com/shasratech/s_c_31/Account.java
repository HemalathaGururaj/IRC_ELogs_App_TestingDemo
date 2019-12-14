package com.shasratech.s_c_31;

import android.annotation.SuppressLint;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import java.util.ArrayList;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.shasratech.s_c_31.DBHelperAppUtils.*;
import static com.shasratech.s_c_31.Globals.*;
import static com.shasratech.s_c_31.DBHelperUserData.*;


public class Account extends AppCompatActivity {

    Context mycontext = this;
    private String TAG = "Account";
    private Handler Table_Display_Handler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        curr_activity = Curr_Activity.Act_Account;

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
        Intent inent = new Intent(this, Trans_Seletion.class);
        startActivity(inent);

    }

    public void on_Modify_PB_Click(View view) {
	ErrMsgDialog("Accounts table Can't be modified directly");
    }

    public void on_DownLoad_PB_Click(View view) {
        DownLoad_Complete_Table_from_Cloud_Act_Account = true;
    }

    public void on_Delete_Data_PB_Click(View view) {
        final String Table = "Account";
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
        final String Table = "Account";

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
        builder.setMessage("Are you sure you want to Delete \"" + Table + "\" Table in Current Mobile\n\nYou will need to restart the Application after this.?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void Populate_Items_into_Table () {
        new Thread() {
            public void run() {
                Table_Display_Handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String Table = "Account";
                        //Log.i (TAG, "11111111111111111111 Get_Val_from_DB_UD for Account = " + myDB_G.Get_Val_from_DB_UD(Table));
                        Cursor OutputC =  myDB_G.ReadData_from_DB_Table(Table, "_id,Acc_id,TimeStamp,Ref_id,Reference,AccountName,Received,Paid,Ttl_Payable_by_Com,Details,SourceSite", "", WhereCluase_Account, "TimeStamp Desc");

                        if (OutputC != null) {
							String[] fromFieldNames1 = {"_id", "Acc_id", "TimeStamp",  "Ref_id", "Reference", "AccountName", "Received", "Paid", "Ttl_Payable_by_Com", "Details", "SourceSite"};


            ArrayList<HashMap<String, String>>val1=new ArrayList<HashMap<String,String>>();
            HashMap<String, String>val=new HashMap<String,String>();
            int a1 = 0;
            int a2 = fromFieldNames1.length;
            for (; a1 < a2; a1++) {
                val.put(fromFieldNames1[a1], fromFieldNames1[a1]);
            }
            val1.add(val);

            int[] ToViewIDs = new int[]{R.id.Account_L_id,  R.id.Account_Acc_id, R.id.Account_TimeStamp,  R.id.Account_Ref_id,
                    R.id.Account_Reference, R.id.Account_AccountName, R.id.Account_Received, R.id.Account_Paid,  R.id.Account_Ttl_Payable_by_Com,
                    R.id.Account_Details, R.id.Account_SourceSite};

            SimpleAdapter k1 = new SimpleAdapter(getBaseContext(), val1, R.layout.account_inner, fromFieldNames1, ToViewIDs);

            ListView myAccount_Items1 = (ListView) findViewById(R.id.Material_Header);
            myAccount_Items1.setAdapter(k1);

            SimpleCursorAdapter myCursorAdapter;
            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.account_inner, OutputC, fromFieldNames1, ToViewIDs, 0);


            GridView myAccount_Items = (GridView) findViewById(R.id.Material_Items_List);
            myAccount_Items.setAdapter(myCursorAdapter);
        }
                    }
                });
            }
        }.start();
    }

}
