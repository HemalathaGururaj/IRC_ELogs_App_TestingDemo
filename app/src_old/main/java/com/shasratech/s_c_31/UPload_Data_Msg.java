package com.shasratech.s_c_31;

public class UPload_Data_Msg {
    String FBAppCustomer;
    String Name;
    Curr_Activity Activity;
    String UpdateStatement;

    public UPload_Data_Msg(String fBAppCustomer, String name, Curr_Activity activity, String updateStatement) {
        FBAppCustomer = fBAppCustomer;
        Name = name;
        Activity = activity;
        UpdateStatement = updateStatement;
    }
}
