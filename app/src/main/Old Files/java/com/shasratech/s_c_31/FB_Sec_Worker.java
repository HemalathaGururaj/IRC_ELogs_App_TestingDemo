package com.shasratech.s_c_31;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;
import static com.shasratech.s_c_31.Globals.FBAppCustomer;

public class FB_Sec_Worker extends Thread {

    private  FirebaseApp secondary = null;
    private  String TAG = "FB_Sec_Worker";
    private  boolean SignInComplete = false;
    private  DBHelperUserData myDB_L = null;

    private  String FBAppCustomer_L = "";
    private  String FB_Client_ID_L = "";
    private  String FB_Current_Key_L = "";
    private  String FB_URL_L = "";
    private  String FB_MobileSDK_App_ID_L = "12313123hjk3hjg213";
    private  String FB_User_Email_Sec_L = "arun.ayachith@gmail.com";
    private  String FB_User_Pass_Sec_L = "asdf1234";

    private  FirebaseDatabase mFDB = null;

    private  DatabaseReference mFDB_IMEI_Sec = null;
    private  DatabaseReference mFDB_client_id = null;
    private  DatabaseReference mFDB_current_key = null;
    private  DatabaseReference mFDB_firebase_url = null;
    private  DatabaseReference mFDB_mobilesdk_app_id = null;
    private  DatabaseReference mFDB_Site = null;

    private  DatabaseReference mFDB_Vehicle = null;
    private  DatabaseReference mFDB_Computers = null;
    private  DatabaseReference mFDB_AutoSaveTrans = null;

    private  DatabaseReference mFDB_Transactions = null;
    private  DatabaseReference mFDB_Transport = null;
    private  DatabaseReference mFDB_MoneyTransaction = null;
    private  DatabaseReference mFDB_Changes = null;
    private  DatabaseReference mFDB_Account = null;
    private  DatabaseReference mFDB_Inventory = null;
    private  DatabaseReference mFDB_Person = null;
    private  DatabaseReference mFDB_Material = null;
    private  DatabaseReference mFDB_IRC_ELogs = null;
    private  DatabaseReference mFDB_TrainDetails = null;
    private  DatabaseReference mFDB_HomeShed = null;
    private  DatabaseReference mFDB_CategorizedProblem = null;
    private  DatabaseReference mFDB_CategorizedSolution = null;
    private  DatabaseReference mFDB_DriverDetails = null;
    private  DatabaseReference mFDB_LocoDetails = null;
    private  DatabaseReference mFDB_Location = null;
    private  DatabaseReference mFDB_CustomerDetails = null;
    private  DatabaseReference mFDB_SalesComp = null;
    private  DatabaseReference mFDB_RFID_Access = null;
    private  DatabaseReference mFDB_RFID_Insert = null;
    private  DatabaseReference mFDB_UpdateStatement = null;
    private  DatabaseReference mFDB_AppUsers = null;
    private  DatabaseReference mFDB_Alarms = null;
    private  DatabaseReference mFDB_EmailConf = null;
    private  DatabaseReference mFDB_ExpenseType = null;


    FB_Sec_Worker (String FBAppCustomer, String FB_Client_ID, String FB_Current_Key, String FB_URL, String FB_MobileSDK_App_ID,
                   String FB_User_Email_Sec, String FB_User_Pass_Sec) {
        FBAppCustomer_L = FBAppCustomer;
        FB_Client_ID_L = FB_Client_ID;
        FB_Current_Key_L = FB_Current_Key;
        FB_URL_L = FB_URL;
        FB_MobileSDK_App_ID_L = FB_MobileSDK_App_ID;

        FB_User_Email_Sec_L = FB_User_Email_Sec;
        FB_User_Pass_Sec_L = FB_User_Pass_Sec;
        for (DBHelperUserData_ref t: DBHelperUserData_Ref ) {
            DBHelperUserData t1 = t.getMyDB(FBAppCustomer_L);
            if (t1 != null) {
                myDB_L = t1;
                Log.i (TAG, "95 1234567890 myDB.Get_DB_Name = " + myDB_L.Get_DB_Name());
                break;
            }
        }
    }

    private void FB_Sec_Worker_Init () {

        FirebaseOptions options = null;

        if (
                ((FB_MobileSDK_App_ID_L != null) && (!FB_MobileSDK_App_ID_L.isEmpty())) &&
                        ((FB_Current_Key_L != null) && (!FB_Current_Key_L.isEmpty())) &&
                        ((FB_URL_L != null) && (!FB_URL_L.isEmpty()))
                ) {
            options = new FirebaseOptions.Builder()
                    .setApplicationId(FB_MobileSDK_App_ID_L) // Required for Analytics.
                    .setApiKey(FB_Current_Key_L) // Required for Auth.
                    .setDatabaseUrl(FB_URL_L) // Required for RTDB.
                    .build();
        }

        if (options == null) {
            Log.i (TAG, "Option is Null");
            return;
        }

        Log.i (TAG, "options:: FB_MobileSDK_App_ID_L " + FB_MobileSDK_App_ID_L);
        Log.i (TAG, "options:: FB_Current_Key_L " + FB_Current_Key_L);
        Log.i (TAG, "options:: FB_URL_L " + FB_URL_L);


        boolean hasBeenInitialized=false;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(myGContext_Global);

        for(FirebaseApp app : firebaseApps){
            if(app.getName().equals(FBAppCustomer)){
                hasBeenInitialized=true;
                secondary = app;
            }
        }

        if(!hasBeenInitialized) {
            // Retrieve secondary app.
            FirebaseApp.initializeApp(myGContext_Global, options, FBAppCustomer);
            secondary = FirebaseApp.getInstance(FBAppCustomer);
        }

        FB_Sec_signIn(FB_User_Email_Sec_L, FB_User_Pass_Sec_L);

    }

