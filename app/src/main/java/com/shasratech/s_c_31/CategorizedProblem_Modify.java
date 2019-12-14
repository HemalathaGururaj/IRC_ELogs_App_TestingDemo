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

public class CategorizedProblem_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Problem = null;
    //private static MySpinner CB_TripNo = null;
    private final String TAG = "CategorizedProblem_Modify";
    EditText ProblemId_ET = null;

    EditText TripNo_ET = null;
    EditText DriverName_ET = null;
    EditText ProblemCategory_ET = null;
    EditText ProblemDecription_ET = null;



    boolean Add_New = false;
    String New_ProblemId_Editor_Text = "";
    String Curr_ProblemId = "";
    String TripNo = "";
    //String Curr_TripNo = "";
    String DriverName = "";
    String ProblemCategory = "";
    String ProblemDecription = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorizedproblem_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.CategorizedProblem_Add_New);
        ProblemId_ET = findViewById(R.id.CategorizedProblem_PI) ;
        TripNo_ET = findViewById(R.id.CategorizedProblem_Modify_TripNo);
        DriverName_ET = findViewById(R.id.CategorizedProblem_Modify_DriverName);
        ProblemCategory_ET= findViewById(R.id.CategorizedProblem_Modify_ProblemCategory);
        ProblemDecription_ET = findViewById(R.id.CategorizedProblem_Modify_ProblemDecription);

        //CB_TripNo = findViewById(R.id.Person_Modify_SellerComp);
        //CB_TripNo.UpdateCB(true, "CategorizedProblem", "Trip_No", "", "DISTINCT");


        ProblemId_ET.setVisibility(View.INVISIBLE);

        CB_Problem = findViewById(R.id.CategorizedProblem_Modify_PI);
        CB_Problem.setOnItemSelectedListener(this);


        CB_Problem.UpdateCB(true, "CategorizedProblem", "Problem_Id", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    ProblemId_ET.setVisibility(View.VISIBLE);
                } else {
                    ProblemId_ET.setVisibility(View.INVISIBLE);
                }
            }

        });


        CB_Problem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_ProblemId = parent.getItemAtPosition(position).toString();
                CurrentSelect_categorizedproblem_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_categorizedproblem_Changed();

    }

    private void CurrentSelect_categorizedproblem_Changed () {
        if ((Curr_ProblemId != null) && (!Curr_ProblemId.isEmpty())) {
            String Sel = String.format("Problem_Id= '%s'", Curr_ProblemId);
            TripNo = myDB_G.Get_Val_from_DB_UD("CategorizedProblem", "Trip_No", "", Sel);
            TripNo_ET.setText(TripNo);
            DriverName = myDB_G.Get_Val_from_DB_UD("CategorizedProblem", "Driver_Name", "", Sel);
            DriverName_ET.setText(DriverName);
            ProblemCategory = myDB_G.Get_Val_from_DB_UD("CategorizedProblem", "Problem_Category", "", Sel);
            ProblemCategory_ET.setText(ProblemCategory);
            ProblemDecription = myDB_G.Get_Val_from_DB_UD("CategorizedProblem", "Problem_Decription", "", Sel);
            ProblemDecription_ET.setText(ProblemDecription);
            //Curr_TripNo = myDB_G.Get_Val_from_DB_UD("CategorizedProblem", "Trip_No", "", Sel);
            // if ((Curr_TripNo != null) && (!Curr_TripNo.isEmpty())) {
            //CB_TripNo.SetSelectedItem(Curr_TripNo);

        }
    }

    public void on_CategorizedProblem_Home_PB_Click (View view){
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }


    public void OnCategorizedProblem_Modify_Save_Clicked (View view){

        if (Add_New) {
            Curr_ProblemId = ProblemId_ET.getText().toString();
        }
        if ((Curr_ProblemId == null) || (Curr_ProblemId.isEmpty())) {
            ErrMsgDialog("Material is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = String.format("Problem_Id = '%s'", Curr_ProblemId);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("CategorizedProblem", "Create_TS", "", Sel);

        Log.i(TAG, "Sel = " + Sel + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;

        String TripNo = TripNo_ET.getText().toString();

        String DriverName = DriverName_ET.getText().toString();
        //Curr_TripNo = (CB_TripNo.getSelectedItem() != null) ? CB_TripNo.getSelectedItem().toString() : "";
        String ProblemCategory = ProblemCategory_ET.getText().toString();

        String ProblemDecription = ProblemDecription_ET.getText().toString();


        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_ProblemId + "," + TripNo + ","
                + "," + DriverName + "," + ProblemCategory + "," + ProblemDecription;

        //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_CategorizedProblem_Ordered(true, Create_TS, Modify_TS, User, Curr_ProblemId, TripNo, DriverName, ProblemCategory, ProblemDecription)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_ProblemId, Curr_Activity.Act_CategorizedProblem, UpdateStatement);
            UPload_Data_Msg_List.add(msg);
        } else {
            ErrMsgDialog("Unable to Insert/Update Problem into Local DB");
        }
    }

    @Override
    public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){

    }

    @Override
    public void onNothingSelected (AdapterView < ? > parent){

    }

    @Override
    protected void onResume () {
        super.onResume();
        CurrentSelect_categorizedproblem_Changed();
    }
}

