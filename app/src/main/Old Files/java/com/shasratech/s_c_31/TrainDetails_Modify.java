package com.shasratech.s_c_31;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import static com.shasratech.s_c_31.Globals.ErrMsgDialog;
import static com.shasratech.s_c_31.Globals.FBAppCustomer;
import static com.shasratech.s_c_31.Globals.FBAppMBName;
import static com.shasratech.s_c_31.Globals.Get_Current_Date_Time;
import static com.shasratech.s_c_31.Globals.UPload_Data_Msg_List;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class TrainDetails_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Train_Num = null;
    private final String TAG = "TrainDetails_Modify";
    EditText Train_NumET = null;

    EditText Train_Name_ET = null;
    Switch Active_CBx = null;



    boolean Add_New = false;
    String New_TrainNum_Editor_Text = "";
    String Curr_Train_Num = "";
    String Train_Name = "";
    boolean Active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traindetails_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.TrainNum_Add_New);
        Train_NumET = findViewById(R.id.Train_Num_TE) ;
        Train_Name_ET = findViewById(R.id.TrainDetails_Modify_Tain_Name);
        Active_CBx = findViewById(R.id.HomeShed_Modify_Active);


        Train_NumET.setVisibility(View.INVISIBLE);

        CB_Train_Num = findViewById(R.id.TrainDetails_Modify_Train_Num);
        CB_Train_Num.setOnItemSelectedListener(this);



        CB_Train_Num.UpdateCB(true, "TrainDetails", "Train_Num", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    Train_NumET.setVisibility(View.VISIBLE);
                } else {
                    Train_NumET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Train_Num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Train_Num = parent.getItemAtPosition(position).toString();
                CurrentSelect_TrainDetails_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_TrainDetails_Changed();

    }

    private void CurrentSelect_TrainDetails_Changed () {
        if ((Curr_Train_Num != null) && (!Curr_Train_Num.isEmpty())) {
            String Sel = String.format("Train_Num = '%s'", Curr_Train_Num);
            Train_Name = myDB_G.Get_Val_from_DB_UD("TrainDetails", "Train_Name", "", Sel); Train_Name_ET.setText(Train_Name);
            Active= (myDB_G.Get_Val_from_DB_UD("TrainDetails", "Active", "", Sel)).equals("YES"); Active_CBx.setChecked(Active);

        }
    }

    public void on_TrainDetails_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnTrainDetails_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Train_Num = Train_NumET.getText().toString();
        }
        if ((Curr_Train_Num == null) || (Curr_Train_Num.isEmpty())) {
            ErrMsgDialog("Train numis Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("Train_Num = '%s'", Curr_Train_Num);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("TrainDetails", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String Train_Name = Train_Name_ET.getText().toString();


        String Active = Active_CBx.isChecked()?"YES":"NO";

        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Train_Num + "," + Train_Name + "," + "," +Active;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_Train_Detailss_Ordered(true, Create_TS, Modify_TS, User, Curr_Train_Num, Train_Name, Active)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Train_Num, Curr_Activity.Act_TrainDetails, UpdateStatement);
            UPload_Data_Msg_List.add(msg);
        } else {
            ErrMsgDialog("Unable to Insert/Update Material into Local DB");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume () {
        super.onResume();
        CurrentSelect_TrainDetails_Changed();
    }
}
