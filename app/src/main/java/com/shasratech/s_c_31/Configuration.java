package com.shasratech.s_c_31;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.DBHelperAppUtils.db_Handle_G_AU;
import static com.shasratech.s_c_31.DBHelperUserData.*;
import static com.shasratech.s_c_31.Globals.*;

public class Configuration extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "Configuration";
    private static Spinner AppNameSpinner;
    //ArrayAdapter<CharSequence> appNameAdapter;
    private static String t_AppName = "";
    Context Config_Context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Config_Context = this;

        Init_Variables();

        Switch newAddition = findViewById(R.id.CBx_Start_App_Install_from_Beginning);
        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    final String table = "App_SysConf";

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //myDB_G.Clear_Existing_DB();
                                    db_Handle_G_AU.execSQL("DROP TABLE IF EXISTS " + table + ";");
                                    ErrMsgDialog("Cleaning table Complete.\n");

                                    myDBAppUtils.InitAll_DB_AU();

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Config_Context, R.style.AlertDialogTheme);
                    builder1.setMessage("Are you sure you want to Clean\nDataBase Application Configuration\n\n" + table + "\n and Start from Beginning\n\n" +
                            "Are you sure you of this").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                    int a = 0;
                    final int b = Table_List.length;
                    for (; a < b; a++) {
                        final String table1 = Table_List[a];

                        Log.i (TAG, "Checking for Table[" + a + "] " + table1 + "================================================");
                        dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        myDB_G.Drop_Table(table1);

                                        Log.i (TAG, "Cleaning for Table " + table1 + "===== Complete ===========================================");
                                        ErrMsgDialog("Cleaning table "+ table1 +" Complete.\n");
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        // Custom Color for Builder and Buttons
                        // https://stackoverflow.com/questions/27965662/how-can-i-change-default-dialog-button-text-color-in-android-5

                        AlertDialog.Builder builder = new AlertDialog.Builder(Config_Context, R.style.AlertDialogTheme);
                        builder.setMessage("Are you sure you want to Clean \n\n" + table1 + "\n and Start from Beginning\n\n" +
                                "Are you sure you of this").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                    myDB_G.Create_DB_Tables();
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Init_Variables();
    }

    private void Init_Variables() {
        AppNameSpinner = (Spinner) findViewById(R.id.AppNameSpinner);

        List<String> AppCategories = new ArrayList<>();
        AppCategories.add("Choose AppName");
        AppCategories.add("Choultry Management System");
        AppCategories.add("SC Basic Version");
        AppCategories.add("SoftCrusher Advanced Version");
        AppCategories.add("Vehicle Tracking System");


        ArrayAdapter<String> appNameAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, AppCategories);
        appNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AppNameSpinner.setAdapter(appNameAdapter);


        int Cat_Pos = 0;
        switch (FBAppName) {
            case "ChMS" : Cat_Pos = 1; break;
            case "SC64" : Cat_Pos = 2; break;
            case "SoftCrusher" : Cat_Pos = 3; break;
            case "VTS" : Cat_Pos = 4; break;
            default: Cat_Pos = 0;
        }

        Log.i (TAG, "FBAppName = " + FBAppName + ", Cat_Pos = " + Cat_Pos + "====================================================================");

        AppNameSpinner.setSelection(Cat_Pos, true);

        AppNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                t_AppName = parent.getItemAtPosition(position).toString();
                switch (t_AppName) {
                    case "Choultry Management System" : FBAppName = "ChMS"; break;
                    case "SC Basic Version" : FBAppName = "SC64"; break;
                    case "SoftCrusher Advanced Version" : FBAppName = "SoftCrusher"; break;
                    case "Vehicle Tracking System" : FBAppName = "VTS"; break;
                    default: FBAppName = "Default"; break;
                }
                //FBAppName = t_AppName;
                //Toast.makeText(getBaseContext(), "Item Selected = "  + t_AppName, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText et_FBAppCustomer = (EditText) findViewById(R.id.ET_FB_App_Customer);
        et_FBAppCustomer.setText(FBAppCustomer);

        EditText et_FBAppMobileUserName = (EditText) findViewById(R.id.ET_FB_App_MobileName);
        et_FBAppMobileUserName.setText(FBAppMBName);

        EditText ET_FB_User_Name = (EditText) findViewById(R.id.ET_FB_User_Name);
        ET_FB_User_Name.setText(FB_User_Email_Sec_G);

        EditText ET_FB_Password = (EditText) findViewById(R.id.ET_FB_Password);
        ET_FB_Password.setText(FB_User_Pass_Sec_G);

        EditText ET_IMEI_Number = (EditText) findViewById(R.id.ET_IMEI_Number);
        ET_IMEI_Number.setText(Device_IMEI_ID);

