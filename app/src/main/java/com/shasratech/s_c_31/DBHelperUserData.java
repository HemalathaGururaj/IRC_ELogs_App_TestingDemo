package com.shasratech.s_c_31;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


//import com.google.android.gms.drive.DriveId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.shasratech.s_c_31.Globals.*;



/**
 * Created by srihari on 17/4/17.
 */

public class DBHelperUserData extends SQLiteOpenHelper {
    public static final int CurrVersion = 2;

    private static final String TAG = "DBHelperUserData";
    private SQLiteDatabase db_Handle;
    public SQLiteDatabase db_Handle_G_UD;
    public SQLiteDatabase db_Handle_Copy;
    public boolean DB_Init_Complete_US = false;
    private boolean AppUtils_B = false;
    private  String DB_Name = "";

    public String Get_DB_Name() {
        return DB_Name;
    }

    public DBHelperUserData(Context context, String AppName, String SiteName) {
        super(context, AppName + "-" + SiteName, null, CurrVersion);
        //Log.i (TAG, "42-AppName-SiteName =" + AppName + "-" + SiteName);
        DB_Name = AppName + "-" + SiteName;

        if ((AppName.equals("AppUtils")) && (SiteName.equals("AppUtils"))) {
            AppUtils_B = true;
        }

        db_Handle = this.getWritableDatabase();
        db_Handle_Copy = db_Handle;
        db_Handle_G_UD = db_Handle;
        if (db_Handle == null) {
            //  Log.i(TAG, "111 XXXXX DBHelperUserData ------- On Create ------------------------------------------ ");
        }
        //  Log.i(TAG, "1111 DBHelperUserData ------------------------------------------------- ");
        InitAll();
        //   Log.i(TAG, "1111 DBHelperUserData -----------------Init Complete -------------------------------- ");

    }

    @Override
    public void onCreate(SQLiteDatabase db_Handle1) {
        if (db_Handle1 != null) {
            db_Handle = db_Handle1;
            //  Log.i(TAG, "2222 DBHelperUserData ------- On Create ------------------------------------------ ");
        } else {
            db_Handle = this.getWritableDatabase();
            //  Log.i(TAG, "3333 DBHelperUserData ------- On Create ------------------------------------------ ");
            if (db_Handle == null) {
                //  Log.i(TAG, "XXXXX DBHelperUserData ------- On Create ------------------------------------------ ");
            }
        }
        db_Handle_Copy = db_Handle;
        //   Log.i(TAG, "4444 DBHelperUserData ------- On Create ------------------------------------------ ");

        InitAll();
    }

    private void InitAll () {
        Create_DB_Tables();
        Manual_Upgrade_Tables();
        Init_Variables();
        DB_Init_Complete_US = true;
    }


    void Create_DB_Tables() {
        Log.i(TAG, "In Create_DB_Tables = =  = = = = = = = = = ");

        if (db_Handle != null) {

            if (!AppUtils_B) {

                // HSM, you can change the these functions to Create_Table_XXXXX
                CreateAccount_Table(db_Handle);
                CreateAutoSaveTrans_Table(db_Handle);
                CreateCustom_Rate_Table(db_Handle);
                CreateDelivery_Gate_Pass_Table(db_Handle);
                CreateExpense_Type_Table(db_Handle);
                CreateInventory_Table(db_Handle);
                Create_Table_Material(db_Handle);
                Create_Table_MoneyTransaction(db_Handle);
                Create_Table_Person(db_Handle);
                Create_Table_Site(db_Handle);
                Create_Table_Transactions(db_Handle);
                Create_Table_Transport(db_Handle);
                Create_Table_Vehicle(db_Handle);
                Create_Table_Computer(db_Handle);
                if (!Create_Table_CustomerDetails()) { return ; }
                if (!Create_Table_SellerComp()) { return ; }
                if (!Create_Table_RFID_Insert()) { return ; }
                if (!Create_Table_RFID_Access()) { return ; }
                if (!Create_Table_AppUsers()) { return ; }
                if (!Create_Table_DriverDetails()) { return ; }
                if (!Create_Table_Location()) { return ; }
                if (!Create_Table_HomeShed()) { return ; }
                if (!Create_Table_CategorizedSolution()) { return ; }
                if (!Create_Table_CategorizedProblem()) { return ; }
                if (!Create_Table_LocoDetails()) { return ; }
                if (!Create_Table_IRC_ELogs()) { return ; }
                if (!Create_Table_TrainDetails()) { return ; }
                if (!Create_Table_Monitor()) { return ; }
            }
        } else {
            //Log.i(TAG, "db_Handle NULL in Create_DB_Tables ======================================");
        }
    }

    public void Init_Variables() {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db_Handle, int oldVersion, int newVersion) {
        Clear_Existing_DB();
        Create_DB_Tables();
    }

    public String GetCompleteDBasString() {
        String output;

        Cursor O_Cursor = ReadData_from_DB_Table("RepeatItems");
        int col_Text_Arr[] = {1, 5};
        output = Get_Insert_Statements_from_Cursor(O_Cursor, "RepeatItems", col_Text_Arr);

        O_Cursor = ReadData_from_DB_Table("RepeatActCount");
        int col_Text_Arr2[] = {1, 2};
        output += Get_Insert_Statements_from_Cursor(O_Cursor, "RepeatActCount", col_Text_Arr2);
        return output;
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

    public boolean Clear_Existing_DB() {
        if (db_Handle == null) { return false; }
        db_Handle.execSQL("DROP TABLE IF EXISTS Transactions; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Account; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS AutoSaveTrans; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS CustomerDetails; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Delivery_Gate_Pass; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Expense_Type; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Inventory; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Material; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS MoneyTransaction; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Person; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Site; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Transactions; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Transport; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Vehicle; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS HomeShed; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS CategorizedSolution; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS CategorizedProblem; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Monitor ");
        db_Handle.execSQL("DROP TABLE IF EXISTS Location; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS DriverDetails; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS TrainDeatils; ");
        db_Handle.execSQL("DROP TABLE IF EXISTS LocoDetails; ");

        return true;
    }

    public String Table_Name_Exists (String Table) {
        String ABCD = "SELECT count(1) FROM sqlite_master WHERE type='table' AND name='"+ Table +"';";
        String ZXCV = RunQueryOnce_S(ABCD);
        //Log.i (TAG, "IsTableExists ?" + ZXCV + "----");
        return ZXCV;
    }

    public void Drop_Table (String Table) {
        //Log.i(TAG, "In Drop_Table Get_DB_Name = " + DB_Name + ", Table = " + Table);

        if ((Table_Name_Exists(Table)).equals("0")) return;
        //Log.i (TAG, "1234567890 Get_DB_Name = " + Get_DB_Name());
        db_Handle.execSQL("DELETE from " + Table + ";");
        db_Handle.execSQL("DROP TABLE IF EXISTS " + Table + ";");
        // Log.i (TAG, "1234567890 Before Count = " + Get_Val_from_DB_UD(Table, "id", "count"));
    }

    public String RunQueryOnce_S(String Statement) {
        Cursor O_Cursor = null;
        String Res = "";

        try {
            O_Cursor = db_Handle.rawQuery(Statement, null);

            if (O_Cursor.getCount() > 0) {
                String Result = "";
                int a = 0;
                int rowCount = O_Cursor.getCount();

                int b = 0;
                int ColCount = O_Cursor.getColumnCount();
                O_Cursor.moveToFirst();
                for (; a < rowCount; a++) {
                    //String Statement = "";
                    for (b = 0; b < ColCount; b++) {
                        String temp = O_Cursor.getString(b);
                        if (temp == null) temp = "";
                        temp = temp.trim();
                        Result += temp;
                        if (b < ColCount - 1) {
                            Result += '\007';
                        }
                        //  Log.i (TAG, "1234567890 DB_Name = " + DB_Name + " RunQueryOnce_UserData temp = "+temp);
                    }
                    Res += Result + "\n";
                    // Result1.add(Result);
                    O_Cursor.moveToNext();
                    Result = "";
                }
            }
        } finally {
            if (O_Cursor != null) {
                O_Cursor.close();
            }
        }
        return Res;
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
        return ReadData_from_DB_Table( tableName,  ColumnName,  Max_Sum_Count,  Condition, "");
    }

    public Cursor ReadData_from_DB_Table(String tableName, String ColumnName, String Max_Sum_Count, String Condition, String OrderBy) {
        // Log.i (TAG, "ReadData_from_DB_Table DB_Name = " + DB_Name);
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

        if (!OrderBy.isEmpty()) {
            myStatement += " Order By " + OrderBy + " ";
        }

        System.out.println("1234567890 "+ DB_Name + " ReadData_from_DB_Table myStatement =" + myStatement);
        // Log.i (TAG, "ReadData_from_DB_Table myStatement =" + myStatement);

        if (db_Handle == null) db_Handle = this.getWritableDatabase();
        Cursor Output = db_Handle.rawQuery(myStatement, null);

        if (Output.getCount() > 0) {
            Output.moveToFirst();
        }

        return Output;
    }

    public  String  Get_Val_from_DB_UD(String  Table) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_UD(Table, "*", "", "", res, "", false, "");
    }

