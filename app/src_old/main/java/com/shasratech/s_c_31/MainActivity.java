package com.shasratech.s_c_31;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabItem;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Button;
import android.provider.Settings;
import android.provider.Settings.System;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.DBHelperAppUtils.Get_Var_Val_from_App_SysConf;
import static com.shasratech.s_c_31.Globals.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean MainActivity_Started = false;
    Toolbar toolbar = null;
    Globals myGLobals = null;
    MySpinner LocationChooser = null;

    static TextView Summary_Type_View = null;
    static TextView Cash_Type_View = null;
    static TextView Credit_Type_View = null;
    static TextView Purchase_Type_View = null;
    static Button Alarms_Button = null;
    static TextView daily_Status = null;

    static TextView TE_Summary_Profit_Loss = null;
    static TextView TE_Summary_Production_Cost = null;
    static TextView TE_Summary_Total_Sales = null;
    static TextView TE_Summary_Cash_Sales = null;
    static TextView TE_Summary_Credit_Sales = null;
    static TextView TE_Summary_Total_Purchases = null;
    static TextView TE_Summary_Credit_Purchases = null;
    static TextView TE_Summary_Cash_Purchases = null;
    static TextView TE_Summary_Inven_Purchases = null;
    static TextView TE_Summary_Inventory_Consumption = null;
    static TextView TE_Summary_Ttl_Open_Bal = null;
    static TextView TE_Summary_Ttl_Sales2 = null;
    static TextView TE_Summary_Ttl_Collection = null;
    static TextView TE_Summary_Ttl_Payments = null;
    static TextView TE_Summary_Ttl_Expenses = null;
    static TextView TE_Summary_Ttl_in_Hand = null;
    static TextView TE_Summary_Ttl_Ttl_in_Hand = null;
    static TextView TE_Summary_Cash_Open_Bal = null;
    static TextView TE_Summary_Cash_Sales2 = null;
    static TextView TE_Summary_Cash_Collection = null;
    static TextView TE_Summary_Cash_Payments = null;
    static TextView TE_Summary_Cash_Expenses = null;
    static TextView TE_Summary_Cash_in_Hand = null;
    static TextView TE_Summary_Ttl_Cash_in_Hand = null;
    static TextView TE_Summary_Bank_Open_Bal = null;
    static TextView TE_Summary_Bank_Sales = null;
    static TextView TE_Summary_Bank_Collection = null;
    static TextView TE_Summary_Bank_Payments = null;
    static TextView TE_Summary_Bank_Expenses = null;
    static TextView TE_Summary_Bank_in_Hand = null;
    static TextView TE_Summary_Ttl_Bank_in_Hand = null;

    public static final String TAG = "MainActivity";
    int Main_Page_Version_Image_x = 0;
    int Main_Page_Version_Image_y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!MainActivity_Started) {
            MainActivity_Started = true;
            Init_All();

            Summary_Type_View = findViewById(R.id.Summary_Type_View);
            Cash_Type_View = findViewById(R.id.Cash_Type_View);
            Credit_Type_View = findViewById(R.id.Credit_Type_View);
            Purchase_Type_View = findViewById(R.id.Purchase_Type_View);
            Alarms_Button = findViewById(R.id.Alarms_Button);
            daily_Status = findViewById (R.id.Daily_Status);

            TE_Summary_Profit_Loss = findViewById (R.id.TE_Summary_Profit_Loss);
            TE_Summary_Production_Cost = findViewById (R.id.TE_Summary_Production_Cost);

            TE_Summary_Total_Sales = findViewById (R.id.TE_Summary_Total_Sales);
            TE_Summary_Cash_Sales = findViewById (R.id.TE_Summary_Cash_Sales);
            TE_Summary_Credit_Sales = findViewById (R.id.TE_Summary_Credit_Sales);

            TE_Summary_Total_Purchases = findViewById (R.id.TE_Summary_Total_Purchases);
            TE_Summary_Credit_Purchases = findViewById (R.id.TE_Summary_Credit_Purchases);
            TE_Summary_Cash_Purchases = findViewById (R.id.TE_Summary_Cash_Purchases);
            TE_Summary_Inven_Purchases = findViewById (R.id.TE_Summary_Inven_Purchases);
            TE_Summary_Inventory_Consumption = findViewById (R.id.TE_Summary_Inventory_Consumption);

            TE_Summary_Ttl_Open_Bal = findViewById (R.id.TE_Summary_Ttl_Open_Bal);
            TE_Summary_Ttl_Sales2 = findViewById (R.id.TE_Summary_Ttl_Sales2);
            TE_Summary_Ttl_Collection = findViewById (R.id.TE_Summary_Ttl_Collection);
            TE_Summary_Ttl_Payments = findViewById (R.id.TE_Summary_Ttl_Payments);
            TE_Summary_Ttl_Expenses = findViewById (R.id.TE_Summary_Ttl_Expenses);
            TE_Summary_Ttl_in_Hand = findViewById (R.id.TE_Summary_Ttl_in_Hand);
            TE_Summary_Ttl_Ttl_in_Hand = findViewById (R.id.TE_Summary_Ttl_Ttl_in_Hand);

            TE_Summary_Cash_Open_Bal = findViewById (R.id.TE_Summary_Cash_Open_Bal);
            TE_Summary_Cash_Sales2 = findViewById (R.id.TE_Summary_Cash_Sales2);
            TE_Summary_Cash_Collection = findViewById (R.id.TE_Summary_Cash_Collection);
            TE_Summary_Cash_Payments = findViewById (R.id.TE_Summary_Cash_Payments);
            TE_Summary_Cash_Expenses = findViewById (R.id.TE_Summary_Cash_Expenses);
            TE_Summary_Cash_in_Hand = findViewById (R.id.TE_Summary_Cash_in_Hand);
            TE_Summary_Ttl_Cash_in_Hand = findViewById (R.id.TE_Summary_Ttl_Cash_in_Hand);

            TE_Summary_Bank_Open_Bal = findViewById (R.id.TE_Summary_Bank_Open_Bal);
            TE_Summary_Bank_Sales = findViewById (R.id.TE_Summary_Bank_Sales);
            TE_Summary_Bank_Collection = findViewById (R.id.TE_Summary_Bank_Collection);
            TE_Summary_Bank_Payments = findViewById (R.id.TE_Summary_Bank_Payments);
            TE_Summary_Bank_Expenses = findViewById (R.id.TE_Summary_Bank_Expenses);
            TE_Summary_Bank_in_Hand = findViewById (R.id.TE_Summary_Bank_in_Hand);
            TE_Summary_Ttl_Bank_in_Hand = findViewById (R.id.TE_Summary_Ttl_Bank_in_Hand);

            RB_Daily_onClick(null);
        }
    }

    private void Init_All() {
        my_MainActivity = this;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        MainWindow_Width = size.x;
        MainWindow_Height = size.y;

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        MainLooper mainLooper = new MainLooper();

        LocationChooser = findViewById(R.id.CB_Main_LocationChooser);
        LocationChooser_Update(true);
        LocationChooser.SetSelectedItem(FBAppCustomer);

        LocationChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
                FBAppCustomer = LocationChooser.getSelectedItemI();
                Log.i (TAG, "FBAppCustomer =" + FBAppCustomer);

                boolean found_myDB_G = false;
                Log.i (TAG, "91 1234567890 myDB_G.Get_DB_Name = " + myDB_G.Get_DB_Name() + ", myDB_G =" + myDB_G);
                for (DBHelperUserData_ref t: DBHelperUserData_Ref ) {
                    DBHelperUserData t1 = t.getMyDB(FBAppCustomer);
                    if (t1 != null) {
                        myDB_G = t1;
                        //Log.i (TAG, "95 1234567890 myDB_G.Get_DB_Name = " + myDB_G.Get_DB_Name());
                        found_myDB_G = true;
                        break;
                    }
                }
                if (!found_myDB_G) {
                    myDB_G = new DBHelperUserData(myGContext_Global, FBAppName, FBAppCustomer);
                    DBHelperUserData_ref t = new DBHelperUserData_ref(FBAppCustomer, myDB_G);
                    DBHelperUserData_Ref.add(t);
                }

                myDBAppUtils.Init_Variables(FBAppCustomer);
                Configuration_Changed = true;
            }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }

        });

        ScrollView scrollView = findViewById(R.id.SCView_Daily);
        LinearLayout LL1 = findViewById(R.id.Main_Page_Version_Image);


        ViewGroup.LayoutParams params1 = LL1.getLayoutParams();
        Main_Page_Version_Image_x = params1.width;
        Main_Page_Version_Image_y = params1.height;


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
              //  Log.i (TAG, "New X = " + scrollX + ", new Y =" + scrollY + ", Old X = " + oldScrollX + ", old Y = " + oldScrollY);
                LinearLayout LL = findViewById(R.id.Main_Page_Version_Image);

                ViewGroup.LayoutParams params = LL.getLayoutParams();

                if (scrollY > oldScrollY) {
                    // Hide Main_Page_Version_Image
                    Hide_Main_Page_Version_Image();
                } else if ((oldScrollY > scrollY) && (scrollY == 0)) {
                    // Show Main_Page_Version_Image
                    LL.setVisibility(View.VISIBLE);
                    params.width = Main_Page_Version_Image_x;
                    params.height = Main_Page_Version_Image_y;
                    LL.setLayoutParams(params);
                }
            }
        });

        Alarms_Button = findViewById(R.id.Alarms_Button);
        /* if (Alarms_Button != null) {
            ViewGroup.LayoutParams params = Alarms_Button.getLayoutParams();
            params.width = 0;
            params.height = 0;
            Alarms_Button.setLayoutParams(params);
            Alarms_Button.setVisibility(View.GONE);
            Alarms_Button.setVisibility(View.INVISIBLE);
        } */

        mainLooper.start();
    }
    public void Hide_Main_Page_Version_Image() {
        LinearLayout LL = findViewById(R.id.Main_Page_Version_Image);
        ViewGroup.LayoutParams params = LL.getLayoutParams();

        LL.setVisibility(View.INVISIBLE);
        params.width = 0;
        params.height = 0;
        LL.setLayoutParams(params);

    }

    class MainLooper extends  Thread {

        MainLooper () {
            myGLobals =new Globals();
            myGLobals.Init_Globals_Context_from_MainActivity(MainActivity .this);
            myGLobals.Init_Globals_Variables_from_MainActivity();
        }

        @Override
        public void run () {
            FB_Main_Worker FB_main_worker = new FB_Main_Worker();
            FB_main_worker.start();
            while (true) {
                if(!READ_PHONE_STATE_PERM_GRANTED &&!Permission_Requested) {
                    if ((ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                android.Manifest.permission.READ_PHONE_STATE)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else if (!Permission_Requested) {
                            // No explanation needed; request the permission
                            Permission_Requested = true;
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                        return;
                    } else {
                        READ_PHONE_STATE_PERM_GRANTED = true;
                    }
                }

                if (READ_PHONE_STATE_PERM_GRANTED && ((Device_IMEI_ID == null) || (Device_IMEI_ID.isEmpty()))) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(myGContext_Global.TELEPHONY_SERVICE);
                    Device_IMEI_ID = telephonyManager.getDeviceId();

                    if ((Device_IMEI_ID == null) &&  android.os.Build.VERSION.SDK_INT >= 26) {
                        Device_IMEI_ID=telephonyManager.getImei();
                    }

                    if (Device_IMEI_ID == null) {
                        Device_IMEI_ID = Get_Var_Val_from_App_SysConf("System", "Device_IMEI_ID");
                    }
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void LocationChooser_Update (boolean force_Update) {
        List<String> res = new ArrayList<String>();
        myDBAppUtils.Get_Val_from_DB_AppUtils("App_SysConf", "Var_Val", "", "Tab = 'FBAppCustomer'", res, "Var_Val");

        final String[] items_Arr = List2_Array(res);
        LocationChooser.SetItems(items_Arr);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(this, Configuration.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {

            case R.id.nav_trans_act:
                Intent i = new Intent(MainActivity.this, Transactions.class);
                startActivity(i);
                break;

            case R.id.nav_Money_Mgmt:
                Intent MoneyTrans = new Intent(MainActivity.this, MoneyTransaction.class);
                startActivity(MoneyTrans);
                break;


            case R.id.nav_Custom_Rate:
                Intent Custom_Rate = new Intent(MainActivity.this, Custom_Rate.class);
                startActivity(Custom_Rate);
                break;


            case R.id.nav_Inventory:
                Intent Inventory = new Intent(MainActivity.this, Inventory.class);
                startActivity(Inventory);
                break;


            case R.id.nav_Material:
                Intent Material = new Intent(MainActivity.this, Material.class);
                startActivity(Material);
                break;

            case R.id.nav_Person:
                Intent Person = new Intent(MainActivity.this, Person.class);
                startActivity(Person);
                break;
            case R.id.nav_IRC_ELogs:
                Intent IRC_ELogs = new Intent(MainActivity.this, IRC_ELogs.class);
                startActivity(IRC_ELogs);
                break;
            case R.id.nav_HomeShed:
                Intent HomeShed = new Intent(MainActivity.this, HomeShed.class);
                startActivity(HomeShed);
                break;

            case R.id.nav_TrainDetails:
                Intent TrainDetails = new Intent(MainActivity.this, TrainDetails.class);
                startActivity(TrainDetails);
                break;


            case R.id.nav_Location:
                Intent Location = new Intent(MainActivity.this, Location.class);
                startActivity(Location);
                break;

            case R.id.nav_Vehicle:
                Intent Vehicle = new Intent(MainActivity.this, Vehicle.class);
                startActivity(Vehicle);
                break;

            case R.id.nav_AutoSave:
                Intent AutoSave = new Intent(MainActivity.this, AutoSaveTrans.class);
                startActivity(AutoSave);
                break;


            case R.id.nav_App_Config:
                Intent iConfig = new Intent(MainActivity.this, Configuration.class);
                startActivity(iConfig);
                break;

            case R.id.nav_CustomerDetails:
                Intent customerDetails = new Intent(MainActivity.this, CustomerDetails.class);
                startActivity(customerDetails);
                break;

            case R.id.nav_SalesComp1:
                Intent salesComp1 = new Intent(MainActivity.this, SellerComp.class);
                startActivity(salesComp1);
                break;
            /*case R.id.nav_KMBooking:
                Intent KMBooking = new Intent(MainActivity.this, KMBookings.class);
                startActivity(KMBooking);
                break; */
            case R.id.nav_Accounts:
                Intent Account = new Intent(MainActivity.this, Account.class);
                startActivity(Account);
                break;
            case R.id.nav_Site:
                Intent Site = new Intent(MainActivity.this, Site.class);
                startActivity(Site);
                break;

            case R.id.nav_transport_act:
                Intent Transport = new Intent(MainActivity.this, Transport.class);
                startActivity(Transport);
                break;

            case R.id.nav_RFID_Access:
                Intent RFID_Access = new Intent(MainActivity.this, RFID_Access.class);
                startActivity(RFID_Access);
                break;

            case R.id.nav_RFID_Insert:
                Intent RFID_Insert = new Intent(MainActivity.this, RFID_Insert.class);
                startActivity(RFID_Insert);
                break;

            case R.id.nav_Grant_Access:
                Intent Grant_Access = new Intent(MainActivity.this, Grant_Access.class);
                startActivity(Grant_Access);
                break;

            case R.id.nav_Alarms_Check:
                Intent Alarms_Check = new Intent(MainActivity.this, Alarms_Check.class);
                startActivity(Alarms_Check);
                break;

            case R.id.nav_ExpenseType:
                Intent ExpenseType = new Intent(MainActivity.this, ExpenseType.class);
                startActivity(ExpenseType);
                break;

                /* case R.id.nav_Dialer:
                Intent Dialer = new Intent(MainActivity.this, Dialer.class);
                startActivity(Dialer);
                break; */
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void Alarm_Button_onClick (View v) {
    }


    public static void Update_Change_List () {
        Summary_Chart_Type_Changed = true;
        Cash_Chart_Type_Changed = true;
        Credit_Chart_Type_Changed = true;
        Purchase_Chart_Type_Changed = true;
        Customer_Chart_Type_Changed = true;
        Purchase1_Chart_Type_Changed = true;
        Inven_Purchase_Chart_Type_Changed = true;
        Inven_Consumption_Chart_Type_Changed = true;
    }

    int tv_width = 0;
    public void RB_Daily_onClick (View v) {
        main_chart_selection = Main_Chart_Selection.MCS_Daily;
        Update_Change_List();
        Update_Summary_TEs();
    }

    public void RB_Monthly_onClick (View v) {
        main_chart_selection = Main_Chart_Selection.MCS_Monthly;
        Update_Change_List();
        Hide_Main_Page_Version_Image();
        Update_Summary_TEs();
    }

    public void RB_Yearly_onClick (View v) {
        main_chart_selection = Main_Chart_Selection.MCS_Yearly;
        Update_Change_List();
        Hide_Main_Page_Version_Image();
        Update_Summary_TEs();
    }

    public void RB_Complete_onClick (View v) {
        main_chart_selection = Main_Chart_Selection.MCS_Complete;
        Update_Change_List();
        Hide_Main_Page_Version_Image();
        Update_Summary_TEs();
    }

    public static void Summary_Type_View_set_Text (String text) {
        Summary_Type_View.setText(text);
    }

    public static void Cash_Type_View_set_Text (String text) {
        Cash_Type_View.setText(text);
    }

    public static void Credit_Type_View_set_Text (String text) {
        Credit_Type_View.setText(text);
    }

    public static void Purchase_Type_View_set_Text (String text) {
        Purchase_Type_View.setText(text);
    }

    public static void Update_Summary_TEs() {
        String From_TS = "";
        String To_TS = Get_Current_Date_Time(System_Date_Format) + " 23:59:59";
        switch (main_chart_selection) {
            case MCS_Daily: {
                daily_Status.setText("Daily Status");
                From_TS = To_TS.substring(0,11) + "00:00:00";
            }
            break;
            case MCS_Monthly: {
                daily_Status.setText("Monthly Status");
                From_TS = To_TS.substring(0,8) + "01 00:00:00";
            }
            break;
            case MCS_Yearly: {
                daily_Status.setText("Yearly Status");
                From_TS = To_TS.substring(0,5) + "01-01 00:00:00";
            }
            break;
            case MCS_Complete: {
                daily_Status.setText("Complete Status");
                From_TS = "2000-01-01 00:00:00 ";
            }
            break;
        }

        String Selection = String.format("Create_TS >= '%s' and Create_TS <= '%s' ", From_TS, To_TS);
        //========== Sales ===================
        double Cash_Sales =   String2Double2P(myDB_G.Get_Val_from_DB_UD("Transactions", "Total_Val", "SUM", String.format("%s and Payment = 'CASH' and In_Out = 'SALE'", Selection)));
        TE_Summary_Cash_Sales.setText(Double2String2P_RupeeFormat(Cash_Sales) + " \u20B9");

        double Credit_Sales = String2Double2P(myDB_G.Get_Val_from_DB_UD("Transactions", "Total_Val", "SUM", String.format("%s and Payment = 'CREDIT' and In_Out = 'SALE'", Selection)));
        TE_Summary_Credit_Sales.setText(Double2String2P_RupeeFormat(Credit_Sales) + " \u20B9");

        TE_Summary_Total_Sales.setText(Double2String2P_RupeeFormat(Cash_Sales + Credit_Sales) + " \u20B9");


        //=========== Purchases ==================
        double Cash_Purchases =   String2Double2P(myDB_G.Get_Val_from_DB_UD("Transactions", "Total_Val", "SUM", String.format("%s and Payment = 'CASH' and In_Out = 'PURCHASE'", Selection)));
        TE_Summary_Cash_Purchases.setText(Double2String2P_RupeeFormat(Cash_Purchases) + " \u20B9");

        double Credit_Purchases = String2Double2P(myDB_G.Get_Val_from_DB_UD("Transactions", "Total_Val", "SUM", String.format("%s and Payment = 'CREDIT' and In_Out = 'PURCHASE'", Selection)));
        TE_Summary_Credit_Purchases.setText(Double2String2P_RupeeFormat(Credit_Purchases) + " \u20B9");

        TE_Summary_Total_Purchases.setText(Double2String2P_RupeeFormat(Cash_Purchases + Credit_Purchases) + " \u20B9");

        //=========== Inventory ==================
        String Selection_In = Selection.replace("Create_TS", "Create_Time");
        double Inventory_Purchase = String2Double2P(myDB_G.Get_Val_from_DB_UD("Inventory", "Rate * Init_Quantity", "SUM", String.format("%s and In_Out = 'IN'", Selection_In)));
        TE_Summary_Inven_Purchases.setText(Double2String2P_RupeeFormat(Inventory_Purchase) + " \u20B9");

        double Inventory_Consumption = String2Double2P(myDB_G.Get_Val_from_DB_UD("Inventory", "Rate * Remain_Quantity", "SUM", String.format("%s and In_Out = 'OUT'", Selection_In)));
        TE_Summary_Inventory_Consumption.setText(Double2String2P_RupeeFormat(Inventory_Consumption) + " \u20B9");


        //=========== Cash Section ==================
        double Cash_In_Hand_F_TS = String2Double2P(Calculate_Cash_In_Hand(From_TS));
        TE_Summary_Cash_Open_Bal.setText(Double2String2P_RupeeFormat(Cash_In_Hand_F_TS) + " \u20B9");

        TE_Summary_Cash_Sales2.setText(Double2String2P_RupeeFormat(Cash_Sales) + " \u20B9");

        double Cash_Collection = String2Double2P(myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", String.format("%s and Type = 'Cash' and To_Ac = '%s'", Selection, CompanyName)));
        TE_Summary_Cash_Collection.setText(Double2String2P_RupeeFormat(Cash_Collection) + " \u20B9");

        double Cash_Payments = String2Double2P(myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", String.format("%s and Type = 'Cash' and From_Ac = '%s'", Selection, CompanyName)));
        TE_Summary_Cash_Payments.setText(Double2String2P_RupeeFormat(Cash_Payments) + " \u20B9");

        double Cash_Expenses = String2Double2P(myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", String.format("%s and Type = 'Cash_Expense'", Selection)));
        TE_Summary_Cash_Expenses.setText(Double2String2P_RupeeFormat(Cash_Expenses) + " \u20B9");

        double Cash_in_Hand = Cash_Sales + Cash_Collection - Cash_Payments - Cash_Expenses;
        TE_Summary_Cash_in_Hand.setText(Double2String2P_RupeeFormat(Cash_in_Hand) + " \u20B9");

        TE_Summary_Ttl_Cash_in_Hand.setText(Double2String2P_RupeeFormat(Cash_in_Hand + Cash_In_Hand_F_TS) + " \u20B9");
        
        
        //=========== Bank Section ==================
        double Bank_In_Hand_F_TS = String2Double2P(Calculate_Bank_Balance(From_TS));
        TE_Summary_Bank_Open_Bal.setText(Double2String2P_RupeeFormat(Bank_In_Hand_F_TS) + " \u20B9");

        double Bank_Sales =   String2Double2P(myDB_G.Get_Val_from_DB_UD("Transactions", "Total_Val", "SUM", String.format("%s and Payment = 'BANK_RTGS' and In_Out = 'SALE'", Selection)));
        TE_Summary_Bank_Sales.setText(Double2String2P_RupeeFormat(Bank_Sales) + " \u20B9");

        double Bank_Collection = String2Double2P(myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", String.format("%s and Type = 'Bank' and To_Ac = '%s'", Selection, CompanyName)));
        TE_Summary_Bank_Collection.setText(Double2String2P_RupeeFormat(Bank_Collection) + " \u20B9");

        double Bank_Payments = String2Double2P(myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", String.format("%s and Type = 'Bank' and From_Ac = '%s'", Selection, CompanyName)));
        TE_Summary_Bank_Payments.setText(Double2String2P_RupeeFormat(Bank_Payments) + " \u20B9");

        double Bank_Expenses = String2Double2P(myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", String.format("%s and Type = 'Bank_Expense'", Selection)));
        TE_Summary_Bank_Expenses.setText(Double2String2P_RupeeFormat(Bank_Expenses) + " \u20B9");

        double Bank_in_Hand = Bank_Sales + Bank_Collection - Bank_Payments - Bank_Expenses;
        TE_Summary_Bank_in_Hand.setText(Double2String2P_RupeeFormat(Bank_in_Hand) + " \u20B9");

        TE_Summary_Ttl_Bank_in_Hand.setText(Double2String2P_RupeeFormat(Bank_in_Hand + Bank_In_Hand_F_TS) + " \u20B9");


        //=========== Total Section ==================
        TE_Summary_Ttl_Open_Bal.setText(Double2String2P_RupeeFormat(Bank_In_Hand_F_TS + Cash_In_Hand_F_TS) + " \u20B9");
        TE_Summary_Ttl_Sales2.setText(Double2String2P_RupeeFormat(Bank_Sales + Cash_Sales) + " \u20B9");
        TE_Summary_Ttl_Collection.setText(Double2String2P_RupeeFormat(Bank_Collection + Cash_Collection) + " \u20B9");
        TE_Summary_Ttl_Payments.setText(Double2String2P_RupeeFormat(Bank_Payments + Cash_Payments) + " \u20B9");
        TE_Summary_Ttl_Expenses.setText(Double2String2P_RupeeFormat(Bank_Expenses + Cash_Expenses) + " \u20B9");
        TE_Summary_Ttl_in_Hand.setText(Double2String2P_RupeeFormat(Bank_in_Hand + Cash_in_Hand) + " \u20B9");
        TE_Summary_Ttl_Ttl_in_Hand.setText(Double2String2P_RupeeFormat(Bank_in_Hand + Cash_in_Hand + Bank_In_Hand_F_TS + Cash_In_Hand_F_TS) + " \u20B9");

        //========== Profit/Loss ====================
        double Profit = Cash_Sales + Bank_Sales + Cash_Collection + Bank_Collection - Cash_Payments - Bank_Payments - Cash_Expenses - Bank_Expenses ;
        TE_Summary_Profit_Loss.setText(Double2String2P_RupeeFormat(Profit) + " \u20B9");
        if (Profit > 0) {
            TE_Summary_Profit_Loss.setBackgroundColor(Color.GREEN);
            TE_Summary_Profit_Loss.setTextColor(Color.BLUE);
        } else {
            TE_Summary_Profit_Loss.setBackgroundColor(Color.RED);
            TE_Summary_Profit_Loss.setTextColor(Color.WHITE);
        }


        //========== Production Cost/Ton ====================
        String Sales_KGs = myDB_G.Get_Val_from_DB_UD("Transactions", "Met_Weight", "SUM", Selection + " and In_Out = 'SALE'");
        double Sales_Tons = String2Double2P(Sales_KGs);
        Sales_Tons /= 1000;


        String Raw_KGs = myDB_G.Get_Val_from_DB_UD("Transactions", "Met_Weight", "SUM", Selection + " and In_Out = 'PURCHASE'");
        double Raw_Tons = String2Double2P(Raw_KGs);
        Raw_Tons /= 1000;

        int Raw_Wastage_Pct = 15;
        double Met_Stock = (Raw_Tons - Sales_Tons) * (100 - Raw_Wastage_Pct)/100;

        double Material_Tons = Sales_Tons;
        if (Met_Stock > 0) Material_Tons += Met_Stock;
        if (Material_Tons == 0) Material_Tons = 1;

        double Raw_Met_Cost = Cash_Purchases + Credit_Purchases;

        double Ttl_Expense = Cash_Expenses + Bank_Expenses;
        double Expenses = (Ttl_Expense + Raw_Met_Cost + Inventory_Consumption);
        if (Expenses == 0) Expenses = 1;

        double Prod_Cost_per_Ton = Expenses/Material_Tons;
        TE_Summary_Production_Cost.setText(Double2String2P_RupeeFormat(Prod_Cost_per_Ton) + " \u20B9");

        double Expected_Prod_Cost_per_Ton = 200;
        if (Prod_Cost_per_Ton > Expected_Prod_Cost_per_Ton) {
            TE_Summary_Production_Cost.setBackgroundColor(Color.RED);
            TE_Summary_Production_Cost.setTextColor(Color.WHITE);
        } else {
            TE_Summary_Production_Cost.setBackgroundColor(Color.GREEN);
            TE_Summary_Production_Cost.setTextColor(Color.BLUE);
        }
    }

    public static String Calculate_Cash_In_Hand (String  From_TS) {
        String Sel = String.format("Create_TS >= '1970-01-01 00:00:00' and Create_TS <= '%s'", From_TS);

        String Cash_Sales = myDB_G.Get_Val_from_DB_UD("Transactions", "Total_Val", "SUM", Sel + " and Payment = 'CASH' and In_Out = 'SALE'");

        String Cash_Collection = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", Sel + "and  Type = 'Cash' and To_Ac = '" + CompanyName + "'");

        String Cash_Payments = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", Sel + "and  Type = 'Cash' and From_Ac = '" + CompanyName + "'");

        String Cash_Expense = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", Sel + "and  Type = 'Cash_Expense'");

        double Cash_In_Hand = (String2Double2P(Cash_Sales) + String2Double2P(Cash_Collection) - String2Double2P(Cash_Payments) - String2Double2P(Cash_Expense) );

        return Double2String2P(Cash_In_Hand);
    }

    public static String Calculate_Bank_Balance (String  From_TS) {
        String Sel = String.format("Create_TS >= '1970-01-01 00:00:00' and Create_TS <= '%s'", From_TS);

        String Bank_Sales = myDB_G.Get_Val_from_DB_UD("Transactions", "Total_Val", "SUM", Sel + " and Payment = 'BANK_RTGS' and In_Out = 'SALE'");

        String Bank_Collection = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", Sel + "and Type = 'Bank' and To_Ac = '" + CompanyName + "'");

        String Bank_Payments = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", Sel + "and  Type = 'Bank' and From_Ac = '" + CompanyName + "'");

        String Bank_Expense = myDB_G.Get_Val_from_DB_UD("MoneyTransaction", "Amount", "SUM", Sel + "and Type = 'Bank_Expense'");

        double Bank_Balance = (String2Double2P(Bank_Sales) + String2Double2P(Bank_Collection) - String2Double2P(Bank_Payments) - String2Double2P(Bank_Expense) );

        return Double2String2P(Bank_Balance);
    }
}
