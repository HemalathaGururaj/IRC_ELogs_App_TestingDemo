package com.shasratech.s_c_31;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.shasratech.s_c_31.Globals.DownLoad_Complete_Table_from_Cloud_Act_HomeShed;
import static com.shasratech.s_c_31.Globals.ErrMsgDialog;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class HomeShed extends AppCompatActivity {

    Context mycontext = this;
    private String TAG = "HomeShed";
    private Handler Table_Display_Handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeshed_activity);
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
        getMenuInflater().inflate(R.menu.homeshed, menu);
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
        Intent HomeShed_Modify = new Intent(this, HomeShed_Modify.class);
        startActivity(HomeShed_Modify);
    }

    public void on_DownLoad_PB_Click(View view) {
        DownLoad_Complete_Table_from_Cloud_Act_HomeShed = true;
    }

    public void on_Delete_Data_PB_Click(View view) {
        final String Table = "HomeShed";
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
        final String Table = "HomeShed";

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

                        String Table = "HomeShed";

                        Cursor OutputC = myDB_G.ReadData_from_DB_Table(Table, "_id, HomeShed,Active, Zone, Create_TS, Modify_TS, User", "", "", "HomeShed");

                        if (OutputC != null) {

                            String[] fromFieldNames1 = {"_id", "HomeShed","Active", "Zone","Create_TS", "Modify_TS", "User"};


                            ArrayList<HashMap<String, String>> val1 = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> val = new HashMap<String, String>();
                            int a1 = 0;
                            int a2 = fromFieldNames1.length;
                            for (; a1 < a2; a1++) {
                                val.put(fromFieldNames1[a1], fromFieldNames1[a1]);

                            }
                            val1.add(val);

                            int[] ToViewIDs = new int[]{R.id.homeshed_L_id, R.id.homeshed_HomeShed, R.id.homeshed_Active, R.id.homeshed_Zone,R.id.homeshed_Create_TS,  R.id.homeshed_Modify_TS, R.id.homeshed_User};
                            SimpleAdapter k1 = new SimpleAdapter(getBaseContext(), val1, R.layout.homeshed_inner, fromFieldNames1, ToViewIDs);

                            ListView myMaterial_Items1 = (ListView) findViewById(R.id.Material_Header);
                            myMaterial_Items1.setAdapter(k1);

                            SimpleCursorAdapter myCursorAdapter;
                            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.homeshed_inner, OutputC, fromFieldNames1, ToViewIDs, 0);

                            GridView myMaterial_Items = (GridView) findViewById(R.id.Material_Items_List);
                            myMaterial_Items.setAdapter(myCursorAdapter);

                        }
                    }
                });
            }
        }.start();
    }

}