    public   String  Get_Val_from_DB_UD(String  Table, String  myColumn) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_UD(Table, myColumn, "", "", res, "", false, "");
    }

    public   String  Get_Val_from_DB_UD(String  Table, String  myColumn, String  SUM_MAX
    ) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_UD(Table, myColumn, SUM_MAX, "", res, "", false, "");
    }

    public   String  Get_Val_from_DB_UD(String  Table, String  myColumn, String  SUM_MAX, String  Selector
    ) {
        List<String> res = new ArrayList<String>();
        return Get_Val_from_DB_UD(Table, myColumn, SUM_MAX, Selector, res, "", false, "");
    }

    public  String Get_Val_from_DB_UD(String  Table, String  myColumn, String  SUM_MAX, String  Selector,
                                      List<String> res) {
        return Get_Val_from_DB_UD(Table, myColumn, SUM_MAX, Selector, res, "", false, "");
    }

    public   String  Get_Val_from_DB_UD(String  Table, String  myColumn, String  SUM_MAX, String  Selector,
                                        List<String> res, String  Orderby) {
        return Get_Val_from_DB_UD(Table, myColumn, SUM_MAX, Selector, res, Orderby, true, "");
    }

    public   String Get_Val_from_DB_UD(String  Table, String  myColumn, String  SUM_MAX, String  Selector,
                                       List<String> res, String  Orderby, boolean print_err) {
        return Get_Val_from_DB_UD(Table, myColumn, SUM_MAX, Selector, res, Orderby, print_err, "");
    }

    public  String Get_Val_from_DB_UD(String Table, String myColumn, String SUM_MAX, String Selector,
                                      List<String> res1, String Orderby, boolean print_err, String GroupBy) {
        List<String> res = res1;

        // Log.i(TAG, "288 DB_Name = " + DB_Name);
        if (res == null) {
            res = new ArrayList<String>();
        }

        //System.out.println ("myDB.Get_Val_from_DB_UD DB_Name = " + DB_Name +", Table =" + Table + ", myColumn = " + myColumn);
        //System.out.println ("myDB.Get_Val_from_DB_UD myColumn =" + myColumn);

        String  Statement = "SELECT ";
        if (!SUM_MAX.isEmpty()) {
            Statement += SUM_MAX + "(" + myColumn + ") ";
        } else {
            Statement += myColumn;
        }
        Statement += " from " + Table;
        //Statement += Table ;

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

        //System.out.println ("myDB.Get_Val_from_DB_UD Statement =" + Statement);
        Result.clear();
        RunQueryOnce_UserData(Statement, Result, print_err);
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

    public  boolean RunQueryOnce_UserData(String  Statement_I) {
        List<String> Result1 = new ArrayList<String>();
        Result1.clear();
        return RunQueryOnce_UserData (Statement_I, Result1);
    }

    public  boolean RunQueryOnce_UserData(String  Statement_I, List<String> Result1) {
        return RunQueryOnce_UserData (Statement_I, Result1, false);
    }

    public  boolean RunQueryOnce_UserData(String  Statement_I, List<String> Result1, boolean print_err) {
        return RunQueryOnce_UserData (Statement_I, Result1, print_err, false);
    }

    public  boolean RunQueryOnce_UserData(String  Statement_I, List<String> Result1, boolean print_err,
                                          boolean sync_this_statement) {
        RQO_Return_Status[] RQO_Status =  new RQO_Return_Status[1];
        RQO_Status[0] = RQO_Return_Status.RQO_Unknown_Err;

        return RunQueryOnce_UserData (Statement_I, Result1, print_err, sync_this_statement, RQO_Status, false);
    }

    public  boolean RunQueryOnce_UserData(String  Statement_I, List<String> Result1, boolean print_err,
                                          boolean sync_this_statement, RQO_Return_Status[] RQO_Status) {
        return RunQueryOnce_UserData (Statement_I, Result1, print_err, sync_this_statement, RQO_Status, false);
    }

    public  boolean RunQueryOnce_UserData(String  Statement_I, List<String> Result1, boolean print_err,
                                          boolean sync_this_statement, RQO_Return_Status[] RQO_Status, boolean Append2SysLog) {
        String DS_Update_S = "";
        String DS_Table = "";
        String Update_Primary_Key = "";
        return RunQueryOnce_UserData(  Statement_I,  Result1,  print_err,
                sync_this_statement,  RQO_Status, Append2SysLog, DS_Update_S, DS_Table, Update_Primary_Key);
    }


    public boolean RunQueryOnce_UserData(String  Statement_I, List<String> Result1, boolean print_err,
                                         boolean sync_this_statement, RQO_Return_Status[] RQO_Status, boolean Append2SysLog,
                                         String DS_Update_S, String DS_Table, String Update_Primary_Key) {
        if (db_Handle == null) {
            db_Handle = db_Handle_Copy;
        }

        if ( (Statement_I.contains("INSERT")) ||
                (Statement_I.contains("UPDATE"))
                ) {
            db_Handle.execSQL(Statement_I);
            return true;
        }

        Cursor O_Cursor = null;
        boolean Ret_Val = false;
        try {
            O_Cursor = db_Handle.rawQuery(Statement_I, null);

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
                        if (b < ColCount - 1) {
                            Result += '\007';
                        }
                        //  Log.i (TAG, "1234567890 DB_Name = " + DB_Name + " RunQueryOnce_UserData temp = "+temp);
                    }
                    Result1.add(Result);
                    O_Cursor.moveToNext();
                    Result = "";
                }
                Ret_Val = true;
                //return true;
            }
        } finally {
            if (O_Cursor != null) {
                O_Cursor.close();
            }
        }
        return Ret_Val;
    }


    public  boolean Get_Col_Names_from_Table (String Table, List<String> Result) {
        Result.clear();

        List<String> Res = new ArrayList<String>();
        Res.clear();
        String Col_Statement = "PRAGMA table_info(\"" + Table + "\");";
        RunQueryOnce_UserData(Col_Statement, Res);

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

    public  boolean RunQueryOnce_InUp_DS(String Statement_I,  String DS_Update_S, String DS_Table,
                                         String Update_Primary_Key, boolean Dynamic_Sync) {
        List<String> Result1 = new ArrayList<>();
        Result1.clear();
        RQO_Return_Status[] RQO_Status =  new RQO_Return_Status[1];
        RQO_Status[0] = RQO_Return_Status.RQO_Unknown_Err;

        return RunQueryOnce_UserData(Statement_I, Result1, false, Dynamic_Sync,RQO_Status,  false, DS_Update_S, DS_Table, Update_Primary_Key);

    }

    public  boolean Update_field_in_DB_UD (String table_t, String column_t, String value, String where) {
        return Update_field_in_DB_UD ( table_t,  column_t,  value,  where, true );
    }
    public  boolean Update_field_in_DB_UD (String table_t, String column_t, String value, String where, boolean Sync) {
        List<String> Result1 = new ArrayList<>();
        Result1.clear();

        String Statement = "UPDATE %1 set %2 = '%3' where  %4;";
        Statement = Statement.replace ("%1", table_t);
        Statement = Statement.replace ("%2", column_t);
        Statement = Statement.replace ("%3", value);
        Statement = Statement.replace ("%4", where);

        //System.out.println("Statement = " + Statement);

        String Table = "UpdateStatement";
        String P_Key_S = Get_Current_Date_Time_millis();


        // if (Sync && Dynamic_Sync_Table_List.contains(table_t)) {
        return RunQueryOnce_InUp_DS(Statement, Statement, Table, P_Key_S, true);
        // } else {
        //    return RunQueryOnce(Statement, Result1, false, false);
        //  }
        //return RunQueryOnce_InUp_DS(Statement, Statement, Table, P_Key_S, true);
        //return RunQueryOnce(Statement, Result1, false, false);
    }

    public  boolean Update_field_in_DB_UD_Number (String table_t, String column_t, String value, String where) {
        return Update_field_in_DB_UD_Number ( table_t,  column_t,  value,  where, true);
    }
    public  boolean Update_field_in_DB_UD_Number (String table_t, String column_t, String value, String where, boolean Sync) {
        String Statement = "UPDATE %1 set %2 = %3 where  %4;";;

        List<String> Result1 = new ArrayList<>();
        Result1.clear();

        Statement = Statement.replace ("%1", table_t);
        Statement = Statement.replace ("%2", column_t);
        Statement = Statement.replace ("%3", value);
        Statement = Statement.replace ("%4", where);

        String Table = "UpdateStatement";
        String P_Key_S = Get_Current_Date_Time_millis();

        //if (Sync && Dynamic_Sync_Table_List.contains(table_t)) {
        return RunQueryOnce_InUp_DS(Statement, Statement, Table, P_Key_S, true);
        //} else {
        //  return RunQueryOnce(Statement, Result1, false, false);
        //}
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

    /*================================== Account ============================= */

    void CreateAccount_Table(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Account (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, TimeStamp DATETIME, Acc_id Integer, Ref_id Text, Reference Text, "
                + "AccountName Text, Received Decimal, Paid Decimal, Ttl_Payable_by_Com Decimal, Details Text, SourceSite Text ) ");
    }

    public  boolean InUp2_Account_UD ( String P_Key_S1, String[] Con_List, boolean Dynamic_Sync, String Data_String) {
        String Last = "";
        int ColCount = 10;

        Log.i (TAG, " Data_String  =" + Data_String);

        if (Con_List.length < (ColCount -1)) {
            //return false;  // Only for Accounts hoping that accounts can be re-calculated.
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String Table = "Account";
        String TimeStamp = Con_List[0].replace ("'", "");
        String Ref_id = Con_List[2].replace ("'", "");
        String Reference = Con_List[3].replace ("'", "");
        String AcName = Con_List[4].replace ("'", "");
        String Details = Con_List[8].replace ("'", "");
        String SourceSite = Last.replace ("'", "");



        String Sel = "AccountName = '" + AcName + "' and  Ref_id = '" + Ref_id + "' and Reference = '" + Reference + "'";
        // Log.i (TAG, "AcName = " +  AcName + ", Ref_Id = " + Ref_id + " , Reference = " + Reference );

        //Acquire_Named_Sem_Lock(Table + "-" + Con_List[4]);

        // Need to add Data to DB
        boolean Expenses = (((Reference).contains("Expense")) && ((String2Double2P(Con_List[6])) != 0)
                && ((String2Double2P(Con_List[5])) == 0));
        boolean OpeningBal = (Reference).contains("OpeningBal");

        //======================== Check for Duplicate and return if Found =============================
        String Sel1 = String.format("TimeStamp = '%s' and Ref_id = '%s' and Reference = '%s' and AccountName = '%s' and Received = %s and Paid = %s and Ttl_Payable_by_Com = %s",
                TimeStamp, Ref_id, Reference, AcName, String2StringDouble2P(Con_List[5]), String2StringDouble2P(Con_List[6]), String2StringDouble2P(Con_List[7]));
        String Found_Acc_id =  myDB_G.Get_Val_from_DB_UD(Table, "Acc_id", "", Sel1);

        Log.i (TAG, "Found the Same Entry\n" + Sel1  + " \nFound_Acc_id= "  + Found_Acc_id);
        if (!Found_Acc_id.isEmpty()) {
            Log.i (TAG, "Found the Same Entry\n\n" + Sel1  + " so returning ");
            return true;
        }

        //======================== Check for Duplicate and return if Found =============================

        boolean Ret_Val = ((Insert_into_account(AcName, String2Double2P(Con_List[5]), String2Double2P(Con_List[6]),
                Details,  Reference, Ref_id, TimeStamp, Expenses, OpeningBal, "", false )) == RQO_Return_Status.RQO_Success);

        Log.i (TAG, "Insert_into_account Ret_Val = " + Ret_Val);
        if (Ret_Val) {
            Ret_Val &= Update_field_in_DB_UD(Table, "Acc_id", Con_List[1], Sel, false);
            Log.i (TAG, "111 Update_field_in_DB_UD Ret_Val = " + Ret_Val);
            Ret_Val &= Update_field_in_DB_UD(Table, "Ttl_Payable_by_Com", Con_List[7], Sel, false);
            Log.i (TAG, "222 Update_field_in_DB_UD Ret_Val = " + Ret_Val);
        }

        //Release_Named_Sem_Lock (Table + "-" + Con_List[4]);

        return Ret_Val;
        //return false;

//        return Insert_update_into_Account_Ordered();
    }

    public  boolean InUp2_Transactions_UD ( String P_Key_S1, String[] Con_List_t, boolean Dynamic_Sync, boolean Insert_Only, String Data_String) {
        int j = 0;

        if (Con_List_t.length < 30) { return false; }


        String Table = "Transactions";
        int ColCount = 41;
        String[] Con_List = null; //new String[ColCount];
        if (Con_List_t.length < 33) {
            // Need to add arr to arr.
            Con_List = new String[ColCount];
            int a = 0;
            for (; a < Con_List_t.length; a++) {
                Con_List[a] = Con_List_t[a];
            }

            for (a = Con_List_t.length; a < ColCount; a++) {
                Con_List[a] = "0";
            }

        } else {
            Con_List = Con_List_t;
        }


        return Insert_update_into_Transactions_Ordered(Insert_Only,
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++], Con_List[j++],
                Con_List[j++]
        );

    }

    public  boolean InUp2_SysConf_UD ( String P_Key_S) {
        return false;
    }

    public  boolean InUp2_Vehicle_UD ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync) {
        return InUp2_Vehicle_UD (P_Key_S, Con_List, Dynamic_Sync,  false,  "");
    }
    public  boolean InUp2_Vehicle_UD ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, boolean Insert_Only, String Data_String) {
        String Last = "";
        int ColCount = 14;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String Table = "Vehicle";
        String Sel = "ID_Number = '" + P_Key_S + "'";
        String Owner = myDB_G.Get_Val_from_DB_UD(Table, "ID_Number", "", Sel);


        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" + Con_List[4] + "'," + Con_List[5] + "," +  Con_List[6] + ",'"
                + (Con_List[7]).replace('\n', '\05').replace(',', '\04')
                + "','" + Con_List[8] + "','" + Con_List[9] + "','"
                + Con_List[10] + "','" + Con_List[11] + "','" + Con_List[12] + "','"
                + Last  + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + "(Create_TS,Modify_TS,User,ID_Number,Type,CFT,TareWeight,OtherDetails,Disabled,Customer,Transport,FC_Date,Insurance_Date,RoadTax_Date) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            Statement = "UPDATE " + Table + " set Modify_TS = '"
                    + Con_List[1] + "', Type = '" + Con_List[4] + "', CFT = " + Con_List[5]
                    + ", TareWeight = " + Con_List[6] + ", OtherDetails = '"
                    + ((Con_List[7]).replace('\n', '\05')).replace(',', '\04')
                    + "', Disabled = '" + Con_List[8] + "', Customer = '" +Con_List[9]
                    + "', Transport = '" + Con_List[10] + "', FC_Date = '" + Con_List[11]
                    + "', Insurance_Date = '" + Con_List[12] + "', RoadTax_Date = '"
                    + Last +  "' where "  + Sel + ";";
        }

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");

        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');

            return RunQueryOnce(Statement);
        }
    }

    boolean Insert_update_into_Account_Ordered(boolean Insert, String TimeStamp, String Acc_id, String Ref_id, String Reference,
                                               String AccountName, String Received, String Paid, String Ttl_Payable_by_Com,
                                               String Details, String SourceSite  ) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;

        contentvalues.put("Acc_id", Acc_id);
        contentvalues.put("Ref_id", Ref_id);
        contentvalues.put("Reference", Reference);
        contentvalues.put("AccountName", AccountName);
        contentvalues.put("Received", Received);
        contentvalues.put("Paid", Paid);
        contentvalues.put("Ttl_Payable_by_Com", Ttl_Payable_by_Com);
        contentvalues.put("Details", Details);
        contentvalues.put("SourceSite", SourceSite);



        if (Insert) {
            contentvalues.put("TimeStamp", TimeStamp);
            int ret_val = (int) db_Handle.insertWithOnConflict("Account", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "TimeStamp = '" + TimeStamp ;
            int ret_val = (int) db_Handle.update("Account", contentvalues,WhereCluause, null  );
        }
        return false;
    }

    /*================================== Account ============================= */


    /*================================== AutoSaveTrans ============================= */
    void CreateAutoSaveTrans_Table(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS AutoSaveTrans (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Id Integer, TimeStamp DATETIME, Load_Val Integer, Type Text, Mapped_ID Text ); ");
    }

    boolean Insert_update_into_AutoSaveTrans_Ordered(boolean Insert, String TimeStamp, String id, String Load_Val, String Type, String Mapped_ID ) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;


        contentvalues.put("id", id);
        contentvalues.put("Load_Val", Load_Val);
        contentvalues.put("Type", Type);
        contentvalues.put("Mapped_ID", Mapped_ID);

        if (Insert) {
            contentvalues.put("TimeStamp", TimeStamp);
            int ret_val = (int) db_Handle.insertWithOnConflict("AutoSaveTrans", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "TimeStamp = '" + TimeStamp ;
            int ret_val = (int) db_Handle.update("AutoSaveTrans", contentvalues,WhereCluause, null  );
        }
        return false;
    }
    /*================================== AutoSaveTrans ============================= */

    /*================================== Custom_Rate ============================= */
    void CreateCustom_Rate_Table(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Custom_Rate (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, TimeStamp DATETIME, Id Integer, Create_Time DATETIME, Customer Text, Material Text, "
                + "Site Text, Price Decimal, BasePrice Decimal ) ");
    }

    boolean Insert_update_into_Custom_Rate_Ordered(boolean Insert, String TimeStamp, String id, String Create_Time, String Customer,
                                                   String Material, String Site, String Price, String BasePrice) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;


        contentvalues.put("id", id);
        contentvalues.put("TimeStamp", TimeStamp);
        contentvalues.put("Create_Time", Create_Time);
        contentvalues.put("Price", Price);
        contentvalues.put("BasePrice", BasePrice);


        if (Insert) {
            contentvalues.put("Site", Site);
            contentvalues.put("Customer", Customer);
            contentvalues.put("Material", Material);
            int ret_val = (int) db_Handle.insertWithOnConflict("Custom_Rate", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Customer  = '" + Customer + "' and Material = '" + Material + "' and Site = '" + Site +"'";
            int ret_val = (int) db_Handle.update("Custom_Rate", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== Custom_Rate ============================= */

    /*================================== Delivery_Gate_Pass ============================= */
    void CreateDelivery_Gate_Pass_Table(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Delivery_Gate_Pass (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, TimeStamp DATETIME, Id Integer, Create_Time TimeStamp, SourceSite Text, "
                + "InvoiceNum Text, Supplier Text,Vehicle Text, Cost Integer, Supervisor Text,  Balance Integer) ");
    }

    boolean Insert_update_into_Delivery_Gate_Pass_Ordered(boolean Insert, String TimeStamp, String id, String Create_Time,
                                                          String SourceSite, String InvoiceNum, String Supplier, String Vehicle, String Cost, String Supervisor, String  Balance) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;


        contentvalues.put("id", id);
        contentvalues.put("TimeStamp", TimeStamp);
        contentvalues.put("Create_Time", Create_Time);
        contentvalues.put("SourceSite", SourceSite);
        contentvalues.put("Supplier", Supplier);
        contentvalues.put("Vehicle", Vehicle);
        contentvalues.put("Cost", Cost);
        contentvalues.put("Supervisor", Supervisor);
        contentvalues.put("Balance", Balance);


        if (Insert) {
            contentvalues.put("InvoiceNum", InvoiceNum);
            int ret_val = (int) db_Handle.insertWithOnConflict("Delivery_Gate_Pass", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "InvoiceNum = '" + InvoiceNum + "'";
            int ret_val = (int) db_Handle.update("Delivery_Gate_Pass", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== Delivery_Gate_Pass ============================= */

    /*================================== Expense_Type ============================= */
    void CreateExpense_Type_Table(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Expense_Type (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, TimeStamp DATETIME, Id Integer, Expense_Type Text  ) ");
    }

    boolean Insert_update_into_Expense_Type_Ordered(boolean Insert, String TimeStamp, String id, String Expense_Type) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;


        contentvalues.put("id", id);
        contentvalues.put("TimeStamp", TimeStamp);


        if (Insert) {
            contentvalues.put("Expense_Type", Expense_Type);
            int ret_val = (int) db_Handle.insertWithOnConflict("Expense_Type", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Expense_Type = '" + Expense_Type + "'";
            int ret_val = (int) db_Handle.update("Expense_Type", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== Expense_Type ============================= */

    /*================================== Inventory ============================= */
    void CreateInventory_Table(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Inventory (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, TimeStamp DATETIME, Id Integer, Create_Time TimeStamp, SourceSite Text, M_Name Text,"
                + "Batch_ID Text, Rate Integer, In_Out Text, Init_Quantity Integer, Remain_Quantity Integer, Supervisor Text, Supplier Text,"
                + "Consumer Text, Alert Integer, Invoice Text ) ");
    }

    boolean Insert_update_into_Inventory_Ordered(boolean Insert, String TimeStamp, String id, String Create_Time, String SourceSite,
                                                 String M_Name, String Batch_ID, String Rate, String In_Out, String Init_Quantity, String Remain_Quantity, String Supervisor,
                                                 String Supplier, String Consumer, String Alert, String Invoice ) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;


        contentvalues.put("id", id);
        contentvalues.put("TimeStamp", TimeStamp);
        contentvalues.put("SourceSite", SourceSite);
        contentvalues.put("M_Name", M_Name);
        contentvalues.put("Batch_ID", Batch_ID);
        contentvalues.put("Rate", Rate);
        contentvalues.put("In_Out", In_Out);
        contentvalues.put("Init_Quantity", Init_Quantity);
        contentvalues.put("Remain_Quantity", Remain_Quantity);
        contentvalues.put("Supervisor", Supervisor);
        contentvalues.put("Supplier", Supplier);
        contentvalues.put("Consumer", Consumer);
        contentvalues.put("Alert", Alert);
        contentvalues.put("Invoice", Invoice);


        if (Insert) {
            contentvalues.put("Create_Time", Create_Time);
            int ret_val = (int) db_Handle.insertWithOnConflict("Inventory", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Create_Time = '" + Create_Time + "' ";
            int ret_val = (int) db_Handle.update("Inventory", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== Inventory ============================= */

    /*================================== Material ============================= */
    void Create_Table_Material(SQLiteDatabase db_Handle) {

        db_Handle.execSQL("create table IF NOT EXISTS Material (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                "Name Text, Price Decimal, Units Decimal,"
                + "Disabled Text, OtherDetails Text, CFT_Rate TEXT,  CFTs_per_Ton TEXT) ");
    }

    boolean Insert_update_into_Material_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                String User, String Name, String Price,
                                                String Units, String Disabled, String OTherDetails, String CFT_Rate, String CFTs_per_Ton ) {

        boolean Insert =  Insert_I;
        ContentValues contentvalues = new ContentValues();

        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("User", User);
        contentvalues.put("Price", Price);
        contentvalues.put("Units", Units);
        contentvalues.put("Disabled", Disabled);
        contentvalues.put("OTherDetails", OTherDetails);
        contentvalues.put("CFT_Rate", CFT_Rate);
        contentvalues.put("CFTs_per_Ton", CFTs_per_Ton);

        if ((Get_Val_from_DB_UD("Material", "Name", "", "Name = '" + Name + "'")).equals(Name)) {
            Insert = false;
        }

        if (Insert) {
            contentvalues.put("Name", Name);
            int ret_val = (int) db_Handle.insertWithOnConflict("Material", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Name = '" + Name + "'";
            int ret_val = (int) db_Handle.update("Material", contentvalues,WhereCluause, null  );
            return true;
        }

        return false;
    }

    /*================================== Material ============================= */

    /*================================== MoneyTransaction ============================= */
    void Create_Table_MoneyTransaction(SQLiteDatabase db_Handle) {
        String Statement = "create table IF NOT EXISTS MoneyTransaction (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, id Integer, Create_TS DATETIME, User Text, Amount Decimal, Type Text, From_Ac Text, To_Ac Text, "
                + "From_Bank Text, To_Bank Text, ChequeDetails Text, OtherDetails Text) ";

        Log.i (TAG, "954 1234567890 myDB_G.Get_DB_Name = " + Get_DB_Name());
        Log.i (TAG, " 1234567890 Creating Create_Table_MoneyTransaction Statement = " + Statement);

        db_Handle.execSQL(Statement);
        Log.i (TAG, "1234567890 after Count = " + Get_Val_from_DB_UD("MoneyTransaction", "id", "count"));
    }

    boolean Insert_update_into_MoneyTransaction_Ordered(boolean Insert, String Create_TS,String User, String id, String Type, String From_Ac,
                                                        String To_Ac, String From_Bank, String To_Bank, String Cheque_Details, String Amount, String OtherDetails ) {
        ContentValues contentvalues = new ContentValues();



        contentvalues.put("id", id);
        contentvalues.put("Type", Type);
        contentvalues.put("From_Ac", From_Ac);
        contentvalues.put("To_Ac", To_Ac);
        contentvalues.put("From_Bank", From_Bank);
        contentvalues.put("To_Bank", To_Bank);
        contentvalues.put("Cheque_Details", Cheque_Details);
        contentvalues.put("Amount", Amount);
        contentvalues.put("OtherDetails", OtherDetails);
        contentvalues.put("User", User);

        if (Insert) {
            contentvalues.put("Create_TS", Create_TS);
            int ret_val = (int) db_Handle.insertWithOnConflict("MoneyTransaction", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Create_TS = '" + Create_TS + "'";
            int ret_val = (int) db_Handle.update("MoneyTransaction", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== MoneyTransaction ============================= */

    /*================================== Person ============================= */
    void Create_Table_Person(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Person (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,  Create_TS TIMESTAMP, Modify_TS TIMESTAMP, User Text, Person_Type Text, "
                + "Name Text, Address Text, PhoneNum Text, Print_Display_Name Text, GSTNum TEXT, SellerComp TEXT,  Disabled Text ) ");
    }

    boolean Insert_update_into_Person_Ordered(boolean Insert, String Create_TS, String Modify_TS, String User, String Person_Type,
                                              String Name, String Address, String PhoneNum, String Print_Display_Name, String GSTNum, String SellerComp, String Disabled ) {
        ContentValues contentvalues = new ContentValues();

        contentvalues.put("User", User);
        contentvalues.put("Address", Address);
        contentvalues.put("PhoneNum", PhoneNum);
        contentvalues.put("Print_Display_Name", Print_Display_Name);
        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("Disabled", Disabled);
        contentvalues.put("GSTNum", GSTNum);
        contentvalues.put("SellerComp", SellerComp);

        if (Insert) {
            //Log.i (TAG, "IN INSert Person ==========================================================");
            contentvalues.put("Person_Type", Person_Type);
            contentvalues.put("Name", Name);
            int ret_val = (int) db_Handle.insertWithOnConflict("Person", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            //Log.i (TAG, "IN Update Person ==========================================================");
            String WhereCluause = "Person_Type = '" + Person_Type + "' and Name = '" + Name + "'";
            int ret_val = (int) db_Handle.update("Person", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== Person ============================= */

    /*================================== Site ============================= */
    void Create_Table_Site(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Site (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, Type Text,  Owner Text, Name Text, Material Text," +
                " Distance Integer, Latitude Text, Longitude Text, Rate_Local Decimal, Rate_Remote Decimal, CFT_Rate Decimal, GST_Included Text, " +
                "Disabled Text, Extra Text, Address TEXT) ");
    }

    boolean Insert_update_into_Site_Ordered(boolean Insert, String Create_TS, String Modify_TS, String User, String Type, String Owner, String Name,
                                            String Material, String Distance, String Latitude, String Longitude, String Rate_Local, String Rate_Remote,
                                            String CFT_Rate, String GST_Included, String Disabled, String Extra, String Address)
    {
        ContentValues contentvalues = new ContentValues();

        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("User", User);
        contentvalues.put("Distance", Distance);
        contentvalues.put("Latitude", Latitude);
        contentvalues.put("Longitude", Longitude);
        contentvalues.put("Rate_Local", Rate_Local);
        contentvalues.put("Rate_Remote", Rate_Remote);
        contentvalues.put("CFT_Rate", CFT_Rate);
        contentvalues.put("GST_Included", GST_Included);
        contentvalues.put("Disabled", Disabled);
        contentvalues.put("Extra", Extra);
        contentvalues.put("Address", Address);

        String Sel = String.format("Type = '%s' and Owner = '%s' and Name = '%s' and Material = '%s'", Type, Owner, Name, Material);

        boolean Insert_Local = (Get_Val_from_DB_UD("Site", "Name", "", Sel)).equals(Name);
        if (!Insert_Local) {
            contentvalues.put("Type", Type);
            contentvalues.put("Name", Name);
            contentvalues.put("Owner", Owner);
            contentvalues.put("Material", Material);
            int ret_val = (int) db_Handle.insertWithOnConflict("Site", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            // Log.i (TAG, "Insert into SIte");
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Type = '" + Type +  "' and Name = '" + Name + "' and Owner = '" + Owner + "' and Material = '" + Material + "'";
            int ret_val = (int) db_Handle.update("Site", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== Site_Details ============================= */

    /*================================== Transactions ============================= */


    void Create_Table_Transactions(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Transactions (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,  id INTEGER NOT NULL, Create_TS TimeStamp, Cust_Suplr TEXT, Material Text ," +
                "Met_Weight INTEGER NOT NULL, Rate Decimal NOT NULL,Total_Val INTEGER NOT NULL,  Ttl_Met_Val INTEGER NOT NULL,Transport_Cost Integer NOT NULL," +
                " Vehicle TEXT , TimeStamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "In_Out TEXT ,Empty TEXT, " +
                "Payment Text,  Load_Val INTEGER NOT NULL, TareWeight INTEGER NOT NULL," +
                " Cash INTEGER NOT NULL , Credit INTEGER NOT NULL," +
                " Quarry TEXT ,Site TEXT ,Transport TEXT, DC_Num TEXT, Other_Details TEXT," +
                " DC_Num_R TEXT, Matched TEXT,CFT_Val Integer, GST_Val Decimal, Permit_Val Decimal, " +
                "User TEXT, Permit_KG Integer, Order_ID TEXT, CGST TEXT, SGST TEXT, IGST TEXT, InvoiceNum TEXT, " +
                "Col5 TEXT, Col6 TEXT, Col7 TEXT, Col8 TEXT, Col9 TEXT, Col10 TEXT) ");
    }

    public  boolean Insert_update_into_Transactions_Ordered(boolean Insert, String TimeStamp, String id, String Vehicle, String In_Out,
                                                            String Empty, String Payment, String Material, String Load_Val, String TareWeight,
                                                            String Met_Weight, String Rate, String Ttl_Met_Val, String Transport_Cost, String Total_Val,
                                                            String Cash, String Credit, String Cust_Suplr, String Quarry, String Site, String Transport,
                                                            String DC_Num, String Other_Details, String Create_TS, String DC_Num_R, String Matched,
                                                            String CFT_Val, String GST_Val, String Permit_Val, String User, String Permit_KG, String Order_ID,
                                                            String CGST, String SGST, String IGST, String InvoiceNum, String Col5,
                                                            String Col6, String Col7, String Col8, String Col9, String Col10 ) {


        ContentValues contentvalues = new ContentValues();

        contentvalues.put("TimeStamp", TimeStamp);
        contentvalues.put("id", id);
        contentvalues.put("Cust_Suplr", Cust_Suplr);
        contentvalues.put("Material", Material);
        contentvalues.put("Vehicle", Vehicle);
        contentvalues.put("Total_Val", Total_Val);
        contentvalues.put("GST_Val", GST_Val);
        contentvalues.put("Permit_Val", Permit_Val);
        contentvalues.put("CFT_Val", CFT_Val);
        contentvalues.put("Load_Val", Load_Val);
        contentvalues.put("TareWeight", TareWeight);
        contentvalues.put("Met_Weight", Met_Weight);
        contentvalues.put("Rate", Rate);
        contentvalues.put("Ttl_Met_Val", Ttl_Met_Val);
        contentvalues.put("Transport_Cost", Transport_Cost);
        contentvalues.put("Cash", Cash);
        contentvalues.put("Credit", Credit);
        contentvalues.put("In_Out", In_Out);
        contentvalues.put("Empty", Empty);
        contentvalues.put("Payment", Payment);
        contentvalues.put("Quarry", Quarry);
        contentvalues.put("Site", Site);
        contentvalues.put("Transport", Transport);
        contentvalues.put("DC_Num", DC_Num);
        contentvalues.put("DC_Num_R", DC_Num_R);
        contentvalues.put("Matched", Matched);
        contentvalues.put("User", User);
        contentvalues.put("Other_Details", Other_Details);
        contentvalues.put("Permit_KG", Permit_KG);
        contentvalues.put("Order_ID", Order_ID);
        contentvalues.put("CGST", CGST);
        contentvalues.put("SGST", SGST);
        contentvalues.put("IGST", IGST);
        contentvalues.put("InvoiceNum", InvoiceNum);
        contentvalues.put("Col5", Col5);
        contentvalues.put("Col6", Col6);
        contentvalues.put("Col7", Col7);
        contentvalues.put("Col8", Col8);
        contentvalues.put("Col9", Col9);
        contentvalues.put("Col10", Col10);

        String Create_TS1 = Get_Val_from_DB_UD("Transactions", "Create_TS", "",
                "Create_TS = '" + Create_TS + "' and Cust_Suplr = '" + Cust_Suplr + "' and Vehicle = '"  + Vehicle + "' and Material = '" +
                        Material + "'");

        if ((Create_TS1 != null)  && (!Create_TS1.isEmpty()) && (Create_TS1.equals(Create_TS))) {
            // ALready in the DB. So just return true.
            return true;
        }

        if (Insert) {
            contentvalues.put("Create_TS", Create_TS);
            int ret_val = (int) db_Handle.insertWithOnConflict("Transactions", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Create_TS = '" + Create_TS + "'";
            int ret_val = (int) db_Handle.update("Transactions", contentvalues,WhereCluause, null  );
        }

        return false;
    }

    /*================================== Transactions ============================= */

    /*================================== Transport ============================= */
    void Create_Table_Transport(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Transport (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User TEXT, Name TEXT, Customer TEXT, Site TEXT," +
                " Rate Integer, Addition Integer, Disabled TEXT ) ");
    }

    boolean Insert_update_into_Transport_Ordered(boolean Insert_I, String Create_TS,  String Modify_TS,  String User, String Name, String Customer,
                                                 String Site, String Rate, String Addition, String Disabled) {
        ContentValues contentvalues = new ContentValues();

        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("User", User);
        contentvalues.put("Rate", Rate);
        contentvalues.put("Addition", Addition);
        contentvalues.put("Disabled", Disabled);


        String Sel = String.format(" Customer = '%s' and Name = '%s' and Site = '%s' ", Customer, Name, Site);

        String Name1 = myDB_G.Get_Val_from_DB_UD("Transport", "Name", "", Sel);

        boolean Insert = Insert_I;
        if ((Name1 != null) && (!Name1.isEmpty())) {
            Insert = false;
        }

        if (Insert) {
            contentvalues.put("Name", Name);
            contentvalues.put("Customer", Customer);
            contentvalues.put("Site", Site);
            int ret_val = (int) db_Handle.insertWithOnConflict("Transport", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Name = '" + Name + "' and Site = '" + Site + "' and Customer = '" + Customer + "'";
            int ret_val = (int) db_Handle.update("Transport", contentvalues, WhereCluause, null  );
            if (ret_val > 0) {
                return true;
            }
        }

        return false;
    }

    /*================================== Transport_Site_Extra ============================= */

    /*================================== Vehicle ============================= */
    void Create_Table_Vehicle(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Vehicle (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, ID_Number Text Not Null, " +
                "Type Text Not Null,"
                + " CFT Integer Not Null, TareWeight Integer Not Null, OtherDetails Text, "
                + "Disabled Text, Customer Text, Transport Text, FC_Date Text, Insurance_Date Text, RoadTax_Date Text  ) ");
    }

    boolean Insert_update_into_Vehicle_Ordered(boolean Insert, String Create_TS, String Modify_TS, String User, String ID_Number, String Type, String CFT,
                                               String TareWeight, String OtherDetails, String Disabled, String Customer, String Transport, String FC_Date,
                                               String Insurance_Date, String RoadTax_Date) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;

        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("User", User);
        contentvalues.put("Type", Type);
        contentvalues.put("CFT", CFT);
        contentvalues.put("TareWeight", TareWeight);
        contentvalues.put("OtherDetails", OtherDetails);
        contentvalues.put("Disabled", Disabled);
        contentvalues.put("Customer", Customer);
        contentvalues.put("Transport", Transport);
        contentvalues.put("FC_Date", FC_Date);
        contentvalues.put("Insurance_Date", Insurance_Date);
        contentvalues.put("RoadTax_Date", RoadTax_Date);

        if (Insert) {
            contentvalues.put("ID_Number", ID_Number);
            int ret_val = (int) db_Handle.insertWithOnConflict("Vehicle", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "ID_Number = '" + ID_Number + "'";
            int ret_val = (int) db_Handle.update("Vehicle", contentvalues,WhereCluause, null  );
        }

        return false;
    }
    /*================================== Vehicle ============================= */

    /*================================== Computer ============================= */
    void Create_Table_Computer(SQLiteDatabase db_Handle) {
        db_Handle.execSQL("create table IF NOT EXISTS Computers (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Name Text) ");
    }

    boolean Insert_update_into_Computer_Ordered(String Name) {
        ContentValues contentvalues = new ContentValues();
        //Long tsLong = System.currentTimeMillis()/1000;


        contentvalues.put("Name", Name);
        int ret_val = (int) db_Handle.insertWithOnConflict("Computers", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
        if (ret_val > 0) {
            return true;
        }


        return false;
    }

    boolean Delete_Computer_Ordered(String Name) {
        String DeleteStatement = "Delete from Computers where Name = '" + Name + "'";
        return RunQueryOnce_UserData(DeleteStatement);
    }
    /*================================== Computer ============================= */

    public void RunRawQuery_C(String Statement) {
        RunRawQuery_C(Statement, false);
    }

    public Cursor RunRawQuery_C(String Statement, boolean CursorOutput) {
        if (CursorOutput) {
            return db_Handle.rawQuery(Statement, null);
        } else {
            db_Handle.execSQL(Statement);
        }
        return null;
    }

    public  boolean RunQueryOnce(String  Statement_I) {
        List<String> Result1 = new ArrayList<>();
        Result1.clear();
        return RunQueryOnce (Statement_I, Result1);
    }

    public  boolean RunQueryOnce(String  Statement_I, List<String> Result1) {
        return RunQueryOnce (Statement_I, Result1, false);
    }

    public  boolean RunQueryOnce(String  Statement_I, List<String> Result1, boolean print_err) {
        return RunQueryOnce (Statement_I, Result1, print_err, false);
    }

    public  boolean RunQueryOnce(String  Statement_I, List<String> Result1, boolean print_err,
                                 boolean sync_this_statement) {
        RQO_Return_Status[] RQO_Status =  new RQO_Return_Status[1];
        RQO_Status[0] = RQO_Return_Status.RQO_Unknown_Err;

        return RunQueryOnce (Statement_I, Result1, print_err, sync_this_statement, RQO_Status, false);
    }

    public  boolean RunQueryOnce(String  Statement_I, List<String> Result1, boolean print_err,
                                 boolean sync_this_statement, RQO_Return_Status[] RQO_Status) {
        return RunQueryOnce (Statement_I, Result1, print_err, sync_this_statement, RQO_Status, false);
    }

    public  boolean RunQueryOnce(String  Statement_I, List<String> Result1, boolean print_err,
                                 boolean sync_this_statement, RQO_Return_Status[] RQO_Status, boolean Append2SysLog) {
        //if (db_Handle == null) db_Handle = this.getWritableDatabase();

        //System.out.println ("RunQueryOnce Statement = " + Statement_I);
        //Log.i (TAG, "RunQueryOnce Statement = " + Statement_I);


        if ((Statement_I.contains("INSERT")) ||
                (Statement_I.contains("UPDATE"))
                ) {
            try {
                db_Handle.execSQL(Statement_I);
            } catch (SQLiteException e) {
                return false;
            }
            return true;
        }

        Cursor O_Cursor = null;
        boolean Ret_Val = false;
        try {
            O_Cursor = db_Handle.rawQuery(Statement_I, null);

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
                        // Log.w (TAG, "temp = " + temp);
                        if (b < ColCount) {
                            Result += '\007';
                        }
                        // Log.i (TAG, "RunQueryOnce temp = "+temp);
                    }
                    //Log.w (TAG, "Result = " + Result);
                    Result1.add(Result);
                    O_Cursor.moveToNext();
                    Result = "";
                }
                Ret_Val = true; //return true;
            }
        } finally {
            if (O_Cursor != null) {
                O_Cursor.close();
            }
        }
        return Ret_Val;
    }

    boolean InUp2_CustomerDetails ( String P_Key_S, String[] Con_List_t, boolean Dynamic_Sync) {
        String Last = "";
        int ColCount = 15;

        // Log.i (TAG, "Con_List_t count = " + Con_List_t.length);

        String[] Con_List = null;
        if (Con_List_t.length == (ColCount -1)) {
            Con_List = Con_List = new String[ColCount];
            int a = 0;
            for (; a < Con_List_t.length; a++) {
                Con_List[a] = Con_List_t[a];

                // Log.i (TAG, "Con_List_t ["+ a + "] = " + Con_List_t[a]);
            }
            for (a = Con_List_t.length; a < ColCount; a++) {
                Con_List[a] = "0";
            }

        } else {
            Con_List = Con_List_t;
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }


        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {

        }
        Last =  Con_List[ColCount - 1];

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "CustomerDetails";
        String Sel = "CustomerName = '" + P_Key_L[0] + "'";
        String Owner = Get_Val_from_DB_UD(Table, "CustomerName", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "'," + Con_List[4] + ",'"  + Con_List[5] + "',"
                + Con_List[6] + "," + Con_List[7] + "," + Con_List[8] + "," + Con_List[9] + ",'"
                + Con_List[10] + "','" + Con_List[11] + "','"  + Con_List[12] + "','" + Con_List[13] + "','"
                + Con_List[14] + "'," + Con_List[14] + ",'"  +  Last + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + "(Create_TS,Modify_TS,User,CustomerName,CreditLimit,SellerComName,Permit_Pct,CGST_Pct,SGST_Pct,Num_Prints,TP_Print_Cash,TP_Print_CompD,PO_Num,Tax_Invoice_Remarks,ISGST_Pct,CreditLimitAlarm,Reference) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {

            // Need to Update
            Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "' , User = '" + Con_List[2] + "' , CreditLimit = "
                    + Con_List[4] + ", SellerComName = '" + Con_List[5] + "', Permit_Pct = " +  Con_List[6]
                    + " ,  CGST_Pct = " +  Con_List[7] + " ,  SGST_Pct = " +  Con_List[8]
                    + " ,  Num_Prints = " +  Con_List[9]
                    + " ,  TP_Print_Cash = '" +  Con_List[10] + "' ,  TP_Print_CompD = '" +  Con_List[11]
                    + "' ,  PO_Num = '" +  Con_List[12] + "' ,  Tax_Invoice_Remarks = '"
                    +  Con_List[13] + "' ,  ISGST_Pct = " + Con_List[14] + ", CreditLimitAlarm = " +  Con_List[15] + "' ,  Reference = '"
                    + Last +  "' where "  + Sel + ";";
        }

        DS_Update_S = DS_Update_S.replace("'", "");

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        System.out.println("Statement = " + Statement);

        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean InUp2_Material ( String P_Key, String[] Con_List, boolean Dynamic_Sync) {
        String Last = "";
        int ColCount = 10;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }

        String Table = "Material";
        String Sel = "Name = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Name", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "'," +  Con_List[4] + "," + Con_List[5] + ",'" + Con_List[6]+ "','"
                + ((Con_List[7]).replace('\n', '\05')).replace(',', '\04') + Con_List[8]+ "','"
                + "','" + Last + "'";

        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            //if (!Name.equals(P_Key)) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Name, Price, Units, Disabled, OTherDetails, CFT_Rate, CFTs_per_Ton)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Price = " + Con_List[4] + ", Units = "
                        + Con_List[5] + ", Disabled = '" +  Con_List[6] +  "', OtherDetails = '"
                        + ((Con_List[7]).replace('\n', '\05')).replace(',', '\04') + "', CFT_Rate = '" +  Con_List[8]
                        + "', CFTs_per_Ton = '"+ Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        //System.out.println("Statement = " + Statement);
        DS_Update_S = DS_Update_S.replace("'", "");

        if (Dynamic_Sync) {
            //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key, Dynamic_Sync);
            if ((Name == null) || (Name.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean InUp2_Person ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync) {
        String Last = "";
        int ColCount = 11;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "Person";
        String Sel = "Person_Type = '" + P_Key_L[0] + "' and Name = '" + P_Key_L[1] + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Name", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','"
                + ((Con_List[5]).replace('\n', '\05')).replace(',', '\04')
                + "','" + Con_List[6]+ "','"
                + Con_List[7]+ "','" + Con_List[8]+ "','"  + Con_List[9]+ "','"  +  Last + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + "(Create_TS, Modify_TS, User, Person_Type, Name, Address, PhoneNum," +
                        "Print_Display_Name, GSTNum, SellerComp, Disabled) values(" + DS_Update_S + ");";

            } else {
                return false;
            }
        } else {
            // Need to Update
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);

            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Address = '"
                        + ((Con_List[5]).replace('\n', '\05')).replace(',', '\04')
                        + "', PhoneNum  = '" + Con_List[6] + "', Print_Display_Name  = '"
                        + Con_List[7] + "', GSTNum  = '" + Con_List[8] + "', SellerComp = '" + Con_List[9] +  "', Disabled  = '"
                        + Last +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        //System.out.println("Statement = " + Statement);
        DS_Update_S = DS_Update_S.replace("'", "");

        if (Dynamic_Sync) {
            //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean InUp2_Site ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync) {
        String Last = "";
        int ColCount = 16;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "Site";
        String Sel = "Type = '" + P_Key_L[0] + "' and Owner = '" + P_Key_L[1] + "' and Name = '" + P_Key_L[2] + "' and Material = '" + P_Key_L[3] + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Owner", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','" + Con_List[5] + "','" + Con_List[6]+ "',"
                + Con_List[7]+ ",'" + Con_List[8]+ "','"  + Con_List[9]+ "',"  + Con_List[10]+ ","  + Con_List[11]+ ","
                + Con_List[12]+ ",'"  + Con_List[13]+ "','"  + Con_List[14]+ "','"
                + Last + "', '_'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into Site (Create_TS , Modify_TS , User , Type ,  Owner , Name , Material, Distance, Latitude, Longitude, Rate_Local, Rate_Remote, " +
                        "CFT_Rate, GST_Included, Disabled, Extra, Address)  values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);

            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Type = '" + Con_List[3] + "', Distance  = " + Con_List[7] + ", Latitude  = '" + Con_List[8]
                        + "', Longitude  = '" + Con_List[9] + "', Rate_Local  = " + Con_List[10] + ", Rate_Remote  = " + Con_List[11]
                        + ", CFT_Rate  = " + Con_List[12] + ", GST_Included  = '" + Con_List[13] + "', Disabled  = '"
                        + Con_List[14] + "', Extra  = '" + Last +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        //System.out.println("Statement = " + Statement);
        //DS_Update_S = DS_Update_S.replace("'", "");
        //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");

        //System.out.println("Statement = " + Statement);
        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }
    boolean InUp2_MoneyTransaction_UD ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync) {
        return InUp2_MoneyTransaction_UD (P_Key_S, Con_List, Dynamic_Sync, false, "");
    }

    boolean InUp2_MoneyTransaction_UD ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync,
                                        boolean Insert, String Data) {
        String Last = "";
        int ColCount = 11;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String Table = "MoneyTransaction";
        String Sel = "Create_TS = '" + P_Key_S + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Create_TS", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "'," + Con_List[2] + ",'"
                + Con_List[3] + "','" + Con_List[4] + "','" + Con_List[5] + "','" +  Con_List[6] + "','"
                + Con_List[7] + "','" + (Con_List[8]).replace('\n', '\05').replace(',', '\04') + "'," + Con_List[9] + ",'"
                + Last.replace('\n', '\05').replace(',', '\04')  + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + "(Create_TS,User,id,Type,From_Ac,To_Ac,From_Bank,To_Ac,ChequeDetails,Amount,OtherDetails) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            return Check_4_Duplicate_MoneyTransactionEntry(Table, Sel, Con_List, ColCount, DS_Update_S);
        }

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");
        //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                // This will come here.
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            boolean Ret_Val =  RunQueryOnce(Statement);
            if (!Ret_Val) {
                return Check_4_Duplicate_MoneyTransactionEntry(Table, Sel, Con_List, ColCount, Statement);
            }
            return Ret_Val;
        }
    }

    boolean InUp2_Transport ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, String Data) {
        String Last = "";
        int ColCount = 9;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "Transport";
        String Sel = "Name = '" + P_Key_L[0] + "' and Customer = '" + P_Key_L[1] +  "' and Site = '" + P_Key_L[2]  + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Name", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','" + Con_List[5] + "'," + Con_List[6]+ ","
                + Con_List[7]+ ",'"  +  Last + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + " (Create_TS,Modify_TS,User,Name,Customer,Site,Rate,Addition,Disabled) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);

            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Rate = " + Con_List[6] + ", Addition  = " + Con_List[7]  + ", Disabled  = '"
                        + Last +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        // System.out.println("Statement = " + Statement);
        //DS_Update_S = DS_Update_S.replace("'", "");
        //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");

        //System.out.println("Statement = " + Statement);
        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    private  boolean Check_4_Duplicate_MoneyTransactionEntry (String Table, String Sel, String[] Con_List, int ColCount, String Statement) {
        String Output = Get_Val_from_DB_UD(Table, "*", "", Sel);
        String[] Out_Arr = Output.split("\\a");

        int ax = 0;
        boolean Matched = true;
        for (; ax < ColCount; ax++) {
            if (!Matched) { break; }
            switch (ax) {
                case 0: case 1: case 2: case 10:
                    break;


                case  9:
                    // Compare Doubles
                    double ad = String2Double2P(Con_List[ax]);
                    double ae = String2Double2P(Out_Arr[ax]);
                    if (ad != ae) {
                        Matched = false;
                    }
                    break;

                case  3: case  4: case  5: case  6: case  7: case  8:
                    if (!Con_List[ax].equals(Out_Arr[ax])) { Matched = false; break; }
                default: break;
            }
        }
        if (!Matched) {
            //AppendData2File(SystemLogFile, "\n\n Match Failed, Row Already Present " + MyDebugPrintf() + ", Trying to Insert Statement = " + Statement, false, false);
            // AppendData2File(SystemLogFile, MyDebugPrintf() + ", Already Present Output = " + Output + "\n\n", false, false);
        } else {
            // AppendData2File(SystemLogFile, "\n\n Matched, Row Already Present " + MyDebugPrintf() + ", Trying to Insert Statement = " + Statement, false, false);
            //AppendData2File(SystemLogFile, MyDebugPrintf() + ", Already Present Output = " + Output + "\n\n", false, false);
        }
        return false;
        //return Matched;
    }

    boolean InUp2_SellerComp ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync) {
        String Last = "";
        int ColCount = 8;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }


        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "SellerComp";
        String Sel = "CompanyName = '" + Con_List[3] + "'";
        String Owner = Get_Val_from_DB_UD(Table, "CompanyName", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','"
                + ((Con_List[4]).replace('\n', '\05')).replace(',', '\04')
                + "','"+ Con_List[5]
                + "','"+ Con_List[6] + "','" +  Last  + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + " (Create_TS,Modify_TS,User,CompanyName,Address,GST,CompLogo,Disabled) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "',User = '" + Con_List[2]
                    + "', Address = '"
                    + ((Con_List[4]).replace('\n', '\05')).replace(',', '\04')
                    + "',GST = '" + Con_List[5] + "',CompLogo = '" + Con_List[6] + "',Disabled = '"
                    + Last +  "' where "  + Sel + ";";
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        //System.out.println("Statement = " + Statement);
        DS_Update_S = DS_Update_S.replace("'", "");

        if (Dynamic_Sync) {
            //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            boolean Ret_Val =  RunQueryOnce(Statement);
            // Log.i (TAG, "InUp2_SellerComp RetVal = " + Ret_Val);
            return Ret_Val;
        }
    }

    boolean Create_Table_SellerComp ()  {
        String Table = "SellerComp";
        //String Count = Get_Val_from_DB_UD(Table, "1", "Count");
        //if (!Count.isEmpty()) return true;

        String a = "CREATE TABLE IF NOT EXISTS " + Table + " (";
        a += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        a += "Create_TS TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',";
        a += "Modify_TS TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP, ";
        a += "User TEXT NOT NULL,";

        a += "CompanyName TEXT ,";
        a += "Address TEXT ,";
        a += "GST TEXT ,";
        a += "CompLogo TEXT ,";
        a += "Disabled TEXT );" ;

        db_Handle.execSQL(a);

        /*if (!RunQueryOnce(a)) {
            ErrMsgDialog("Unable to Create Table SellerComp");
            return false;
        } */
        return true;
    }

    boolean Insert_update_into_SalesCompany_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                    String User, String Curr_SalesCompanyl, String Address,
                                                    String GST, String CompLogo, String Disabled ) {

        boolean Insert =  Insert_I;
        ContentValues contentvalues = new ContentValues();

        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("User", User);
        contentvalues.put("CompanyName", Curr_SalesCompanyl);
        contentvalues.put("Address", Address);
        contentvalues.put("GST", GST);
        contentvalues.put("CompLogo", CompLogo);
        contentvalues.put("Disabled", Disabled);

        if ((Get_Val_from_DB_UD("SellerComp", "CompanyName", "", "CompanyName = '" + Curr_SalesCompanyl + "'")).equals(Curr_SalesCompanyl)) {
            Insert = false;
        }

        if (Insert) {
            contentvalues.put("CompanyName", Curr_SalesCompanyl);
            int ret_val = (int) db_Handle.insertWithOnConflict("SellerComp", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "CompanyName = '" + Curr_SalesCompanyl + "'";
            int ret_val = (int) db_Handle.update("SellerComp", contentvalues,WhereCluause, null  );
            return true;
        }

        return false;
    }

    boolean Create_Table_CustomerDetails ()  {
        String Table = "CustomerDetails";

        String a = "CREATE TABLE IF NOT EXISTS " + Table + " (";
        a += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        a += "Create_TS TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',";
        a += "Modify_TS TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP, ";
        a += "User TEXT NOT NULL,";
        a += "CustomerName TEXT ,";
        a += "CreditLimit INTEGER ,";
        a += "SellerComName TEXT ,";
        a += "Permit_Pct Decimal(5,2),";
        a += "CGST_Pct Decimal(5,2),";
        a += "SGST_Pct Decimal(5,2),";
        a += "Num_Prints Integer,";
        a += "TP_Print_Cash TEXT,";
        a += "TP_Print_CompD TEXT,";
        a += "PO_Num TEXT,";
        a += "Tax_Invoice_Remarks TEXT,";
        a += "ISGST_Pct TEXT, " ;
        a += "CreditLimitAlarm INTEGER,";
        a += "Reference TEXT)";



        db_Handle.execSQL(a);

        /* if (!RunQueryOnce(a)) {
            ErrMsgDialog("Unable to Create Table CustomerDetails");
            return false;
        } */
        return true;
    }

    boolean Insert_update_into_Customer_Details_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                        String User, String CustomerName, String CreditLimit,
                                                        String CreditLimitAlarm, String SellerComName, String Permit_Pct, String CGST_Pct,
                                                        String SGST_Pct, String Num_Prints, String TP_Print_Cash,
                                                        String TP_Print_CompD, String PO_Num, String Tax_Invoice_Remarks, String ISGST_Pct) {

        boolean Insert =  Insert_I;
        ContentValues contentvalues = new ContentValues();

        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("User", User);
        contentvalues.put("CustomerName", CustomerName);
        contentvalues.put("CreditLimit", CreditLimit);
        contentvalues.put("SellerComName", SellerComName);
        contentvalues.put("Permit_Pct", Permit_Pct);
        contentvalues.put("CGST_Pct", CGST_Pct);
        contentvalues.put("SGST_Pct", SGST_Pct);
        contentvalues.put("Num_Prints", Num_Prints);
        contentvalues.put("TP_Print_Cash", TP_Print_Cash);
        contentvalues.put("TP_Print_CompD", TP_Print_CompD);
        contentvalues.put("PO_Num", PO_Num);
        contentvalues.put("Tax_Invoice_Remarks", Tax_Invoice_Remarks);
        contentvalues.put("ISGST_Pct", ISGST_Pct);
        contentvalues.put("CreditLimitAlarm", CreditLimitAlarm);

        // Log.i (TAG, "CreditLimitAlarm = "  + CreditLimitAlarm + ", ISGST_Pct = " + ISGST_Pct );

        if ((Get_Val_from_DB_UD("CustomerDetails", "CustomerName", "", "CustomerName = '" + CustomerName + "'")).equals(CustomerName)) {
            Insert = false;
        }

        if (Insert) {
            contentvalues.put("CustomerName", CustomerName);
            int ret_val = (int) db_Handle.insertWithOnConflict("CustomerDetails", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "CustomerName = '" + CustomerName + "'";
            int ret_val = (int) db_Handle.update("CustomerDetails", contentvalues,WhereCluause, null  );
            return true;
        }

        return false;
    }

    boolean Create_Table_RFID_Insert() {
        // String Count = Get_Val_from_DB_UD("RFID_Insert", "1", "Count");
        //if (!Count.isEmpty()) return true;

        String a = "CREATE TABLE IF NOT EXISTS RFID_Insert (";
        a += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        a += "Create_TS timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," ;
        a += "Modify_TS timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," ;
        a += "User TEXT DEFAULT NULL," ;
        a += "RFID_id TEXT NOT NULL," ;
        a += "Supplier TEXT DEFAULT NULL," ;
        a += "Vehicle TEXT DEFAULT NULL," ;
        a += "Material TEXT DEFAULT NULL," ;
        a += "Disabled TEXT DEFAULT NULL);" ;

        db_Handle.execSQL(a);

        return true;
    }

    boolean Create_Table_RFID_Access() {
        //String Count = Get_Val_from_DB_UD("RFID_Access", "1", "Count");
        //if (!Count.isEmpty()) return true;

        String a = "CREATE TABLE IF NOT EXISTS RFID_Access (";
        a += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        a += "AccessTS timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," ;
        a += "id INTEGER," ;
        a += "RFID_id TEXT NOT NULL," ;
        a += "Vehicle TEXT NOT NULL," ;
        a += "Load TEXT NOT NULL);" ;

        db_Handle.execSQL(a);
        return true;
    }

    boolean InUp2_RFID_Insert_UD ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, String Row_Data) {
        String Last = "";
        int ColCount = 8;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String Table = "RFID_Insert";
        String Sel = "RFID_id = '" + P_Key_S + "'";
        String Owner = Get_Val_from_DB_UD(Table, "RFID_id", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" + Con_List[4] + "','" + Con_List[5] + "','"
                + Con_List[6] + "','" + Last  + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + "(Create_TS, Modify_TS, User, RFID_Id, Supplier, Vehicle, Material, Disabled) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            Statement = "UPDATE " + Table + " set Modify_TS = '"
                    + Con_List[1] + "', User = '" + Con_List[2] + "', Supplier = '" + Con_List[4]
                    + "', Vehicle = '" + Con_List[5] + "', Material = '" + Con_List[6]
                    + "', Disabled = '" + Last +  "' where "  + Sel + ";";
        }

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        System.out.println("Statement = " + Statement);
        DS_Update_S = DS_Update_S.replace("'", "");

        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean InUp2_RFID_Access_UD ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, String Row_Data) {
        String Last = "";
        int ColCount = 5;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String Table = "RFID_Access";

        String DS_Update_S = "'" + Con_List[0] + "'," + Con_List[1] + ",'" + Con_List[2] + "','"
                + Con_List[3] + "','" + Last + "'";

        String Statement;

        if (Con_List.length >= (ColCount -1)) {
            Statement = "INSERT into " + Table + "(AccessTS, id, RFID_id, Vehicle, Load) values(" + DS_Update_S + ");";
        } else {
            return false;
        }

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        //    System.out.println("Statement = " + Statement);
        DS_Update_S = DS_Update_S.replace("'", "");

        if (Dynamic_Sync) {
            return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean Insert_update_into_RFID_Insert_Table (String RFID_id) {
        String Vehicle = "";
        String Material = "";
        String Quarry = "";
        String Active = "NO";

        return Insert_update_into_RFID_Insert_Table ( RFID_id,  Vehicle,
                Material,
                Quarry, "YES");
    }

    boolean Insert_update_into_RFID_Insert_Table (String RFID_id, String Supplier,
                                                  String Vehicle, String Material, String Disabled) {

        String[] Con_List = new String[8];

        String CreateTS = Get_Current_Date_Time();

        Con_List[0] = CreateTS;
        Con_List[1] = CreateTS;
        Con_List[2] = FBAppMBName;
        Con_List[3] = RFID_id;
        Con_List[4] = Supplier;
        Con_List[5] = Vehicle;
        Con_List[6] = Material;
        Con_List[7] = Disabled;

        return InUp2_RFID_Insert_UD(Con_List[3], Con_List , true, "");

    }

    boolean Insert_update_into_RFID_Access_Table (String RFID_id, String  Vehicle, double Load_Val) {
        String AccessTS = Get_Current_Date_Time();
        return Insert_update_into_RFID_Access_Table ( RFID_id,   Vehicle,  Load_Val,  AccessTS);
    }

    boolean Insert_update_into_RFID_Access_Table (String RFID_id, String  Vehicle, double Load_Val, String AccessTS) {
        String id = Get_Val_from_DB_UD("RFID_Access", "id", "MAX");
        id = Integer.toString( String2Int(id) + 1);

        String[] Con_List = new String[5];

        String CreateTS = Get_Current_Date_Time();

        Con_List[0] = CreateTS;
        Con_List[1] = id;
        Con_List[2] = RFID_id;
        Con_List[3] = Vehicle;
        Con_List[4] = Double2String2P(Load_Val);

        return InUp2_RFID_Access_UD(Con_List[1], Con_List , true, "");
    }

    public boolean InUp2_AutoSaveTrans ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, boolean Insert, String Data) {
        String Last = "";
        int ColCount = 5;

        Log.i (TAG, "============================ InUp2_AutoSaveTrans Data = " + Data);
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String[] P_Key_L = P_Key_S.split ("-");
        String Table = "AutoSaveTrans";
        String Sel = "TimeStamp = '" + P_Key_S + "'";
        temp_Debug = true;
        String Owner = myDB_G.Get_Val_from_DB_UD(Table, "TimeStamp", "", Sel);
        System.out.println("Output  = " + Owner);
        temp_Debug = false;

        String DS_Update_S = "'" + Con_List[0] + "'," + Con_List[1] + "," + Con_List[2] + ","
                + Con_List[3] + "," +  Last;
        String Statement;

        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB
            // MyDebugPrintf();
            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + "(TimeStamp,Id,Load_Val,Type,Mapped_ID) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            Statement = "UPDATE " + Table + " set Mapped_ID = " + Last +  " where "  + Sel + ";";
        }
        System.out.println("Statement = " + Statement);
        DS_Update_S = DS_Update_S.replace("'", "");

        RunQueryOnce_S (Statement);
        return true;
    }

    public int Get_Table_Column_Count(String TableName) {
        String Header_Statement = "PRAGMA table_info(" + TableName + ");";
        String Header_Str = "";

        List<String> Header_Result = new ArrayList<>();
        Header_Result.clear();

        RunQueryOnce(Header_Statement, Header_Result);

        int a = 0;
        int b = Header_Result.size();
        return b;
    }

    public void Manual_Upgrade_Tables () {
        Upgrade_Table_Customer_Details();
        Upgrade_Table_Material();
        Upgrade_Table_Site();
        Upgrade_Table_RFID_Access();
    }

    private void Upgrade_Table_Customer_Details () {
        String Table = "CustomerDetails";

        String Err_Report = "";

        int exp_col = 18;
        int res_col = Get_Table_Column_Count(Table);
        Log.i (TAG, "^^^^^^^^^^^^^^^^^ Update Upgrade_Table_Customer_Details Expected Columns = " + exp_col + " and res_col = " +  res_col);
        if (res_col < exp_col) {
            Err_Report += "'" + Table + "' Table Column does not match Expected Columns = [" + Integer.toString(exp_col) + "] Found = [" + Integer.toString(res_col) + "]\n";
            ErrMsgDialog(Err_Report);


            String Comp_Statement = "PRAGMA foreign_keys = 0;" +
                    "" +
                    "CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM CustomerDetails;" +
                    "" +
                    "DROP TABLE CustomerDetails;" +
                    "" +
                    "CREATE TABLE CustomerDetails (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    Create_TS           TIMESTAMP      NOT NULL DEFAULT '0000-00-00 00:00:00'," +
                    "    Modify_TS           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "    User                TEXT           NOT NULL," +
                    "    CustomerName        TEXT," +
                    "    CreditLimit         INTEGER," +
                    "    SellerComName       TEXT," +
                    "    Permit_Pct          DECIMAL (5, 2)," +
                    "    CGST_Pct            DECIMAL (5, 2)," +
                    "    SGST_Pct            DECIMAL (5, 2)," +
                    "    Num_Prints          INTEGER," +
                    "    TP_Print_Cash       TEXT," +
                    "    TP_Print_CompD      TEXT," +
                    "    PO_Num              TEXT," +
                    "    Tax_Invoice_Remarks TEXT," +
                    "    ISGST_Pct           TEXT," +
                    "    CreditLimitAlarm    INTEGER," +
                    "    Reference           TEXT" +
                    ");" +
                    "" +
                    "INSERT INTO CustomerDetails (" +
                    "                                _id," +
                    "                                Create_TS," +
                    "                                Modify_TS," +
                    "                                User," +
                    "                                CustomerName," +
                    "                                CreditLimit," +
                    "                                SellerComName," +
                    "                                Permit_Pct," +
                    "                                CGST_Pct," +
                    "                                SGST_Pct," +
                    "                                Num_Prints," +
                    "                                TP_Print_Cash," +
                    "                                TP_Print_CompD," +
                    "                                PO_Num," +
                    "                                Tax_Invoice_Remarks," +
                    "                                ISGST_Pct," +
                    "                                CreditLimitAlarm" +
                    "                            )" +
                    "                            SELECT _id, Create_TS," +
                    "                                   Modify_TS," +
                    "                                   User," +
                    "                                   CustomerName," +
                    "                                   CreditLimit," +
                    "                                   SellerComName," +
                    "                                   Permit_Pct," +
                    "                                   CGST_Pct," +
                    "                                   SGST_Pct," +
                    "                                   Num_Prints," +
                    "                                   TP_Print_Cash," +
                    "                                   TP_Print_CompD," +
                    "                                   PO_Num," +
                    "                                   Tax_Invoice_Remarks," +
                    "                                   ISGST_Pct," +
                    "                                   CreditLimitAlarm" +
                    "                              FROM sqlitestudio_temp_table;" +
                    "" +
                    "DROP TABLE sqlitestudio_temp_table;" +
                    "" +
                    "PRAGMA foreign_keys = 1;";

            String[] t_Statements = Comp_Statement.split(";");
            int a = 0;
            int b = t_Statements.length;
            boolean Result = true;
            for (; a < b; a++) {
                String t = t_Statements[a].replace("\n", "");
                if (!t.isEmpty()) {
                    if (!RunQueryOnce(t)) {
                        Result &= false;
                    }
                }
            }

            if (Result) {
                ErrMsgDialog(Table + " Table Upgrade Successful");
            } else {
                ErrMsgDialog("Failure:: " + Table + " Table Upgrade failed");
            }
        }
    }

    private void Upgrade_Table_Material () {

        String Table = "Material";
        String Err_Report = "";

        int exp_col = 11;
        int res_col = Get_Table_Column_Count(Table);
        Log.i (TAG, "^^^^^^^^^^^^^^^^^ Update Material Expected Columns = 11 and res_col = " +  res_col);
        if (res_col < exp_col) {
            Log.i (TAG, "Need to Update Material Table");
        }
        if (res_col < exp_col) {
            Err_Report += "'" + Table + "' Table Column does not match Expected Columns = [" + Integer.toString(exp_col) + "] Found = [" + Integer.toString(res_col) + "]\n";
            ErrMsgDialog(Err_Report);

            String Comp_Statement = "";
            if (res_col == 10) { // 10 or 9
                  Comp_Statement = "PRAGMA foreign_keys = 0;" +
                         "" +
                         "CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM Material;" +
                         "" +
                         "DROP TABLE Material;" +
                         "" +
                         "CREATE TABLE Material (" +
                         "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "    Create_TS    TIMESTAMP          NOT NULL DEFAULT '0000-00-00 00:00:00'," +
                         "    Modify_TS    TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                         "    User         TEXT               NOT NULL," +
                         "    Name         TEXT               NOT NULL," +
                         "    Price        DECIMAL (11, 2)    NOT NULL DEFAULT 0," +
                         "    Units        INT (11)           NOT NULL DEFAULT 0," +
                         "    Disabled     [TEXTDEFAULT 'NO']," +
                         "    OtherDetails TEXT               DEFAULT NULL," +
                         "    CFT_Rate     TEXT," +
                         "    CFTs_per_Ton TEXT);" +
                         "" +
                         "INSERT INTO Material (" +
                         "                         Create_TS," +
                         "                         Modify_TS," +
                         "                         User," +
                         "                         Name," +
                         "                         Price," +
                         "                         Units," +
                         "                         Disabled," +
                         "                         OTherDetails," +
                         "                         CFT_Rate" +
                         "                     )" +
                         "                     SELECT Create_TS," +
                         "                            Modify_TS," +
                         "                            User," +
                         "                            Name," +
                         "                            Price," +
                         "                            Units," +
                         "                            Disabled," +
                         "                            OTherDetails," +
                         "                            CFT_Rate" +
                         "                       FROM sqlitestudio_temp_table;" +
                         "" +
                         "DROP TABLE sqlitestudio_temp_table;" +
                         "" +
                         "PRAGMA foreign_keys = 1;";
             } else if (res_col == 9) {
                Comp_Statement = "PRAGMA foreign_keys = 0;" +
                        "" +
                        "CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM Material;" +
                        "" +
                        "DROP TABLE Material;" +
                        "" +
                        "CREATE TABLE Material (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    Create_TS    TIMESTAMP          NOT NULL DEFAULT '0000-00-00 00:00:00'," +
                        "    Modify_TS    TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "    User         TEXT               NOT NULL," +
                        "    Name         TEXT               NOT NULL," +
                        "    Price        DECIMAL (11, 2)    NOT NULL DEFAULT 0," +
                        "    Units        INT (11)           NOT NULL DEFAULT 0," +
                        "    Disabled     [TEXTDEFAULT 'NO']," +
                        "    OtherDetails TEXT               DEFAULT NULL," +
                        "    CFT_Rate     TEXT," +
                        "    CFTs_per_Ton TEXT);" +
                        "" +
                        "INSERT INTO Material (" +
                        "                         Create_TS," +
                        "                         Modify_TS," +
                        "                         User," +
                        "                         Name," +
                        "                         Price," +
                        "                         Units," +
                        "                         Disabled," +
                        "                         OtherDetails" +
                        "                     )" +
                        "                     SELECT Create_TS," +
                        "                            Modify_TS," +
                        "                            User," +
                        "                            Name," +
                        "                            Price," +
                        "                            Units," +
                        "                            Disabled," +
                        "                            OtherDetails" +
                        "                       FROM sqlitestudio_temp_table;" +
                        "" +
                        "DROP TABLE sqlitestudio_temp_table;" +
                        "" +
                        "PRAGMA foreign_keys = 1;";
            }

            String[] t_Statements = Comp_Statement.split(";");
            int a = 0;
            int b = t_Statements.length;
            boolean Result = true;
            for (; a < b; a++) {
                String t = t_Statements[a].replace("\n", "");
                if (!t.isEmpty()) {
                    if (!RunQueryOnce(t)) {
                        Result &= false;
                    }
                }
            }

            if (Result) {
                ErrMsgDialog(Table + " Table Upgrade Successful");
            } else {
                ErrMsgDialog("Failure:: " + Table + " Table Upgrade failed");
            }
        }
    }

    private void Upgrade_Table_Site () {
        String Table = "Site";
        String Err_Report = "";

        int exp_col = 18;
        int res_col = Get_Table_Column_Count(Table);
        if (res_col < exp_col) {
            Err_Report += "'" + Table + "' Table Column does not match Expected Columns = [" + Integer.toString(exp_col) + "] Found = [" + Integer.toString(res_col) + "]\n";
            ErrMsgDialog(Err_Report);

            String Comp_Statement = "PRAGMA foreign_keys = 0;" +
                    "" +
                    "CREATE TABLE sqlitestudio_temp_Site_table AS SELECT *" +
                    "                                          FROM Site;" +
                    "" +
                    "DROP TABLE Site;" +
                    "" +
                    "CREATE TABLE Site (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    Create_TS    TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00'," +
                    "    Modify_TS    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "    User         TEXT      NOT NULL," +
                    "    Type         TEXT      NOT NULL," +
                    "    Owner        TEXT," +
                    "    Name         TEXT," +
                    "    Material     TEXT      NOT NULL," +
                    "    Distance     INTEGER   NOT NULL," +
                    "    Latitude     TEXT      NOT NULL," +
                    "    Longitude    TEXT      NOT NULL," +
                    "    Rate_Local   INTEGER   NOT NULL," +
                    "    Rate_Remote  INTEGER   NOT NULL," +
                    "    CFT_Rate     INTEGER   NOT NULL," +
                    "    GST_Included TEXT      NOT NULL," +
                    "    Disabled     TEXT      NOT NULL," +
                    "    Extra        TEXT      NOT NULL," +
                    "    Address      TEXT      NOT NULL," +
                    ");" +
                    "" +
                    "INSERT INTO Site (" +
                    "                     Create_TS," +
                    "                     Modify_TS," +
                    "                     User," +
                    "                     Type," +
                    "                     Owner," +
                    "                     Name," +
                    "                     Material," +
                    "                     Distance," +
                    "                     Latitude," +
                    "                     Longitude," +
                    "                     Rate_Local," +
                    "                     Rate_Remote," +
                    "                     CFT_Rate," +
                    "                     GST_Included," +
                    "                     Disabled," +
                    "                     Extra" +
                    "                     Address" +
                    "                 )" +
                    "                 SELECT Create_TS," +
                    "                        Modify_TS," +
                    "                        User," +
                    "                        Type," +
                    "                        Owner," +
                    "                        Name," +
                    "                        Material," +
                    "                        Distance," +
                    "                        Latitude," +
                    "                        Longitude," +
                    "                        Rate_Local," +
                    "                        Rate_Remote," +
                    "                        CFT_Rate," +
                    "                        GST_Included," +
                    "                        Disabled," +
                    "                        Extra" +
                    "                        Address" +
                    "                   FROM sqlitestudio_temp_Site_table;" +
                    "" +
                    "DROP TABLE sqlitestudio_temp_Site_table;" +
                    "" +
                    "PRAGMA foreign_keys = 1;";

            String[] t_Statements = Comp_Statement.split(";");
            int a = 0;
            int b = t_Statements.length;
            boolean Result = true;
            for (; a < b; a++) {
                String t = t_Statements[a].replace("\n", "");
                if (!t.isEmpty()) {
                    if (!RunQueryOnce(t)) {
                        Result &= false;
                    }
                }
            }

            if (Result) {
                ErrMsgDialog(Table + " Table Upgrade Successful");
            } else {
                ErrMsgDialog("Failure:: " + Table + " Table Upgrade failed");
            }
        }

    }

    private void Upgrade_Table_RFID_Access () {
        String Table = "RFID_Access";
        String Err_Report = "";

        int exp_col = 6;
        int res_col = Get_Table_Column_Count(Table);
        if (res_col < exp_col) {
            Err_Report += "'" + Table + "' Table Column does not match Expected Columns = [" + Integer.toString(exp_col) + "] Found = [" + Integer.toString(res_col) + "]\n";
            ErrMsgDialog(Err_Report);

            String Comp_Statement = "PRAGMA foreign_keys = 0;" +
                    "" +
                    "CREATE TABLE sqlitestudio_temp_RFID_Access_table AS SELECT *" +
                    "                                          FROM RFID_Access;" +
                    "" +
                    "DROP TABLE RFID_Access;" +
                    "" +
                    "CREATE TABLE RFID_Access (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    AccessTS    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "    id     INTEGER   NOT NULL," +
                    "    RFID_id     TEXT      NOT NULL," +
                    "    Vehicle    TEXT      NOT NULL," +
                    "    Load      TEXT      NOT NULL," +
                    ");" +
                    "" +
                    "INSERT INTO RFID_Access (" +
                    "                     _id," +
                    "                     AccessTS," +
                    "                     id," +
                    "                     RFID_id," +
                    "                     Vehicle," +
                    "                     Load" +
                    "                 )" +
                    "                 SELECT _id," +
                    "                        AccessTS," +
                    "                        id," +
                    "                        RFID_id," +
                    "                        Vehicle," +
                    "                        Load" +
                    "                   FROM sqlitestudio_temp_RFID_Access_table;" +
                    "" +
                    "DROP TABLE sqlitestudio_temp_RFID_Access_table;" +
                    "" +
                    "PRAGMA foreign_keys = 1;";

            String[] t_Statements = Comp_Statement.split(";");
            int a = 0;
            int b = t_Statements.length;
            boolean Result = true;
            for (; a < b; a++) {
                String t = t_Statements[a].replace("\n", "");
                if (!t.isEmpty()) {
                    if (!RunQueryOnce(t)) {
                        Result &= false;
                    }
                }
            }

            if (Result) {
                ErrMsgDialog(Table + " Table Upgrade Successful");
            } else {
                ErrMsgDialog("Failure:: " + Table + " Table Upgrade failed");
            }
        }

    }

    boolean Create_Table_AppUsers() {
        //String Count = Get_Val_from_DB_UD("AppUsers", "1", "Count");
        //if (!Count.isEmpty()) return true;

        String a = "CREATE TABLE IF NOT EXISTS AppUsers (";
        a += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        a += "TimeStamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," ;
        a += "id INTEGER," ;
        a += "Name TEXT NOT NULL," ;
        a += "Password TEXT NOT NULL," ;
        a += "Groups TEXT NOT NULL," ;
        a += "Disabled TEXT NOT NULL);" ;

        db_Handle.execSQL(a);
        return true;
    }

    boolean InUp2_AppUsers ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, String Data) {
        String Last = "";
        int ColCount = 6;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "AppUsers";
        String Sel = "Name = '" + P_Key_L[0] + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Name", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "'," + Con_List[1] + ",'" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','"  +  Last + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + " (TimeStamp,id,Name,Password,Groups,Disabled) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update

            Statement = "UPDATE " + Table + " set Password = '" + Con_List[3] + "', Groups = '" + Con_List[4]
                    + "', Disabled  = '" + Last +  "' where "  + Sel + ";";
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");

        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_AppUsers, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean InUp2_Alarms ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, String Data) {
        String Last = "";
        int ColCount = 9;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "Transport";
        String Sel = "Name = '" + P_Key_L[0] + "' and Customer = '" + P_Key_L[1] +  "' and Site = '" + P_Key_L[2]  + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Name", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','" + Con_List[5] + "'," + Con_List[6]+ ","
                + Con_List[7]+ ",'"  +  Last + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + " (Create_TS,Modify_TS,User,Name,Customer,Site,Rate,Addition,Disabled) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);

            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Rate = " + Con_List[6] + ", Addition  = " + Con_List[7]  + ", Disabled  = '"
                        + Last +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        // System.out.println("Statement = " + Statement);
        //DS_Update_S = DS_Update_S.replace("'", "");
        //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");

        //System.out.println("Statement = " + Statement);
        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean InUp2_EmailConf ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, String Data) {
        String Last = "";
        int ColCount = 9;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "Transport";
        String Sel = "Name = '" + P_Key_L[0] + "' and Customer = '" + P_Key_L[1] +  "' and Site = '" + P_Key_L[2]  + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Name", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','" + Con_List[5] + "'," + Con_List[6]+ ","
                + Con_List[7]+ ",'"  +  Last + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + " (Create_TS,Modify_TS,User,Name,Customer,Site,Rate,Addition,Disabled) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);

            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Rate = " + Con_List[6] + ", Addition  = " + Con_List[7]  + ", Disabled  = '"
                        + Last +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        // System.out.println("Statement = " + Statement);
        //DS_Update_S = DS_Update_S.replace("'", "");
        //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");

        //System.out.println("Statement = " + Statement);
        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }

    boolean InUp2_ExpenseType ( String P_Key_S, String[] Con_List, boolean Dynamic_Sync, String Data) {
        String Last = "";
        int ColCount = 9;

        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        String[] P_Key_L = P_Key_S.split ("-");

        String Table = "Transport";
        String Sel = "Name = '" + P_Key_L[0] + "' and Customer = '" + P_Key_L[1] +  "' and Site = '" + P_Key_L[2]  + "'";
        String Owner = Get_Val_from_DB_UD(Table, "Name", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','" + Con_List[5] + "'," + Con_List[6]+ ","
                + Con_List[7]+ ",'"  +  Last + "'";

        String Statement;
        if ((Owner == null) || (Owner.isEmpty())) {
            // Need to add Data to DB

            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table + " (Create_TS,Modify_TS,User,Name,Customer,Site,Rate,Addition,Disabled) values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            // Need to Update
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);

            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Rate = " + Con_List[6] + ", Addition  = " + Con_List[7]  + ", Disabled  = '"
                        + Last +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        // System.out.println("Statement = " + Statement);
        //DS_Update_S = DS_Update_S.replace("'", "");
        //return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        DS_Update_S = DS_Update_S.replace("'", "");

        //System.out.println("Statement = " + Statement);
        if (Dynamic_Sync) {
            if ((Owner == null) || (Owner.isEmpty())) {
                return RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                return (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            return RunQueryOnce(Statement);
        }
    }
    boolean Create_Table_DriverDetails() {
        db_Handle.execSQL("create table IF NOT EXISTS DriverDetails (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                "Driver_UID Text, Designation Text, PhNum Decimal,"
                + "FullName Text, Address Text, Zone TEXT,  Active TEXT) ");
        return true;
    }
    boolean Insert_update_into_DriverDetails_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                     String User, String Driver_UID, String Designation,
                                                     String PhNum, String FullName, String Address, String Zone, String Active ) {
        String[] Con_List = new String[10];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = Driver_UID;
        Con_List[4] = Designation;
        Con_List[5] = PhNum;
        Con_List[6] = FullName;
        Con_List[7] = Address;
        Con_List[8] = Zone;
        Con_List[9] = Active;
        return InUp2_DriverDetails(Driver_UID, Con_List, true);
    }
    boolean InUp2_DriverDetails ( String P_Key, String[] Con_List, boolean Dynamic_Sync){
        String data="";
        return  InUp2_DriverDetails ( P_Key, Con_List,  Dynamic_Sync, data);
    }
    boolean InUp2_DriverDetails ( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 10;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }
        String Table = "DriverDetails";
        String Sel = "Driver_UID = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Driver_UID", "", Sel);
        String DS_Update_S =  "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','" + Con_List[5] + "','" + Con_List[6]+ "',"
                + Con_List[7]+ ",'"
                + "','" + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            if (Con_List.length >= (ColCount -1)) {
                Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Driver_UID, Designation, PhNum, FullName, Address, Zone, Active)  values(" + DS_Update_S + ");";
            } else {
                return false;
            }
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "',Designation = '" + Con_List[3] + "', PhNum  = '" + Con_List[4] + "', FullName  = '" + Con_List[5]
                        + "', Address  = '" + Con_List[6] + "', Zone  =' " + Con_List[7] + ", Active  = "
                        + Last +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_DriverDetails, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');
                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_DriverDetails statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }
    boolean Create_Table_LocoDetails() {
        db_Handle.execSQL("create table IF NOT EXISTS LocoDetails  (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                " Loco_Num Text ,Loco_Type TEXT,  Active TEXT) ");
        return true;
    }
    boolean Insert_update_into_LocoDetails_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                 String User,
                                                 String Loco_Num, String Loco_Type, String Active) {
        String[] Con_List = new String[6];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = Loco_Num;
        Con_List[4] = Loco_Type;
        Con_List[5] = Active;
        return InUp2_LocoDetails(Loco_Num, Con_List, true);
    }
    boolean InUp2_LocoDetails ( String P_Key, String[] Con_List, boolean Dynamic_Sync){
        String data="";
        return  InUp2_LocoDetails ( P_Key, Con_List,  Dynamic_Sync, data);
    }
    boolean InUp2_LocoDetails ( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 6;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }
        String Table = "LocoDetails";
        String Sel = "Loco_Num = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Loco_Num", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4]   + "','" + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Loco_Num,  Loco_Type,Active)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Loco_Type = '" + Con_List[4] + "', Active = '" + Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_HomeShed, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');
                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_Homeshed statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }
    boolean Create_Table_HomeShed() {
        db_Handle.execSQL("create table IF NOT EXISTS HomeShed (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                " HomeShed Text, Zone TEXT,  Active TEXT) ");
        return true;
    }
    boolean Insert_update_into_HomeSheds_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                 String User,
                                                 String HomeShed, String Zone, String Active) {
        String[] Con_List = new String[6];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = HomeShed;
        Con_List[4] = Zone;
        Con_List[5] = Active;
        return InUp2_HomeShed(HomeShed, Con_List, true);
    }
    boolean InUp2_HomeShed ( String P_Key, String[] Con_List, boolean Dynamic_Sync){
        String data="";
        return  InUp2_HomeShed ( P_Key, Con_List,  Dynamic_Sync, data);
    }
    boolean InUp2_HomeShed ( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 6;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }
        String Table = "HomeShed";
        String Sel = "HomeShed = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "HomeShed", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4]   + "','" + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, HomeShed,  Zone,Active)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                       + "', Zone = '" + Con_List[4] + "', Active = '" + Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_HomeShed, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');
                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_HomeShed statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }

    boolean Create_Table_Monitor() {

        db_Handle.execSQL("create table IF NOT EXISTS Monitor (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                " Monitor_Time TIME , Trip_No TEXT,  Monitoring_Breaking TEXT, KM Decimal, Load_Meter_Reading Text, Notch Text , Battery_Ammeter_Reading Text) ");

        return true;
    }

    boolean Insert_update_into_Monitor_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                               String User,
                                               String Monitor_Time, String Trip_No, String KM, String Monitoring_Breaking, String Load_Meter_Reading, String Notch , String Battery_Ammeter_Reading) {

        /*
        boolean Insert =  Insert_I;
        ContentValues contentvalues = new ContentValues();

        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("User", User);
        contentvalues.put("HomeShed", HomeShed);
        contentvalues.put("Zone", Zone);
        contentvalues.put("Active", Active);

        if ((Get_Val_from_DB_UD("HomeShed", "HomeShed", "", "HomeShed = '" + HomeShed + "'")).equals(HomeShed)) {
            Insert = false;
        }

        if (Insert) {
            contentvalues.put("HomeShed", HomeShed);
            int ret_val = (int) db_Handle.insertWithOnConflict("HomeShed", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "HomeShed = '" + HomeShed + "'";
            int ret_val = (int) db_Handle.update("HomeShed", contentvalues,WhereCluause, null  );
            return true;
        }

        return false; */
        String[] Con_List = new String[10];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = Monitor_Time;
        Con_List[4] = Trip_No;
        Con_List[5] = KM;
        Con_List[6] = Monitoring_Breaking;
        Con_List[7] = Load_Meter_Reading;
        Con_List[8] = Notch;
        Con_List[9] = Battery_Ammeter_Reading;



        return InUp2_Monitor(Monitor_Time, Con_List, true);
    }


    boolean InUp2_Monitor( String P_Key, String[] Con_List, boolean Dynamic_Sync){

        String data="";
        return  InUp2_Monitor ( P_Key, Con_List,  Dynamic_Sync, data);
    }

    boolean InUp2_Monitor ( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 10;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }

        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }

        String Table = "Monitor";
        String Sel = "Monitor_Time = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Monitor_Time", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "'," +  Con_List[4] + "," + Con_List[5] + ",'" + Con_List[6]+ "','"
                + Con_List[7] + Con_List[8]+ "','"
                + "','" + Last + "'";

        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            //if (!Name.equals(P_Key)) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Monitor_Time, Trip_No,KM, Monitoring_Breaking, Load_Meter_Reading, Notch, Battery_Ammeter_Reading)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Trip_No = " + Con_List[4] + ", KM = "
                        + Con_List[5] + ", Monitoring_Breaking = '" +  Con_List[6] +  "', Load_Meter_Reading= '"
                        + Con_List[7] + "', Notch= '" +  Con_List[8]
                        + "', Battery_Ammeter_Reading = '"+ Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }

        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');

        //System.out.println("Statement = " + Statement);
        DS_Update_S = DS_Update_S.replace("'", "");

        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_Monitor, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_Monitor statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }

    boolean Create_Table_CategorizedSolution() {
        db_Handle.execSQL("create table IF NOT EXISTS CategorizedSolution (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                " Trip_No TEXT, Driver_Name TEXT,Solution_Category TEXT,Problem_Id TEXT, Solution_Id Text, Solution_Decription TEXT) ");
        return true;
    }
    boolean Insert_update_into_CategorizedSolution_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                           String User,
                                                            String Trip_No,String Driver_Name, String Solution_Category,String Problem_Id, String Solution_Id, String Solution_Decription) {
        String[] Con_List = new String[9];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = Trip_No;
        Con_List[4] = Driver_Name;
        Con_List[5] = Solution_Category;
        Con_List[6] = Problem_Id;
        Con_List[7] = Solution_Id;
        Con_List[8] = Solution_Decription;
        return InUp2_CategorizedSolution(Solution_Id, Con_List, true);
    }
    boolean InUp2_CategorizedSolution ( String P_Key, String[] Con_List, boolean Dynamic_Sync){
        String data="";
        return  InUp2_CategorizedSolution ( P_Key, Con_List,  Dynamic_Sync, data);
    }
    boolean InUp2_CategorizedSolution( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 9;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }
        String Table = "CategorizedSolution";
        String Sel = "Solution_Id = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Solution_Id", "", Sel);

        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4]   + "','" +  Con_List[5]   +"','" +  Con_List[6]   +
                "','" +  Con_List[7]  + "','" +  Con_List[8]   + "','" + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Trip_No,  Driver_Name, Solution_Category, Problem_Id, Solution_Id, Solution_Decription)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "',  Trip_No = '" + Con_List[4] + "',  Driver_Name = '" + Con_List[5] +"',Solution_Category  = '" + Con_List[6] +"', Problem_Id = '" + Con_List[7] +
                        "', Solution_Decription = '"  + Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_CategorizedSolution, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');
                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_CategorizedSolution statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }
    boolean Create_Table_CategorizedProblem() {
        db_Handle.execSQL("create table IF NOT EXISTS CategorizedProblem (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                " Problem_Id Text, Trip_No TEXT, Driver_Name Text,  Problem_Category TEXT, Problem_Decription Text) ");
        return true;
    }
    boolean Insert_update_into_CategorizedProblem_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                          String User,
                                                          String Problem_Id, String Trip_No, String Driver_Name, String Problem_Category, String Problem_Decription ) {
        String[] Con_List = new String[8];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = Problem_Id;
        Con_List[4] = Trip_No;
        Con_List[5] = Driver_Name;
        Con_List[6] = Problem_Category;
        Con_List[7] = Problem_Decription;
        return InUp2_CategorizedProblem(Problem_Id, Con_List, true);
    }
    boolean InUp2_CategorizedProblem ( String P_Key, String[] Con_List, boolean Dynamic_Sync){
        String data="";
        return  InUp2_CategorizedProblem( P_Key, Con_List,  Dynamic_Sync, data);
    }
    boolean InUp2_CategorizedProblem( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 8;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }
        String Table = "CategorizedProblem";
        String Sel = "Problem_Id = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Problem_Id", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4]   + "','" +  Con_List[5]   + "','" +  Con_List[6]   +"','" +  Con_List[7]  + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Problem_Id, Trip_No,Driver_Name,Problem_Category,Problem_Decription)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Trip_No = '" + Con_List[3] + "', Driver_Name = '"+ Con_List[4]+
                        "', Problem_Category = '"+ Con_List[5]+ "', Problem_Decription= '" + Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_CategorizedProblem, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');
                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_CategorizedProblem statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }
    boolean Create_Table_Location() {
        db_Handle.execSQL("create table IF NOT EXISTS Location (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                " Location Text, Zone TEXT,  Active TEXT) ");
        return true;
    }
    boolean Insert_update_into_Location_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                 String User,
                                                 String Location, String Zone, String Active ) {
        String[] Con_List = new String[6];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = Location;
        Con_List[4] = Zone;
        Con_List[5] = Active;
        return InUp2_Location(Location, Con_List, true);
    }
    boolean InUp2_Location ( String P_Key, String[] Con_List, boolean Dynamic_Sync){
        String data="";
        return  InUp2_Location ( P_Key, Con_List,  Dynamic_Sync, data);
    }
    boolean InUp2_Location ( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 6;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }
        String Table = "Location";
        String Sel = "Location = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Location", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4]   + "','" + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Location,  Zone,Active)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Zone = '" + Con_List[4] + "', Active = '" + Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_Location, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');
                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_Locotion statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }
    boolean Create_Table_TrainDetails() {
        db_Handle.execSQL("create table IF NOT EXISTS TrainDetails (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, " +
                " Train_Num Text, Train_Name TEXT,  Active TEXT) ");
        return true;
    }
    boolean Insert_update_into_Train_Detailss_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                 String User,
                                                 String Train_Num, String Train_Name, String Active) {
        String[] Con_List = new String[6];
        Con_List[0] = Create_TS;
        Con_List[1] = Modify_TS;
        Con_List[2] = User;
        Con_List[3] = Train_Num;
        Con_List[4] = Train_Name;
        Con_List[5] = Active;
        return InUp2_TrainDetails(Train_Num, Con_List, true);
    }
    boolean InUp2_TrainDetails ( String P_Key, String[] Con_List, boolean Dynamic_Sync){
        String data="";
        return  InUp2_TrainDetails ( P_Key, Con_List,  Dynamic_Sync, data);
    }
    boolean InUp2_TrainDetails ( String P_Key, String[] Con_List, boolean Dynamic_Sync, String data) {
        String Last = "";
        int ColCount = 6;
        if (Con_List.length < (ColCount -1)) {
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
        }
        for (int a = 0; a < Con_List.length; a++) {
            if ((Con_List[a]).isEmpty()) Con_List[a] = "0";
        }
        String Table = "TrainDetails";
        String Sel = "Train_Num = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Train_Num", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4]   + "','" + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, Train_Num,  Train_Name,Active)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Train_Name = '" + Con_List[4] + "', Active = '" + Last
                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        String P_Key_S = Name;
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key_S, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S, Curr_Activity.Act_TrainDetails, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');
                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_TrainDetails statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }
    boolean Create_Table_IRC_ELogs() {
        //db_Handle.execSQL("DROP TABLE IF EXISTS IRC_ELogs; ");
        db_Handle.execSQL("create table IF NOT EXISTS IRC_ELogs (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, Create_TS DATETIME, Modify_TS DATETIME, User Text, TripNumber Text," +
                "Ticket_Num Text, Ticket_Num2 Text, HomeShed1 TEXT ,HomeShed2 TEXT,CrewDetails TEXT,DriverName TEXT,DriverDesig TEXT,Asst_DriverName TEXT, " +
                "Asst_DriverDesig TEXT, Load TEXT, KM Text, TripDetails Text,Start_P Text,End_P Text, TrainNum Text,Attended_At Timestamp, Arrival Timestamp," +
                "Departure Timestamp, LocoDetails Text, LocoNum TEXT ,Last_Sch_Attdnd TEXT,Next_Sch_On TEXT,Levels TEXT,Fuel_Init TEXT,Fuel_Final TEXT, " +
                "Fuel_Consmp Text, Lube_Init Text, Lube_Final TEXT ,Lube_Consmp TEXT,CompressorOil TEXT,GovernorOil TEXT,Cool_Wtr TEXT," +
                        "StartFuse Text, FireExting TEXT ,FuelFill TEXT,AlertWork TEXT,MUCode TEXT,SPeedoM TEXT,HI_Flash TEXT,LVC_Locks Text, " +
                "GR_Seal Text, DB_Seal Text, Wiper TEXT ,Sanders TEXT,Sig_Drv1 TEXT,DrvName1 TEXT,Sig_Drv2 TEXT,DrvName2 TEXT ,Ticket_P2_1 Text,Shed_P2_1 Text,Ticket_P2_2 Text,Shed_P2_2 Text" +
                ")");
        return true;
    }
    boolean Insert_update_into_IRC_ELogs_Ordered(boolean Insert_I, String Create_TS, String Modify_TS,
                                                     String User, String Ticket_Num,String Ticket_Num2,String HomeShed1,  String HomeShed2,
                                                 String CrewDetails, String DriverName, String DriverDesig, String Asst_DriverName, String Asst_DriverDesig,
                                                  String Load, String KM, String TripDetails, String Start_P, String End_P,
                                                  String TrainNum, String Attended_At, String Arrival, String Departure, String locoDetails,String locoNum, String Last_Sch_Attdnd,String Next_Sch_On, String Levels,String Fuel_Consmp, String Lube_Init,String Fuel_Init,
                                                 String Fuel_Final, String Lube_Final, String Lube_Consmp, String CompressorOil, String GovernorOil, String Cool_Wtr,

                                                 String StartFuse, String FireExting, String FuelFill, String AlertWork, String MUCode,
                                                 String SPeedoM, String HI_Flash, String LVC_Locks, String GR_Seal, String DB_Seal,
                                                 String Wiper, String Sanders, String Sig_Drv1, String DrvName1, String Sig_Drv2,
                                                 String DrvName2, String Ticket_P2_1, String Shed_P2_1, String Ticket_P2_2, String Shed_P2_2
                                                 ) {

        boolean Insert =  Insert_I;
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("Create_TS", Create_TS);
        contentvalues.put("Modify_TS", Modify_TS);
        contentvalues.put("User", User);
        contentvalues.put("TripNumber", "TripNumber");
        contentvalues.put("Ticket_Num", Ticket_Num);
        contentvalues.put("Ticket_Num", Ticket_Num2);
        contentvalues.put("HomeShed1", HomeShed1);
        contentvalues.put("HomeShed2", HomeShed2);
        contentvalues.put("CrewDetails", CrewDetails);
        contentvalues.put("DriverName", DriverName);
        contentvalues.put("DriverDesig", DriverDesig);
        contentvalues.put("Asst_DriverName", Asst_DriverName);
        contentvalues.put("Asst_DriverDesig", Asst_DriverDesig);
        contentvalues.put("Load", Load);
        contentvalues.put("KM", KM);
        contentvalues.put("TripDetails", TripDetails);
        contentvalues.put("Start_P", Start_P);
        contentvalues.put("End_P", End_P);
        contentvalues.put("TrainNum", TrainNum);
        contentvalues.put("Attended_At", Attended_At);
        contentvalues.put("Arrival", Arrival);
        contentvalues.put("Departure", Departure);
        contentvalues.put("locoDetails", locoDetails);
        contentvalues.put("locoNum", locoNum);
        contentvalues.put("Last_Sch_Attdnd",Last_Sch_Attdnd);
        contentvalues.put("Next_Sch_On",Next_Sch_On);
        contentvalues.put(" Levels", Levels);
        contentvalues.put(" Fuel_Init", Fuel_Init);
        contentvalues.put(" Fuel_Final",Fuel_Final);
        contentvalues.put("Fuel_Consmp", Fuel_Consmp);
        contentvalues.put("Lube_Init", Lube_Init);
        contentvalues.put("Lube_Final", Lube_Final);
        contentvalues.put("Lube_Consmp", Lube_Consmp);
        contentvalues.put("CompressorOil", CompressorOil);
        contentvalues.put("GovernorOil", GovernorOil);
        contentvalues.put("Cool_Wtr", Cool_Wtr);

        contentvalues.put("StartFuse", StartFuse);
        contentvalues.put("FireExting", FireExting);
        contentvalues.put("FuelFill", FuelFill);
        contentvalues.put("AlertWork", AlertWork);
        contentvalues.put("MUCode", MUCode);
        contentvalues.put("SPeedoM", SPeedoM);
        contentvalues.put("HI_Flash", HI_Flash);
        contentvalues.put("LVC_Locks", LVC_Locks);
        contentvalues.put("GR_Seal", GR_Seal);
        contentvalues.put("DB_Seal", DB_Seal);
        contentvalues.put("Wiper", Wiper);
        contentvalues.put("Sanders", Sanders);
        contentvalues.put("Sig_Drv1", Sig_Drv1);
        contentvalues.put("DrvName1", DrvName1);
        contentvalues.put("Sig_Drv2", Sig_Drv2);
        contentvalues.put("DrvName2", DrvName2);
        contentvalues.put("Ticket_P2_1", Ticket_P2_1);
        contentvalues.put("Shed_P2_1", Shed_P2_1);
        contentvalues.put("Ticket_P2_2", Ticket_P2_2);
        contentvalues.put(" Shed_P2_2",Shed_P2_2);
        if ((Get_Val_from_DB_UD("IRC_ELogs", "Ticket_Num", "", "Ticket_Num = '" + Ticket_Num + "'")).equals(Ticket_Num)) {
            Insert = false;
        }
        if (Insert) {
            contentvalues.put("Ticket_Num", Ticket_Num);
            int ret_val = (int) db_Handle.insertWithOnConflict("IRC_ELogs", null, contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
            if (ret_val > 0) {
                return true;
            }
        } else {
            String WhereCluause = "Ticket_Num = '" + Ticket_Num + "'";
            int ret_val = (int) db_Handle.update("IRC_ELogs", contentvalues,WhereCluause, null  );
            return true;
        }
        return false;
    }
    boolean InUp2_IRC_ELogs ( String P_Key, String[] Con_List, boolean Dynamic_Sync) {
         String data="";
        return InUp2_IRC_ELogs (  P_Key, Con_List,  Dynamic_Sync, data) ;
    }
    boolean InUp2_IRC_ELogs ( String P_Key, String[] Con_List_I, boolean Dynamic_Sync, String data) {
        Log.i (TAG, "In InUp2_IRC_ELogs");

        String Last = "";
        int ColCount = 56; //56;
        String[] Con_List = null; //new String[ColCount];
        if (Con_List_I.length < ColCount) {
            // Need to add arr to arr.
            Con_List = new String[ColCount];
            int a = 0;
            for (; a < Con_List_I.length; a++) {
                Con_List[a] = Con_List_I[a];
            }

            for (a = Con_List_I.length; a < ColCount; a++) {
                Con_List[a] = "0";
            }

        } else {
            Con_List = Con_List_I;
        }

        if (Con_List.length < (ColCount -1)) {
            Log.i (TAG, "In InUp2_IRC_ELogs IFIFIF = " + Con_List.length);
            return false;
        } else if (Con_List.length == ColCount) {
            Last =  Con_List[ColCount - 1];
            Log.i (TAG, "In InUp2_IRC_ELogs ELSE ");
        }


        Log.i (TAG, "In InUp2_IRC_ELogs 1111222222 ");
        String Table = "IRC_ELogs";
        String Sel = "Ticket_Num = '" + P_Key + "'";
        String Name = Get_Val_from_DB_UD(Table, "Ticket_Num", "", Sel);
        String DS_Update_S = "'" + Con_List[0] + "','" + Con_List[1] + "','" + Con_List[2] + "','"
                + Con_List[3] + "','" +  Con_List[4] + "','" + Con_List[5] + "','" + Con_List[6] + "','"
                + Con_List[7] + "','" +  Con_List[8] + "','" + Con_List[9] + "','" + Con_List[10] + "','"
                + Con_List[11] + "','" +  Con_List[12] + "','" + Con_List[13] + "','" + Con_List[14] + "','"
                + Con_List[15] + "','" +  Con_List[16] + "','" + Con_List[17] + "','" + Con_List[18] + "','"
                + Con_List[19] + "','" +  Con_List[20] + "','" + Con_List[21] + "','" + Con_List[22] + "','"
                + Con_List[23] + "','" +  Con_List[24] + "','" + Con_List[25] + "','" + Con_List[26] + "','"
                + Con_List[27] + "','" +  Con_List[28] + "','" + Con_List[29] + "','" + Con_List[30] + "','"
                + Con_List[31] + "','" +  Con_List[32] + "','" + Con_List[33] + "','" + Con_List[34] + "','"
                + Con_List[35] + "','" +  Con_List[36] + "','" + Con_List[37] + "','" + Con_List[38] + "','"
                + Con_List[39] + "','" +  Con_List[40] + "','" + Con_List[41] + "','" + Con_List[42] + "','"
                + Con_List[43] + "','" +  Con_List[44] + "','" + Con_List[45] + "','" + Con_List[46] + "','"
                + Con_List[47] + "','" +  Con_List[48] + "','" + Con_List[49] + "','" + Con_List[50] + "','"
                + Con_List[51] + "','" +  Con_List[52] + "','" + Con_List[53] + "','" + Con_List[54] + "','"
               + Last + "'";
        String Statement;
        if ((Name == null) || (Name.isEmpty())) {
            Statement = "INSERT into " + Table  + "(Create_TS, Modify_TS, User, TripNumber, Ticket_Num, Ticket_Num2,HomeShed1,HomeShed2,CrewDetails ,DriverName, DriverDesig, Asst_DriverName, Asst_DriverDesig,Load,KM, TripDetails, Start_P, End_P, TrainNum,\n" +
                    " Attended_At,  Arrival, Departure, LocoDetails, LocoNum, Last_Sch_Attdnd, Next_Sch_On, \n" +
                    " Levels,Fuel_Init,Fuel_Final, Fuel_Consmp, Lube_Init, Lube_Final, Lube_Consmp, CompressorOil, GovernorOil,Cool_Wtr, \n" +

                    "     StartFuse,  FireExting, FuelFill,  AlertWork ,MUCode ,SPeedoM, HI_Flash, LVC_Locks, GR_Seal, DB_Seal, Wiper, Sanders, Sig_Drv1,\n" +
                    "  DrvName1,  Sig_Drv2, DrvName2 ,Ticket_P2_1, Shed_P2_1, Ticket_P2_2, \n" +
                    " Shed_P2_2)  values(" + DS_Update_S + ");";
        } else {
            String Modify_TS = Get_Val_from_DB_UD(Table, "Modify_TS", "", Sel);
            if (!Modify_TS.equals(Con_List[1])) {
                Statement = "UPDATE " + Table + " set Modify_TS = '" + Con_List[1] + "', User = '" + Con_List[2]
                        + "', Trip_Num = '" + Con_List[3]
                        + "', Ticket_Num2 = '" + Con_List[5]
                        + "', HomeShed1 = '" + Con_List[6]
                        + "', HomeShed2 = '" + Con_List[7]
                        + "', CrewDetails = '" + Con_List[8]
                        + "', DriverName = '" + Con_List[9]
                        + "', DriverDesig = '" + Con_List[10]
                        + "', Asst_DriverName = '" + Con_List[11]
                        + "', Asst_DriverDesig = '" + Con_List[12]
                        + "', Load = '" + Con_List[13]
                        + "', KM = '" + Con_List[14]
                        + "', TripDetails = '" + Con_List[15]
                        + "', Start_P = '" + Con_List[16]
                        + "', End_P = '" + Con_List[17]
                        + "', TrainNum = '" + Con_List[18]
                        + "', Attended_At = '" + Con_List[19]
                        + "', Arrival = '" + Con_List[20]
                        + "', Departure = '" + Con_List[21]
                        + "', LocoDetails = '" + Con_List[22]
                        + "', LocoNum = '" + Con_List[23]
                        + "', Last_Sch_Attdnd = '" + Con_List[24]
                        + "', Next_Sch_On = '" + Con_List[25]
                        + "', Level = '" + Con_List[26]
                        + "', Fuel_Init = '" + Con_List[27]
                        + "', Fuel_Final = '" + Con_List[28]
                        + "', Fuel_Consmp = '" + Con_List[29]
                        + "', Lube_Init = '" + Con_List[30]
                        + "', Lube_Final = '" + Con_List[31]
                        + "', Lube_Consmp = '" + Con_List[32]
                        + "', CompressOil = '" + Con_List[33]
                        + "', GovernorOil = '" + Con_List[34]
                        + "', Cool_Wtr = '" + Con_List[35]

                        + "', StartFuse = '" + Con_List[36]
                        + "', FireExting = '" + Con_List[37]
                        + "', FuelFill = '" + Con_List[38]
                        + "', AlertWork = '" + Con_List[39]
                        + "', MUCode = '" + Con_List[40]
                        + "', SPeedoM = '" + Con_List[41]
                        + "', HI_Flash = '" + Con_List[42]
                        + "', LVC_Locks = '" + Con_List[43]
                        + "', GR_Seal = '" + Con_List[44]
                        + "', DB_Seal = '" + Con_List[45]
                        + "', Wiper = '" + Con_List[46]
                        + "', Sanders = '" + Con_List[47]

                        + "', Sig_Drv1 = '" + Con_List[48]
                        + "', DrvName1 = '" + Con_List[49]

                        + "', Sig_Drv2 = '" + Con_List[50]
                        + "', DrvName2 = '" + Con_List[51]

                        + "', Ticket_P2_1 = '" + Con_List[52]
                        + "', Shed_P2_1 = '" + Con_List[53]

                        + "', Ticket_P2_2 = '" + Con_List[54]
                        + "', Shed_P2_2 = '" + Con_List[55]

                        +  "' where "  + Sel + ";";
            } else {
                return true;
            }
        }
        Statement = Statement.replace('\05', '\n');
        Statement = Statement.replace('\04', ',');
        DS_Update_S = DS_Update_S.replace("'", "");
        Log.i (TAG, "Statement = " + Statement);
        Log.i (TAG, "In InUp2_IRC_ELogs 333333 ");
        if (Dynamic_Sync) {
            if ((Name == null) || (Name.isEmpty())) {
                boolean Ret_Val = RunQueryOnce_InUp_DS(Statement, DS_Update_S, Table, P_Key, Dynamic_Sync);
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key, Curr_Activity.Act_IRC_ELogs, DS_Update_S);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            } else {
                String DS_Table = "UpdateStatement";
                String P_Key_S_New = Get_Current_Date_Time_millis();
                String Statement2 = Statement;
                Statement2 = Statement2.replace ('\n', '\05').replace(',', '\04');

                boolean Ret_Val = (RunQueryOnce_InUp_DS(Statement, Statement2, DS_Table, P_Key_S_New, true));
                UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, P_Key_S_New, Curr_Activity.Act_UpdateStatement, Statement2);
                UPload_Data_Msg_List.add(msg);
                return Ret_Val;
            }
        } else {
            Statement = Statement.replace("\\\\004", "\004");
            Statement = Statement.replace("\\\\005", "\005");
            Statement = Statement.replace('\05', '\n');
            Statement = Statement.replace('\04', ',');
            Log.i (TAG, "InUp2_Monitor statment = " + Statement);
            return RunQueryOnce(Statement);
        }
    }
}
