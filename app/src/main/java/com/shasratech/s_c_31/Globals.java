package com.shasratech.s_c_31;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

//import com.google.android.gms.drive.DriveId;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.shasratech.s_c_31.Curr_Activity.*;
import static com.shasratech.s_c_31.DBHelperUserData.*;

//import static android.support.v4.content.ContextCompat.getSystemService;

//import static com.shasratech.s_c_31.GDD_Download_Files_BG.*;

/**
 * Created by srihari on 21/4/17.
 */

enum RQO_Return_Status {
    RQO_Unknown_Err,
    RQO_Duplicate_Err,
    RQO_Ignore_Duplicate,
    RQO_Input_Empty,
    RQO_Success;
}

enum Main_Chart_Selection {
    MCS_Daily,
    MCS_Monthly,
    MCS_Yearly,
    MCS_Complete
}

enum Curr_Activity {
    Act_Main,
    Act_FB_Main,
    Act_Conf,
    Act_AutoSaveTrans,
    Act_FB_Sec,
    Act_Material,
    Act_IRC_ELogs,
    Act_HomeShed,
    Act_Monitor,
    Act_CategorizedSolution,
    Act_CategorizedProblem,
    Act_Location,
    Act_TrainDetails,
    Act_DriverDetails,
    Act_LocoDetails,
    Act_Person,
    Act_Vehicle,
    Act_Site,
    Act_Transport,
    Act_Transactions,
    Act_MoneyTransaction,
    Act_SellerComp,
    Act_CustomerDetails,
    Act_Inventory,
    Act_Account,
    Act_RFID_Access,
    Act_RFID_Insert,
    Act_UpdateStatement,
    Act_AppUsers,
    Act_Alarms,
    Act_EmailConf,
    Act_ExpenseType,

    Act_None;
}

enum FB_Activity_Req {
    FB_Act_SignIn,
    FB_Act_SignOut,
    FB_Act_Email_ReVerify,
    FB_Act_Init,
    FB_Act_Unknown;
}

enum Trans_Update_Condition {
    TU_Trans_Cost, TU_Trans_New_Ac, TU_Trans_Old_Ac,
    TU_CS_Ttl, TU_CS_New_Ac, TU_CS_Old_Ac, TU_OB_New_Ac,
    TU_GST_Revert, TU_Permit_Revert,
    TU_Default
}

public class Globals {
    public static Main_Chart_Selection main_chart_selection = Main_Chart_Selection.MCS_Daily;
    private static final String TAG = "SC-MyGlobals";
    public static int Check_pref_Fragment = 0;

    public static boolean login_Completed = false;
    public static boolean Summary_Chart_Type_Changed = true;
    public static boolean DB_Summary_Chart_Data_Changed = true;

    public static boolean Cash_Chart_Type_Changed = true;
    public static boolean DB_Cash_Chart_Data_Changed = true;

    public static boolean Credit_Chart_Type_Changed = true;
    public static boolean DB_Credit_Chart_Data_Changed = true;

    public static boolean Purchase_Chart_Type_Changed = true;
    public static boolean DB_Purchase_Chart_Data_Changed = true;

    public static boolean Customer_Chart_Type_Changed = true;
    public static boolean DB_Customer_Chart_Data_Changed = true;

    public static boolean Purchase1_Chart_Type_Changed = true;
    public static boolean DB_Purchase1_Chart_Data_Changed = true;

    public static boolean Inven_Purchase_Chart_Type_Changed = true;
    public static boolean DB_Inven_Purchase_Chart_Data_Changed = true;

    public static boolean Inven_Consumption_Chart_Type_Changed = true;
    public static boolean DB_Inven_Consumption_Chart_Data_Changed = true;

    public static String CompanyName = "ABCD";

    public static Curr_Activity curr_activity = Act_None;
    public static FB_Activity_Req fb_activity_req = FB_Activity_Req.FB_Act_Init;

    public static MainActivity my_MainActivity;
    public static String Current_Site = "Site1";
    public static Context myGContext_Global;
    public static List<DBHelperUserData_ref> DBHelperUserData_Ref = new ArrayList<>();
    public static DBHelperUserData myDB_G = null;
    public static DBHelperAppUtils myDBAppUtils = null;
    public static boolean FB_N_Activity_Started = false;
    public static boolean Configuration_Changed = false;

    public static String FBAppName = "";
    public static String FBAppCustomer = "";
    public static String FBAppMBName = "";
    public static String FB_IMEI_Number = "";
    public final static String FB_User_Email_Main = "arun.ayachith@gmail.com";
    public final static String FB_User_Pass_Main = "asdf1234";
    public static boolean temp_Debug = false;
    public static boolean Global_Debug = false;
    public static boolean Trans_Audio_Notify = false;
    public static boolean MTrans_Audio_Notify = false;
    public static boolean Account_Audio_Notify = false;
    public static boolean CustomRate_Audio_Notify = false;
    public static boolean Inventory_Audio_Notify = false;
    public static boolean Person_Audio_Notify = false;
    public static boolean Material_Audio_Notify = false;
    public static boolean IRC_ELogs_Audio_Notify = false;
    public static boolean HomeShed_Audio_Notify = false;
    public static boolean Location_Audio_Notify = false;
    public static boolean TrainDetails_Audio_Notify = false;
    public static boolean DriverDetails_Audio_Notify = false;
    public static boolean LocoDetails_Audio_Notify = false;
    public static boolean Vehicle_Audio_Notify = false;
    public static boolean AutoSaveTrans_Audio_Notify = false;
    public static List<FB_Sec_Worker> FB_Sec_Worker_List = new ArrayList<>();

    public static boolean Valid_IMEI_Combination = false;
    public static  String[] Table_List = {
            "Appusers",
            "TrainDetails", "Location", "LocoDetails", "homeShed", "IRC_ELogs",
            "DriverDetails",
            //"Problems", "Solutions",
            //"Monitoring",
            "CategorizedSolution", "CategorizedProblem"};

    public static String Device_IMEI_ID = "";
    public static boolean FB_IMEI_Setup = false;
    public static boolean FB_Timer_Activity_Pending = false;
    public static boolean FB_Timer_Activity_Pending_Ret_Val = false;
    public static boolean FB_Timer_Activity_Pending_Action_Complete = false;
    public static boolean FB_IMEI_Max_Attempt_Failed = false;
    public static boolean LocationChooser_Updated_B = false;
    public static String WhereCluase_Trans = "";
    public static String WhereCluase_AutoSaveTrans = "";
    public static String WhereCluase_Account = "";
    public static String WhereCluase_MoneyTransaction = "";

    public static int[] myColors_int_Arr = null;
    //public static String Trans_Sel = "";
    public static String M_Trans_Sel = "";
    public static String Accounts_Sel = "";

    public static int MainWindow_Width = 0;
    public static int MainWindow_Height = 0;
    public static int Trans_Canvas_Height = 0;

    public static boolean Trans_Boolean_Bar_Chart_FullSize = true;

    public static String Today_Start_Date_Time_Format = "yyyy-MM-dd 00:00:00";
    public static String Today_End_Date_Time_Format = "yyyy-MM-dd 23:59:59";

    public static String System_Date_Time_Format = "yyyy-MM-dd HH:mm:ss";
    public static String System_Date_Time_Format_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String System_Date_Time_Format_3Mon = "yyyy-MMM-dd HH:mm:ss";
    public static String System_Date_Time_Format_3Mon_P = "dd/MMM/yyyy hh:mm:ss a";
    public static String File_Date_Time_Format_3Mon = "dd-MMM-yyyy-hh-mm-ss-a";
    public static String BarCode_Date_Time_Format = "ddMMHHmm";

    public static String MDPMDR_Date_Time_Format = "dd/MM/yyyy hh:mm:ss AP";

