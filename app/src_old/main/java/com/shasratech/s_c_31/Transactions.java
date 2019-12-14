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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;
import static com.shasratech.s_c_31.DBHelperUserData.*;

public class Transactions extends AppCompatActivity {

    // DBHelperUserData myDB;
    Context mycontext = this;
    private String TAG = "Transactions";
    int Bar_Chart_Orig_Height = 0;
    boolean Bar_Chart_Orig_Height_confg = false;
    TransCanvas Trans_Canvas_Image = null;
    LinearLayout Trans_Canvas_LL = null;

    private Handler Table_Display_Handler = null;
    Switch BarChart = null;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curr_activity = Curr_Activity.Act_Transactions;
        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Trans_Canvas_Image = findViewById(R.id.Trans_Canvas_Image);
        Trans_Canvas_LL = findViewById(R.id.TransCanvas_LL);
        Table_Display_Handler = new Handler();

        BarChart = findViewById(R.id.Sw_Trans_BarChart);


        //Trans_Boolean_Bar_Chart_FullSize = true;
        BarChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Trans_Boolean_Bar_Chart_FullSize = isChecked;
                if (isChecked) {
                    ScrollView t = findViewById(R.id.Trans_Table_View_111HSV);
                    ViewGroup.LayoutParams lp = t.getLayoutParams();
                    lp.height = MainWindow_Height - 100;
                    t.setLayoutParams(lp);


                    ViewGroup.LayoutParams  LLLP= Trans_Canvas_LL.getLayoutParams();
                    LLLP.height = MainWindow_Height - 100;
                    Trans_Canvas_LL.setLayoutParams(LLLP);


                   // Trans_Canvas_Image.setLayoutParams(lp);

                    //PersonNameET.setVisibility(View.VISIBLE);
                } else {
                    ScrollView t = findViewById(R.id.Trans_Table_View_111HSV);
                    ViewGroup.LayoutParams lp = t.getLayoutParams();
                    int min_height = t.getMinimumHeight();
                    lp.height = min_height;
                    t.setLayoutParams(lp);
                    ViewGroup.LayoutParams  LLLP= Trans_Canvas_LL.getLayoutParams();
                    LLLP.height = min_height;
                    Trans_Canvas_LL.setLayoutParams(LLLP);

                   // Trans_Canvas_Image.setLayoutParams(lp);
                    //PersonNameET.setVisibility(View.INVISIBLE);
                }
            }

        });

        Populate_Items_into_Table();
    }

    @Override
    public void onResume() {
        super.onResume();
        Populate_Items_into_Table();
       // Transactions_TH.Resume_Timer();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transactions, menu);
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

    public void on_Transactions_Home_PB_Click(View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void on_Transactions_refresh_PB_Click(View view) {
        Populate_Items_into_Table();
        Trans_Canvas_Image.invalidate();
    }

    public void on_Transactions_Select_PB_Click(View view) {
        Intent inent = new Intent(this, Trans_Seletion.class);
        startActivity(inent);
    }

    public void on_Transactions_Modify_PB_Click(View view) {
        Intent inent = new Intent(this, Transactions_Modify.class);
        startActivity(inent);
    }

    public void on_DownLoad_PB_Click(View view) {
        DownLoad_Complete_Table_from_Cloud_Act_Transactions = true;
    }

    public void on_Delete_Data_PB_Click(View view) {
        final String Table = "Transactions";
        // Need to Add Code to Download the data present in the Cloud

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        myDB_G.db_Handle_G_UD.execSQL("DELETE FROM " + Table + ";");
                        ErrMsgDialog("Cleanin "+ Table + " table Complete.\n");
                        Populate_Items_into_Table();
                        Trans_Canvas_Image.invalidate();
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
        final String Table = "Transactions";

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

    public void Populate_Items_into_Table() {
	        new Thread() {
            public void run() {
                Table_Display_Handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String Table = "Transactions";

                        Log.i (TAG, "Populate_Items_into_Table ========= = = = = = = = == = = = WhereCluase_Trans"  + WhereCluase_Trans);

                        Cursor OutputC = myDB_G.ReadData_from_DB_Table(Table, "_id,  id, Create_TS, Cust_Suplr,  Vehicle, Material,  Load_Val ,TareWeight , Met_Weight, Rate," +
                                "Total_Val ,  Ttl_Met_Val , Transport_Cost , GST_Val, CGST , SGST, IGST, Permit_Val , CFT_Val ," +
                                "Cash , Credit, TimeStamp, In_Out ,Empty , Payment, Quarry ,Site ,Transport , InvoiceNum, DC_Num , DC_Num_R," +
                                "Matched , User ,Permit_KG , Order_ID," +
                                "Col5 ,Col6 ,Col7 ,Col8 ,Col9 ,Col10 , Other_Details ", "", WhereCluase_Trans, "Create_TS Desc");

                        if (OutputC != null) {
                            String[] fromFieldNames1 = {"_id", "id", "Create_TS", "Cust_Suplr", "Vehicle", "Material", "Load_Val", "TareWeight", "Met_Weight", "Rate",
                                    "Total_Val", "Ttl_Met_Val", "Transport_Cost", "GST_Val", "CGST", "SGST", "IGST", "Permit_Val", "CFT_Val",
                                    "Cash", "Credit", "TimeStamp", "In_Out", "Empty", "Payment", "Quarry", "Site", "Transport", "InvoiceNum", "DC_Num", "DC_Num_R",
                                    "Matched", "User", "Permit_KG", "Order_ID",
                                    "Col5", "Col6", "Col7", "Col8", "Col9", "Col10", "Other_Details"};

                            ArrayList<HashMap<String, String>> val1 = new ArrayList<HashMap<String, String>>();

                            HashMap<String, String> val = new HashMap<String, String>();

                            int a1 = 0;
                            int a2 = fromFieldNames1.length;
                            for (; a1 < a2; a1++) {
                                val.put(fromFieldNames1[a1], fromFieldNames1[a1]);
                            }
                            val1.add(val);

                            int[] ToViewIDs = new int[]{R.id.Trans_L_id, R.id.Trans_R_id, R.id.Trans_Create_TS, R.id.Trans_Customer, R.id.Trans_Vehicle, R.id.Trans_Material,
                                    R.id.Trans_Load_Val, R.id.Trans_TareWeight, R.id.Trans_Mtl_Wt, R.id.Trans_Rate, R.id.Trans_Total_Val, R.id.Trans_Ttl_Met_Val, R.id.Trans_Transport_Cost,
                                    R.id.Trans_GST_Val, R.id.Trans_CGST, R.id.Trans_SGST, R.id.Trans_IGST, R.id.Trans_Permit_Val, R.id.Trans_CFT_Val, R.id.Trans_Cash, R.id.Trans_Credit,
                                    R.id.Trans_TimeStamp, R.id.Trans_In_Out, R.id.Trans_Empty, R.id.Trans_Payment,
                                    R.id.Trans_Quarry, R.id.Trans_D_Site, R.id.Trans_Transport, R.id.Trans_InvoiceNum, R.id.Trans_DC_Num,
                                    R.id.Trans_DC_Num_R, R.id.Trans_Matched, R.id.Trans_User, R.id.Trans_Permit_KG, R.id.Trans_Order_ID,
                                    R.id.Trans_COl5, R.id.Trans_Col6, R.id.Trans_Col7, R.id.Trans_Col8, R.id.Trans_Col9, R.id.Trans_Col10, R.id.Trans_Other_Details};

                            SimpleAdapter k1 = new SimpleAdapter(getBaseContext(), val1, R.layout.transactions_inner, fromFieldNames1, ToViewIDs);

                            ListView myTransactions_Items1 = (ListView) findViewById(R.id.Trans_Header);
                            myTransactions_Items1.setAdapter(k1);

                            SimpleCursorAdapter myCursorAdapter;
                            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.transactions_inner, OutputC, fromFieldNames1, ToViewIDs, 0);

                            ListView myTransactions_Items = (ListView) findViewById(R.id.Trans_Items_List);
                            myTransactions_Items.setAdapter(myCursorAdapter);
                        }
                        Trans_Canvas_Image.invalidate();
                    }
                });
            }
        }.start();
    }
}
