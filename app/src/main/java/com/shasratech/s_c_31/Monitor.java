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
import static com.shasratech.s_c_31.Globals.DownLoad_Complete_Table_from_Cloud_Act_Monitor;
import static com.shasratech.s_c_31.Globals.ErrMsgDialog;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class Monitor extends AppCompatActivity {

    Context mycontext = this;
    private String TAG = "Monitor";
    private Handler Table_Display_Handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_activity);
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
        getMenuInflater().inflate(R.menu.monitor, menu);
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

    public void on_Monitor_PB_Click(View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void on_Monitor_refresh_PB_Click(View view) {
        Populate_Items_into_Table();
    }


    public void on_Select_PB_Click(View view) {
        ErrMsgDialog("Noting to Select or This feature is not Implemented");
        //Intent Person_Modify = new Intent(this, Person_Modify.class);
        //startActivity(Person_Modify);
    }

    public void on_Modify_PB_Click(View view) {
        Intent HomeShed_Modify = new Intent(this, Monitor_Modify.class);
        startActivity(HomeShed_Modify);
    }

    public void on_DownLoad_PB_Click(View view) {
        DownLoad_Complete_Table_from_Cloud_Act_Monitor = true;
    }

    public void on_Delete_Data_PB_Click(View view) {
        final String Table = "Monitor";
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
        final String Table = "Monitor";

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

                        String Table = "Monitor";

                        Cursor OutputC = myDB_G.ReadData_from_DB_Table(Table, "_id, Monitor_Time, Trip_No, Monitoring_Breaking, KM, Load_Meter_Reading, Notch, Battery_Ammeter_Reading, Create_TS, Modify_TS, User", "", "", "Monitor_Time");

                        if (OutputC != null) {

                            String[] fromFieldNames1 = {"_id", "Monitor_Time","Trip_No", "Monitoring_Breaking","KM", "Load_Meter_Reading","Notch","Battery_Ammeter_Reading","Create_TS", "Modify_TS", "User"};


                            ArrayList<HashMap<String, String>> val1 = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> val = new HashMap<String, String>();
                            int a1 = 0;
                            int a2 = fromFieldNames1.length;
                            for (; a1 < a2; a1++) {
                                val.put(fromFieldNames1[a1], fromFieldNames1[a1]);

                            }
                            val1.add(val);

                            int[] ToViewIDs = new int[]{R.id.Monitor__L_id, R.id.Monitor_Time, R.id.Monitor_KM, R.id.Monitor_TripNo,R.id.Monitor_Monitoring_Breaking,R.id.Monitor_Load_Meter_Reading,
                                    R.id.Monitor_Notch,R.id.Monitor_Battery_Ammeter_Reading,R.id.Monitor_Create_TS,  R.id.Monitor_Modify_TS, R.id.Monitor_User};
                            SimpleAdapter k1 = new SimpleAdapter(getBaseContext(), val1, R.layout.monitor_inner, fromFieldNames1, ToViewIDs);

                            ListView myMaterial_Items1 = (ListView) findViewById(R.id.Material_Header);
                            myMaterial_Items1.setAdapter(k1);

                            SimpleCursorAdapter myCursorAdapter;
                            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.monitor_inner, OutputC, fromFieldNames1, ToViewIDs, 0);

                            GridView myMaterial_Items = (GridView) findViewById(R.id.Material_Items_List);
                            myMaterial_Items.setAdapter(myCursorAdapter);

                        }
                    }
                });
            }
        }.start();
    }

}
