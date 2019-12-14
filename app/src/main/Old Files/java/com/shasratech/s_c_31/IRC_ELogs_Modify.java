package com.shasratech.s_c_31;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import static com.shasratech.s_c_31.Globals.ErrMsgDialog;
import static com.shasratech.s_c_31.Globals.FBAppCustomer;
import static com.shasratech.s_c_31.Globals.FBAppMBName;
import static com.shasratech.s_c_31.Globals.Get_Current_Date_Time;
import static com.shasratech.s_c_31.Globals.UPload_Data_Msg_List;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class IRC_ELogs_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    private final String TAG = "IRC_ELogs_Modify";



    EditText CrewDetailsET = null;
    EditText Asst_DriverNameET = null;
    EditText DriverNameET = null;
    EditText Asst_DriverDesigET = null;
    EditText DriverDesigET = null;
    EditText Ticket_NumET = null;
    EditText Ticket_Num2ET = null;
    EditText HomeShed1ET = null;
    EditText HomeShed2ET = null;
    EditText TripDetailsET = null;
    EditText Start_PET = null;
    EditText End_PET = null;
    EditText TrainNumET = null;
    EditText KMET = null;
    EditText LoadET = null;
    EditText ArrivalET = null;
    EditText DepartureET = null;
    EditText LocoDetailsET = null;
    EditText Last_Sch_AttdndET = null;
    EditText LocoNumET = null;
    EditText Attended_AtET = null;
    EditText Next_Sch_OnET = null;
    EditText LevelsET = null;
    EditText Lube_ConsmpET = null;
    EditText Fuel_InitET = null;
    EditText Fuel_FinalET = null;
    EditText Fuel_ConsmpET = null;
    EditText Lube_InitET = null;
    EditText Lube_FinalET = null;
    EditText CompressOilET = null;
    EditText GovernorOilET = null;
    EditText Cool_WtrET = null;
    EditText Sig_Drv1ET = null;
    EditText Sig_Drv2ET = null;
    EditText DrvName1ET = null;
    EditText DrvName2ET = null;
    EditText Ticket_P2_1ET = null;
    EditText Shed_P2_1ET = null;
    EditText Ticket_P2_2ET = null;
    EditText Shed_P2_2ET = null;



    String Curr_Ticket_Num = "";
    String CrewDetails =  "";
    String Asst_DriverName =  "";
    String DriverName =  "";
    String Asst_DriverDesig =  "";
    String DriverDesig =  "";
    String Ticket_Num =  "";
    String Ticket_Num2 =  "";
    String HomeShed1 =  "";
    String HomeShed2 =  "";
    String TripDetails =  "";
    String Asst_Start_P =  "";
    String Asst_End_P =  "";
    String TrainNum =  "";
    String Asst_KM =  "";
    String Load =  "";
    String Arrival =  "";
    String Departure =  "";
    String LocoDetails =  "";
    String Last_Sch_Attdnd =  "";
    String LocoNum =  "";
    String Attended_At =  "";
    String Next_Sch_On =  "";
    String Levels =  "";
    String Lube_Consmp =  "";
    String Fuel_Init =  "";
    String Fuel_Final =  "";
    String Fuel_Consmp =  "";
    String Lube_Init =  "";
    String Lube_Final =  "";
    String CompressOil =  "";
    String GovernorOil =  "";
    String Cool_Wtr =  "";
    String Sig_Drv1 =  "";
    String Sig_Drv2 =  "";
    String DrvName1 =  "";
    String DrvName2 =  "";
    String Ticket_P2_1 =  "";
    String Shed_P2_1 =  "";
    String Ticket_P2_2 = "";
    String Shed_P2_2 = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.irc_elogs_modify_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



         CrewDetailsET =        findViewById(R.id.IRC_ELogs_Modify_CrewDetails) ;
          Asst_DriverNameET =   findViewById(R.id.IRC_ELogs_Modify_Asst_DriverName) ;
         DriverNameET =        findViewById(R.id.IRC_ELogs_Modify_DriverName) ;
         Asst_DriverDesigET =   findViewById(R.id.IRC_ELogs_Modify_Asst_DriverDesig) ;
         DriverDesigET =        findViewById(R.id.IRC_ELogs_Modify_DriverDesig) ;
         Ticket_NumET =   findViewById(R.id.IRC_ELogs_Modify_Ticket_Num) ;
         Ticket_Num2ET =        findViewById(R.id.IRC_ELogs_Modify_Ticket_Num2) ;
         HomeShed1ET =        findViewById(R.id.IRC_ELogs_Modify_HomeShed1) ;
         HomeShed2ET =        findViewById(R.id.IRC_ELogs_Modify_HomeShed2) ;
         TripDetailsET =        findViewById(R.id.IRC_ELogs_Modify_TripDetails) ;
         Start_PET =      findViewById(R.id.IRC_ELogs_Modify_Start_P) ;
         End_PET =        findViewById(R.id.IRC_ELogs_Modify_End_P) ;
         TrainNumET =        findViewById(R.id.IRC_ELogs_Modify_TrainNum) ;
         KMET =        findViewById(R.id.IRC_ELogs_Modify_KM) ;
         LoadET =           findViewById(R.id.IRC_ELogs_Modify_Load) ;
         ArrivalET =        findViewById(R.id.IRC_ELogs_Modify_Arrival) ;
         DepartureET =        findViewById(R.id.IRC_ELogs_Modify_Departure) ;
         LocoDetailsET =        findViewById(R.id.IRC_ELogs_Modify_LocoDetails) ;
         Last_Sch_AttdndET =  findViewById(R.id.IRC_ELogs_Modify_Last_Sch_Attended) ;
         LocoNumET =        findViewById(R.id.IRC_ELogs_Modify_LocoNum) ;
         Attended_AtET =        findViewById(R.id.IRC_ELogs_Modify_Attended_At) ;
         Next_Sch_OnET =        findViewById(R.id.IRC_ELogs_Modify_Next_Sch_On) ;
         LevelsET =        findViewById(R.id.IRC_ELogs_Modify_Levels) ;
         Lube_ConsmpET =        findViewById(R.id.IRC_ELogs_Modify_Lube_Consmp) ;
         Fuel_InitET =        findViewById(R.id.IRC_ELogs_Modify_Fuel_Init) ;
         Fuel_FinalET =        findViewById(R.id.IRC_ELogs_Modify_Fuel_Final) ;
         Fuel_ConsmpET =        findViewById(R.id.IRC_ELogs_Modify_Fuel_Consmp) ;
         Lube_InitET =        findViewById(R.id.IRC_ELogs_Modify_Lube_Init) ;
         Lube_FinalET =        findViewById(R.id.IRC_ELogs_Modify_Lube_Final) ;
         CompressOilET =        findViewById(R.id.IRC_ELogs_Modify_CompressOil) ;
         GovernorOilET =        findViewById(R.id.IRC_ELogs_Modify_GovernorOil) ;
         Cool_WtrET =        findViewById(R.id.IRC_ELogs_Modify_Cool_Wtr) ;
         Sig_Drv1ET =        findViewById(R.id.IRC_ELogs_Modify_Sig_Drv1) ;
         Sig_Drv2ET =        findViewById(R.id.IRC_ELogs_Modify_Sig_Drv2) ;
         DrvName1ET =        findViewById(R.id.IRC_ELogs_Modify_DrvName1) ;
         DrvName2ET =        findViewById(R.id.IRC_ELogs_Modify_DrvName2) ;
         Ticket_P2_1ET =        findViewById(R.id.IRC_ELogs_Modify_Ticket_P2_1) ;
         Shed_P2_1ET =        findViewById(R.id.IRC_ELogs_Modify_Shed_P2_1) ;
         Ticket_P2_2ET =        findViewById(R.id.IRC_ELogs_Modify_Ticket_P2_2) ;
         Shed_P2_2ET =        findViewById(R.id.IRC_ELogs_Modify_Shed_P2_2) ;






    }

    private void CurrentSelect_IRC_ELogs_Changed () {
        if ((Curr_Ticket_Num != null) && (!Curr_Ticket_Num.isEmpty())) {
            String Sel = String.format("Ticket_Num = '%s'", Curr_Ticket_Num);


            Curr_Ticket_Num = myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Ticket_Num", "", Sel); Ticket_NumET.setText(Ticket_Num);
            CrewDetails =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "CrewDetails", "", Sel);CrewDetailsET.setText(CrewDetails);
            Asst_DriverName =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Asst_DriverName", "", Sel); Asst_DriverNameET.setText(Asst_DriverName);
            DriverName =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "DriverName", "", Sel); DriverNameET.setText(DriverName);
            Asst_DriverDesig =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Asst_DriverDesig", "", Sel); Asst_DriverDesigET.setText(Asst_DriverDesig);
            DriverDesig =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "DriverDesig", "", Sel); DriverDesigET.setText(DriverDesig);
            Ticket_Num =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Ticket_Num", "", Sel); Ticket_NumET.setText(Ticket_Num);
            Ticket_Num2 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Ticket_Num2", "", Sel); Ticket_Num2ET.setText(Ticket_Num2);
            HomeShed1 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "HomeShed1", "", Sel); HomeShed1ET.setText(HomeShed1);
            HomeShed2 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "HomeShed2", "", Sel); HomeShed2ET.setText(HomeShed2);
            TripDetails =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "TripDetails", "", Sel); TripDetailsET.setText(TripDetails);
            Asst_Start_P =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Asst_Start_P", "", Sel); Start_PET.setText(Asst_Start_P);
            Asst_End_P =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Asst_End_P", "", Sel); End_PET.setText(Asst_End_P);
            TrainNum =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "TrainNum", "", Sel); TrainNumET.setText(TrainNum);
            Asst_KM =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Asst_KM", "", Sel); KMET.setText(Asst_KM);
            Load =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Load", "", Sel); LoadET.setText(Load);
            Arrival =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Arrival", "", Sel); ArrivalET.setText(Arrival);
            Departure =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Departure", "", Sel); DepartureET.setText(Departure);
            LocoDetails =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "LocoDetails", "", Sel); LocoDetailsET.setText(LocoDetails);
            Last_Sch_Attdnd =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Last_Sch_Attdnd", "", Sel); Last_Sch_AttdndET.setText(Last_Sch_Attdnd);
            LocoNum =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "LocoNum", "", Sel); LocoNumET.setText(LocoNum);
            Attended_At =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Attended_At", "", Sel); Attended_AtET.setText(Attended_At);
            Next_Sch_On =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Next_Sch_On", "", Sel); Next_Sch_OnET.setText(Next_Sch_On);
            Levels =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Levels", "", Sel); LevelsET.setText(Levels);
            Lube_Consmp =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Lube_Consmp", "", Sel); Lube_ConsmpET.setText(Lube_Consmp);
            Fuel_Init =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Fuel_Init", "", Sel); Fuel_InitET.setText(Fuel_Init);
            Fuel_Final =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Fuel_Final", "", Sel); Fuel_FinalET.setText(Fuel_Final);
            Fuel_Consmp =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Fuel_Consmp", "", Sel); Fuel_ConsmpET.setText(Fuel_Consmp);
            Lube_Init =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Lube_Init", "", Sel); Lube_InitET.setText(Lube_Init);
            Lube_Final =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Lube_Final", "", Sel); Lube_FinalET.setText(Lube_Final);
            CompressOil =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "CompressOil", "", Sel); CompressOilET.setText(CompressOil);
            GovernorOil =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "GovernorOil", "", Sel); GovernorOilET.setText(GovernorOil);
            Cool_Wtr =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Cool_Wtr", "", Sel); Cool_WtrET.setText(Cool_Wtr);
            Sig_Drv1 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Sig_Drv1", "", Sel); Sig_Drv1ET.setText(Sig_Drv1);
            Sig_Drv2 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Sig_Drv2", "", Sel); Sig_Drv2ET.setText(Sig_Drv2);
            DrvName1 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "DrvName1", "", Sel); DrvName1ET.setText(DrvName1);
            DrvName2 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "DrvName2", "", Sel); DrvName2ET.setText(DrvName2);
            Ticket_P2_1 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Ticket_P2_1", "", Sel); Ticket_P2_1ET.setText(Ticket_P2_1);
            Shed_P2_1 =  myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Shed_P2_1", "", Sel); Shed_P2_1ET.setText(Shed_P2_1);
            Ticket_P2_2 = myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Ticket_P2_2", "", Sel); Ticket_P2_2ET.setText(Ticket_P2_2);
            Shed_P2_2 = myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Shed_P2_2", "", Sel); Shed_P2_2ET.setText(Shed_P2_2);


        }
    }

    public void on_IRC_ELogs_Home_PB_Click (View view) {
        Intent inent = new Intent(this, MainActivity.class);
        startActivity(inent);
    }

    public void OnIRC_ELogs_Modify_Add_Problem_Clicked (View view) {
        Log.i (TAG, "I am here in OnIRC_ELogs_Modify_Save_Clicked1");
        ErrMsgDialog("This feature is under Development");
    }

    public void OnIRC_ELogs_Modify_Add_Solutions_Clicked (View view) {
        Log.i (TAG, "I am here in OnIRC_ELogs_Modify_Save_Clicked1");
        ErrMsgDialog("This feature is under Development");
    }

    public void OnIRC_ELogs_Modify_Add_Monitor_Clicked (View view) {
        Log.i (TAG, "I am here in OnIRC_ELogs_Modify_Save_Clicked1");
        ErrMsgDialog("This feature is under Development");
    }

    public void OnIRC_ELogs_Modify_Save_Clicked (View view) {
        String Sel = String.format("Ticket_Num = '%s'", Curr_Ticket_Num);

        String Create_TS = myDB_G.Get_Val_from_DB_UD("IRC_ELogs", "Create_TS", "", Sel);

        Log.i (TAG, "Sel = " + Sel  + ", Create_TS " + Create_TS);

        String Modify_TS = Get_Current_Date_Time();

        if ((Create_TS == null) || (Create_TS.isEmpty())) {
            Create_TS = Modify_TS;
        }
        String User = FBAppMBName;





      //  String Active = Active_CBx.isChecked()?"YES":"NO";


        String Curr_Ticket_Num =Ticket_NumET.getText().toString();
        String CrewDetails = CrewDetailsET.getText().toString();
        String Asst_DriverName = Asst_DriverNameET.getText().toString();
        String DriverName = DriverNameET.getText().toString();
        String Asst_DriverDesig = Asst_DriverDesigET.getText().toString();
        String DriverDesig = DriverDesigET.getText().toString();

        String Ticket_Num2 = Ticket_Num2ET.getText().toString();
        String HomeShed1 = HomeShed1ET.getText().toString();
        String HomeShed2 = HomeShed2ET.getText().toString();
        String TripDetails = TripDetailsET.getText().toString();
        String Start_P = Start_PET.getText().toString();
        String End_P = End_PET.getText().toString();
        String TrainNum = TrainNumET.getText().toString();
        String KM = KMET.getText().toString();
        String Load = LoadET.getText().toString();
        String Arrival = ArrivalET.getText().toString();
        String Departure = DepartureET.getText().toString();
        String locoDetails = LocoDetailsET.getText().toString();
        String Last_Sch_Attdnd = Last_Sch_AttdndET.getText().toString();
        String locoNum = LocoNumET.getText().toString();
        String Attended_At = Attended_AtET.getText().toString();
        String Next_Sch_On = Next_Sch_OnET.getText().toString();
        String Levels = LevelsET.getText().toString();
        String Lube_Consmp = Lube_ConsmpET.getText().toString();
        String Fuel_Init = Fuel_InitET.getText().toString();
        String Fuel_Final = Fuel_FinalET.getText().toString();
        String Fuel_Consmp = Fuel_ConsmpET.getText().toString();
        String Lube_Init = Lube_InitET.getText().toString();
        String Lube_Final = Lube_FinalET.getText().toString();
        String CompressorOil = CompressOilET.getText().toString();
        String GovernorOil = GovernorOilET.getText().toString();
        String Cool_Wtr = Cool_WtrET.getText().toString();
        String Sig_Drv1 = Sig_Drv1ET.getText().toString();
        String Sig_Drv2 = Sig_Drv2ET.getText().toString();
        String DrvName1 = DrvName1ET.getText().toString();
        String DrvName2 = DrvName2ET.getText().toString();
        String Ticket_P2_1 = Ticket_P2_1ET.getText().toString();
        String Shed_P2_1 = Shed_P2_1ET.getText().toString();
        String Ticket_P2_2 =Ticket_P2_2ET.getText().toString();
        String Shed_P2_2 =Shed_P2_2ET.getText().toString();

        CheckBox StartFuse_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_StartFuse);
        String StartFuse = StartFuse_CBx.isChecked()?"YES":"NO";
        CheckBox FireExting_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_FireExting);
        String FireExting = FireExting_CBx.isChecked()?"YES":"NO";
        CheckBox FuelFill_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_FuelFill);
        String FuelFill = FuelFill_CBx.isChecked()?"YES":"NO";
        CheckBox AlertWork_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_AlertWork);
        String AlertWork = AlertWork_CBx.isChecked()?"YES":"NO";
        CheckBox MUCode_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_MUCode);
        String MUCode =   MUCode_CBx.isChecked()?"YES":"NO";
        CheckBox SPeedoM_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_SPeedoM);
        String SPeedoM =   SPeedoM_CBx.isChecked()?"YES":"NO";
        CheckBox HI_Flash_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_HI_Flash);
        String HI_Flash =   HI_Flash_CBx.isChecked()?"YES":"NO";
        CheckBox LVC_Locks_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_LVC_Locks);
        String LVC_Locks =   LVC_Locks_CBx.isChecked()?"YES":"NO";
        CheckBox GR_Seal_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_GR_Seal);
        String GR_Seal =   LVC_Locks_CBx.isChecked()?"YES":"NO";
        CheckBox DB_Seal_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_DB_Seal);
        String DB_Seal =  DB_Seal_CBx.isChecked()?"YES":"NO";
        CheckBox Wiper_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_Wiper);
        String Wiper =   Wiper_CBx.isChecked()?"YES":"NO";
        CheckBox Sanders_CBx = (CheckBox) findViewById(R.id.IRC_ELogs_Modify_Sanders);
        String Sanders =   Sanders_CBx.isChecked()?"YES":"NO";




        String UpdateStatement = Create_TS + "," + Modify_TS + "," + User + "," + Curr_Ticket_Num + "," + CrewDetails + "," + Asst_DriverName +  "," + DriverName + ","
                +Asst_DriverDesig +  "," + DriverDesig +  ","+  Ticket_Num2 +  ","
                + HomeShed1 +  ","  + HomeShed2 +  ","  + TripDetails +  ","  +  Asst_Start_P +  ","
                +Asst_End_P +  "," +  TrainNum +  "," + Asst_KM +  "," + Load +  ","+ Arrival +  ","
                + Departure +  ","+  LocoDetails +  ","+ Last_Sch_Attdnd +  "," + LocoNum +  ","
                +  Attended_At +  ","  +  Next_Sch_On +  "," +  Levels +  ","+  Lube_Consmp +  ","
                +   Fuel_Init +  ","+  Fuel_Final +  "," + Fuel_Consmp +  ","
                +  Lube_Init +  "," + Lube_Final +  "," +   CompressOil +  ","
                +  GovernorOil +  "," +  Cool_Wtr +  "," + Sig_Drv1 + "," + Sig_Drv2 +  ","
                + DrvName1 +  ","  + DrvName2 +  ","

                 + Ticket_P2_1 + "," + Shed_P2_1 + ","  +Ticket_P2_2 + ","    + "," + Shed_P2_2;





        Log.i (TAG, "UpdateStatement = " + UpdateStatement);

        if (myDB_G.Insert_update_into_IRC_ELogs_Ordered(true,  Create_TS,  Modify_TS, User,Ticket_Num, Ticket_Num2, HomeShed1,HomeShed2,
                CrewDetails,DriverName, DriverDesig, Asst_DriverName, Asst_DriverDesig,Load, KM,TripDetails,Start_P, End_P, TrainNum, Attended_At, Arrival,Departure,locoDetails,locoNum,Last_Sch_Attdnd,
                Next_Sch_On, Levels,Fuel_Consmp,Lube_Init, Fuel_Init,Fuel_Final, Lube_Final, Lube_Consmp,CompressorOil, GovernorOil,Cool_Wtr, StartFuse, FireExting, FuelFill, AlertWork, MUCode, SPeedoM,  HI_Flash, LVC_Locks,
                GR_Seal,DB_Seal,Wiper, Sanders, Sig_Drv1, DrvName1, Sig_Drv2,DrvName2, Ticket_P2_1, Shed_P2_1, Ticket_P2_2, Shed_P2_2)) {
            ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

            UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Curr_Ticket_Num, Curr_Activity.Act_IRC_ELogs, UpdateStatement);
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
        CurrentSelect_IRC_ELogs_Changed();
    }
}
