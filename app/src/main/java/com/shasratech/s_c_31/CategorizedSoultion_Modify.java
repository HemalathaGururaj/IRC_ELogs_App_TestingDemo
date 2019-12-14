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

public class CategorizedSoultion_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static MySpinner CB_Problem_Id = null;
    private static MySpinner CB_Trip_No = null;
    private static MySpinner CB_Driver_Name = null;
    private static MySpinner CB_Solution_Category = null;
    private final String TAG = "CategorizedSoultion";
    EditText Solution_Id_ET = null;
    EditText Problem_Id_ET = null;
    EditText Solution_Category_ET = null;
    EditText Solution_Decriptipn_ET = null;

    Switch newAddition = null;
    Switch newAddition1 = null;
    //Switch Disabled_CBx = null;


    boolean Add_New = false;
    boolean Add_New1 = false;
    String New_Problem_Id_Editor_Text = "";
    String New_Solution_Category_Editor_Text = "";
    String Solution_Id = "";
    String Curr_Problem_Id = "";
    String Curr_Trip_No = "";
    String Curr_Driver_Name = "";
    String Curr_Solution_Category = "";
    String  Solution_Decription = "";


    //boolean Disabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorizedsolution_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch newAddition = findViewById(R.id.CategorizedSolution_Add_New);

        Solution_Id_ET = findViewById(R.id.CategorizedSolution_SI);
        Problem_Id_ET = findViewById(R.id.CategorizedSolution_PI);
        Solution_Category_ET = findViewById(R.id.CategorizedSolution_Modify_SolutionCategory);
        Solution_Decriptipn_ET = findViewById(R.id.CategorizedSolution_Modify_SolutionDecription);


       // Disabled_CBx = findViewById(R.id.Person_Modify_Disabled);

        Problem_Id_ET.setVisibility(View.INVISIBLE);

        CB_Problem_Id = findViewById(R.id.CategorizedSolution_Modify_PI);
        CB_Problem_Id .setOnItemSelectedListener(this);
        //String[] temp_type = {"CUSTOMER", "DRIVER", "EMPLOYEE", "OWNER", "SERVICE_CONTR", "STORAGE", "SUPPLIER", "TRANSPORTOR"};
        //CB_Type.SetItems(temp_type);

        CB_Trip_No = findViewById(R.id.CategorizedSolution_Modify_TripNo);
        CB_Trip_No.setOnItemSelectedListener(this);
        CB_Trip_No.UpdateCB(true, "CategorizedProblem", "Trip_No", "", "DISTINCT");

        CB_Driver_Name = findViewById(R.id.CategorizedSolution_Modify_Driver_Name);
        CB_Driver_Name.UpdateCB(true, "DriverDetails", "FullName", "", "DISTINCT");

        CB_Solution_Category = findViewById(R.id.CategorizedSolution_Modify_Solution_Category);
        CB_Solution_Category.UpdateCB(true, "CategorizedSolution", "Solution_Category", "", "DISTINCT");

        newAddition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New = isChecked;
                if (isChecked) {
                    Problem_Id_ET.setVisibility(View.VISIBLE);
                } else {
                    Problem_Id_ET.setVisibility(View.INVISIBLE);
                }
            }

        });

       /* newAddition1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                Add_New1 = isChecked;
                if (isChecked) {
                    Solution_Category_ET.setVisibility(View.VISIBLE);
                } else {
                    Solution_Category_ET.setVisibility(View.INVISIBLE);
                }
            }

        });*/


        CB_Problem_Id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curr_Problem_Id = parent.getItemAtPosition(position).toString();
                CurrentSelect_Problem_Id_Changed ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        CurrentSelect_Problem_Id_Changed();

    }

    private void CurrentSelect_Problem_Id_Changed () {
        if ((Curr_Problem_Id != null) && (!Curr_Problem_Id.isEmpty())) {
            String Sel = String.format("Problem_Id= '%s'", Curr_Problem_Id);
            Curr_Solution_Category = myDB_G.Get_Val_from_DB_UD("CategorizedSolution", "Solution_Category", "", Sel);
            if ((Curr_Solution_Category != null) && (!Curr_Solution_Category.isEmpty())) {
                CB_Solution_Category.SetSelectedItem(Curr_Solution_Category);

            }
          //  Curr_Driver_Name = myDB_G.Get_Val_from_DB_UD("CategorizedSolution", "Driver_Name", "", Sel);Driver_Name_ET .setText(Curr_Driver_Name);
            if ((Curr_Driver_Name != null) && (!Curr_Driver_Name.isEmpty())) {
                CB_Driver_Name .SetSelectedItem(Curr_Driver_Name);

            }
           // Curr_Trip_No = myDB_G.Get_Val_from_DB_UD("CategorizedSolution", "Trip_No", "", Sel); Trip_No_ET.setText(Curr_Trip_No);
            if ((Curr_Trip_No != null) && (!Curr_Trip_No.isEmpty())) {
                CB_Trip_No .SetSelectedItem(Curr_Trip_No);

            }
            Solution_Decription = myDB_G.Get_Val_from_DB_UD("CategorizedSolution", "Solution_Decription", "", Sel); Solution_Decriptipn_ET.setText(Solution_Decription);
            Solution_Id = (myDB_G.Get_Val_from_DB_UD("CategorizedSolution", "Solution_Id", "", Sel)); Solution_Id_ET.setText(Solution_Id);
            //Curr_SellerComp = myDB_G.Get_Val_from_DB_UD("Person", "SellerComp", "", Sel);

        }
    }

    public void on_CatgorizedSolution_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnCategorizedSolution_Modify_Save_Clicked (View view) {

        if (Add_New) {
            Curr_Problem_Id= Problem_Id_ET.getText().toString();
        }
        if (((Curr_Problem_Id == null) || (Curr_Problem_Id.isEmpty())) && (Solution_Id == null)) {
            ErrMsgDialog("Solution and Problem Id is Null/Empty\n\nReturning ...");
            return;
        }

        String Sel = "Problem_Id = '" + Curr_Problem_Id  + "'";

        String Create_TS = myDB_G.Get_Val_from_DB_UD("CategorizedSolution", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }


        Solution_Id = Solution_Id_ET.getText().toString();
       // Curr_Trip_No = Trip_No_ET.getText().toString();
      //  Curr_Driver_Name = Driver_Name_ET.getText().toString();
        Curr_Solution_Category = Solution_Category_ET.getText().toString();
        Solution_Decription = Solution_Decriptipn_ET.getText().toString();
        Curr_Trip_No = (CB_Trip_No.getSelectedItem() != null)?CB_Trip_No.getSelectedItem().toString():"";
        Curr_Driver_Name = (CB_Driver_Name.getSelectedItem() != null)?CB_Driver_Name.getSelectedItem().toString():"";
        Curr_Solution_Category = (CB_Solution_Category.getSelectedItem() != null)?CB_Solution_Category.getSelectedItem().toString():"";



        String UpdateStatement = Create_TS + "," + Modify_TS + "," + FBAppMBName + ","  + Curr_Trip_No   + "," + Curr_Driver_Name + "," + Curr_Solution_Category
                + "," + Curr_Problem_Id + "," + Solution_Id + "," +Solution_Decription + ", NA";

        Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        // Need to add to Local DB
        //String[] Con_List = {Create_TS, Modify_TS, User, Curr_Person, Rate, Units, Disabled, OtherDetails, CFT_Rate};
        if (myDB_G.Insert_update_into_CategorizedSolution_Ordered(true, Create_TS, Modify_TS, FBAppMBName, Solution_Id, Curr_Problem_Id, Curr_Trip_No, Curr_Driver_Name, Curr_Solution_Category, Solution_Decription)) {
            // Upload to Could
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");
            //FB_N_Activity_2.Person_InUp2_FB(Curr_Type + "-" +  Curr_Person, UpdateStatement);
            //UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Problem_Id + "-", Curr_Activity.Act_CategorizedSolution, UpdateStatement);
            //UPload_Data_Msg_List.add(msg);

            //Upload_Data_InUp2_FB(Curr_Type + "-" +  Curr_Person, Curr_Activity.Act_Person, UpdateStatement );
        } else {
            ErrMsgDialog("Unable to Insert/Update Person into Local DB");
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
        CurrentSelect_Problem_Id_Changed();
    }
}
