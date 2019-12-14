package com.shasratech.s_c_31;

import android.util.Log;

public class DBHelperUserData_ref {
    String FBAppCustomer1;
    static String FB;
    DBHelperUserData myDB1;
    private final static String TAG = "DBHelperUserData_ref";

    public DBHelperUserData_ref(String FBAppCustomer, DBHelperUserData myDB) {
        this.FBAppCustomer1 = FBAppCustomer;
        FB = FBAppCustomer;
        this.myDB1 = myDB;
        Log.i (TAG, "13 Argument = " + FBAppCustomer + ", myDB1 = " + myDB1);
    }

    public DBHelperUserData getMyDB(String fBAppCustomer) {
        Log.i (TAG, "17 Argument = " + fBAppCustomer + ", FBAppCustomer = " + FBAppCustomer1);
        if (fBAppCustomer.equals(FBAppCustomer1)) {
            Log.i (TAG, "19 Argument = " + fBAppCustomer + ", FBAppCustomer = " + FBAppCustomer1 + ", myDB =" + myDB1);
            return myDB1;
        } else {
            return null;
        }
    }

    public String Get_Name () {
        return FB + "-" + FBAppCustomer1;
    }
}