    public static String System_Date_Format = "yyyy-MM-dd";
    public static String System_Date_Format_3Mon = "yyyy-MMM-dd";
    public static String System_Date_Format_3Mon_P = "dd/MMM/yyyy";
    public static String System_Date_Format_3Mon_P_SMS = "dd/MMM/yy";

    public static String Print_Date_Format = "dd-MM-yyyy";
    public static String Print_Date_Format_3Mon = "dd-MMM-yyyy";
    public static String Print_Date_Time_Format_3Mon = "dd-MMM-yyyy hh:mm:ss a";
    public static String Print_Date_Time_Format_3Mon_No_AP = "dd-MMM-yyyy hh:mm:ss";
    public static String Print_Date_Time_Format_3Mon_MS = "dd-MMM-yyyy hh:mm:ss.SSS a";
    public static String System_Time_Format = "hh:mm:ss";
    public static String System_Time_Format_P_SMS = "hh:mm";
    public static String System_Time_Format_HMP = "hh:mm a";

    public static String AES_Secret_Key = "KNMG1@5HS_PaBs";
    public static List<MySemaphore> List_MySem_G = null;

    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static boolean READ_PHONE_STATE_PERM_GRANTED = false;
    public static boolean Permission_Requested = false;


    public static String FB_Client_ID_G = "";
    public static String FB_Current_Key_G = "";
    public static String FB_URL_G = "";
    public static String FB_MobileSDK_App_ID_G = "12313123hjk3hjg213";
    public static String FB_User_Email_Sec_G = "arun.ayachith@gmail.com";
    public static String FB_User_Pass_Sec_G = "asdf1234";

    public static boolean DownLoad_Complete_Table_from_Cloud_Act_CustomerDetails = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Material = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Person = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_SellerComp = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Transactions = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Site = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Vehicle = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_MoneyTransaction = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Person_Complete = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_IRC_ELogs = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_HomeShed = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Monitor = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_CategorizedProblem=false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_CategorizedSolution = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Location = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_TrainDetails = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_DriverDetails = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_LocoDetails = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_AutoSaveTrans = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Account = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_RFID_Access = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_RFID_Insert = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Transport = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_AppUsers = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Alarms = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_EmailConf = false;
    public static boolean DownLoad_Complete_Table_from_Cloud_Act_Expense = false;

    public static AES myAESHelper = null;


    public static List<UPload_Data_Msg> UPload_Data_Msg_List = null;



    public void on_Global_Home_PB_Click (View view) {
        Intent inent = new Intent(myGContext_Global, MainActivity.class);
        myGContext_Global.startActivity(inent);
    }

    public Globals () {
        List_MySem_G = new ArrayList<>();
        UPload_Data_Msg_List = new ArrayList<>();
        myAESHelper = new AES();
    }

    public static void Init_myColors_int_Arr () {
        myColors_int_Arr = new int[20];

        myColors_int_Arr[0] = Color.rgb(0x9c, 0x27, 0xB0);
        myColors_int_Arr[1] = Color.rgb(0xDC, 0x27, 0x2F);
        myColors_int_Arr[2] = Color.rgb(0x51, 0x2D, 0xAF);
        myColors_int_Arr[3] = Color.rgb(0x88, 0x0E, 0x4F);
        myColors_int_Arr[4] = Color.rgb(0x1A, 0x2E, 0x7F);

        myColors_int_Arr[5] = Color.rgb(0x4c, 0xA7, 0x40);
        myColors_int_Arr[6] = Color.rgb(0x7C, 0x77, 0x1F);
        myColors_int_Arr[7] = Color.rgb(0x33, 0x6D, 0x1F);
        myColors_int_Arr[8] = Color.rgb(0xE6, 0x51, 0x40);
        myColors_int_Arr[9] = Color.rgb(0x1A, 0x2E, 0x7F);


        myColors_int_Arr[10] = Color.rgb(0xAc, 0x87, 0x70);
        myColors_int_Arr[11] = Color.rgb(0xFC, 0x57, 0x1F);
        myColors_int_Arr[12] = Color.rgb(0x61, 0x7D, 0x8F);
        myColors_int_Arr[13] = Color.rgb(0xDD, 0x2C, 0x00);
        myColors_int_Arr[14] = Color.rgb(0x3E, 0x2E, 0x2F);

        myColors_int_Arr[15] = Color.rgb(0x21, 0x21, 0x21);
        myColors_int_Arr[16] = Color.rgb(0x5D, 0x40, 0x37);
        myColors_int_Arr[17] = Color.rgb(0x00, 0x8D, 0xEF);
        myColors_int_Arr[18] = Color.rgb(0x60, 0x7E, 0x6F);
        myColors_int_Arr[19] = Color.rgb(0x1A, 0x5E, 0x9F);

    }


