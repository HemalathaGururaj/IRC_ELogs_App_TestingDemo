package com.shasratech.s_c_31;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;

//import com.google.android.gms.drive.DriveId;


/**
 * Created by srihari on 17/4/17.
 */

public class DBHelperAppUtils extends SQLiteOpenHelper {
    public static final int CurrVersion = 2;

    private static final String TAG = "DBHelperAppUtils";
    private static SQLiteDatabase db_Handle;
    public static SQLiteDatabase db_Handle_G_AU;
    public static boolean DB_Init_Complete = false;

    public DBHelperAppUtils(Context context, String AppName, String SiteName) {
        super(context, AppName + "-" + SiteName, null, CurrVersion);

        Log.i(TAG, "111 XXXXX DBHelperAppUtils ------- On Create ------------------------------------------ ");
        db_Handle = this.getWritableDatabase();
        if (db_Handle == null) {
            Log.i(TAG, "111 XXXXX DBHelperAppUtils ------- On Create ------------------------------------------ ");
        }
        db_Handle_G_AU = db_Handle;
          Log.i(TAG, "1111 DBHelperAppUtils ------------------------------------------------- ");
        InitAll_DB_AU();
        Log.i(TAG, "1111 DBHelperAppUtils -----------------Init Complete -------------------------------- ");

    }

    @Override
    public void onCreate(SQLiteDatabase db_Handle1) {
        if (db_Handle1 != null) {
            db_Handle = db_Handle1;
            db_Handle_G_AU = db_Handle1;
            Log.i(TAG, "2222 DBHelperAppUtils ------- On Create ------------------------------------------ ");
        } else {
            db_Handle = this.getWritableDatabase();
            Log.i(TAG, "3333 DBHelperAppUtils ------- On Create ------------------------------------------ ");
            if (db_Handle == null) {
                Log.i(TAG, "XXXXX DBHelperAppUtils ------- On Create ------------------------------------------ ");
            }
        }
        Log.i(TAG, "4444 DBHelperAppUtils ------- On Create ------------------------------------------ ");

        InitAll_DB_AU();
    }

    public void Delete_DataBase(Context context) {

    }

    public void InitAll_DB_AU () {
        Create_DB_Tables();
        Init_Variables();
        DB_Init_Complete = true;
    }


    void Create_DB_Tables() {
        Log.i(TAG, "In Create_DB_Tables = =  = = = = = = = = = ");

        if (db_Handle != null) {
             CreateApp_SysConf_Table(db_Handle);

        } else {
            Log.i(TAG, "db_Handle NULL in Create_DB_Tables ======================================");
        }
    }

    public void Init_Variables() {
        Init_Variables("");
    }

