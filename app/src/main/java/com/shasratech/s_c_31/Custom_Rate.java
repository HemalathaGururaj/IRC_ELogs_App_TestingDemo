package com.shasratech.s_c_31;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import static com.shasratech.s_c_31.Globals.*;
import static com.shasratech.s_c_31.DBHelperUserData.*;



public class Custom_Rate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mycontext = this;
    private String TAG = "Custom_Rate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom__rate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Custom_Rate_Drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_Custom_Rate);
        navigationView.setNavigationItemSelectedListener(this);

        Populate_Rep_Items_Count_LV();
    }

       @Override
    public  void onResume () {
        super.onResume();
        Populate_Rep_Items_Count_LV();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.custom__rate, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void Populate_Rep_Items_Count_LV () {

        List<String> Result = new ArrayList<String>();

        Result.clear();
        String Table = "Custom_Rate";
        myDB_G.Get_Val_from_DB_UD(Table, "*", "", "", Result, "TimeStamp", false);
        Cursor OutputC = myDB_G.ReadData_from_DB_Table(Table);

        if (OutputC.getCount () > 0) {
            myDB_G.Get_Col_Names_from_Table(Table, Result);

            String[] fromFieldNames1 = new String[Result.size()];
            Result.toArray(fromFieldNames1);

            ArrayList<HashMap<String, String>>val1=new ArrayList<HashMap<String,String>>();
            HashMap<String, String>val=new HashMap<String,String>();
            int a1 = 0;
            int a2 = fromFieldNames1.length;
            for (; a1 < a2; a1++) {
                val.put(fromFieldNames1[a1], fromFieldNames1[a1]);
            }
            val1.add(val);

            int[] ToViewIDs = new int[]{R.id.CustomRate__L_id, R.id.CustomRate__R_id,  R.id.CustomRate__TimeStamp, R.id.CustomRate__Customer, R.id.CustomRate__Material,
                    R.id.CustomRate__Site, R.id.CustomRate__RemotePrice, R.id.CustomRate__BasePrice,  R.id.CustomRate__Change};

            SimpleAdapter k1 = new SimpleAdapter(getBaseContext(), val1, R.layout.customrate_inner, fromFieldNames1, ToViewIDs);

            ListView myRep_Items1 = (ListView) findViewById(R.id.Custom_Rate_Header);
            myRep_Items1.setAdapter(k1);

            SimpleCursorAdapter myCursorAdapter;
            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.customrate_inner, OutputC, fromFieldNames1, ToViewIDs, 0);


            ListView myRep_Items = (ListView) findViewById(R.id.Custom_Rate_Items_List);
            myRep_Items.setAdapter(myCursorAdapter);
        }
    }
    public void on_Rep_Home_PB_Click(View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

}