    static int String2Int(String a) {

        if ((a == null) || (a.isEmpty())) {
            return 0;
        }
        try {
            return Integer.parseInt(a);
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }

    public static void Init_Globals_Context_from_MainActivity (Context myGContext) {
        myGContext_Global = myGContext;
        Init_myColors_int_Arr();
        String Curr_Date = Get_Current_Date_Time(System_Date_Format);
        WhereCluase_Trans = " Create_TS >= '" + Curr_Date + " 00:00:00' and Create_TS <= '" + Curr_Date + " 23:59:59' and Empty = 'NO' and In_Out = 'SALE'";
        WhereCluase_AutoSaveTrans = " TimeStamp >= '" + Curr_Date + " 00:00:00' and TimeStamp <= '" + Curr_Date + " 23:59:59' ";
        WhereCluase_Account = " TimeStamp >= '" + Curr_Date + " 00:00:00' and TimeStamp <= '" + Curr_Date + " 23:59:59' ";
    }

    public static void Init_Globals_Variables_from_MainActivity () {
        //Log.i (TAG, "================== Init_Globals_Variables_from_MainActivity ========== myDBAppUtils = " + myDBAppUtils + " , myDB_G = " + myDB_G);
        if (myDBAppUtils == null) {
            myDBAppUtils = new DBHelperAppUtils(myGContext_Global, "AppUtils", "AppUitls");
        }

        if (myDB_G == null) {
            if ((FBAppName == null) || (FBAppName.isEmpty())) FBAppName = "SoftCrusher";
            if ((FBAppCustomer == null) || (FBAppCustomer.isEmpty())) FBAppCustomer = "Demo1";
            myDB_G = new DBHelperUserData(myGContext_Global, FBAppName, FBAppCustomer);

            boolean found_myDB_G = false;
            //Log.i (TAG, "91 1234567890 myDB_G.Get_DB_Name = " + myDB_G.Get_DB_Name() + ", myDB_G =" + myDB_G);
            /* for (DBHelperUserData_ref t: DBHelperUserData_Ref ) {
                if (t.getmyDB_G(FBAppCustomer) != null) {
                    myDB_G = t.getmyDB_G(FBAppCustomer);
                    Log.i (TAG, "95 1234567890 myDB_G.Get_DB_Name = " + myDB_G.Get_DB_Name());
                    found_myDB_G = true;
                    break;
                }
            } */

            DBHelperUserData_ref t = new DBHelperUserData_ref(FBAppCustomer, myDB_G);
            //Log.i (TAG, "102 1234567890 myDB_G.Get_DB_Name = " + myDB_G.Get_DB_Name());
            DBHelperUserData_Ref.add(t);

            //Log.i (TAG, "105 1234567890 myDB_G.Get_DB_Name = " + myDB_G.Get_DB_Name());
           // Log.i (TAG, "1234567890 FBAppCustomer =" + FBAppCustomer + ", myDB_G = " + myDB_G);
           // ErrMsgDialog("FBAppCustomer =" + FBAppCustomer + ", myDB_G = " + myDB_G);
            //Log.i (TAG, "108 1234567890 myDB_G.Get_DB_Name = " + myDB_G.Get_DB_Name());

            myDBAppUtils.Init_Variables(FBAppCustomer);
            Configuration_Changed = true;
        }
    }

    public static String getCurrentTimeStamp(String format_I) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format_I);//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String getCurrentTimeStamp() {
        String format = "dd-MM-yyyy HH:mm:ss";
        return getCurrentTimeStamp(format);
    }

    public static void ErrMsgDialog (String Msg) {
        ShowMyToast(Msg, Toast.LENGTH_SHORT);
    }

    public static void ErrMsgDialog_L (String Msg) {
        ShowMyToast(Msg, Toast.LENGTH_LONG);
    }

    public static void ShowMyToast (final String text, final int duration) {
        my_MainActivity.runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                Toast toast = Toast.makeText(my_MainActivity.getApplicationContext(), text, duration);
                View view = toast.getView();
                view.setPadding(20,10,20,30);
                //view.setBackgroundColor(Color.RED);
                view.setBackgroundColor(Color.argb(0xFF, 0xFf, 0xA0, 0xcd));

                ////view.getBackground().setColorFilter(Color.BLACK , PorterDuff.Mode.SRC_IN);

                //toast.setGravity(Gravity.TOP, 0, 0);
                //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                // https://stackoverflow.com/questions/11288475/custom-toast-on-android-a-simple-example

                TextView text = view.findViewById(android.R.id.message);
                text.setPadding(20, 10,20,30);
                text.setTextColor(Color.BLUE);
                //text.setShadowLayer(10, 0, 0, Color.DKGRAY);
                //text.setTextSize(2 + Integer.valueOf(getResources().getString(R.string.text_size)));
                text.setTextSize((float) 22.0);
                toast.show();
            }
        });
    }

    public static void ShowMyToast (Context context, String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void ShowMyToast (Context context, String text) {
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
       // Log.i ("SoftCrusher", text);
    }




    public static void Switch2MainActivity (Context context) {
        Intent inent = new Intent(context, MainActivity.class);
        context.startActivity(inent);
    }

    public static String DateFormat3Mon = "dd-MMM-yyyy";
    public static String DateFormatSQlite = "yyyy-MM-dd";
    public  static String TimeFormat = "kk:mm:ss";
    public static String DateFormat3MonT = DateFormat3Mon + " " + TimeFormat;
    public static String DateFormatSqliteT = DateFormatSQlite + " " + TimeFormat;

    public static long DaysSinceDB = 0;

    public static void Get_Yes_No_Dialog (Context context, String Question) {
        String Title = "";
        Get_Yes_No_Dialog (context,  Question, Title);
    }

    public static void Get_Yes_No_Dialog (Context context, String Question, String Title) {
        String Yes = "Yes";
        String No = "No";
        String Alternate = "";
        Get_Yes_No_Dialog ( context,  Question,  Title,  Yes,  No,  Alternate);

    }
    public static void Get_Yes_No_Dialog (Context context, String Question, String Title, String Yes, String No, String Alternate) {
        myGContext_Global = context;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Change_of_Yes_No_Dialog_CurrInt(which);
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(myGContext_Global);

        if (!Alternate.isEmpty()) {
            builder.setNeutralButton(Alternate, dialogClickListener);
        }

        if (!Title.isEmpty()) {
            builder.setTitle(Title);
        }

        builder.setMessage(Question)
                .setPositiveButton(Yes, dialogClickListener)
                .setNegativeButton(No, dialogClickListener).show();
    }

    public static  void Change_of_Yes_No_Dialog_CurrInt (int which) {
        AppCompatActivity temp = null;

        // https://stackoverflow.com/questions/16508105/dialog-callback
        switch (curr_activity) {
            case Act_FB_Main:
                temp = (MainActivity) myGContext_Global;
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: Log.i (TAG, "Yes Clicked for the FB_N_Activity"); //temp.Work_Offline_Dialog_Yes_Handler(); break;
                    case DialogInterface.BUTTON_NEGATIVE: break;
                    case DialogInterface.BUTTON_NEUTRAL: break;
                    default: break;
                }
                break;
        }
    }

    public static void Play_Notification () {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(myGContext_Global, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static char Convert_from_Ascii (char a) {
        char x = a;
        if ((x >= 0x30) && (x <= 0x39)) {
            x -= 0x30;
        } else if ((x >= 0x41) && (x <= 0x46)) {
            x -= 0x41;
            x += 10;
        } else if ((x >= 0x61) && (x <= 0x66)) {
            x -= 0x61;
            x += 10;
        }
        return x;
    }

    public static String Convert_to_UniCode (String input) {
        int a = 0;
        int b = input.length();
        //b = 100;

        String Output = "";
        char[] input_char = input.toCharArray();
        String t11 = new String(input_char);
        for (; a< b; a++) {
            char j = input_char[a];

            if ( j == 0x25 /*  % */) {
                int x = (int) Convert_from_Ascii(input_char[++a]);
                int y = (int) Convert_from_Ascii(input_char[++a]);

                int z = (x << 4) | (y);

                if ((z <= 0x7F) && (z >= 0x20)) {
                    j = (char)z;
                    Output += j;
                    continue ; // Needs to be Checked
                } else {
                    if (z == 0xE0) {
                        int x1 = (int) input_char[++a];
                        int x2 = (int) input_char[++a];
                        int x3 = (int) input_char[++a];
                        int x4 = (int) input_char[++a];

                        if ((x1 == 0x25) && (x2 == 'B') && (x3 == '2') && (x4 == 0x25)) {
                            z = 0xC;
                            x = (int) Convert_from_Ascii(input_char[++a]);
                        } else if ((x1 == 0x25) && (x2 == 'B') && (x3 == '3') && (x4 == 0x25)) {
                            int x5 = (int) input_char[++a];
                            int x6 = (int) Convert_from_Ascii(input_char[++a]);
                            if ((x5 == '8')) {
                                int k2 = (0xCC<< 4) | x6;
                                char[] uni_c1 = Character.toChars(k2);
                                String uni_s1 = new String(uni_c1);
                                Output += uni_s1;
                                continue;
                            }
                        }
                    }

                    y = (int) Convert_from_Ascii(input_char[++a]);
                    int z1 = (x << 4) | (y);
                    int k = z << 8 | z1;

                    char[] uni_c = Character.toChars(k);
                    String uni_s = new String(uni_c);
                    Output += uni_s;
                }
            } else  if ((j <= 0x7F) && (j >= 0x20)) {
                Output += j;
            }
        }
        return Output;
    }

    //public static String System_Date_Time_Format = "yyyy-MM-dd HH:mm:ss";
    //public static String System_Date_Format = "yyyy-MM-dd";
    public static String User_Date_Format = "dd/MMM/yyyy";



    public static  String Get_Current_Date_Time() {
        Date myDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(System_Date_Time_Format);
        return ft.format(myDate);
    }


    public static String Get_Current_Date_Time_millis() {
        Date myDate = new Date();
        long millis = myDate.getTime();
        return Long.toString(millis);
    }

    public static String Get_Current_Date_Time(String format_I) {
        Date myDate = new Date();
        String format = format_I;
        if ((format == null) || (format.isEmpty())) {
            format = System_Date_Time_Format;
        }
        SimpleDateFormat ft = new SimpleDateFormat(format);
        return ft.format(myDate);
    }

    public static String Add_Secs_Time_String(String in_time, int secs) {
        return Add_Secs_Time_String(in_time, secs, System_Date_Time_Format);
    }

    public static String Add_Secs_Time_String(String in_time, int secs, String in_format) {
        String out_format = in_format;
        return Add_Secs_Time_String(in_time, secs, in_format, out_format);
    }

    public static String Add_Secs_Time_String(String in_time, int secs, String in_format, String out_format) {
        java.util.Date Max_TS_TD = new java.util.Date();
        try {
            Max_TS_TD = new SimpleDateFormat(in_format).parse(in_time);
        } catch (ParseException ex) {
            // Logger.getLogger(SQLiteHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(Max_TS_TD);
        cal.add(Calendar.SECOND, secs);
        Max_TS_TD = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat(out_format);
        String newDate = dateFormat.format(Max_TS_TD);
        return newDate;
    }

    public static String Extract_Key_from_Pair(String pair) {
        return Extract_Key_from_Pair( pair, "=");
    }

    public static String Extract_Key_from_Pair(String pair, String Separator) {
        String[] temp = pair.split(Separator);
        String key = temp[0];
        key = key.replace('\'', ' ');
        key = key.trim();

        return key;
    }

    public static String Extract_value_from_Pair(String pair) {
        return Extract_value_from_Pair( pair, "=");
    }

    public static String Extract_value_from_Pair(String pair, String Separator) {
        String[] temp = pair.split(Separator, -1);
        String val = temp[1];
        val = val.replace('\'', ' ');
        val = val.trim();

        return val;
    }

    //========================= To be Checked for correctness =====================================
    static double Double2Double2P ( double a) {
        DecimalFormat df = new DecimalFormat("#.##");
        double b = Double.valueOf(df.format(a));

        return b;

       // return 0;
    }

    static double String2Double2P(String a) {
        if ((a == null) || (a.isEmpty())) {
            return 0;
        }
        try {
            double x = Double.parseDouble(a);
            return Double2Double2P(x);
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }

    public static RQO_Return_Status Insert_into_account(String Curr_Account, double Amount_r, double Amount_p, String Details, String Ref, String Ref_id) {
        String Time_Stamp_i = Get_Current_Date_Time();
        boolean Expense = false;
        boolean Comp_OB = false;
        String SourceSite = "";

        return Insert_into_account( Curr_Account,  Amount_r,  Amount_p, Details,  Ref,  Ref_id, Time_Stamp_i, Expense,  Comp_OB,  SourceSite);
    }

    public static RQO_Return_Status Insert_into_account(String Curr_Account, double Amount_r, double Amount_p, String Details, String Ref, String Ref_id, String Time_Stamp_i) {
        boolean Expense = false; boolean Comp_OB = false; String SourceSite = "";
        return Insert_into_account( Curr_Account,  Amount_r,  Amount_p, Details,  Ref,  Ref_id, Time_Stamp_i, Expense,  Comp_OB,  SourceSite);
    }

    public static RQO_Return_Status Insert_into_account(String Curr_Account, double Amount_r, double Amount_p,
                                                        String Details, String Ref_I, String Ref_id, String Time_Stamp_i, boolean Expense, boolean Comp_OB, String SourceSite) {
        boolean Dynamic_Sync = true;
        return Insert_into_account( Curr_Account,  Amount_r,  Amount_p,
                Details,  Ref_I,  Ref_id, Time_Stamp_i, Expense,  Comp_OB,  SourceSite, Dynamic_Sync);
    }

    //private static boolean temp_false = false;
    public static RQO_Return_Status Insert_into_account(String Curr_Account, double Amount_r, double Amount_p,
                                                        String Details, String Ref_I, String Ref_id,
                                                        String Time_Stamp_i,
                                                        boolean Expense, boolean Comp_OB, String SourceSite, boolean Dynamic_Sync) {


        List<String> Res1 = new ArrayList<>();
        Res1.clear();
        String Check_Statement;
        double Ttl_Payable_by_Com = 0;
        boolean Dont_Compare_TS = false;
        String Ref = "";
        if ((Ref_I != null) && (!Ref_I.isEmpty())) Ref = Ref_I;

        if (Curr_Account.isEmpty()) {
            ErrMsgDialog("Account is Empty\n\n Contact Developer");
            Log.i (TAG, "680 Insert_into_account Returning here ");
            return RQO_Return_Status.RQO_Input_Empty;
        }
        //Acquire_Named_Sem_Lock("Account-" + Curr_Account);

        int Max_Acc_ID = 0;
        if (Max_Acc_ID == 0) {
            String a = myDB_G.Get_Val_from_DB_UD ("Account", "Acc_id", "MAX");
            if (!a.isEmpty()) {
                Max_Acc_ID = String2Int(a);
            }
        }
        Max_Acc_ID++;

        String curr_time = Time_Stamp_i.replace ('T', ' ');
        String time_Now = Get_Current_Date_Time();
        if (curr_time.isEmpty()) {
            curr_time = time_Now;
           // Log.i (TAG, "111 curr_time = " + curr_time + ", Time_Stamp_i = " + Time_Stamp_i);
            Dont_Compare_TS = true;
        }
       // Log.i (TAG, "2222 curr_time = " + curr_time + ", Time_Stamp_i = " + Time_Stamp_i);


        String Sel = "AccountName = '" + Curr_Account + "' and TimeStamp <= '" + curr_time + "'";
        String Max_TS = myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp", "MAX", Sel);
        Max_TS = Max_TS.replace ('T', ' ');

        // Try to Get previous Ttl_Payable_by_Com
        if (!Max_TS.isEmpty ()) {
            Sel = "TimeStamp = '%1' and AccountName = '%2'";
            Sel = Sel.replace("%1", Max_TS);
            Sel = Sel.replace("%2", Curr_Account);
            Sel = myDB_G.Get_Val_from_DB_UD ("Account", "Ttl_Payable_by_Com", "", Sel);
            Ttl_Payable_by_Com = String2Double2P(Sel);
        }

        if (!Expense) {
            if (Comp_OB) {
                Ttl_Payable_by_Com = (Amount_r - Amount_p) - Ttl_Payable_by_Com;
            } else {
                Ttl_Payable_by_Com += (Amount_r - Amount_p);
            }
        }

        RQO_Return_Status[] RQO_Status = new RQO_Return_Status[1];
        RQO_Status[0] = RQO_Return_Status.RQO_Success;

        /*
        if (!Dont_Compare_TS) {
            String Sel1 = "TimeStamp <= '" + curr_time  + "'";
            String Min_TS = myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp", "MAX", Sel1);
            Min_TS = Min_TS.replace ('T', ' ');
            if (Min_TS.isEmpty ()) {
                Min_TS =   "2000-01-01 00:00:00";
            }//"1990-01-01 00:00:00"; }

            java.util.Date Min_TS_TD = String2Date(Min_TS);
            java.util.Date Curr_TS_TD = String2Date(curr_time);

            Log.i (TAG, "Min_TS = " + Min_TS + ", curr_time = " + curr_time);

            int asdf1 = Curr_TS_TD.compareTo(Min_TS_TD);
            int asdf2 = Min_TS_TD.compareTo(Curr_TS_TD);
            int asdf3 = Min_TS_TD.compareTo(Min_TS_TD);
            Log.i (TAG, "MIN asdf1 = " + asdf1 +  "asdf2 = " + asdf2 + "asdf3 = " + asdf3 );

            if (Min_TS_TD.compareTo(Curr_TS_TD) < 0) {
                // We can use Curr_Time. No need to do anything.
                Log.i(TAG, "Min_TS_TD Good, We can use Curr_Time. No need to do anything.");
            } else {
                java.util.Date Max_TS_TD;
                do {
                    Max_TS = myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp", "MIN", "TimeStamp > '" + curr_time + "'");
                    if (Max_TS.isEmpty ()) {
                        Max_TS = Get_Current_Date_Time ();
                        break;
                    }
                    Max_TS = Max_TS.replace ('T', ' ');


                    Max_TS_TD = String2Date(Max_TS);
                    Log.i (TAG, "Max_TS = " + Max_TS + ", Max_TS_TD = " + Max_TS_TD);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(Curr_TS_TD);
                    cal.add(Calendar.SECOND, 1);
                    Curr_TS_TD = cal.getTime();
                    //Curr_TS_TD.setTime(Curr_TS_TD.getTime() + 1);
                    curr_time = Date2String(Curr_TS_TD);

                    Log.i (TAG, "MAX Curr_TS_TD = " + Curr_TS_TD + ", Max_TS_TD = " + Max_TS_TD);
                    asdf1 = Curr_TS_TD.compareTo(Max_TS_TD);
                    asdf2 = Max_TS_TD.compareTo(Curr_TS_TD);
                    asdf3 = Max_TS_TD.compareTo(Max_TS_TD);
                    Log.i (TAG, "asdf1 = " + asdf1 +  "asdf2 = " + asdf2 + "asdf3 = " + asdf3 );

                    if (Curr_TS_TD.compareTo(Max_TS_TD) < 0) {
                        // accept it.
                        break;
                    }


                    if (!temp_false) {
                       // Log.i (TAG, "I am returning here RQO_Return_Status.RQO_Unknown_Err");
                        Log.i (TAG, "783 Insert_into_account Returning here ");
                      //  return RQO_Return_Status.RQO_Unknown_Err;
                    }
                } while (Curr_TS_TD.compareTo(Max_TS_TD) >= 0);
            }
        } */

        if (!Dont_Compare_TS) {
            String  Sel1 = "TimeStamp = '" + curr_time  + "'";
            String Curr_TS_Present = myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp", "", Sel1);

            if (Curr_TS_Present.isEmpty ()) {
                // We can use Curr_Time [curr_time]. No need to do anything.
            } else {
               // java.util.Date Max_TS_TD;
                boolean found_TS = false;
                do {
                    Max_TS = myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp", "", "TimeStamp = '" + curr_time + "'");
                    if (Max_TS.isEmpty ()) {
                        found_TS = true;
                        // Requested TS NOT in DB, use it
                        break;
                    }

                    curr_time = Add_Secs_Time_String(curr_time, 1);
                } while (!found_TS);
            }
        }


        Check_Statement = "INSERT INTO Account (TimeStamp,Acc_id,Ref_id,Reference,AccountName,Received,Paid,Ttl_Payable_by_Com,Details,SourceSite) values (";
        String DS_Update_S = "'%1',%2,";
        DS_Update_S = DS_Update_S.replace ("%1", curr_time);
        DS_Update_S = DS_Update_S.replace ("%2", Long.toString(Max_Acc_ID));

        DS_Update_S += "'%1','%2','%4',%5,%6,%7,'%3 %8'";
        DS_Update_S = DS_Update_S.replace ("%1", Ref_id);
        DS_Update_S = DS_Update_S.replace ("%2", Ref);
        DS_Update_S = DS_Update_S.replace ("%3", Details);
        DS_Update_S = DS_Update_S.replace ("%4", Curr_Account);
        DS_Update_S = DS_Update_S.replace ("%5", Double2String2P(Amount_r));
        DS_Update_S = DS_Update_S.replace ("%6", Double2String2P(Amount_p));
        DS_Update_S = DS_Update_S.replace ("%7", Double2String2P(Ttl_Payable_by_Com));
        DS_Update_S = DS_Update_S.replace ("%8", time_Now);

        DS_Update_S +=  ", '" + SourceSite +  "'";
        Check_Statement +=  DS_Update_S +  ");";
        String Table = "Account";
        String P_Key_S = curr_time;
        DS_Update_S = DS_Update_S.replace("'", "");

        if (temp_Debug || Global_Debug) {
            System.out.println("Double2String2P(Amount_r) = " + Double2String2P(Amount_r));
            System.out.println("Insert_into_Account Statement = %s\n" + Check_Statement);
        }
        System.out.println("Insert_into_Account Statement = %s\n" + Check_Statement);

        if (!myDB_G.RunQueryOnce_InUp_DS(Check_Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync)) {
            //Release_Named_Sem_Lock ("Account-" + Curr_Account);
            Log.i (TAG, "333 Insert_into_account returning RQO_Unknown_Err");
            return RQO_Return_Status.RQO_Unknown_Err;
        }
        if (Dynamic_Sync) {
            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_Account, DS_Update_S);
            UPload_Data_Msg_List.add(msg);
        }

        //==================================================
        // Need to check in case of remote SYnc what would be the side effects
        // and Update_Accounts_Tables should be called are not.
        //==================================================
        int indexof = Ref_id.indexOf('_');

        String Ref_mod = Ref_id.substring(0, indexof);                 // Ref_id.left (Ref_id.indexOf ('_') + 1);
        String orig_id = Ref_id.substring(indexof + 1, Ref_id.length ());  //  - (Ref_id.indexOf ('_')+ 1 ));

        // Update Ttl_Payable_Com for Remote_PC. and Mobiles
        //

        if (!Update_Accounts_Tables (Trans_Update_Condition.TU_Trans_Cost, "", Curr_Account, String2Int(orig_id),
                Amount_r,Amount_p, Comp_OB?Ttl_Payable_by_Com:(Expense?0:(Amount_r - Amount_p)), "",
                Ref_mod, false, "", Comp_OB, curr_time, "", false, Dynamic_Sync)) {
            //Release_Named_Sem_Lock ("Account-" + Curr_Account);

            Log.i (TAG, "444 Update_Accounts_Tables returning RQO_Unknown_Err");
            return RQO_Return_Status.RQO_Unknown_Err;
        }

        //Release_Named_Sem_Lock ("Account-" + Curr_Account);

        Log.i (TAG, "848 Insert_into_account Returning here ");
        return RQO_Return_Status.RQO_Success;
    }

    public static boolean Update_Accounts_Tables (
            Trans_Update_Condition TU_Cond, String Curr_Id, String Curr_AcName_I,
            int Trans_Id,
            double Recv_diff, double Paid_diff, double Ttl_payable_diff  ) {

        String New_AcName = "";
        String Ref_ID  = "Trans_";
        boolean Modify_Curr_Entry = true;
        String Source_Site  = "";
        boolean Opening_Balance = false;
        String TS_Curr_I = "";
        String Reference = "";
        boolean Insert_if_Required  = false;

        return Update_Accounts_Tables  ( TU_Cond,  Curr_Id,  Curr_AcName_I,  Trans_Id, Recv_diff,  Paid_diff,  Ttl_payable_diff,
                New_AcName, Ref_ID,  Modify_Curr_Entry, Source_Site,  Opening_Balance,  TS_Curr_I,  Reference,  Insert_if_Required ) ;
    }

    public static boolean Update_Accounts_Tables (
            Trans_Update_Condition TU_Cond, String Curr_Id, String Curr_AcName_I,
            int Trans_Id,
            double Recv_diff, double Paid_diff, double Ttl_payable_diff , String New_AcName ) {

        String Ref_ID  = "Trans_";
        boolean Modify_Curr_Entry = true;
        String Source_Site  = "";
        boolean Opening_Balance = false;
        String TS_Curr_I = "";
        String Reference = "";
        boolean Insert_if_Required  = false;

        return Update_Accounts_Tables  ( TU_Cond,  Curr_Id,  Curr_AcName_I,  Trans_Id, Recv_diff,  Paid_diff,  Ttl_payable_diff,
                New_AcName, Ref_ID,  Modify_Curr_Entry, Source_Site,  Opening_Balance,  TS_Curr_I,  Reference,  Insert_if_Required ) ;
    }

    public static boolean Update_Accounts_Tables (Trans_Update_Condition TU_Cond, String Curr_Id, String Curr_AcName_I,
                                                  int Trans_Id, double Recv_diff, double Paid_diff, double Ttl_payable_diff,
                                                  String New_AcName, String Ref_ID,  boolean Modify_Curr_Entry ) {
        return Update_Accounts_Tables (TU_Cond,  Curr_Id,  Curr_AcName_I, Trans_Id, Recv_diff,  Paid_diff,  Ttl_payable_diff,
                New_AcName, Ref_ID, Modify_Curr_Entry, "",  false,  "",  "",  false);
    }

    public static boolean Update_Accounts_Tables (
            Trans_Update_Condition TU_Cond, String Curr_Id, String Curr_AcName_I,
            int Trans_Id,
            double Recv_diff, double Paid_diff, double Ttl_payable_diff,
            String New_AcName, // In case you are changing A/c Name
            String Ref_ID, // Either Trans_, Money_ used for first_time getting Acc_id
            boolean Modify_Curr_Entry, //, // In case you are modifying Existing entry with values
            String Source_Site, boolean Opening_Balance, String TS_Curr_I, String Reference, boolean Insert_if_Required
    ) {
        return Update_Accounts_Tables (TU_Cond, Curr_Id,  Curr_AcName_I, Trans_Id,
                Recv_diff,  Paid_diff,  Ttl_payable_diff, New_AcName, Ref_ID, Modify_Curr_Entry,
                Source_Site,  Opening_Balance,  TS_Curr_I,  Reference,  Insert_if_Required, true);
    }

    public static boolean Update_Accounts_Tables (
            Trans_Update_Condition TU_Cond, String Curr_Id, String Curr_AcName_I,
            int Trans_Id,
            double Recv_diff, double Paid_diff, double Ttl_payable_diff,
            String New_AcName, // In case you are changing A/c Name
            String Ref_ID, // Either Trans_, Money_ used for first_time getting Acc_id
            boolean Modify_Curr_Entry, //, // In case you are modifying Existing entry with values
            String Source_Site, boolean Opening_Balance, String TS_Curr_I, String Reference, boolean Insert_if_Required,
            boolean Dynamic_Sync
    ) {
        String Curr_AcName = Curr_AcName_I;
        double Recv_l = 0;
        double Paid_l = 0;
        double Ttl_Payable_by_Com = 0;
        String TS_Curr = TS_Curr_I;

        String  sel_condition = "Ref_id = '" + Ref_ID + Long.toString(Trans_Id) + "'  and AccountName =  '" + Curr_AcName + "' ";


        if (!Reference.isEmpty()) {
            sel_condition += " and Reference = '" + Reference + "'";
        }

        if (!Curr_Id.isEmpty()) {
            TS_Curr = myDB_G.Get_Val_from_DB_UD("Account", "TimeStamp", "MIN", "Acc_id = " + Curr_Id);
        }

        if (TS_Curr.isEmpty()) {
            TS_Curr = myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp", "", sel_condition);
            if ((TS_Curr == null) || (TS_Curr.isEmpty())) {
                //ErrMsgDialog("Update_Accounts_Tables TS_Curr is Invalid");
                return false;
            }
            TS_Curr = TS_Curr.replace ('T', ' ');
        }
        //Acquire_Named_Sem_Lock( "UpAccount-" + Curr_AcName_I); // TOBECHECKEDLATER
        if ((New_AcName != null) && (!New_AcName.isEmpty()) && (!New_AcName.equals(Curr_AcName_I))) {
            // Acquire_Named_Sem_Lock( "UpAccount-" + New_AcName);
        }

        if (Modify_Curr_Entry) {
            // Set Received Diff
            if (Recv_diff != 0) {
                String t1 = myDB_G.Get_Val_from_DB_UD ("Account", "Received", "", "TimeStamp = '"+ TS_Curr + "'", null, "", false);
                if (t1.isEmpty()) { Recv_l = 0; } else { Recv_l = String2Double2P(t1); }

                myDB_G.Update_field_in_DB_UD ("Account", "Received", Double2String2P(Recv_l + Recv_diff),
                        "TimeStamp = '" + TS_Curr + "'", Dynamic_Sync);
            }

            // Set Paid Diff
            if (Paid_diff != 0) {
                String t1 = myDB_G.Get_Val_from_DB_UD ("Account", "Paid", "", "TimeStamp = '"+ TS_Curr + "'", null, "", false);
                if (t1.isEmpty()) { Paid_l = 0; } else { Paid_l = String2Double2P(t1); }
                //Paid_l = (myDB_G.Get_Val_from_DB_UD ("Account", "Paid", "", String("TimeStamp = '%1'").arg(TS_Curr), NULL, "", false)).toLongLong ();

                myDB_G.Update_field_in_DB_UD ("Account", "Paid", Double2String2P(Paid_l + Paid_diff), "TimeStamp = '" + TS_Curr + "'",
                        Dynamic_Sync);
            }

            // Set Ttl_payable Diff
            String t1 = myDB_G.Get_Val_from_DB_UD ("Account", "Ttl_Payable_by_Com", "", "TimeStamp = '"+ TS_Curr + "'", null, "", false);
            if (t1.isEmpty()) { Ttl_Payable_by_Com = 0; } else { Ttl_Payable_by_Com = String2Double2P(t1); }

            myDB_G.Update_field_in_DB_UD ("Account", "Ttl_Payable_by_Com", Double2String2P(Ttl_Payable_by_Com
                    + Ttl_payable_diff), "TimeStamp = '" + TS_Curr + "'", Dynamic_Sync);
        }

        switch (TU_Cond) {
            case TU_Trans_New_Ac:
            {
                myDB_G.Update_field_in_DB_UD ("Account", "AccountName", New_AcName, "TimeStamp = '" + TS_Curr + "'", Dynamic_Sync);
                Curr_AcName = New_AcName;

                String TS_Priv = myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp", "MAX", "TimeStamp < '" + TS_Curr  + "' and  AccountName = '" +Curr_AcName  + "'");
                TS_Priv = TS_Priv.replace ('T', ' ');

                if (TS_Priv.isEmpty ()) {
                    Ttl_Payable_by_Com = 0;
                } else {
                    String t1 = myDB_G.Get_Val_from_DB_UD ("Account", "Ttl_Payable_by_Com", "", "TimeStamp = '"+ TS_Priv + "' and AccountName = '" + Curr_AcName + "'", null, "", false);
                    if (t1.isEmpty()) { Ttl_Payable_by_Com = 0; } else { Ttl_Payable_by_Com = String2Double2P(t1); }

                }
                myDB_G.Update_field_in_DB_UD ("Account", "Ttl_Payable_by_Com", Double2String2P(Ttl_Payable_by_Com + Ttl_payable_diff),
                        "TimeStamp = '" + TS_Curr + "'", Dynamic_Sync);
            }
            break;

            case TU_GST_Revert:
            case TU_Permit_Revert:
                myDB_G.Update_field_in_DB_UD ("Account", "AccountName", New_AcName, "TimeStamp = '" + TS_Curr + "'",
                        Dynamic_Sync);
                break;

            default: break;
        }

        if (Opening_Balance) {
            myDB_G.Update_field_in_DB_UD ("Account", "Ttl_Payable_by_Com", Double2String2P(Recv_diff - Paid_diff),
                    "TimeStamp = '" + TS_Curr + "'", Dynamic_Sync);
        }

        List<String> Query_Statement_Output = new ArrayList<>();
        Query_Statement_Output.clear();

        String Selection = "AccountName = '" + Curr_AcName + "' and TimeStamp > '" + TS_Curr + "'";

        if (Ttl_payable_diff != 0) {
            myDB_G.Get_Val_from_DB_UD ("Account", "TimeStamp, Ttl_Payable_by_Com", "", Selection, Query_Statement_Output, "TimeStamp") ;

            int a = 0;
            int b = Query_Statement_Output.size();

            if (b == 0) {
                //Release_Named_Sem_Lock ( "UpAccount-" + Curr_AcName_I); // TOBECHECKEDLATER
                if ((New_AcName != null) && (!New_AcName.isEmpty()) && (!New_AcName.equals(Curr_AcName_I))) {
                    // Release_Named_Sem_Lock( "UpAccount-" + New_AcName); // TOBECHECKEDLATER
                }
                return true;
            }

            String Ttl_Update = "UPDATE Account SET Ttl_Payable_by_Com = (CASE ";

            for (; a < b; a++) {
                String temp1 = Query_Statement_Output.get(a);
                String[] temp = temp1.split("\\a", -1);

                String Ts = temp[0];
                Ts = Ts.replace ('T' , ' ');

                double ttl_pll = String2Double2P(temp[1]);
                if (!Ts.equals(TS_Curr)) {
                    ttl_pll += Ttl_payable_diff;
                } else {
                    //ttl_pll = Ttl_payable_diff;
                }

                Ttl_Update += "WHEN TimeStamp = '" + Ts + "' THEN " + Double2String2P(ttl_pll) + " ";
            }

            //Ttl_Update += "ELSE  Ttl_Payable_by_Com END) Where TimeStamp > '" + TS_Curr + "' and AccountName = '" + Curr_AcName + "';";
            Ttl_Update += "ELSE  Ttl_Payable_by_Com END) Where TimeStamp > '" + TS_Curr + "' and AccountName = '" + Curr_AcName + "';";

            String Table = "UpdateStatement";
            String P_Key_S = Get_Current_Date_Time_millis();
            myDB_G.RunQueryOnce_InUp_DS(Ttl_Update, Ttl_Update, Table, P_Key_S, Dynamic_Sync);

            if (Dynamic_Sync) {
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_UpdateStatement, Ttl_Update);
                UPload_Data_Msg_List.add(msg);
            }
        }
        //Release_Named_Sem_Lock ( "UpAccount-" + Curr_AcName_I); // TOBECHECKEDLATER
        if ((New_AcName != null) && (!New_AcName.isEmpty()) && (!New_AcName.equals(Curr_AcName_I))) {
            //Release_Named_Sem_Lock( "UpAccount-" + New_AcName); // TOBECHECKEDLATER
        }
        return true;
    }


    /* public static String Add_Secs_Time_String(String in_time, int secs) {
        return Add_Secs_Time_String(in_time, secs, System_Date_Time_Format);
    } */

    /* public static String Add_Secs_Time_String(String in_time, int secs, String in_format) {
        String out_format = in_format;
        return Add_Secs_Time_String(in_time, secs, in_format, out_format);
    } */

    /*public static String Add_Secs_Time_String(String in_time, int secs, String in_format, String out_format) {
        java.util.Date Max_TS_TD = new java.util.Date();
        / * try {
            Max_TS_TD = new SimpleDateFormat(in_format).parse(in_time);
        } catch (ParseException ex) {
            getLogger(SQLiteHandler.class.getName()).log(SEVERE, null, ex);
        } * / // TOBECHECKEDLATER

        Calendar cal = Calendar.getInstance();
        cal.setTime(Max_TS_TD);
        cal.add(Calendar.SECOND, secs);
        Max_TS_TD = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat(out_format);
        String newDate = dateFormat.format(Max_TS_TD);
        return newDate;
    } */

    public static String Get_Start_of_Month() {
        return Get_Start_of_Month(Get_Current_Date_Time(), System_Date_Time_Format, System_Date_Time_Format);
    }

    public static String Get_Start_of_Month(String in_time, String in_format, String out_format) {
        java.util.Date Max_TS_TD = new java.util.Date();
        /* try {
            Max_TS_TD = new SimpleDateFormat(in_format).parse(in_time);
        } catch (ParseException ex) {
            getLogger(SQLiteHandler.class.getName()).log(SEVERE, null, ex);
        } */ // TOBECHECKEDLATER
        //Max_TS_TD.setTime(Max_TS_TD.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(Max_TS_TD);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        cal.set(y, m, 1, 0, 0, 0);

        Max_TS_TD = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat(out_format);
        String newDate = dateFormat.format(Max_TS_TD);
        return newDate;
    }

    public static String Get_End_of_Month() {
        return Get_End_of_Month(Get_Current_Date_Time(), System_Date_Time_Format, System_Date_Time_Format);
    }

    public static String Get_End_of_Month(String in_time, String in_format, String out_format) {
        java.util.Date Max_TS_TD = new java.util.Date();
        /*try {
            Max_TS_TD = new SimpleDateFormat(in_format).parse(in_time);
        } catch (ParseException ex) {
            getLogger(SQLiteHandler.class.getName()).log(SEVERE, null, ex);
        } */ // TOBECHECKEDLATER
        //Max_TS_TD.setTime(Max_TS_TD.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(Max_TS_TD);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(y, m, days, 23, 59, 59);

        Max_TS_TD = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat(out_format);
        String newDate = dateFormat.format(Max_TS_TD);
        return newDate;
    }

    public static Date Date_Add_Secs_Time_String(String in_time, int secs) {
        return Date_Add_Secs_Time_String(in_time, secs, System_Date_Time_Format);
    }

    public static Date String2Date(String in_time) {
        return Date_Add_Secs_Time_String(in_time, 0, System_Date_Time_Format);
    }

    public static Date String2Date(String in_time, String format) {
        return Date_Add_Secs_Time_String(in_time, 0, format);
    }

    public static Date Date_Add_Secs_Time_String(String in_time, int secs, String format) {
        java.util.Date Max_TS_TD = new java.util.Date();
        /* try {
            Max_TS_TD = new SimpleDateFormat(format).parse(in_time);
        } catch (ParseException ex) {
            getLogger(SQLiteHandler.class.getName()).log(SEVERE, null, ex);
        } */ // TOBECHECKEDLATER
        //Max_TS_TD.setTime(Max_TS_TD.getTime() + secs);
        Calendar cal = Calendar.getInstance();
        cal.setTime(Max_TS_TD);
        cal.add(Calendar.SECOND, secs);
        Max_TS_TD = cal.getTime();

        return Max_TS_TD;
    }

    public static long Long_Add_Secs_Time_String(String in_time, int secs) {
        return Long_Add_Secs_Time_String(in_time,  secs, System_Date_Time_Format);
    }

    public static long Long_Add_Secs_Time_String(String in_time, int secs, String format) {
        java.util.Date Max_TS_TD = new java.util.Date();
        /* try {
            Max_TS_TD = new SimpleDateFormat(format).parse(in_time);
        } catch (ParseException ex) {
            getLogger(SQLiteHandler.class.getName()).log(SEVERE, null, ex);
        } */ // TOBECHECKEDLATER
        //Max_TS_TD.setTime(Max_TS_TD.getTime() + secs);
        Calendar cal = Calendar.getInstance();
        cal.setTime(Max_TS_TD);
        cal.add(Calendar.SECOND, secs);

        return cal.getTimeInMillis();
    }

    public static long Long_Time_diff_between_2_Time_String(String time1, String time2) {
        return Long_Time_diff_between_2_Time_String( time1, time2, System_Date_Time_Format, System_Date_Time_Format);
    }

    public static long Long_Time_diff_between_2_Time_String(String time1, String time2, String Format1, String Format2) {
        java.util.Date Max_TS_TD = new java.util.Date();
        /* try {
            Max_TS_TD = new SimpleDateFormat(Format1).parse(time1);
        } catch (ParseException ex) {
            getLogger(SQLiteHandler.class.getName()).log(SEVERE, null, ex);
        } */  // TOBECHECKEDLATER
        Calendar cal = Calendar.getInstance();
        cal.setTime(Max_TS_TD);

        long t1 = cal.getTimeInMillis();


        /* try {
            Max_TS_TD = new SimpleDateFormat(Format2).parse(time2);
        } catch (ParseException ex) {
            getLogger(SQLiteHandler.class.getName()).log(SEVERE, null, ex);
        } */ // TOBECHECKEDLATER
        cal.setTime(Max_TS_TD);
        long t2 = cal.getTimeInMillis();

        return t1 - t2;
        //return cal.getTimeInMillis();
    }

    public static String Date2String(Date in_time) {
        String format = System_Date_Time_Format;
        return Date2String(in_time, format);
    }

    public static String Date2String(Date in_time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String newDate = dateFormat.format(in_time);
        return newDate;
    }

    /* static int String2Int(String a) {

        if ((a == null) || (a.isEmpty())) {
            return 0;
        }
        try {
            return Integer.parseInt(a);
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    } */

    static long String2Long(String a) {
        if ((a == null) || (a.isEmpty())) {
            return 0;
        }
        try {
            return Long.parseLong(a);
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }

    static double String2Double(String a) {
        if ((a == null) || (a.isEmpty())) {
            return 0;
        }
        try {
            return Double.parseDouble(a);
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }

    /* static double String2Double2P(String a) {
        if ((a == null) || (a.isEmpty())) {
            return 0;
        }
        try {
            double x = Double.parseDouble(a);
            return Double2Double2P(x);
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    } */

    static String String2StringLong2PRupeeFormat(String a) {
        return String.format("%1$,d", String2Long(a));
    }

    static String String2StringDouble2PRupeeFormat(String a) {
        return Double2String2P_RupeeFormat(String2Double2P(a));
    }

    static String String2StringDouble2P(String a) {
        return Double2String2P(String2Double2P(a));
    }

    static String Double2String2P_RupeeFormat ( double a) {
        double value = a;
        if (a == 0) return "0.00";

        boolean is_negative = false;
        if (a < 0) {
            is_negative = true;
            value *= -1;
        }

        String Output = Double2String2P(value);

        String x = Output;
        String y = "";
        int t_ind = x.indexOf(".");

        if (t_ind > 9) {
            y = x.substring(0, t_ind -9) + ","
                    + x.substring(t_ind - 9, t_ind -7) + ","
                    + x.substring(t_ind - 7, t_ind -5) + ","
                    + x.substring(t_ind - 5, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else if (t_ind > 7) {
            y = x.substring(0, t_ind -7) + ","
                    + x.substring(t_ind - 7, t_ind -5) + ","
                    + x.substring(t_ind - 5, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else if (t_ind > 5) {
            y = x.substring(0, t_ind -5) + ","
                    + x.substring(t_ind -5, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else if (t_ind > 3) {
            y = x.substring(0, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else {
            y = x;
        }

        if (is_negative) {
            y = "-" + y;
        }

        int ind = y.indexOf(".");
        int len = y.length();
        if (ind < 0) {
            y += ".00";
        } else if ((len - ind) == 1) {
            y += "0";
        }
        return y;
    }

    static String Double2String0P_RupeeFormat ( double a) {
        double value = a;
        if (a == 0) return "0";

        boolean is_negative = false;
        if (a < 0) {
            is_negative = true;
            value *= -1;
        }

        String Output = Double2String2P(value);

        String x = Output;
        String y = "";
        int t_ind = x.indexOf(".");

        if (t_ind > 9) {
            y = x.substring(0, t_ind -9) + ","
                    + x.substring(t_ind - 9, t_ind -7) + ","
                    + x.substring(t_ind - 7, t_ind -5) + ","
                    + x.substring(t_ind - 5, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else if (t_ind > 7) {
            y = x.substring(0, t_ind -7) + ","
                    + x.substring(t_ind - 7, t_ind -5) + ","
                    + x.substring(t_ind - 5, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else if (t_ind > 5) {
            y = x.substring(0, t_ind -5) + ","
                    + x.substring(t_ind -5, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else if (t_ind > 3) {
            y = x.substring(0, t_ind -3) + ","
                    + x.substring(t_ind -3);
        } else {
            y = x;
        }

        if (is_negative) {
            y = "-" + y;
        }

        int ind = y.indexOf(".");
        int len = y.length();
        y = y.substring(0, ind);
        if (ind < 0) {
        //    y += ".00";
        } else if ((len - ind) == 1) {
       //     y += "0";
        }
        return y;
    }

    static String Double2String2P ( double a) {

        double b = Double2Double2P(a);
        String str = String.format("%1.02f", a);
        return str;
    }

    /*static double Double2Double2P ( double a) {
        //DecimalFormat df = new DecimalFormat("#.##");
        //double b = Double.valueOf(df.format(a));
        //System.out.println("time  200.0099956 = " + b);

        //return b;
        // TOBECHECKEDLATER
        return 0;
    } */

    public static String YMD2String(int y, int m, int d) {
        return YMD2String(y, m, d, System_Date_Format);
    }

    public static String YMD2String(int y, int m, int d, String format) {
        String a = Integer.toString(y) + "-";
        a += Integer.toString(m) + "-";
        a += Integer.toString(d);
        a = Add_Secs_Time_String(a, 0, System_Date_Format, format);
        return a;
    }

    void String2YMD(int[] y, String in_time, String format) {
        Date date = String2Date(in_time, format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        y[0] = cal.get(Calendar.YEAR);
        y[1] = cal.get(Calendar.MONTH);
        y[2] = cal.get(Calendar.DATE);
    }


    //========================= To be Checked for correctness =====================================

    public static boolean Acquire_Named_Sem_Lock (String Name) {
        int a = 0;
        int b = List_MySem_G.size();
        boolean Found_Sem = false;
        for (; a < b; a++) {
            MySemaphore t = List_MySem_G.get(a);
            String Name1 = t.getLock_Name();
            System.out.println("PresentName = " + Name1 + ", Req Name =" + Name);
            if ((t.getLock_Name()).equals(Name)) {
                //MyDebugPrintf("Lock Exists Trying Lock:: "  + Name);
                if (t.IsSQL_Sem_Locked()) {
                    //MyDebugPrintf("New Sem is Locked Name " + Name);
                }
                t.Sem_acquire();
                //MyDebugPrintf("Lock Exists Trying Lock:: "  + Name);
                Found_Sem = true;
                break;
            }
        }
        if (!Found_Sem) {
            MySemaphore e = new MySemaphore();
            e.setLock_Name(Name);
            List_MySem_G.add(e);

            //MyDebugPrintf("Lock Does NOT Exists, Creating and Trying Lock:: " + Name);
            e.Sem_acquire();
            if (e.IsSQL_Sem_Locked()) {
                //MyDebugPrintf("New Sem is Locked Name " + Name);
            }

            //MyDebugPrintf("Lock Does NOT Exists, Creating and Trying Lock:: " + Name);

        }
        return true;
    }


    public static FirebaseAuth mFAuth_Main = null;
    public static FirebaseAuth mFAuth_Sec = null;
    public static FirebaseUser FB_user = null;

    public static DatabaseReference mFDB_AppCustomer = null;

    // public static DatabaseReference mFDB_Site = null;
    // public static DatabaseReference mFDB_Vehicle = null;
    // public static DatabaseReference mFDB_Computers = null;
    // public static DatabaseReference mFDB_AutoSaveTrans = null;

    public static boolean Release_Named_Sem_Lock (String Name) {
        int a = 0;
        int b = List_MySem_G.size();
        boolean Found_Sem = false;
        for (; a < b; a++) {
            MySemaphore t = List_MySem_G.get(a);
            if ((t.getLock_Name()).equals(Name)) {
                t.Sem_Release();
                //MyDebugPrintf("Releasing Lock:: " + Name);
                return true;
            }
        }
        return false;
    }

    public static boolean Is_Network_Available() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) myGContext_Global.getSystemService(myGContext_Global.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean is_Network_Available  = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        //Log.i (TAG, "is_Network_Available = " + is_Network_Available);
        return is_Network_Available;
    }



    static int ErrM_Count = 0;
    public static void Show_Network_Config_Err_Msg(){
        Show_Network_Config_Err_Msg("");
    }

    public static void Show_Network_Config_Err_Msg(String ErrMsg){

        if (ErrM_Count == 0) {
            ErrMsgDialog_L("Err:: "+ ErrMsg +"\n\nPlease Check Network Connections\n\nPlease Check Configuration\n\nCloud Data Sync will not happen");
        } else if (ErrM_Count > 200) {
            ErrM_Count = 0;
        }
        ErrM_Count++;
    }


    public static String[] List2_Array (List<String> Input_List) {
        String[] Output_Arr = null;
        if (Input_List.size() > 0) {
            Output_Arr = new String[Input_List.size()];
            Input_List.toArray(Output_Arr);
        } else {
            Output_Arr = new String[0];
        }

        return Output_Arr;
    }


}