/*
        Log.i(TAG, "Trans_Audio_Notify = " + Trans_Audio_Notify + "=================================================");
        CheckBox CBx_Trans = (CheckBox) findViewById(R.id.CBx_Trans_Sound_Notify);
        CBx_Trans.setChecked(Trans_Audio_Notify);


        CBx_Trans = (CheckBox) findViewById(R.id.CBx_MTrans_Sound_Notify);
        CBx_Trans.setChecked(MTrans_Audio_Notify);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_CR_Sound_Notify);
        CBx_Trans.setChecked(CustomRate_Audio_Notify);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Inventory_Sound_Notify);
        CBx_Trans.setChecked(Inventory_Audio_Notify);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Person_Sound_Notify);
        CBx_Trans.setChecked(Person_Audio_Notify);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Material_Sound_Notify);
        CBx_Trans.setChecked(Material_Audio_Notify);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Vehicle_Sound_Notify);
        CBx_Trans.setChecked(Vehicle_Audio_Notify);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_AutoSaveTrans_Sound_Notify);
        CBx_Trans.setChecked(AutoSaveTrans_Audio_Notify);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_IRC_ELogs_Sound_Notify);
        CBx_Trans.setChecked(IRC_ELogs_Audio_Notify);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_HomeShed_Sound_Notify);
        CBx_Trans.setChecked(HomeShed_Audio_Notify);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Location_Sound_Notify);
        CBx_Trans.setChecked(Location_Audio_Notify);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_TrainDetails_Sound_Notify);
        CBx_Trans.setChecked(TrainDetails_Audio_Notify);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_DriverDetails_Sound_Notify);
        CBx_Trans.setChecked(DriverDetails_Audio_Notify);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_LocoDetails_Sound_Notify);
        CBx_Trans.setChecked(LocoDetails_Audio_Notify); */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Configuration_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configuration, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Configuration_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void on_Conf_Home_PB_Click(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onTE_Dialog_Save_Clicked(View view) {
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "FBAppName", FBAppName, false);

        EditText ET_FB_App_Customer = (EditText) findViewById(R.id.ET_FB_App_Customer);
        FBAppCustomer = String.valueOf(ET_FB_App_Customer.getText());
        myDBAppUtils.InUp2_SysConf_T_AU("FBAppCustomer", FBAppCustomer, FBAppCustomer, false);

        EditText ET_FB_MobileUserName = (EditText) findViewById(R.id.ET_FB_App_MobileName);
        FBAppMBName = String.valueOf(ET_FB_MobileUserName.getText());
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "FBAppMBName", FBAppMBName, false);

        EditText ET_FB_User_Name = (EditText) findViewById(R.id.ET_FB_User_Name);
        FB_User_Email_Sec_G = String.valueOf(ET_FB_User_Name.getText());
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", FBAppCustomer + "-FB_User_Email_Sec", FB_User_Email_Sec_G, false);

        EditText ET_FB_Password = (EditText) findViewById(R.id.ET_FB_Password);
        FB_User_Pass_Sec_G = String.valueOf(ET_FB_Password.getText());
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", FBAppCustomer + "-FB_User_Pass_Sec", FB_User_Pass_Sec_G, false);

        EditText ET_Device_IMEI_ID = (EditText) findViewById(R.id.ET_IMEI_Number);
        Device_IMEI_ID = String.valueOf(ET_Device_IMEI_ID.getText());
        myDBAppUtils.InUp2_SysConf_T_AU("System", "Device_IMEI_ID", Device_IMEI_ID, false);

      /*  CheckBox CBx_Trans = (CheckBox) findViewById(R.id.CBx_Trans_Sound_Notify);
        Trans_Audio_Notify = CBx_Trans.isChecked();
        Log.i (TAG, "Saving Trans_Audio_Notify = " + Trans_Audio_Notify + ", Vehicle_Audio_Notify = " + Vehicle_Audio_Notify);
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "Trans_Audio_Notify", Trans_Audio_Notify?"YES":"NO", false);


        CBx_Trans = (CheckBox) findViewById(R.id.CBx_MTrans_Sound_Notify);
        MTrans_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "MTrans_Audio_Notify", MTrans_Audio_Notify?"YES":"NO", false);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_CR_Sound_Notify);
        CustomRate_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "CustomRate_Audio_Notify", CustomRate_Audio_Notify?"YES":"NO", false);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Inventory_Sound_Notify);
        Inventory_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "Inventory_Audio_Notify", Inventory_Audio_Notify?"YES":"NO", false);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Material_Sound_Notify);
        Material_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "Material_Audio_Notify", Material_Audio_Notify?"YES":"NO", false);

         CBx_Trans = (CheckBox) findViewById(R.id.CBx_IRC_ELogs_Sound_Notify);
        IRC_ELogs_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "IRC_ELogs_Audio_Notify", IRC_ELogs_Audio_Notify?"YES":"NO", false);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_HomeShed_Sound_Notify);
        HomeShed_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "HomeShed_Audio_Notify", HomeShed_Audio_Notify?"YES":"NO", false);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Location_Sound_Notify);
        Location_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "Location_Audio_Notify", Location_Audio_Notify?"YES":"NO", false);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_TrainDetails_Sound_Notify);
        TrainDetails_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "TrainDetails_Audio_Notify", TrainDetails_Audio_Notify?"YES":"NO", false);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_DriverDetails_Sound_Notify);
        DriverDetails_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "DriverDetails_Audio_Notify", DriverDetails_Audio_Notify?"YES":"NO", false);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_LocoDetails_Sound_Notify);
        LocoDetails_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "LocoDetails_Audio_Notify", LocoDetails_Audio_Notify?"YES":"NO", false);
        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Person_Sound_Notify);
        Person_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "Person_Audio_Notify", Person_Audio_Notify?"YES":"NO", false);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_Vehicle_Sound_Notify);
        Vehicle_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "Vehicle_Audio_Notify", Vehicle_Audio_Notify?"YES":"NO", false);

        CBx_Trans = (CheckBox) findViewById(R.id.CBx_AutoSaveTrans_Sound_Notify);
        AutoSaveTrans_Audio_Notify = CBx_Trans.isChecked();
        myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "AutoSaveTrans_Audio_Notify", AutoSaveTrans_Audio_Notify?"YES":"NO", false);

        */
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        boolean found_myDB_G = false;
                        //Log.i (TAG, "355 Configuration myDB_G=" + myDB_G);
                        for (DBHelperUserData_ref t: DBHelperUserData_Ref ) {
                            if (t.getMyDB(FBAppCustomer) != null) {
                                myDB_G = t.getMyDB(FBAppCustomer);
                                found_myDB_G = true;
                            }
                        }
                        if (!found_myDB_G) {
                            myDB_G = new DBHelperUserData(myGContext_Global, FBAppName, FBAppCustomer);
                            DBHelperUserData_ref t = new DBHelperUserData_ref(FBAppCustomer, myDB_G);
                            DBHelperUserData_Ref.add(t);
                        }
                        //Log.i (TAG, "367 Configuration myDB_G=" + myDB_G);
                        myDBAppUtils.Init_Variables(FBAppCustomer);
                        Configuration_Changed = true;
                        MainActivity.Update_Change_List();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage("Are you sure you want to Switch Database?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void onTE_Dialog_Cancel_Clicked (View view) {
        finish();
    }
}