    private void FB_Sec_signIn(final String email, final String password) {

        if ((email == null) || (email.isEmpty()) || (password == null) || (password.isEmpty())) {
            Show_Network_Config_Err_Msg("FB_Sec_signIn Email/Passwor Empty or null");
            return;
        }
        //if (!Valid_IMEI_Combination) return;
        Log.d(TAG, "signIn: Email =-" + email + "-, Password" + password);

        mFAuth_Sec = FirebaseAuth.getInstance(secondary);
        mFAuth_Sec.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) myGContext_Global, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "188 signInWithEmail:success Email::" + email + ", Password = " + password);
                            FB_user = mFAuth_Sec.getCurrentUser();
                            SignInComplete = true;
                            //sendEmailVerification();
                            Add_Change_Listener();
                            //Init_All();
                        } else {
                            Log.i(TAG, "signInWithEmail:failure", task.getException());
                            return;
                        }

                        if (!task.isSuccessful()) {
                            if (Is_Network_Available()) {
                                ErrMsgDialog("Unable to Sign In,\n\nPlease Check Network Connections\n\nPlease Check Configuration");
                                Log.i(TAG, "signInWithEmail:UnSuccessful", task.getException());
                             //   Intent intent = new Intent(myGContext_Global, Configuration.class);
                             //   myGContext_Global.startActivity(intent);
                            }
                        }
                        Log.i(TAG, "I am here =====================================");
                    }

                });
    }


    private void Add_Change_Listener() {
        // Get the database for the other app.
        mFDB = FirebaseDatabase.getInstance(secondary);
        mFDB_Changes = mFDB.getReference(FBAppName).child(FBAppCustomer).child(FBAppMBName).child("Changes");
        mFDB_Computers = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Computers");
        mFDB_AppCustomer = mFDB.getReference(FBAppName).child(FBAppCustomer);
        mFDB_Material = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Material");
        mFDB_IRC_ELogs = mFDB.getReference(FBAppName).child(FBAppCustomer).child("IRC_ELogs");
        mFDB_HomeShed = mFDB.getReference(FBAppName).child(FBAppCustomer).child("HomeShed");
        mFDB_CategorizedProblem = mFDB.getReference(FBAppName).child(FBAppCustomer).child("CategorizedProblem");
        mFDB_CategorizedSolution = mFDB.getReference(FBAppName).child(FBAppCustomer).child("CategorizedSolution");
        mFDB_DriverDetails = mFDB.getReference(FBAppName).child(FBAppCustomer).child("DriverDetails");
        mFDB_LocoDetails = mFDB.getReference(FBAppName).child(FBAppCustomer).child("LocoDetails");
        mFDB_Location = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Location");
        mFDB_TrainDetails = mFDB.getReference(FBAppName).child(FBAppCustomer).child("TrainDetails");
        mFDB_CustomerDetails = mFDB.getReference(FBAppName).child(FBAppCustomer).child("CustomerDetails");
        mFDB_Site = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Site");
        mFDB_SalesComp = mFDB.getReference(FBAppName).child(FBAppCustomer).child("SellerComp");
        mFDB_Person = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Person");
        mFDB_Transactions = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Transactions");
        mFDB_Transport = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Transport");
        mFDB_Vehicle = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Vehicle");
        mFDB_MoneyTransaction = mFDB.getReference(FBAppName).child(FBAppCustomer).child("MoneyTransaction");
        mFDB_AutoSaveTrans = mFDB.getReference(FBAppName).child(FBAppCustomer).child("AutoSaveTrans");
        mFDB_Account = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Account");
        mFDB_RFID_Access = mFDB.getReference(FBAppName).child(FBAppCustomer).child("RFID_Access");
        mFDB_RFID_Insert = mFDB.getReference(FBAppName).child(FBAppCustomer).child("RFID_Insert");
        mFDB_UpdateStatement = mFDB.getReference(FBAppName).child(FBAppCustomer).child("UpdateStatement");
        mFDB_AppUsers = mFDB.getReference(FBAppName).child(FBAppCustomer).child("AppUsers");
        mFDB_Alarms = mFDB.getReference(FBAppName).child(FBAppCustomer).child("Alarms");
        mFDB_EmailConf = mFDB.getReference(FBAppName).child(FBAppCustomer).child("EmailConf");
        mFDB_ExpenseType = mFDB.getReference(FBAppName).child(FBAppCustomer).child("ExpenseType");


        Add_Changes_Child_Event_Listeners();
        Add_Device_List_Change_Listener();
    }

    private void Add_Changes_Child_Event_Listeners () {
        mFDB_Changes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String Privious_ChildName) {
                String Key = dataSnapshot.getKey();

                //Log.i (TAG, "Add_Changes_Child_Event_Listeners FBAppCustomer_L = " + FBAppCustomer_L + ", myDB_L = " + myDB_L + ", DB_Name = " + myDB_L.Get_DB_Name());
                Process_FB_Input_Changes(Key);
                try {
                    Thread.sleep(1); // For now just Sleep after every Input so, that you don't consume all resources.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private boolean Process_FB_Input_Changes (final String Input) {
        Log.i (TAG, "Process_FB_Input_Changes = " + Input);
        new Thread() {
            public void run() {
                String[] Data = Input.split("~");
                String Data_TimeStamp = (Data != null)?Data[0]:"";
                final String Table = ((Data != null) && (Data.length >= 2))?Data[1]:"";
                final String P_Key = ((Data != null) && (Data.length >= 3))?Data[2]:"";
/*
                final String Table = Data[1];
                final String P_Key = Data[2];
*/
                final boolean[] ret_Val = {false};

                if ((Table == null) || (Table.isEmpty())
                        || (P_Key == null) || (P_Key.isEmpty())) {
                    DatabaseReference mFDB_Changes_Child = mFDB.getReference(FBAppName).child(FBAppCustomer).child(FBAppMBName).child("Changes").child(Input);
                    Log.i(TAG, "Removing Changes " + Input);
                    mFDB_Changes_Child.removeValue();
                    return;
                }

                DatabaseReference mFDB_Temp = mFDB.getReference(FBAppName).child(FBAppCustomer).child(Table).child(P_Key);

                if (mFDB_Temp != null) {
                    mFDB_Temp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String Key = dataSnapshot.getKey();
                            String data = dataSnapshot.getValue(String.class);

                            Log.i(TAG, "~~~~~~~~~~~~~~~~~~~~~~~Received Key = " + Key + ", Table = " + Table);
                            if (data == null) return;
                            Log.i(TAG, "~~~~~~~~~~~~~~~~~~~~~~~Received Data = " + data);

                            String Row = data;

                            String[] Con_List = data.split(",");

                            boolean DB_Update_Response = false;

                            switch (Table) {
                                case "Account":
                                    DB_Update_Response = myDB_L.InUp2_Account_UD(P_Key, Con_List, false, Row);
                                    break;
                                case "AppUsers":
                                    DB_Update_Response = myDB_L.InUp2_AppUsers(P_Key, Con_List, false, Row);
                                    break;
                                case "RFID_Access":
                                    DB_Update_Response = myDB_L.InUp2_RFID_Access_UD(P_Key, Con_List, false, Row);
                                    break;
                                case "RFID_Insert":
                                    DB_Update_Response = myDB_L.InUp2_RFID_Insert_UD(P_Key, Con_List, false, Row);
                                    break;
                                case "AutoSaveTrans":
                                    DB_Update_Response = myDB_L.InUp2_AutoSaveTrans(P_Key, Con_List, false, true, Row);
                                    break;
                                case "CustomerDetails":
                                    DB_Update_Response = myDB_L.InUp2_CustomerDetails(P_Key, Con_List, false);
                                    break;
                                case "Material":
                                    DB_Update_Response = myDB_L.InUp2_Material(P_Key, Con_List, false);
                                    break;
                                case "IRC_ELogs":
                                    DB_Update_Response = myDB_L.InUp2_IRC_ELogs(P_Key, Con_List, false);
                                    break;
                                case "HomeShed":
                                    DB_Update_Response = myDB_L.InUp2_HomeShed(P_Key, Con_List, false);
                                    break;
                                case "CategorizedProblem":
                                    DB_Update_Response = myDB_L.InUp2_CategorizedProblem(P_Key, Con_List, false);
                                    break;
                                case "Location":
                                    DB_Update_Response = myDB_L.InUp2_Location(P_Key, Con_List, false);
                                    break;
                                case "TrainDetails":
                                    DB_Update_Response = myDB_L.InUp2_Location(P_Key, Con_List, false);
                                    break;
                                case "DriverDetails":
                                    DB_Update_Response = myDB_L.InUp2_DriverDetails(P_Key, Con_List, false);
                                    break;
                                case "LocoDetails":
                                    DB_Update_Response = myDB_L.InUp2_LocoDetails(P_Key, Con_List, false);
                                    break;
                                case "MoneyTransaction":
                                    DB_Update_Response = myDB_L.InUp2_MoneyTransaction_UD(P_Key, Con_List, false);
                                    break;
                                case "Person":
                                    DB_Update_Response = myDB_L.InUp2_Person(P_Key, Con_List, false);
                                    break;
                                case "SellerComp":
                                    DB_Update_Response = myDB_L.InUp2_SellerComp(P_Key, Con_List, false);
                                    break;
                                case "Site":
                                    DB_Update_Response = myDB_L.InUp2_Site(P_Key, Con_List, false);
                                    break;
                                case "Transactions":
                                    DB_Update_Response = myDB_L.InUp2_Transactions_UD(P_Key, Con_List, false, true, Row);
                                    break;
                                case "Transport":
                                    DB_Update_Response = myDB_L.InUp2_Transport(P_Key, Con_List, false, Row);
                                    break;
                                // case "GatePass" : DB_Update_Response = InUp2_GatePass(P_Key, Con_List, false); break;
                                // case "Inventory" : DB_Update_Response = InUp2_Inventory(P_Key, Con_List, false); break;
                                // case "System_Configuration" : DB_Update_Response = InUp2_SysConf_DS(P_Key, Con_List, false); break;
                                case "UpdateStatement": {
                                    Row = Row.replace("\\\\004", "\004");
                                    Row = Row.replace("\\\\005", "\005");
                                    Row = Row.replace("\\\\t", "\t");
                                    Row = Row.replace('\005', '\n').replace('\004', ',');
                                    Row = Row.replace('\004', ',');
                                    Row = Row.replace("\\005", "\n").replace("\\004", ",");
                                    Row = Row.replace("\004", ",");

                                    Log.i(TAG, "287 System_Configuration Row = " + Row);

                                    if (Row.contains("UPDATE System_Configuration set")) {

                                        //AppendData2File(SystemLogFile,  Table + " ----306----, " + Row + "\n\n", false, false);
                                        if ((Row.contains("SelectedColumns")) ||
                                                (Row.contains("AlterColumns")) ||
                                                (Row.contains("IntSortColumns"))
                                                ) {
                                            //if (Sem_Aquired) { Account_Lock.Sem_Release(); }
                                            ret_Val[0] = true;
                                            //return true;
                                        }
                                        //DB_Update_Response = myDB_L.InUp2_SysConf_UD(Row);

                                        DB_Update_Response = true;
                                    } else if (Row.contains("UPDATE GatePass set")) {
                                    } else {
                                        if (Row.contains("Transactions")) {
                                            Row = Row.replace("Pmt_KG", "Permit_KG");
                                        }
                                        if (Row.contains("UPDATE AutoSaveTrans")) {
                                            Row = Row.replace("Match_ID", "Mapped_ID");
                                        }
                                        DB_Update_Response = myDB_G.RunQueryOnce(Row);
                                        Log.i(TAG, "300 DB_Update_Response Row = " + Row);
                                        Log.i(TAG, "DB_Update_Response = " + DB_Update_Response);
                                    }
                                }
                                break;
                                case "Vehicle":
                                    DB_Update_Response = myDB_L.InUp2_Vehicle_UD(P_Key, Con_List, false);
                                    break;

                                default:
                                    // AppendData2File(SystemLogFile,  Table + " Case Not Handled ----314----, " + Row + "\n\n", false, false);
                                    // if (Sem_Aquired) { Account_Lock.Sem_Release(); }
                                    return;
                            }

                            Log.i(TAG, "Table = " + Table + " :: DB_Update_Response = ================= " + DB_Update_Response);
                            if (DB_Update_Response) {
                                // Need to Delete from Cloud
                                DatabaseReference mFDB_Changes_Child = mFDB.getReference(FBAppName).child(FBAppCustomer).child(FBAppMBName).child("Changes").child(Input);
                                Log.i(TAG, "Removing Changes " + Input);
                                mFDB_Changes_Child.removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i (TAG, "Event Called:: "  + Input);

                        }
                    });
                }

                // if (Sem_Aquired) { Account_Lock.Sem_Release(); }
                //MyDebugPrintf();
                return;
            }
        }.start();
        return true;
    }

    private void Add_Device_List_Change_Listener() {
        if (mFDB_Computers != null) {
            mFDB_Computers.child(FBAppMBName).setValue(FBAppMBName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            // ...

                          //  Log.i(TAG, "IMEI_Update_Complete = ");

                        }
                    });

            mFDB_Computers.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String Privious_ChildName) {
                    String Key = dataSnapshot.getKey();
                    String data = dataSnapshot.getValue(String.class);

                    myDB_L.Insert_update_into_Computer_Ordered(data);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public void run () {
        FB_Sec_Worker_Init ();

        while (true) {

            //====================================== Download Section =========================================
            if (DownLoad_Complete_Table_from_Cloud_Act_CustomerDetails) {
                DownLoad_Complete_Table_from_Cloud_Act_CustomerDetails = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_CustomerDetails);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Material) {
                DownLoad_Complete_Table_from_Cloud_Act_Material = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Material);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_IRC_ELogs) {
                DownLoad_Complete_Table_from_Cloud_Act_IRC_ELogs = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_IRC_ELogs);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_HomeShed) {
                DownLoad_Complete_Table_from_Cloud_Act_HomeShed = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_HomeShed);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_CategorizedProblem) {
                DownLoad_Complete_Table_from_Cloud_Act_CategorizedProblem = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_CategorizedProblem);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_CategorizedSolution) {
                DownLoad_Complete_Table_from_Cloud_Act_CategorizedSolution = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_CategorizedSolution);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_Location) {
                DownLoad_Complete_Table_from_Cloud_Act_Location = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Location);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_TrainDetails) {
                DownLoad_Complete_Table_from_Cloud_Act_TrainDetails = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_TrainDetails);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_DriverDetails) {
                DownLoad_Complete_Table_from_Cloud_Act_DriverDetails = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_DriverDetails);
            }
            if (DownLoad_Complete_Table_from_Cloud_Act_LocoDetails) {
                DownLoad_Complete_Table_from_Cloud_Act_LocoDetails = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_LocoDetails);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Person) {
                DownLoad_Complete_Table_from_Cloud_Act_Person = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Person);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_SellerComp) {
                DownLoad_Complete_Table_from_Cloud_Act_SellerComp = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_SellerComp);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Transactions) {
                DownLoad_Complete_Table_from_Cloud_Act_Transactions = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Transactions);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Site) {
                DownLoad_Complete_Table_from_Cloud_Act_Site = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Site);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Vehicle) {
                DownLoad_Complete_Table_from_Cloud_Act_Vehicle = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Vehicle);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_MoneyTransaction) {
                DownLoad_Complete_Table_from_Cloud_Act_MoneyTransaction = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_MoneyTransaction);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_AutoSaveTrans) {
                DownLoad_Complete_Table_from_Cloud_Act_AutoSaveTrans = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_AutoSaveTrans);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Account) {
                DownLoad_Complete_Table_from_Cloud_Act_Account = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Account);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_RFID_Access) {
                DownLoad_Complete_Table_from_Cloud_Act_RFID_Access = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_RFID_Access);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_RFID_Insert) {
                DownLoad_Complete_Table_from_Cloud_Act_RFID_Insert = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_RFID_Insert);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Transport) {
                DownLoad_Complete_Table_from_Cloud_Act_Transport = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Transport);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_AppUsers) {
                DownLoad_Complete_Table_from_Cloud_Act_AppUsers = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_AppUsers);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Alarms) {
                DownLoad_Complete_Table_from_Cloud_Act_Alarms = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_Alarms);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_EmailConf) {
                DownLoad_Complete_Table_from_Cloud_Act_EmailConf = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_EmailConf);
            }

            if (DownLoad_Complete_Table_from_Cloud_Act_Expense) {
                DownLoad_Complete_Table_from_Cloud_Act_Expense = false;
                DownLoad_Complete_Table_from_Cloud(Curr_Activity.Act_ExpenseType);
            }
            //====================================== Download Section =========================================


            //====================================== Upload Section ===========================================
            int a = 0;
            int size = UPload_Data_Msg_List.size();
            for (; a < size; a++) {
                UPload_Data_Msg temp = UPload_Data_Msg_List.get(a);

                if (FBAppCustomer_L.equals(temp.FBAppCustomer)) {
                    Upload_Data_InUp2_FB(temp.Name, temp.Activity, temp.UpdateStatement);
                    UPload_Data_Msg_List.remove(temp);
                }
                break;
            }
            //====================================== Upload Section ===========================================

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.i (TAG, "FBAppCustomer_L = " + FBAppCustomer_L + ", myDB_L = "  + myDB_L + ", DB_Name = " + myDB_L.Get_DB_Name());
        }
    }


     void Upload_Data_InUp2_FB(final String Name, final Curr_Activity Activity, final String UpdateStatement) {

        new Thread () {
            public void run () {
                Log.i(TAG, "UpdateStatement = " + UpdateStatement);
                final String P_Key = Name;
                String Table = "";
                final String Time_milli = Get_Current_Date_Time_millis();

                DatabaseReference mFDB_temp = null;

                switch (Activity) {
                    case Act_Material:
                        Table = "Material";
                        mFDB_temp = mFDB_Material;
                        break;
                    case Act_IRC_ELogs:
                        Table = "IRC_ELogs";
                        mFDB_temp = mFDB_IRC_ELogs;
                        break;
                    case Act_HomeShed:
                        Table = "HomeShed";
                        mFDB_temp = mFDB_HomeShed;
                        break;
                    case Act_CategorizedProblem:
                        Table = "CategorizedProblem";
                        mFDB_temp = mFDB_CategorizedProblem;
                        break;
                    case Act_CategorizedSolution:
                        Table = "CategorizedSolution";
                        mFDB_temp = mFDB_CategorizedSolution;
                        break;
                    case Act_Location:
                        Table = "Location";
                        mFDB_temp = mFDB_Location;
                        break;
                    case Act_TrainDetails:
                        Table = "TrainDetails";
                        mFDB_temp = mFDB_TrainDetails;
                        break;
                    case Act_DriverDetails:
                        Table = "DriverDetails";
                        mFDB_temp = mFDB_DriverDetails;
                        break;
                    case Act_LocoDetails:
                        Table = "LocoDetails";
                        mFDB_temp = mFDB_LocoDetails;
                        break;
                    case Act_Person:
                        Table = "Person";
                        mFDB_temp = mFDB_Person;
                        break;
                    case Act_CustomerDetails:
                        Table = "CustomerDetails";
                        mFDB_temp = mFDB_CustomerDetails;
                        break;
                    case Act_SellerComp:
                        Table = "Seller_Comp";
                        mFDB_temp = mFDB_SalesComp;
                        break;
                    case Act_Site:
                        Table = "Site";
                        mFDB_temp = mFDB_Site;
                        break;
                    case Act_Transport:
                        Table = "Transport";
                        mFDB_temp = mFDB_Transport;
                        break;
                    case Act_Vehicle:
                        Table = "Vehicle";
                        mFDB_temp = mFDB_Vehicle;
                        break;
                    case Act_MoneyTransaction:
                        Table = "MoneyTransaction";
                        mFDB_temp = mFDB_MoneyTransaction;
                        break;

                    case Act_Account:
                        Table = "Account";
                        mFDB_temp = mFDB_Account;
                        break;

                    case Act_RFID_Access:
                        Table = "RFID_Access";
                        mFDB_temp = mFDB_RFID_Access;
                        break;

                    case Act_RFID_Insert:
                        Table = "RFID_Insert";
                        mFDB_temp = mFDB_RFID_Insert;
                        break;

                    case Act_UpdateStatement:
                        Table = "UpdateStatement";
                        mFDB_temp = mFDB_UpdateStatement;
                        break;

                    case Act_AppUsers:
                        Table = "AppUsers";
                        mFDB_temp = mFDB_AppUsers;
                        break;

                    case Act_Alarms:
                        Table = "Alarms";
                        mFDB_temp = mFDB_Alarms;
                        break;

                    case Act_EmailConf:
                        Table = "EmailConf";
                        mFDB_temp = mFDB_EmailConf;
                        break;

                    case Act_ExpenseType:
                        Table = "ExpenseType";
                        mFDB_temp = mFDB_ExpenseType;
                        break;


                    default:
                        return;
                }

                final int Ret_Val = 0;
                final int Expected_Result = 0;

                //Log.i (TAG, "Material_InUp2_FB Name = " + Name + ",UpdateStatement =  " + UpdateStatement);
                final String finalTable = Table;
                mFDB_temp.child(Name).setValue(UpdateStatement)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Ret_Val++;
                                List<String> ComputerList = new ArrayList<>();

                                myDB_L.Get_Val_from_DB_UD("Computers", "Name", "", "", ComputerList);
                                int a = 0;
                                int b = ComputerList.size();
                                //Expected_Result = b;

                                for (String PC : ComputerList) {
                                    if (PC.equals(FBAppMBName)) continue; // This Mobile
                                    //Log.i (TAG, "mFDB_Material ComputerList  PC = " + PC);
                                    mFDB_AppCustomer.child(PC).child("Changes").child(Time_milli + "~" + finalTable + "~" + P_Key).setValue(FBAppMBName)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Ret_Val++;
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        }.start();
    }

    private void DownLoad_Complete_Table_from_Cloud (final Curr_Activity Activity) {

        Log.i (TAG, "FBAppCustomer_L = " + FBAppCustomer_L + ", FB_Client_ID_L = " + FB_Client_ID_L);
        Log.i (TAG, "FB_Current_Key_L = " + FB_Current_Key_L + ", FB_URL_L = " + FB_URL_L);
        Log.i (TAG, "FB_User_Email_Sec_L = " + FB_User_Email_Sec_L + ", FB_User_Pass_Sec_L = " + FB_User_Pass_Sec_L);
        Log.i (TAG, "FB_MobileSDK_App_ID_L = " + FB_MobileSDK_App_ID_L);


        Log.i (TAG, "DownLoad_Complete_Table_from_Cloud called for " + Activity);
        new Thread() {
            public void run() {
                Log.i (TAG, "111 Valid_IMEI_Combination " + Valid_IMEI_Combination);
                if (!Valid_IMEI_Combination) {
                    ErrMsgDialog_L(TAG + " Not a Valid Mobile [" + FBAppMBName + "]\n\nContact ShasRaTech [9731270001]\nfor further details");
                    return;
                }
                DatabaseReference mFDB_temp = null;
                String Table = "";
                switch(Activity) {
                    case Act_AppUsers:
                        mFDB_temp = mFDB_AppUsers;
                        Table = "AppUsers";
                        break;
                    case Act_Material:
                        mFDB_temp = mFDB_Material;
                        Table = "Material";
                        break;
                    case Act_IRC_ELogs:
                        mFDB_temp = mFDB_IRC_ELogs;
                        Table = "IRC_ELogs";
                        break;
                    case Act_HomeShed:
                        mFDB_temp = mFDB_HomeShed;
                        Table = "HomeShed";
                        break;
                    case Act_CategorizedProblem:
                        mFDB_temp = mFDB_CategorizedProblem;
                        Table = "CategorizedProblem";
                        break;
                    case Act_CategorizedSolution:
                        mFDB_temp = mFDB_CategorizedSolution;
                        Table = "CategorizedSolution";
                        break;
                    case Act_Location:
                        mFDB_temp = mFDB_Location;
                        Table = "Location";
                        break;
                    case Act_TrainDetails:
                        mFDB_temp = mFDB_TrainDetails;
                        Table = "TrainDetails";
                        break;
                    case Act_DriverDetails:
                        mFDB_temp = mFDB_DriverDetails;
                        Table = "DriverDetails";
                        break;
                    case Act_LocoDetails:
                        mFDB_temp = mFDB_LocoDetails;
                        Table = "LocoDetails";
                        break;
                    case Act_Person:
                        mFDB_temp = mFDB_Person;
                        Table = "Person";
                        break;
                    case Act_SellerComp:
                        mFDB_temp = mFDB_SalesComp;
                        Table = "SellerComp";
                        break;

                    case Act_CustomerDetails:
                        mFDB_temp = mFDB_CustomerDetails;
                        Table = "CustomerDetails";
                        break;

                    case Act_Site:
                        mFDB_temp = mFDB_Site;
                        Table = "Site";
                        break;
                    case Act_Transactions:
                        mFDB_temp = mFDB_Transactions;
                        Table = "Transactions";
                        break;

                    case Act_Vehicle:
                        mFDB_temp = mFDB_Vehicle;
                        Table = "Vehicle";
                        break;

                    case Act_MoneyTransaction:
                        mFDB_temp = mFDB_MoneyTransaction;
                        Table = "MoneyTransaction";
                        break;

                    case Act_AutoSaveTrans:
                        mFDB_temp = mFDB_AutoSaveTrans;
                        Table = "AutoSaveTrans";
                        break;

                    case Act_Account:
                        mFDB_temp = mFDB_Account;
                        Table = "Account";
                        break;

                    case Act_RFID_Access:
                        mFDB_temp = mFDB_RFID_Access;
                        Table = "RFID_Access";
                        break;

                    case Act_RFID_Insert:
                        mFDB_temp = mFDB_RFID_Insert;
                        Table = "RFID_Insert";
                        break;

                    case Act_Transport:
                        mFDB_temp = mFDB_Transport;
                        Table = "Transport";
                        break;

                    case Act_Alarms:
                        mFDB_temp = mFDB_Alarms;
                        Table = "Alarms";
                        break;

                    case Act_EmailConf:
                        mFDB_temp = mFDB_EmailConf;
                        Table = "EmailConf";
                        break;

                    case Act_ExpenseType:
                        mFDB_temp = mFDB_ExpenseType;
                        Table = "ExpenseType";
                        break;

                }

                Log.i (TAG, "mFDB_temp = " + mFDB_temp);
                if(mFDB_temp !=null)

                {
                    final String finalTable = Table;
                    mFDB_temp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final ProgressDialog pd = new ProgressDialog(myGContext_Global);

                            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                            // Set the progress dialog title and message
                            pd.setTitle(finalTable + " data downloading from Cloud");
                            pd.setMessage("Loading.........");

                            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

                            pd.setIndeterminate(false);

                            // Finally, show the progress dialog
                            pd.show();

                            // Set the progress status zero on each button click

                            long a = 0;
                            long b;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                b = child.getChildrenCount();
                                pd.setMessage("Loading [" + a + " ]/[" + b + "]");
                                pd.setProgress((int) a);

                                try {
                                    Thread.sleep(1); // Sleep For 1 msec, so, that we give some space for others.
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                String P_Key = child.getKey();
                                String data = child.getValue(String.class);
                                if (data == null) continue;

                                String[] Con_List = data.split(",");

                                switch (Activity) {
                                    case Act_Material:
                                        myDB_L.InUp2_Material(P_Key, Con_List, false);
                                        break;
                                    case Act_Person:
                                        Log.i (TAG, "myGB = " + myDB_L);
                                        myDB_L.InUp2_Person(P_Key, Con_List, false);
                                        break;
                                    case Act_SellerComp:
                                        myDB_L.InUp2_SellerComp(P_Key, Con_List, false);
                                        break;
                                    case Act_CustomerDetails:
                                        myDB_L.InUp2_CustomerDetails(P_Key, Con_List, false);
                                        break;
                                    case Act_Site:
                                        myDB_L.InUp2_Site(P_Key, Con_List, false);
                                        break;
                                    case Act_Transactions:
                                        myDB_L.InUp2_Transactions_UD(P_Key, Con_List, false, true, data);
                                        break;
                                    case Act_Vehicle:
                                        myDB_L.InUp2_Vehicle_UD(P_Key, Con_List, false, true, data);
                                        break;
                                    case Act_MoneyTransaction:
                                        myDB_L.InUp2_MoneyTransaction_UD(P_Key, Con_List, false, true, data);
                                        break;

                                    case Act_AutoSaveTrans:
                                        myDB_L.InUp2_AutoSaveTrans(P_Key, Con_List, false, true, data);
                                        break;

                                    case Act_Account:
                                        myDB_L.InUp2_Account_UD(P_Key, Con_List, false,  data);
                                        break;

                                    case Act_RFID_Access:
                                        myDB_L.InUp2_RFID_Access_UD(P_Key, Con_List, false,  data);
                                        break;

                                    case Act_RFID_Insert:
                                        myDB_L.InUp2_RFID_Insert_UD(P_Key, Con_List, false,  data);
                                        break;

                                    case Act_Transport:
                                        myDB_L.InUp2_Transport(P_Key, Con_List, false,  data);
                                        break;

                                    case Act_AppUsers:
                                        myDB_L.InUp2_AppUsers(P_Key, Con_List, false,  data);
                                        break;

                                    case Act_IRC_ELogs:
                                        myDB_L.InUp2_IRC_ELogs(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_HomeShed:
                                        myDB_L.InUp2_HomeShed(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_CategorizedSolution:
                                        myDB_L.InUp2_CategorizedSolution(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_CategorizedProblem:
                                        myDB_L.InUp2_CategorizedProblem(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_Location:
                                        myDB_L.InUp2_Location(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_TrainDetails:
                                        myDB_L.InUp2_TrainDetails(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_DriverDetails:
                                        myDB_L.InUp2_DriverDetails(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_LocoDetails:
                                        myDB_L.InUp2_LocoDetails(P_Key, Con_List, false,  data);
                                        break;
                                    case Act_Alarms:
                                        myDB_L.InUp2_Alarms(P_Key, Con_List, false,  data);
                                        break;

                                    case Act_EmailConf:
                                        myDB_L.InUp2_EmailConf(P_Key, Con_List, false,  data);
                                        break;

                                    case Act_ExpenseType:
                                        myDB_L.InUp2_ExpenseType(P_Key, Con_List, false,  data);
                                        break;

                                }
                            }
                            pd.dismiss();
                            ErrMsgDialog(finalTable + " DownLoad Complete");


                            Context this_Context = null;

                            switch (Activity) {
                                case Act_Material:
                                    Intent Material = new Intent(myGContext_Global, Material.class);
                                    myGContext_Global.startActivity(Material);
                                    break;
                                case Act_IRC_ELogs:
                                    Intent IRC_ELogs = new Intent(myGContext_Global , IRC_ELogs.class);
                                    myGContext_Global.startActivity(IRC_ELogs);
                                    break;
                                case Act_HomeShed:
                                    Intent HomeShed = new Intent(myGContext_Global , HomeShed.class);
                                    myGContext_Global.startActivity(HomeShed);
                                    break;
                                case Act_CategorizedProblem:
                                    Intent CategorizedProblem = new Intent(myGContext_Global , CategorizedProblem.class);
                                    myGContext_Global.startActivity(CategorizedProblem);
                                    break;
                                case Act_CategorizedSolution:
                                    Intent CategorizedSolution = new Intent(myGContext_Global , com.shasratech.s_c_31.CategorizedSolution.class);
                                    myGContext_Global.startActivity(CategorizedSolution);
                                    break;
                                case Act_Location:
                                    Intent Location = new Intent(myGContext_Global , Location.class);
                                    myGContext_Global.startActivity(Location);
                                    break;
                                case Act_TrainDetails:
                                    Intent TrainDetails = new Intent(myGContext_Global , TrainDetails.class);
                                    myGContext_Global.startActivity(TrainDetails);
                                    break;
                                case Act_DriverDetails:
                                    Intent DriverDetails = new Intent(myGContext_Global , DriverDetails.class);
                                    myGContext_Global.startActivity(DriverDetails);
                                    break;
                                case Act_LocoDetails:
                                    Intent LocoDetails = new Intent(myGContext_Global , LocoDetails.class);
                                    myGContext_Global.startActivity(LocoDetails);
                                    break;
                                case Act_Person:
                                    Intent Person = new Intent(myGContext_Global, Person.class);
                                    myGContext_Global.startActivity(Person);
                                    break;
                                case Act_SellerComp:
                                    Intent SellerComp = new Intent(myGContext_Global, SellerComp.class);
                                    myGContext_Global.startActivity(SellerComp);
                                    break;

                                case Act_CustomerDetails:
                                    Intent CustomerDetails = new Intent(myGContext_Global, CustomerDetails.class);
                                    myGContext_Global.startActivity(CustomerDetails);
                                    break;

                                case Act_Site:
                                    Intent Site = new Intent(myGContext_Global, Site.class);
                                    myGContext_Global.startActivity(Site);
                                    break;

                                case Act_Transactions:
                                    Intent Transactions = new Intent(myGContext_Global, Transactions.class);
                                    myGContext_Global.startActivity(Transactions);
                                    break;

                                case Act_Vehicle:
                                    Intent Vehicle = new Intent(myGContext_Global, Vehicle.class);
                                    myGContext_Global.startActivity(Vehicle);
                                    break;

                                case Act_MoneyTransaction:
                                    Intent MoneyTransaction = new Intent(myGContext_Global, MoneyTransaction.class);
                                    myGContext_Global.startActivity(MoneyTransaction);
                                    break;
                                case Act_AutoSaveTrans:
                                    Intent AutoSaveTrans = new Intent(myGContext_Global, AutoSaveTrans.class);
                                    myGContext_Global.startActivity(AutoSaveTrans);
                                    break;

                                case Act_Account:
                                    Intent Account = new Intent(myGContext_Global, Account.class);
                                    myGContext_Global.startActivity(Account);
                                    break;

                                case Act_RFID_Access:
                                    //Intent RFID_Access = new Intent(myGContext_Global, RFID_Access.class);
                                    //myGContext_Global.startActivity(RFID_Access);
                                    break;

                                case Act_RFID_Insert:
                                    //Intent RFID_Insert = new Intent(myGContext_Global, RFID_Insert.class);
                                    //myGContext_Global.startActivity(RFID_Insert);
                                    break;


                                case Act_Transport:
                                    Intent Transport = new Intent(myGContext_Global, Transport.class);
                                    myGContext_Global.startActivity(Transport);
                                    break;

                                case Act_AppUsers:
                                    Intent AppUsers = new Intent(myGContext_Global, AppUsers.class);
                                    myGContext_Global.startActivity(AppUsers);
                                    break;

                                case Act_Alarms:
                                    Intent Alarms = new Intent(myGContext_Global, Alarms_Check.class);
                                    myGContext_Global.startActivity(Alarms);
                                    break;

                                case Act_EmailConf:
                                    Intent EmailConf = new Intent(myGContext_Global, EmailConf.class);
                                    myGContext_Global.startActivity(EmailConf);
                                    break;

                                case Act_ExpenseType:
                                    Intent ExpenseType = new Intent(myGContext_Global, ExpenseType.class);
                                    myGContext_Global.startActivity(ExpenseType);
                                    break;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        }.start();
    }
}
