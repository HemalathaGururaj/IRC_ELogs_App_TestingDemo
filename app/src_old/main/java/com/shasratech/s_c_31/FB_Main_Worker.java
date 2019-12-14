package com.shasratech.s_c_31;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

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

import java.util.concurrent.Executor;

import static com.shasratech.s_c_31.DBHelperAppUtils.*;
import static com.shasratech.s_c_31.DBHelperUserData.*;
import static com.shasratech.s_c_31.Globals.*;
import static com.shasratech.s_c_31.MainActivity.*;

public class FB_Main_Worker  extends Thread  {
    private static final String TAG = "FB_Main_Worker";

    private static boolean FB1_InitComplete = false;
    private static boolean SignInComplete = false;
    private static FirebaseDatabase mFDB = null;

   // public static boolean IMEI_Verification_Complete = false;
   public static boolean IMEI_Upload_Complete = false;

    public static DatabaseReference mFDB_IMEI_Main = null;
    int Loop_Interval = 1000;
    boolean Permission_Denied = false;
    static boolean SignInMessageDisplayed = false;


    FB_Main_Worker() {
        //Globals.Init_Globals_Context_from_MainActivity(this);

    }

    @Override
    public void run () {
        int Secs_Count = 0;
        while (true) {

           // Log.i (TAG, "59 In While Loop =============");
            while (!Is_Network_Available() || !myDB_G.DB_Init_Complete_US) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Log.i (TAG, "67 In While Loop =============FB_N_Activity_Started=" + FB_N_Activity_Started + ", Configuration_Changed= "
           // + Configuration_Changed + ", Permission_Denied = " + Permission_Denied);

            if (!FB_N_Activity_Started || Configuration_Changed) {
                if (Configuration_Changed) {
                    Permission_Denied = false;
                }
                if (!Permission_Denied) {
                    Init_All();
                }
            }
           // Log.i (TAG, "77 In While Loop =============");


            //if (Configuration_Changed) {
                Loop_Interval = 1000;
            //}

            try {
                //Thread.sleep((Configuration_Changed)?1000:Loop_Interval);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.i (TAG, "90 In While Loop =============");

            Secs_Count++;
            if ((Permission_Denied) && ((Secs_Count % 60) == 0)) {
                Permission_Denied = false;
            }
            if (Secs_Count > 86400) {
                Secs_Count = 0;
                Periodic_Verify_IMEI_Code();
            }
            //Log.i (TAG, "100 In While Loop =============");
        }
    }

    private void Init_All() {
      //  Log.i (TAG, "DEvice ID = " + Device_IMEI_ID + "==============================================================");
      //  Log.i (TAG, "106 In Init_All  =============");

        if ( ((Device_IMEI_ID != null) && (!Device_IMEI_ID.isEmpty()))) {
            //Log.i (TAG, "Calling IsNetAvailable ===================");

            if (!Is_Network_Available ()) {
                Show_Network_Config_Err_Msg();
            }

            //Log.i (TAG, "115 In Init_All  =============");

            if (!FB_N_Activity_Started || Configuration_Changed) {
                if (((FBAppName != null) && (!FBAppName.isEmpty())) &&
                        ((FBAppCustomer != null) && (!FBAppCustomer.isEmpty())) &&
                        ((FB_User_Email_Main != null) && (!FB_User_Email_Main.isEmpty())) &&
                        ((FB_User_Pass_Main != null) && (!FB_User_Pass_Main.isEmpty()))
                        ) {

                   // Log.i (TAG, "124 In Init_All  =============");
                    mFAuth_Main = FirebaseAuth.getInstance();

                   // Log.i (TAG, "mFAuth_Main is " + mFAuth_Main);
                    if (mFAuth_Main == null) {
                        Log.i (TAG, "mFAuth_Main is NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    }
                    mFDB = FirebaseDatabase.getInstance();
                    if (mFDB == null) {
                        Log.i (TAG, "mFDB is NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    }
                    mFDB_AppCustomer = mFDB.getReference(FBAppCustomer);
                    if (mFDB_AppCustomer == null) {
                        Log.i (TAG, "mFDB_AppCustomer is NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    }

                    Create_FB_User();
                   // Log.i(TAG, "142 Calling Singin ==========================  SignInComplete = " + SignInComplete);
                    if (!SignInComplete) {
                        //Log.i(TAG, "Calling Create_FB_User ==========================");


                     //   Log.i(TAG, "Calling Singin ==========================  SignInComplete = " + SignInComplete);
                        FB_Main_SignIn(FB_User_Email_Main, FB_User_Pass_Main);
                    }

                    if (SignInComplete) {
                        if (!IMEI_Upload_Complete || Configuration_Changed) {
                            Verify_IMEI_Code();
                        }
                       // Log.i (TAG, "153 In Init_All  =============");

                        if (IMEI_Upload_Complete || Configuration_Changed) {
                            if (fb_activity_req != FB_Activity_Req.FB_Act_SignOut) {
                                FB_N_Activity_Started = true;
                                mFAuth_Main = FirebaseAuth.getInstance();
                                mFDB = FirebaseDatabase.getInstance();


                                DatabaseReference mFDB_client_id = mFDB.getReference(FBAppCustomer).child("client_id");
                                DatabaseReference mFDB_current_key = mFDB.getReference(FBAppCustomer).child("current_key");
                                DatabaseReference mFDB_firebase_url = mFDB.getReference(FBAppCustomer).child("firebase_url");
                                DatabaseReference mFDB_mobilesdk_app_id = mFDB.getReference(FBAppCustomer).child("mobilesdk_app_id");

                                FB_User_Email_Sec_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_User_Email_Sec");
                                FB_User_Pass_Sec_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_User_Pass_Sec");

                                FB_Client_ID_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_Client_ID");
                                FB_Current_Key_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_Current_Key");
                                FB_URL_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_URL");
                                FB_MobileSDK_App_ID_G = Get_Var_Val_from_App_SysConf("FireBase", FBAppCustomer + "-FB_MobileSDK_App_ID");

                                Log.i (TAG, "174 In Init_All  =============");
                                Log.i (TAG, " ======================================== FB_User_Email_Sec_G = " + FB_User_Email_Sec_G );
                                Log.i (TAG, " ======================================== FB_User_Pass_Sec_G = " + FB_User_Pass_Sec_G );
                                Log.i (TAG, " ======================================== FB_Client_ID_G = " + FB_Client_ID_G );
                                Log.i (TAG, " ======================================== FB_Current_Key_G = " + FB_Current_Key_G );
                                Log.i (TAG, " ======================================== FB_URL_G = " + FB_URL_G );
                                Log.i (TAG, " ======================================== FB_MobileSDK_App_ID_G = " + FB_MobileSDK_App_ID_G );

                                if (mFDB_client_id != null) {
                                    Log.i (TAG, "mFDB_client_id = " + mFDB_client_id);
                                    mFDB_client_id.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String data = dataSnapshot.getValue(String.class);
                                            FB_Client_ID_G = data;
                                            myDBAppUtils.InUp2_SysConf_T_AU("FireBase", FBAppCustomer + "-FB_Client_ID",data, false);
                                            Log.i (TAG, "2222 ======================================== FB_Client_ID_G = " + FB_Client_ID_G );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                if (mFDB_current_key != null) {
                                    mFDB_current_key.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String data = dataSnapshot.getValue(String.class);
                                            FB_Current_Key_G = data;
                                           // Log.i (TAG, "FB_Current_Key = " + FB_Current_Key_G);
                                            myDBAppUtils.InUp2_SysConf_T_AU("FireBase", FBAppCustomer + "-FB_Current_Key",data, false);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                if (mFDB_firebase_url != null) {
                                    mFDB_firebase_url.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String data = dataSnapshot.getValue(String.class);
                                            FB_URL_G = data;
                                            myDBAppUtils.InUp2_SysConf_T_AU("FireBase", FBAppCustomer + "-FB_URL",data, false);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                if (mFDB_mobilesdk_app_id != null) {
                                    mFDB_mobilesdk_app_id.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String data = dataSnapshot.getValue(String.class);
                                            FB_MobileSDK_App_ID_G = data;
                                            myDBAppUtils.InUp2_SysConf_T_AU("FireBase", FBAppCustomer + "-FB_MobileSDK_App_ID",data, false);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                                //Log.i (TAG, "240 In Init_All  =============");
                                while ( (FB_Client_ID_G == null) || (FB_Client_ID_G.isEmpty()) ||
                                        (FB_Current_Key_G == null) || (FB_Current_Key_G.isEmpty()) ||
                                        (FB_URL_G == null) || (FB_URL_G.isEmpty()) ||
                                        (FB_MobileSDK_App_ID_G == null) || (FB_MobileSDK_App_ID_G.isEmpty())
                                        ) {
                                    if (Configuration_Changed) break;
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                               // Log.i (TAG, "254 In Init_All  =============");

                                if ( ((FB_Client_ID_G != null) && (!FB_Client_ID_G.isEmpty())) &&
                                        ((FB_Current_Key_G != null) && (!FB_Current_Key_G.isEmpty())) &&
                                        ((FB_URL_G != null) && (!FB_URL_G.isEmpty())) &&
                                        ((FB_MobileSDK_App_ID_G != null) && (!FB_MobileSDK_App_ID_G.isEmpty())) ) {
                                    FB1_InitComplete = true;
                                    Configuration_Changed = false;

                                    start_FB_Sec_ActivityFromMainThread();
                                } else {
                                    if (Is_Network_Available()) {
                                         Show_Network_Config_Err_Msg();
                                    } else {
                                        // Main Activity is Anyway there, since moving FB from Activity to thread.
                                        // So, no need to start main activity here.
                                    }
                                }

                            } else if (fb_activity_req == FB_Activity_Req.FB_Act_SignOut) {
                                signOut();
                            }
                        }
                    }
                } else {
                    // Call Configuration;
                    //Log.i (TAG, "Calling Show_Network_Config_Err_Msg");
                    Show_Network_Config_Err_Msg();
                }
            }
        } else {
            Verify_IMEI_Code();
        }
    }

    private void signOut() {
        //Log.i (TAG, "In SignOut =================================================================");
        mFAuth_Main.signOut();

    }

    private void sendEmailVerification() {
        if (mFAuth_Main == null) return;
        FB_user = mFAuth_Main.getCurrentUser();
        if (FB_user  != null) {
            FB_user.sendEmailVerification()
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //SignInSuccess = true;??
                                //Log.i(TAG, "I am here in sendEmailVerification =============================== ");
                            } else {
                                Log.e(TAG, "sendEmailVerification", task.getException());
                            }
                        }
                    });
        }
    }

    private void Verify_IMEI_Code() {
        if (FBAppCustomer == null) return;
        if (mFDB == null) return;
       // Log.i (TAG, "\n\nIN Verify_IMEI_Code **********************************");
        if (!IMEI_Upload_Complete) {
            //mFDB_IMEI = mFDB.getReference(FBAppName).child(FBAppCustomer).child("IMEI").child(Device_IMEI_ID);
            String Val1 = FBAppMBName;
            if ((Val1 == null) || (Val1.isEmpty())) {
                Val1 = Device_IMEI_ID;
            }
            mFDB_IMEI_Main = mFDB.getReference(FBAppCustomer).child("IMEI").child(Val1);

           // Log.i (TAG, "FBAppName ="  +  FBAppName + " ,  FBAppCustomer =" + FBAppCustomer + ", Device_IMEI_ID =" + Device_IMEI_ID);
            FB_IMEI_Setup = Get_Var_Val_from_App_SysConf("FireBase", "FB_IMEI_Setup").equals("YES");
           // Log.i (TAG, "FB_IMEI_Setup ############################################= "  + FB_IMEI_Setup);

            if (!FB_IMEI_Setup || Configuration_Changed) {
               // Log.i (TAG, "Calling SetValue for  Device_IMEI_ID = "  + Device_IMEI_ID);

                mFDB_IMEI_Main.setValue(Device_IMEI_ID)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Write was successful!
                                // ...

                                myDBAppUtils.InUp2_SysConf_T_AU("FireBase", "FB_IMEI_Setup","YES", false);
                                FB_IMEI_Setup = true;
                                IMEI_Upload_Complete = true;

                              //  Log.i (TAG, "IMEI_Update_Complete = " + IMEI_Upload_Complete);

                                Periodic_Verify_IMEI_Code();
                                Init_All();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                              //  Log.i (TAG, "Failure Message = " + e.getMessage());
                                if ((e.getMessage()).contains("Permission denied")) {
                                    Permission_Denied = true;
                                    Configuration_Changed = false;
                                  //  Log.i (TAG, "Permission denied =========== ");
                                    Show_Network_Config_Err_Msg("Permission denied");
                                }
                                Loop_Interval = 1000 * 60;
                                Configuration_Changed = false;
                            }
                        });
            } else {
                Periodic_Verify_IMEI_Code();
                IMEI_Upload_Complete = true;
            }
        }
    }

    private boolean Periodic_Verify_IMEI_Code() {
        final boolean[] ret_val = {false};
        final Context F1_Context = myGContext_Global;
        if (Device_IMEI_ID == null) {
            ErrMsgDialog("Invalid Device_IMEI_ID\n Can't Progress\nReturning ...");
            //Valid_IMEI_Combination = false;
        }
        DatabaseReference   mFDB_Valid_IMEI = mFDB.getReference(FBAppCustomer).child("Valid_IMEI");

        Log.i (TAG, " $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Periodic_Verify_IMEI_Code GetVal = " + mFDB_Valid_IMEI.toString() );

        mFDB_Valid_IMEI.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String P_Key = child.getKey();
                    String data = child.getValue(String.class);
                    Log.i (TAG, "Child _PKey= " + P_Key + ", data = " + data + ", Device_IMEI_ID" + Device_IMEI_ID);
                    if (data == null) continue;

                    if (data.equals(Device_IMEI_ID)) {
                        Log.i(TAG, "Found Valid IMEI ========================================= " + Device_IMEI_ID);
                        Valid_IMEI_Combination = true;
                        if ( ((FB_Client_ID_G != null) && (!FB_Client_ID_G.isEmpty())) &&
                                ((FB_Current_Key_G != null) && (!FB_Current_Key_G.isEmpty())) &&
                                ((FB_URL_G != null) && (!FB_URL_G.isEmpty())) &&
                                ((FB_MobileSDK_App_ID_G != null) && (!FB_MobileSDK_App_ID_G.isEmpty())) && (Valid_IMEI_Combination)) {
                            FB1_InitComplete = true;
                            start_FB_Sec_ActivityFromMainThread();
                        }
                        //Init_All();
                        break;
                    }
                }
                if (!Valid_IMEI_Combination) {
                    Log.i(TAG, "Could not find Valid IMEI =========================================");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

    private static void FB_Main_SignIn (final String email, String password) {
        Log.i (TAG,"In FB_Main_SignIn Email = " + email + ", Password = " + password);
        if (Is_Network_Available()) {
            mFAuth_Main.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) myGContext_Global, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "signIn With Email:Success");
                                FB_user = mFAuth_Main.getCurrentUser();
                                SignInComplete = true;
                                if (!SignInMessageDisplayed) {
                                    SignInMessageDisplayed = true;
                                    ErrMsgDialog_L("Signed to [" + FBAppCustomer + "] \nas User [" + email + "]");
                                }
                            } else {
                                Log.i(TAG, "signInWithEmail:failure" + task.getException());
                            }

                            if (!task.isSuccessful()) {
                                Log.i(TAG, "signInWithEmail:UnSuccessful", task.getException());
                                Show_Network_Config_Err_Msg("FB_Main_SignIn");
                            }
                            Log.i(TAG, "I am here =====================================");
                        }

                    });
        }
    }



    public static void Create_FB_User () {
        FB_user = mFAuth_Main.getCurrentUser();
        //Context this_Context = myGContext_Global;
        if (FB_user != null) {
           // Log.i(TAG, "FB_User = " + FB_user.toString() + "========================================");
        } else {
            mFAuth_Main.createUserWithEmailAndPassword(FB_User_Email_Main, FB_User_Pass_Main)
                    .addOnCompleteListener((Activity) myGContext_Global, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FB_user = mFAuth_Main.getCurrentUser();
                                //Toast.makeText(this, "Success ================================", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(this, "Failed ================================", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public static void start_FB_Sec_ActivityFromMainThread(){
        FB_Sec_Worker FB_Sec_Worker_ = new FB_Sec_Worker(FBAppCustomer, FB_Client_ID_G, FB_Current_Key_G, FB_URL_G,
                FB_MobileSDK_App_ID_G, FB_User_Email_Sec_G, FB_User_Pass_Sec_G);
        FB_Sec_Worker_List.add(FB_Sec_Worker_);
        FB_Sec_Worker_.start();
    }


}