    public void Init_Variables(String FBAppCustomer_I) {
        Log.i(TAG, "\n\n\nIn Init_Variables = = =            ================================ ===============");
        //if (Current_Site.isEmpty()) Current_Site = "Site1";
        Current_Site = "Site1";

        FBAppName = Get_Var_Val_from_App_SysConf("FireBase", "FBAppName");
        if ((FBAppCustomer_I == null) || (FBAppCustomer_I.isEmpty())) {
            FBAppCustomer = Get_Var_Val_from_App_SysConf("FBAppCustomer");
            Log.i (TAG, "97 FBAppCustomer = " + FBAppCustomer + "==========");
        } else {
            FBAppCustomer = FBAppCustomer_I;
            Log.i (TAG, "100 FBAppCustomer = " + FBAppCustomer + "==========");
        }
        Log.i (TAG, "FBAppCustomer = " + FBAppCustomer + "==========");
        FBAppMBName = Get_Var_Val_from_App_SysConf("FireBase", "FBAppMBName");
        Log.i (TAG, "FBAppMBName = " + FBAppMBName + "==========");

        FB_User_Email_Sec_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_User_Email_Sec");
        FB_User_Pass_Sec_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_User_Pass_Sec");

        FB_Client_ID_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_Client_ID");
        FB_Current_Key_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_Current_Key");
        FB_URL_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_URL");
        FB_MobileSDK_App_ID_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_MobileSDK_App_ID");

        FB_IMEI_Setup = Get_Var_Val_from_App_SysConf("FireBase", "FB_IMEI_Setup").equals("YES");

        CustomRate_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "CustomRate_Audio_Notify").equals("YES");
        Inventory_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "Inventory_Audio_Notify").equals("YES");
        Person_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "Person_Audio_Notify").equals("YES");
        Trans_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "Trans_Audio_Notify").equals("YES");
        Material_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "Material_Audio_Notify").equals("YES");
        MTrans_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "MTrans_Audio_Notify").equals("YES");
        Vehicle_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "Vehicle_Audio_Notify").equals("YES");

        AutoSaveTrans_Audio_Notify = Get_Var_Val_from_App_SysConf("FireBase", "AutoSaveTrans_Audio_Notify").equals("YES");

        Log.i(TAG, "Init_Variables === FBAppName =" + FBAppName + ", FBAppCustomer = " + FBAppCustomer);
        Log.i(TAG, "Init_Variables === FB_User_Email =" + FB_User_Email_Sec_G + ", FB_User_Pass = " + FB_User_Pass_Sec_G);
        Log.i(TAG, "Init_Variables === FB_Client_ID =" + FB_Client_ID_G + ", FB_Current_Key = " + FB_Current_Key_G);
        Log.i(TAG, "Init_Variables === FB_URL =" + FB_URL_G + ", FB_MobileSDK_App_ID = " + FB_MobileSDK_App_ID_G);

        Log.i(TAG, "Init_Variables === Trans_Audio_Notify =" + Trans_Audio_Notify + ", Vehicle_Audio_Notify = " + Vehicle_Audio_Notify);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db_Handle, int oldVersion, int newVersion) {
        Create_DB_Tables();
    }


    public String Get_Insert_Statements_from_Cursor(Cursor O_Cursor, String TableName, int[] col_Text_Arr) {
        String id[] = new String[O_Cursor.getCount()];
        int i = 0;
        String Output = "";
        int a = 0;
        int rowCount = O_Cursor.getCount();

        int b = 0;
        int ColCount = O_Cursor.getColumnCount();
        O_Cursor.moveToFirst();
        for (; a < rowCount; a++) {
            String Statement = "";
            for (b = 0; b < ColCount; b++) {
                if (b == 0) {
                    Statement += "Insert into " + TableName + " values ( ";
                }
                boolean col_is_Text = false;
                int j = 0;
                int k = col_Text_Arr.length;
                for (; j < k; j++)
                    if (col_Text_Arr[j] == b) {
                        col_is_Text = true;
                        break;
                    }

                if (col_is_Text) Statement += "\"";
                Statement += O_Cursor.getString(b);
                if (col_is_Text) Statement += "\"";

                if (b != (ColCount - 1)) {
                    Statement += ",";
                } else {
                    Statement += ");";
                }
            }
            Output += Statement + "\n";
            O_Cursor.moveToNext();
        }
        return Output;
    }


    public String ReadData_from_DB_Table_S(String tableName, String ColumnName, String Max_Sum_Count, String Condition) {
        Cursor Output = ReadData_from_DB_Table(tableName, ColumnName, Max_Sum_Count, Condition);
        if (Output.getCount() > 0) {
            return Output.getString(0);
        } else {
            return "";
        }
    }

    public Cursor ReadData_from_DB_Table(String tableName, String ColumnName, String Max_Sum_Count, String Condition) {
        String myStatement = "select ";
        if ((Max_Sum_Count != null) && (!Max_Sum_Count.isEmpty())) {
            myStatement += Max_Sum_Count + "(" + ColumnName + ") ";
        } else {
            myStatement += ColumnName + " ";
        }

        myStatement += " from " + tableName + " ";

        if (!Condition.isEmpty()) {
            myStatement += " WHERE " + Condition + " ";
        }
        if (db_Handle == null) db_Handle = this.getWritableDatabase();
        Cursor Output = db_Handle.rawQuery(myStatement, null);

        if (Output.getCount() > 0) {
            Output.moveToFirst();
        }

        return Output;
    }

    public static String  Get_Val_from_DB_AppUtils(String  Table) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_AppUtils(Table, "*", "", "", res, "", false, "");
    }

    public static  String  Get_Val_from_DB_AppUtils(String  Table, String  myColumn) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_AppUtils(Table, myColumn, "", "", res, "", false, "");
    }

    public static  String  Get_Val_from_DB_AppUtils(String  Table, String  myColumn, String  SUM_MAX
    ) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_AppUtils(Table, myColumn, SUM_MAX, "", res, "", false, "");
    }

    public static  String  Get_Val_from_DB_AppUtils(String  Table, String  myColumn, String  SUM_MAX, String  Selector
    ) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_AppUtils(Table, myColumn, SUM_MAX, Selector, res, "", false, "");
    }

    public static String  Get_Val_from_DB_AppUtils(String  Table, String  myColumn, String  SUM_MAX, String  Selector,
                                          List<String> res) {
        return Get_Val_from_DB_AppUtils(Table, myColumn, SUM_MAX, Selector, res, "", false, "");
    }

    public static  String  Get_Val_from_DB_AppUtils(String  Table, String  myColumn, String  SUM_MAX, String  Selector,
                                           List<String> res, String  Orderby) {
        return Get_Val_from_DB_AppUtils(Table, myColumn, SUM_MAX, Selector, res, Orderby, true, "");
    }

    public static  String  Get_Val_from_DB_AppUtils(String  Table, String  myColumn, String  SUM_MAX, String  Selector,
                                           List<String> res, String  Orderby, boolean print_err) {
        return Get_Val_from_DB_AppUtils(Table, myColumn, SUM_MAX, Selector, res, Orderby, print_err, "");
    }

    public static  String  Get_Val_from_DB_AppUtils(String  Table, String  myColumn, String  SUM_MAX, String  Selector,
                                           List<String> res1, String  Orderby, boolean print_err, String  GroupBy) {
        List<String> res = res1;

        if (res == null) {
            res = new ArrayList<String>();
        }

        String  Statement = "SELECT ";
        if (!SUM_MAX.isEmpty()) {
            Statement += SUM_MAX + "(" + myColumn + ") ";
        } else {
            Statement += myColumn;
        }
        Statement += " from " + Table;

        if (!Selector.isEmpty()) {
            Statement += " where " + Selector;
        }

        if (!GroupBy.isEmpty()) {
            Statement += " group by  " + GroupBy;
        }

        if (!Orderby.isEmpty()) {
            Statement += " order by  " + Orderby;
        }

        Statement += " ;";

        List<String> Result = new ArrayList<String>();

       // System.out.println ("Get_Val_from_DB_AppUtils Statement =" + Statement);
        Result.clear();
        RunQueryOnce_AppUtils(Statement, Result, print_err);
        res.clear();
        res.addAll(Result);

        if (Result.size() > 0) {
            String Output = Result.get(0);
            Output = Output.trim();
            return Output;
        } else {
            return "";
        }
    }

    public static boolean RunQueryOnce_AppUtils(String  Statement_I) {
        List<String> Result1 = new ArrayList<String>();
        Result1.clear();
        return RunQueryOnce_AppUtils (Statement_I, Result1);
    }

    public static boolean RunQueryOnce_AppUtils(String  Statement_I, List<String> Result1) {
        return RunQueryOnce_AppUtils (Statement_I, Result1, false);
    }

    public static boolean RunQueryOnce_AppUtils(String  Statement_I, List<String> Result1, boolean print_err) {
        return RunQueryOnce_AppUtils (Statement_I, Result1, print_err, false);
    }

    public static boolean RunQueryOnce_AppUtils(String  Statement_I, List<String> Result1, boolean print_err,
                                       boolean sync_this_statement) {
        RQO_Return_Status[] RQO_Status =  new RQO_Return_Status[1];
        RQO_Status[0] = RQO_Return_Status.RQO_Unknown_Err;

        return RunQueryOnce_AppUtils (Statement_I, Result1, print_err, sync_this_statement, RQO_Status, false);
    }

    public static boolean RunQueryOnce_AppUtils(String  Statement_I, List<String> Result1, boolean print_err,
                                       boolean sync_this_statement, RQO_Return_Status[] RQO_Status) {
        return RunQueryOnce_AppUtils (Statement_I, Result1, print_err, sync_this_statement, RQO_Status, false);
    }

    public static boolean RunQueryOnce_AppUtils(String  Statement_I, List<String> Result1, boolean print_err,
                                       boolean sync_this_statement, RQO_Return_Status[] RQO_Status, boolean Append2SysLog) {

        Cursor O_Cursor = db_Handle.rawQuery(Statement_I, null);

        if (O_Cursor.getCount() > 0) {
            String Result = "";
            int a = 0;
            int rowCount = O_Cursor.getCount();

            int b = 0;
            int ColCount = O_Cursor.getColumnCount();
            O_Cursor.moveToFirst();
            for (; a < rowCount; a++) {
                String Statement = "";
                for (b = 0; b < ColCount; b++) {
                    String temp = O_Cursor.getString(b);
                    if (temp == null) temp = "";
                    temp = temp.trim();
                    Result += temp;
                    if ((ColCount > 1) && (b < ColCount)) {
                        Result += '\007';
                    }
                    // Log.i (TAG, "RunQueryOnce_AppUtils temp = "+temp);
                }
                Result1.add(Result);
                O_Cursor.moveToNext();
                Result = "";
            }
            return true;
        }
        return false;
    }


    public static boolean Get_Col_Names_from_Table (String Table, List<String> Result) {
        Result.clear();

        List<String> Res = new ArrayList<String>();
        Res.clear();
        String Col_Statement = "PRAGMA table_info(\"" + Table + "\");";
        RunQueryOnce_AppUtils(Col_Statement, Res);

        int a = 0;
        int b = Res.size();

        for (a = 0; a < b; a++) {
            String e1 = Res.get(a);
            String[] each_cols = e1.split("\\a", -1);
            Result.add(each_cols[1]);
            //Log.i (TAG, "each_cols[1] = " + each_cols[1] + ", e1 = " + e1);

        }
        return true;
    }

    public Cursor ReadData_from_DB_Table(String tableName, String ColumnName, String Max_Sum_Count) {
        return ReadData_from_DB_Table(tableName, ColumnName, Max_Sum_Count, "");
    }

    public Cursor ReadData_from_DB_Table(String tableName, String ColumnName) {
        return ReadData_from_DB_Table(tableName, ColumnName, "", "");
    }

    public Cursor ReadData_from_DB_Table(String tableName) {
        return ReadData_from_DB_Table(tableName, "*", "", "");
    }


    public void RunRawQuery(String Statement) {
        RunRawQuery(Statement, false);
    }

    public Cursor RunRawQuery(String Statement, boolean CursorOutput) {
        if (CursorOutput) {
            return db_Handle.rawQuery(Statement, null);
        } else {
            db_Handle.execSQL(Statement);
        }
        return null;
    }

    void CreateApp_SysConf_Table(SQLiteDatabase db_Handle) {
        //db_Handle.execSQL("DROP TABLE IF EXISTS App_SysConf; ");
        db_Handle.execSQL("create table IF NOT EXISTS App_SysConf (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, TimeStamp DATETIME, Tab Text, Var_Name Text, Var_Val TEXT)");
    }

    public static boolean InUp2_SysConf_T_AU (String Tab, String Var_Name, String Var_Val, boolean sync) {
        if (Tab.isEmpty() || Var_Name.isEmpty() || Var_Val.isEmpty()) return false;


        List<String> Result1 = new ArrayList<String>();
        Result1.clear();

        String Name1 =  Get_Val_from_DB_AppUtils("App_SysConf", "Var_Name", "", "Tab = '"+ Tab + "' and Var_Name = '" + Var_Name + "'");

        Log.i (TAG, "In App_SysConf Name1 = " + Name1 + ", Var_Name = " + Var_Name);
        boolean Update = Name1.equals(Var_Name);

        String curr_date = Get_Current_Date_Time();
        RQO_Return_Status[] RRS = new RQO_Return_Status[1];
        RRS[0] =    RQO_Return_Status.RQO_Unknown_Err;

        if (Update ) {
            Log.i (TAG," In App_SysConf Update :::: ===== Tab = "+ Tab + ", Var_Name = " + Var_Name + ", Var_Val = " + Var_Val);
            String UStatement = "UPDATE App_SysConf set Var_Val = '%1', TimeStamp = '%4' where Tab = '%2' and Var_Name = '%3'";

            UStatement = UStatement.replace("%1", Var_Val);
            UStatement = UStatement.replace("%2", Tab);
            UStatement = UStatement.replace("%3", Var_Name);
            UStatement = UStatement.replace("%4", curr_date);
            return RunQueryOnce_AppUtils(UStatement, Result1, true, false);
        } else {
            Log.i (TAG," In App_SysConf Insert :::: ===== Tab = "+ Tab + ", Var_Name = " + Var_Name + ", Var_Val = " + Var_Val);
            String IStatement = "INSERT into App_SysConf  ('TimeStamp', 'Tab', 'Var_Name', 'Var_Val')  values('%4','%1', '%2', '%3');";
            IStatement = IStatement.replace("%1", Tab);
            IStatement = IStatement.replace("%2", Var_Name);
            IStatement = IStatement.replace("%3", Var_Val);
            IStatement = IStatement.replace("%4", curr_date);
            return RunQueryOnce_AppUtils(IStatement, Result1, false, false, RRS, false);
        }
    }

    public static String Get_Var_Val_from_App_SysConf(String Tab) {

        List<String> Result1 = new ArrayList<String>();
        Result1.clear();

        String Statement = String.format("SELECT Var_Val from App_SysConf  where Tab = '%s';", Tab );


        //System.out.println("Statement = " + Statement);
        if (RunQueryOnce_AppUtils(Statement, Result1)) {
            if (Result1.size() > 0) {
                String Output = Result1.get(0);
                Output = Output.trim();
                return Output;
            }
        }
        return "";
    }


    public static String Get_Var_Val_from_App_SysConf(String Tab, String Var_Name) {

        List<String> Result1 = new ArrayList<String>();
        Result1.clear();

        String Statement = String.format("SELECT Var_Val from App_SysConf  where Tab = '%s' and Var_Name = '%s';", Tab, Var_Name );

        //System.out.println("Statement = " + Statement);
        if (RunQueryOnce_AppUtils(Statement, Result1)) {
            if (Result1.size() > 0) {
                String Output = Result1.get(0);
                Output = Output.trim();
                return Output;
            }
        }
        return "";
    }

    public static boolean Del_Var_Val_from_App_SysConf(String Tab, String Var_Name)
    {
        String Statement = "DELETE from App_SysConf  where Tab ='%1' and Var_Name = '%2'";
        //).arg (Tab).arg(Var_Name);
        Statement = Statement.replace("%1", Tab);
        Statement = Statement.replace("%2", Var_Name);

        return RunQueryOnce_AppUtils(Statement);
    }

    public static boolean Clear_Customer_Data () {
        String[] Table_Name = new String[] { "Account", "Transactions"};

        return  false;
    }
}
