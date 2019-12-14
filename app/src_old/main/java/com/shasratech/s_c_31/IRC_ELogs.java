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

import static com.shasratech.s_c_31.Globals.DownLoad_Complete_Table_from_Cloud_Act_IRC_ELogs;
import static com.shasratech.s_c_31.Globals.DownLoad_Complete_Table_from_Cloud_Act_Material;
import static com.shasratech.s_c_31.Globals.ErrMsgDialog;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class IRC_ELogs extends AppCompatActivity {

    Context mycontext = this;
    private String TAG = "Material";
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
        Intent Material_Modify = new Intent(this, Material_Modify.class);
        startActivity(Material_Modify);
    }

    public void on_DownLoad_PB_Click(View view) {
        DownLoad_Complete_Table_from_Cloud_Act_IRC_ELogs = true;
    }

    public void on_Delete_Data_PB_Click(View view) {
        final String Table = "Material";
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
        final String Table = "Material";

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

                        String Table = "IRC_ELogs";




                        Cursor OutputC = myDB_G.ReadData_from_DB_Table(Table, "_id,Ticket_Num, Ticket_Num2,HomeShed1,HomeShed2,CrewDetails ,DriverName,Asst_DriverDesig,Load,KM, TripDetails, Start_P, End_P, TrainNum," +
                                " Attended_At,  Arrival, Departure, LocoDetails, LocoNum, Last_Sch_Attdnd, Next_Sch_On, " +
                                "Levels,Fuel_Init,Fuel_Final, Fuel_Consmp, Lube_Init, Lube_Final, Lube_Consmp, CompressorOil, GovernorOil, " +
                                "Cool_Wtr, Driver_Book1, Maintenance1, Driver_Book2, Maintenance2,Driver_Book3, Maintenance3,Driver_Book4, Maintenance4,Driver_Book5," +
                                " Maintenance5,    StartFuse,  FireExting, FuelFill,  AlertWork ,MUCode ,SPeedoM, HI_Flash, LVC_Locks, GR_Seal, DB_Seal, Wiper, Sanders, Sig_Drv1," +
                                " DrvName1,  Sig_Drv2, DrvName2 ,Ticket_P2_1, Shed_P2_1, Ticket_P2_2 ,Shed_P2_2 ,Time1, KM1, Motoring1, Notch1, Load_Read1," +
                                " Battery_Read1, Time2, KM2, Motoring2, Notch2, Load_Read2, Battery_Read2, Time3, KM3, Motoring3, Notch3, Load_Read3, Battery_Read3,Create_TS, Modify_TS, User", "", "", "Ticket_Num");


                        if (OutputC != null) {

                            String[] fromFieldNames1 = {"_id","Ticket_Num","Ticket_Num2","HomeShed1","HomeShed2","CrewDetails","DriverName","Asst_DriverDesig","Load","KM","TripDetails", "Start_P", "End_P", "TrainNum", "Attended_At",
                                    "Arrival","Departure","LocoDetails", "LocoNum", "Last_Sch_Attdnd", "Next_Sch_On", "Levels","Fuel_Init","Fuel_Final","Fuel_Consmp", "Lube_Init", "Lube_Final", "Lube_Consmp", "CompressorOil", "GovernorOil",
                                    "Cool_Wtr",  "Driver_Book1", "Maintenance1", "Driver_Book2", "Maintenance2","Driver_Book3", "Maintenance3","Driver_Book4", "Maintenance4","Driver_Book5",
                                    "Maintenance5", "StartFuse",  "FireExting", "FuelFill",  "AlertWork","MUCode","SPeedoM", "HI_Flash", "LVC_Locks", "GR_Seal", "DB_Seal", "Wiper", "Sanders", "Sig_Drv1",
                                    "DrvName1","Sig_Drv2", "DrvName2","Ticket_P2_1", "Shed_P2_1", "Ticket_P2_2","Shed_P2_2" ,"Time1", "KM1", "Motoring1", "Notch1", "Load_Read1",
                                    "Battery_Read1",  "Time2", "KM2", "Motoring2", "Notch2", "Load_Read2", "Battery_Read2", "Time3", "KM3", "Motoring3", "Notch3", "Load_Read3", "Battery_Read3","Create_TS", "Modify_TS", "User"};


                            ArrayList<HashMap<String, String>> val1 = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> val = new HashMap<String, String>();
                            int a1 = 0;
                            int a2 = fromFieldNames1.length;
                            for (; a1 < a2; a1++) {
                                val.put(fromFieldNames1[a1], fromFieldNames1[a1]);

                            }
                            val1.add(val);

                            int[] ToViewIDs = new int[]{R.id.IRC_ELogs__L_id,R.id.IRC_ELogs_Ticket_Num,R.id.IRC_ELogs_Ticket_Num2, R.id.IRC_ELogs_HomeShed1,R.id.IRC_ELogs_HomeShed2,R.id.IRC_ELogs_CrewDetails,R.id.IRC_ELogs_DriverName,R.id.IRC_ELogs_Asst_DriverDesig,R.id.IRC_ELogs_Load,R.id.IRC_ELogs_KM,/*R.id.Material_Name, R.id.Material_price, R.id.Material__units, R.id.Material__CFT_Rate,
                                    R.id.Material__Disabled,*/  R.id.IRC_ELogs_TripDetails, R.id.IRC_ELogs_Start_P,  R.id.IRC_ELogs_End_P, R.id.IRC_ELogs_TrainNum, R.id.IRC_ELogs_Attended_At,  R.id.IRC_ELogs_Arrival,
                                    R.id.IRC_ELogs_Departure, R.id.IRC_ELogs_LocoDetails,  R.id.IRC_ELogs_LocoNum,  R.id.IRC_ELogs_Last_Sch_Attdnd,  R.id.IRC_ELogs_Next_Sch_On, R.id.IRC_ELogs_Levels,R.id.IRC_ELogs_Fuel_Init, R.id.IRC_ELogs_Fuel_Final, R.id.IRC_ELogs_Fuel_Consmp, R.id.IRC_ELogs_Lube_Init, R.id.IRC_ELogs_Lube_Final, R.id.IRC_ELogs_Lube_Consmp, R.id.IRC_ELogs_CompressorOil, R.id.IRC_ELogs_GovernorOil,
                                    R.id.IRC_ELogs_Cool_Wtr,R.id.IRC_ELogs_Driver_Book1, R.id.IRC_ELogs_Maintenance1, R.id.IRC_ELogs_Driver_Book2, R.id.IRC_ELogs_Maintenance2,R.id.IRC_ELogs_Driver_Book3, R.id.IRC_ELogs_Maintenance3,R.id.IRC_ELogs_Driver_Book4, R.id.IRC_ELogs_Maintenance4,R.id.IRC_ELogs_Driver_Book5,
                                    R.id.IRC_ELogs_Maintenance5, R.id.IRC_ELogs_StartFuse,  R.id.IRC_ELogs_FireExting, R.id.IRC_ELogs_FuelFill,  R.id.IRC_ELogs_AlertWork ,R.id.IRC_ELogs_MUCode ,R.id.IRC_ELogs_SPeedoM, R.id.IRC_ELogs_HI_Flash, R.id.IRC_ELogs_LVC_Locks, R.id.IRC_ELogs_GR_Seal, R.id.IRC_ELogs_DB_Seal, R.id.IRC_ELogs_Wiper, R.id.IRC_ELogs_Sanders, R.id.IRC_ELogs_Sig_Drv1,
                                    R.id.IRC_ELogs_DrvName1, R.id.IRC_ELogs_Sig_Drv2, R.id.IRC_ELogs_DrvName2 ,R.id.IRC_ELogs_Ticket_P2_1, R.id.IRC_ELogs_Shed_P2_1, R.id.IRC_ELogs_Ticket_P2_2 ,R.id.IRC_ELogs_Shed_P2_2 ,R.id.IRC_ELogs_Time1, R.id.IRC_ELogs_KM1, R.id.IRC_ELogs_Motoring1, R.id.IRC_ELogs_Notch1, R.id.IRC_ELogs_Load_Read1,
                                    R.id.IRC_ELogs_Battery_Read1,R.id.IRC_ELogs_Time2, R.id.IRC_ELogs_KM2, R.id.IRC_ELogs_Motoring2, R.id.IRC_ELogs_Notch2, R.id.IRC_ELogs_Load_Read2, R.id.IRC_ELogs_Battery_Read2, R.id.IRC_ELogs_Time3, R.id.IRC_ELogs_KM3, R.id.IRC_ELogs_Motoring3, R.id.IRC_ELogs_Notch3, R.id.IRC_ELogs_Load_Read3, R.id.IRC_ELogs_Battery_Read3,R.id.IRC_ELogs__Create_TS, R.id.IRC_ELogs__Modify_TS, R.id.IRC_ELogs__User,/* R.id.Material__OtherDetails, R.id.Material__CFTs_Per_Ton */};

                            SimpleAdapter k1 = new SimpleAdapter(getBaseContext(), val1, R.layout.irc_elogs_inner, fromFieldNames1, ToViewIDs);

                            ListView myMaterial_Items1 = (ListView) findViewById(R.id.Material_Header);
                            myMaterial_Items1.setAdapter(k1);

                            SimpleCursorAdapter myCursorAdapter;
                            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.irc_elogs_inner, OutputC, fromFieldNames1, ToViewIDs, 0);

                            GridView myMaterial_Items = (GridView) findViewById(R.id.Material_Items_List);
                            myMaterial_Items.setAdapter(myCursorAdapter);

                        }
                    }
                });
            }
        }.start();
    }

}
