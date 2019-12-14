package com.shasratech.s_c_31;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.ErrorManager;
import static com.shasratech.s_c_31.Globals.*;

public class CashBank_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Calendar myCalendar;
    EditText Trans_Date;
    EditText Trans_Time;
    Switch Sw_Cash = null;
    Switch Sw_Core = null;

    MySpinner fromAccount = null;
    MySpinner toAccount = null;
    MySpinner expenseType = null;
    EditText ET_OtherDetails = null;

    EditText ET_Amount = null;
    final String TAG = "CashBank_Modify";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_bank_modify);

        Sw_Cash =  findViewById(R.id.Sw_Cash);
        Sw_Core =  findViewById(R.id.Sw_Core);

        fromAccount =  findViewById(R.id.fromAccount);
        toAccount =  findViewById(R.id.toAccount);
        expenseType =  findViewById(R.id.expenseType);

        myCalendar = Calendar.getInstance();
        Trans_Date = findViewById(R.id.Trans_Date) ;
        Trans_Date.setText(Get_Current_Date_Time(System_Date_Format_3Mon_P));
        Trans_Time = findViewById(R.id.Trans_Time) ;
        Trans_Time.setText(Get_Current_Date_Time(System_Time_Format_HMP));
        ET_Amount = findViewById(R.id.Trans_Amount) ;
        ET_OtherDetails = findViewById(R.id.Cash_Bank_OtherDetails) ;
        Button save = findViewById(R.id.Csah_Bank_Save) ;

        fromAccount.setOnItemSelectedListener(this);
        toAccount.setOnItemSelectedListener(this);
        expenseType.setOnItemSelectedListener(this);

        fromAccount.UpdateCB(true, "Person", "Name", "Disabled = 'NO'", "");
        toAccount.UpdateCB(true, "Person", "Name", "Disabled = 'NO'", "");

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        Log.i (TAG, "Trans_Date" + Trans_Date);
        Trans_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CashBank_Modify.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Log.i (TAG, "Trans_Time" + Trans_Time);
        Trans_Time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CashBank_Modify.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Trans_Time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Table = "MoneyTransaction";

                boolean Cash = !Sw_Cash.isChecked();
                boolean Core = !Sw_Core.isChecked();

                String t1am_pm = (Trans_Time.getText().toString()).trim();
                int abcd_am_pm = (t1am_pm.contains("PM") || t1am_pm.contains("pm"))?(12):0;
                t1am_pm = t1am_pm.substring(0,5);
                t1am_pm += ":00";
                //t1am_pm = Add_Secs_Time_String (t1am_pm, abcd_am_pm, System_Time_Format_HMP, System_Time_Format);
                String t_am_pm = t1am_pm.substring(0,2);
                String t_am_pm1 = t1am_pm.substring(2,8);
                t1am_pm = Integer.toString(String2Int(t_am_pm) + abcd_am_pm) + t_am_pm1;
                String Create_TS =  Add_Secs_Time_String ((Trans_Date.getText()).toString(), 0, User_Date_Format, System_Date_Format) + " "
                        + t1am_pm;

                String Modify_TS = Get_Current_Date_Time();

                if ((Create_TS == null) || (Create_TS.isEmpty())) {
                    Create_TS = Modify_TS;
                }

                String Amount = ET_Amount.getText().toString();

                String OtherDetails = ET_OtherDetails.getText().toString();

                String[] Con_List = new String[11];
                Con_List[0] = Create_TS;
                Con_List[1] = FBAppMBName;

                int id = String2Int(myDB_G.Get_Val_from_DB_UD(Table, "id", "max", ""));
                id++;

                Con_List[2] =  Integer.toString(id);

                String Reference;
                if (Cash) {
                    if (Core) {
                        Reference = "Cash";
                    } else {
                        Reference = "Cash_Expense";
                    }
                } else {
                    if (Core) {
                        Reference = "Bank";
                    } else {
                        Reference = "Bank_Expense";
                    }
                }

                String From_ACC = fromAccount.getSelectedItemI();
                String To_ACC = toAccount.getSelectedItemI();
                Con_List[3] = Reference;
                Con_List[4] = Cash?From_ACC:"-";
                Con_List[5] = Cash?To_ACC:"-";
                Con_List[6] = Cash?"-":From_ACC;
                Con_List[7] = Cash?"-":To_ACC;
                Con_List[8] =  "";//TE_Cheque.getText();
                Con_List[9] = Amount;
                Con_List[10] = OtherDetails;

                String UpdateStatement = Con_List[0] + "," + Con_List[1] + "," + Con_List[2] + "," + Con_List[3] + "," + Con_List[4]
                        + "," + Con_List[5] + "," + Con_List[6] + "," + Con_List[7] + "," + Con_List[8] + "," + Con_List[9] + "," + Con_List[10];

                //Log.i (TAG, "UpdateStatement = " + UpdateStatement);

                String ref_id = "Money_" + Con_List[2];

                if (myDB_G.InUp2_MoneyTransaction_UD(Create_TS, Con_List, true)) {
                    ErrMsgDialog("Data Updated to DB, Now Syncing to Cloud");

                    UPload_Data_Msg msg = new UPload_Data_Msg(FBAppCustomer, Create_TS, Curr_Activity.Act_MoneyTransaction, UpdateStatement);
                    UPload_Data_Msg_List.add(msg);

                    // Add Data to Accounts Table Local and Remote.
                    Insert_into_account(From_ACC, String2Double2P(Amount), 0, OtherDetails, Reference,ref_id,
                    Create_TS, false, false, "", true);

                    Insert_into_account(From_ACC, 0, String2Double2P(Amount), OtherDetails, Reference,ref_id,
                            Add_Secs_Time_String(Create_TS, 3), !Core, false, "", true);
                } else {
                    ErrMsgDialog("Unable to Insert/Update Material into Local DB");
                }

                //ErrMsgDialog("This Feature is not Completely Implemented as of now.\n\nIt will be implemented in coming Releases");

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void updateLabel() {
        String myFormat = "dd/MMM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Trans_Date.setText(sdf.format(myCalendar.getTime()));
    }
}
