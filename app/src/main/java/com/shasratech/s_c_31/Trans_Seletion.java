package com.shasratech.s_c_31;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.shasratech.s_c_31.Globals.*;

public class Trans_Seletion extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    EditText date1,time1,date2,time2;
    Calendar myCalendar;
    Button year,quarter,month,week,today;

    MySpinner CB_Sel1 = null;
    MySpinner CB_Sel2 = null;
    MySpinner CB_SSel1 = null;
    MySpinner CB_SSel2 = null;

    MySpinner CB_PaymentType = null;
    MySpinner CB_LoadType = null;
    MySpinner CB_InOut = null;

    String CB_Sel1_Curr_Item = "";
    String CB_Sel2_Curr_Item = "";
    String CB_SSel1_Curr_Item = "";
    String CB_SSel2_Curr_Item = "";
    String Selector1 = "";
    String Selector2 = "";
    static final String TAG = "Trans_Seletion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans__seletion);

        date1 = (EditText) findViewById(R.id.editText1);
        date2 = (EditText) findViewById(R.id.editText3);
        time1 = (EditText) findViewById(R.id.editText2);
        time2 = (EditText) findViewById(R.id.editText4);
        CB_Sel1 = (MySpinner) findViewById(R.id.CB_Sel1);
        CB_Sel2 = (MySpinner) findViewById(R.id.CB_Sel2);
        CB_SSel1 = (MySpinner) findViewById(R.id.CB_SSel1);
        CB_SSel2 = (MySpinner) findViewById(R.id.CB_SSel2);

        CB_PaymentType = (MySpinner) findViewById(R.id.CB_PaymentType);
        CB_LoadType = (MySpinner) findViewById(R.id.CB_LoadType);
        CB_InOut = (MySpinner) findViewById(R.id.CB_InOut);

        Button year = (Button)findViewById(R.id.year);
        Button quarter = (Button)findViewById(R.id.quarter);
        Button month = (Button)findViewById(R.id.month);
        Button week = (Button)findViewById(R.id.week);
        Button today = (Button)findViewById(R.id.today);

        myCalendar = Calendar.getInstance();

        CB_Sel1.setOnItemSelectedListener(this);
        CB_Sel2.setOnItemSelectedListener(this);
        CB_SSel1.setOnItemSelectedListener(this);
        CB_SSel2.setOnItemSelectedListener(this);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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

        final DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };

        date1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Trans_Seletion.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Trans_Seletion.this, dateClick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Trans_Seletion.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        time2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Trans_Seletion.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        Init_Spinners();

        CB_Sel1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CB_Sel1_Curr_Item = parent.getItemAtPosition(position).toString();
                CurrentSelect_Type_Changed_1 (CB_Sel1_Curr_Item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CB_Sel1_Curr_Item = "";
                CurrentSelect_Type_Changed_1 (CB_Sel1_Curr_Item);
            }

        });

        CB_Sel2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CB_Sel2_Curr_Item = parent.getItemAtPosition(position).toString();
                CurrentSelect_Type_Changed_2 (CB_Sel2_Curr_Item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CB_Sel2_Curr_Item = "";
                CurrentSelect_Type_Changed_2 (CB_Sel2_Curr_Item);
            }
        });

        CB_SSel1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CB_SSel1_Curr_Item = parent.getItemAtPosition(position).toString();
                Selector1 = CurrentSelect_Val_Changed_Gen(CB_SSel1_Curr_Item, CB_Sel1_Curr_Item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //CB_SSel1_Curr_Item = "";
                //Selector1 = CurrentSelect_Val_Changed_Gen(CB_SSel1_Curr_Item, CB_Sel1_Curr_Item);
            }



        });

        CB_SSel2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CB_SSel2_Curr_Item = parent.getItemAtPosition(position).toString();
                Selector2 = CurrentSelect_Val_Changed_Gen(CB_SSel2_Curr_Item, CB_Sel2_Curr_Item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //CB_SSel2_Curr_Item = "";
                //Selector2 = CurrentSelect_Val_Changed_Gen(CB_SSel2_Curr_Item, CB_Sel2_Curr_Item);
            }
        });

        switch (curr_activity) {
            case Act_Transactions: Update_from_WhereClause(WhereCluase_Trans); break;
            case Act_AutoSaveTrans: Update_from_WhereClause(WhereCluase_AutoSaveTrans); break;
            case Act_Account: Update_from_WhereClause(WhereCluase_Account); break;
        }
    }

    private boolean Update_from_WhereClause (String WhereCluase) {
        Log.i (TAG, "Update_from_WhereClause WhereCluase  = " + WhereCluase);
        String[] w_arr = WhereCluase.split("and");
        int a = 0;
        int b = w_arr.length;
        boolean Sel1_Set = false;
        for (; a < b; a++) {
            String temp = (w_arr[a]).replace("'" , "");
            if (temp.isEmpty()) continue;
            if ((w_arr[a]).contains("Create_TS >=")) {

                int i = (temp).indexOf("Create_TS >=");

                i = (temp).indexOf(" ", i + 1);
                i = (temp).indexOf(" ", i + 1);
                int j = (temp).indexOf(" ", i + 1);

                String d = (temp).substring(i, j);
                String t = (temp).substring (j, (temp.length()));
                t = t.substring(0,9);

                Log.i(TAG, "Update_from_WhereClause i = " + i + ", j = " + j + ", d = " + d + ", t" + t);

                date1.setText(Add_Secs_Time_String(d, 0, System_Date_Format, User_Date_Format));
                time1.setText(t);
            } else if ((temp).contains("Create_TS <=")) {
                int i = (temp).indexOf("Create_TS <=");
                i = (temp).indexOf(" ", i + 1);
                i = (temp).indexOf(" ", i + 1);
                int j = (temp).indexOf(" ", i + 1);
                String d = (temp).substring(i, j);
                String t = (temp).substring(j, (temp.length()));
                t = t.substring(0, 9);

                date2.setText(Add_Secs_Time_String(d, 0, System_Date_Format, User_Date_Format));
                time2.setText(t);

                Log.i(TAG, "Update_from_WhereClause i = " + i + ", j = " + j + ", d = " + d + ", t" + t);
            } else if ((w_arr[a]).contains("TimeStamp >=")) {

                int i = (temp).indexOf("TimeStamp >=");

                i = (temp).indexOf(" ", i + 1);
                i = (temp).indexOf(" ", i + 1);
                int j = (temp).indexOf(" ", i + 1);

                String d = (temp).substring(i, j);
                String t = (temp).substring (j, (temp.length()));
                t = t.substring(0,9);

                Log.i(TAG, "Update_from_WhereClause i = " + i + ", j = " + j + ", d = " + d + ", t" + t);

                date1.setText(Add_Secs_Time_String(d, 0, System_Date_Format, User_Date_Format));
                time1.setText(t);
            } else if ((temp).contains("TimeStamp <=")) {
                int i = (temp).indexOf("TimeStamp <=");
                i = (temp).indexOf(" ", i + 1);
                i = (temp).indexOf(" ", i + 1);
                int j = (temp).indexOf(" ", i + 1);
                String d = (temp).substring(i, j);
                String t = (temp).substring (j, (temp.length()));
                t = t.substring(0, 9);

                date2.setText(Add_Secs_Time_String(d, 0, System_Date_Format, User_Date_Format));
                time2.setText(t);

                Log.i(TAG, "Update_from_WhereClause i = " + i + ", j = " + j + ", d = " + d + ", t" + t);

            } else  {
                switch (curr_activity) {

                    case Act_AutoSaveTrans: break;
                    case Act_Account: break;
                    default: break;

                    case Act_Transactions:
                        if (!Sel1_Set) {

                            String a1 = (Extract_Key_from_Pair(temp)).trim();
                            if (a1.equals("Cust_Suplr")) a1 = "CUSTOMER";
                            String a2 = (Extract_value_from_Pair(temp)).trim();

                            Log.i(TAG, "11111111111 Update_from_WhereClause a1 = " + a1);
                            Log.i(TAG, "11111111111 Update_from_WhereClause a2 = " + a2);

                            CB_SSel1_Curr_Item = a2;
                            CB_Sel1.SetSelectedItem(a1);
                            CurrentSelect_Type_Changed_1(a1);
                            CB_SSel1.SetSelectedItem(a2);
                        } else {
                            String a1 = (Extract_Key_from_Pair(temp)).trim();
                            if (a1.equals("Cust_Suplr")) a1 = "CUSTOMER";
                            String a2 = (Extract_value_from_Pair(temp)).trim();

                            Log.i(TAG, "22222222222 Update_from_WhereClause a1 = " + a1);
                            Log.i(TAG, "22222222222 Update_from_WhereClause a2 = " + a2);
                            CB_SSel2_Curr_Item = a2;
                            CB_Sel2.SetSelectedItem(a1);
                            CurrentSelect_Type_Changed_2(a1);
                            CB_SSel2.SetSelectedItem(a2);
                        }
                        break;
                }
            }
        }
        return true;
    }


    public void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat(User_Date_Format, Locale.US);

        date1.setText(sdf.format(myCalendar.getTime()));
    }

    public void updateLabel2() {
        SimpleDateFormat sdf = new SimpleDateFormat(User_Date_Format, Locale.US);

        date2.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void on_Transactions_Selection_Submit_Click(View view) {
        String WhereCluase_Temp = "";

        WhereCluase_Temp = "Create_TS >= '" + Add_Secs_Time_String ((date1.getText()).toString(), 0, User_Date_Format, System_Date_Format) + " "
                + (time1.getText().toString()).trim() + "' and Create_TS <= '"
                + Add_Secs_Time_String ((date2.getText()).toString(), 0, User_Date_Format, System_Date_Format) + " "
                + (time2.getText().toString()).trim() + "'";


        if ((Selector1 != null) && (!Selector1.isEmpty())) { WhereCluase_Temp += "  and " + Selector1 + " "; }
        if ((Selector2 != null) && (!Selector2.isEmpty())) { WhereCluase_Temp += " and " + Selector2 + " "; }

        Log.i (TAG, "curr_activity = " + curr_activity);
        switch (curr_activity) {
            case Act_AutoSaveTrans:
                WhereCluase_AutoSaveTrans = WhereCluase_Temp.replace("Create_TS", "TimeStamp");
                //Log.i (TAG, "WhereCluase_AutoSaveTrans = " + WhereCluase_AutoSaveTrans);
                break;
            case Act_Account:
                WhereCluase_Account = WhereCluase_Temp.replace("Create_TS", "TimeStamp");
                break;

            default:  break;

            case Act_Transactions: {
                String temp = CB_InOut.getSelectedItemI();
                if (!temp.equals("ALL")) {
                    WhereCluase_Temp += "  and In_Out = '" + temp + "' ";
                }

                temp = CB_LoadType.getSelectedItemI();
                if (!temp.equals("ALL")) {
                    switch (temp) {
                        case "LOADED":
                            temp = "NO";
                            break;
                        case "EMPTY":
                            temp = "YES";
                            break;
                    }
                    WhereCluase_Temp += "  and Empty = '" + temp + "' ";
                }

                temp = CB_PaymentType.getSelectedItemI();
                if (!temp.equals("ALL")) {
                    WhereCluase_Temp += "  and Payment = '" + temp + "' ";
                }

                WhereCluase_Trans = WhereCluase_Temp;
            }
        }

        ShowMyToast("Selection is \n\n " + WhereCluase_Temp, 10000);
        finish();
    }


    public void Init_Spinners () {
        List<String> categories = new ArrayList<String>();
        categories.add("ALL"); //CB_Sel1.addItem("ALL");
        categories.add("CUSTOMER");
        categories.add("SUPPLIER");
        categories.add("VEHICLE");
        categories.add("MATERIAL");
        categories.add("RATE");
        categories.add("SITE");
        categories.add("QUARRY");
        categories.add("TRANSPORT");
        categories.add("IN_OUT");
        categories.add("PAYMENT");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CB_Sel1.SetAdapter(dataAdapter1);
        CB_Sel2.SetAdapter(dataAdapter1);


        String[] temp_type = {"EMPTY", "LOADED", "ALL"};
        CB_LoadType.SetItems(temp_type);
        CB_LoadType.SetSelectedItem("LOADED");


        String[] temp_type1 = {"CASH", "CREDIT", "RTGS", "ALL"};
        CB_PaymentType.SetItems(temp_type1);
        CB_PaymentType.SetSelectedItem("CREDIT");

        String[] temp_type2 = {"SALE", "PURCHASE",  "ALL"};
        CB_InOut.SetItems(temp_type2);
        CB_InOut.SetSelectedItem("SALE");

    }

    public String CurrentSelect_Val_Changed_Gen(String current_Selection_val,
                                                String current_Selection_Type)
    {
        if ((current_Selection_val == null) || (current_Selection_val.isEmpty())) return "";
        if ((current_Selection_Type == null) || (current_Selection_Type.isEmpty())) return "";

        String Selector = "";
        if ((current_Selection_Type.equals("SUPPLIER"))  ||
                (current_Selection_Type.equals("CUSTOMER"))) {
            Selector = " Cust_Suplr = '" +  current_Selection_val + "'";

        } else if (current_Selection_Type.equals("ACCOUNT_NAME")) {
            Selector = "AccountName =  '"+  current_Selection_val  + "'";
            //Acc_Name = current_Selection_val;

        } else if (current_Selection_Type.equals("SourceSite")) {
            Selector = "SourceSite =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("FROM_AC")) {
            Selector = "FROM_AC =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("TO_AC")) {
            Selector = "TO_AC =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("Money_Trans_Type")) {
            Selector = "Type =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("FROM_BANK")) {
            Selector = "FROM_BANK =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("TO_BANK")) {
            Selector = "TO_BANK =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("VEHICLE")) {
            Selector = "VEHICLE =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("MATERIAL")) {
            Selector = "Material =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("RATE")) {
            Selector = "Rate =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("IN_OUT")) {
            Selector = "In_Out =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("PAYMENT")) {
            Selector = "Payment =  '"+  current_Selection_val  + "'";


        } else if (current_Selection_Type.equals("REFERENCE")) {
            Selector = "Reference =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("Ref_ID")) {
            Selector = "Ref_id =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("TRANSPORT")) {
            Selector = "Transport =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("Transport/Customer")) {
            Selector = "Transport =  '"+  current_Selection_val  + "' or Cust_Suplr = '" + current_Selection_val + "'";

        } else if (current_Selection_Type.equals("SITE")) {
            Selector = "SITE =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("QUARRY")) {
            Selector = "Quarry =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("OTHER_DETAILS")) {
            Selector = "Other_Details =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("Name")) {
            Selector = "Name =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("Number")) {
            Selector = "Number =  '"+  current_Selection_val  + "'";

        } else if (current_Selection_Type.equals("Sent")) {
            Selector = "Sent =  '"+  current_Selection_val  + "'";
        }
        return Selector;
    }

    void CurrentSelect_Type_Changed_1(String current_Selection_Type)
    {
        String Sel = "";
        ////MyDebugPrintf();
        //  if (Init_Done == false) return;
        String Selector1 = "";
        //System.out.println("11111 current_Selection_Type = " + current_Selection_Type);

        /* CB_SSel1.setConvert_Space2_Dash(false);
        CB_SSel1.setConvert_Hyphen2_UnderScore(true);
        CB_SSel1.setConvert_Space2_UnderScore(true); */

        String myTable = "Person";
        String myColumn = "Name";
        Selector1 = "";
        if ((current_Selection_Type == null) || (current_Selection_Type.isEmpty())) {
            Selector1 = "";
            return;
        }

        if (current_Selection_Type.equals("ALL")) {
            CB_SSel1.setEnabled(false);
            //CB_SSel1.Remove_AllItems_from_CB_List();
            //CB_SSel1.removeAllViews();
            //Acc_Name = ""; * /

            return;
        } else {
            CB_SSel1.setEnabled(true);
        }
        /* if (current_Selection_Type.equals("ALL")) {
            return;
        } */

        if (current_Selection_Type.equals("SUPPLIER")) {
            Selector1 = "Person_Type =  'SUPPLIER' ";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("CUSTOMER")) {
            Selector1 = "Person_Type =  'CUSTOMER' ";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("VEHICLE")) {
            myTable = "Vehicle";
            myColumn = "ID_Number";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("MATERIAL")) {
            myTable = "Material";
            myColumn = "Name";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("SITE")) {
            myTable = "SITE";
            myColumn = "Name";
            Sel = "Disabled = 'NO'";
            // selector_l += "Type =  'SITE' ";

        } else if (current_Selection_Type.equals("RATE")) {
            myTable = "Transactions";
            myColumn = "Rate";

        } else if (current_Selection_Type.equals("IN_OUT")) {
            myTable = "Transactions";
            myColumn = "In_Out";

        } else if (current_Selection_Type.equals("PAYMENT")) {
            myTable = "Transactions";
            myColumn = "Payment";

        } else if (current_Selection_Type.equals("TRANSPORT")) {
            myTable = "Transport";
            myColumn = "Name";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("FROM_AC")) {
            myTable = "MoneyTransaction";
            myColumn = "FROM_AC";

        } else if (current_Selection_Type.equals("TO_AC")) {
            myTable = "MoneyTransaction";
            myColumn = "TO_AC";

        } else if (current_Selection_Type.equals("Money_Trans_Type")) {
            myTable = "MoneyTransaction";
            myColumn = "Type";

        } else if (current_Selection_Type.equals("FROM_BANK")) {
            myTable = "MoneyTransaction";
            myColumn = "FROM_BANK";

        } else if (current_Selection_Type.equals("TO_BANK")) {
            myTable = "MoneyTransaction";
            myColumn = "TO_BANK";

        } else if (current_Selection_Type.equals("ACCOUNT_NAME")) {
            myTable = "Account";
            myColumn = "AccountName";

        } else if (current_Selection_Type.equals("SourceSITE")) {
            myTable = "Account";
            myColumn = "SourceSITE";

        } else if (current_Selection_Type.equals("REFERENCE")) {
            myTable = "Account";
            myColumn = "Reference";

        } else if (current_Selection_Type.equals("Ref_ID")) {
            myTable = "Account";
            myColumn = "Ref_id";

        } else if (current_Selection_Type.equals("OTHER_DETAILS")) {
            myTable = "Transactions";
            myColumn = "Other_Details";

        } else if (current_Selection_Type.equals("Name")) {
            myTable = "Sent_Site_SMS";
            myColumn = "Name";

        } else if (current_Selection_Type.equals("Number")) {
            myTable = "Sent_Site_SMS";
            myColumn = "Number";

        } else if (current_Selection_Type.equals("Sent")) {
            myTable = "Sent_Site_SMS";
            myColumn = "Sent";
        }

        if (current_Selection_Type.equals("ALL")) {
            CB_SSel1.setEnabled(false);
            //CB_SSel1.Remove_AllItems_from_CB_List();
            //CB_SSel1.removeAllViews();
            //Acc_Name = ""; */

            return;
        } else {
            CB_SSel1.setEnabled(true);
        }

        Log.i (TAG, "566 Table = " + myTable + ", myCol = " + myColumn  + ", Sel = " + Sel + " , CB_SSel1_Curr_Item =" + CB_SSel1_Curr_Item + "=");
        CB_SSel1.UpdateCB( true,  myTable,  myColumn, Sel, "DISTINCT", CB_SSel1_Curr_Item);
    }

    void CurrentSelect_Type_Changed_2(String current_Selection_Type)
    {
        //MyDebugPrintf();
        String Sel = "";
        // if (Init_Done == false) return;
        String Selector2 = "";

        String myTable = "Person";
        String myColumn = "Name";
        Selector2 = "";

        if ((current_Selection_Type == null) || (current_Selection_Type.isEmpty())) {
            Selector2 = "";
            return;
        }

        if (current_Selection_Type.equals("ALL")) {
            CB_SSel2.setEnabled(false);
            //CB_SSel2.Remove_AllItems_from_CB_List();
            //CB_SSel2.removeAllViews();
            //Acc_Name = "";
            return;
        } else {
            CB_SSel2.setEnabled(true);
        }



        if (current_Selection_Type.equals("SUPPLIER")) {
            Selector2 = "Person_Type =  'SUPPLIER' ";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("CUSTOMER")) {
            Selector2 = "Person_Type =  'CUSTOMER'";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("VEHICLE")) {
            myTable = "Vehicle";
            myColumn = "ID_Number";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("MATERIAL")) {
            myTable = "Material";
            myColumn = "Name";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("SITE")) {
            myTable = "SITE";
            myColumn = "Name";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("RATE")) {
            myTable = "Transactions";
            myColumn = "Rate";

        } else if (current_Selection_Type.equals("IN_OUT")) {
            myTable = "Transactions";
            myColumn = "In_Out";

        } else if (current_Selection_Type.equals("PAYMENT")) {
            myTable = "Transactions";
            myColumn = "Payment";

        } else if (current_Selection_Type.equals("TRANSPORT")) {
            myTable = "Transport";
            myColumn = "Name";
            Sel = "Disabled = 'NO'";

        } else if (current_Selection_Type.equals("FROM_AC")) {
            myTable = "MoneyTransaction";
            myColumn = "FROM_AC";

        } else if (current_Selection_Type.equals("TO_AC")) {
            myTable = "MoneyTransaction";
            myColumn = "TO_AC";

        } else if (current_Selection_Type.equals("Money_Trans_Type")) {
            myTable = "MoneyTransaction";
            myColumn = "Type";

        } else if (current_Selection_Type.equals("FROM_BANK")) {
            myTable = "MoneyTransaction";
            myColumn = "FROM_BANK";

        } else if (current_Selection_Type.equals("TO_BANK")) {
            myTable = "MoneyTransaction";
            myColumn = "TO_BANK";

        } else if (current_Selection_Type.equals("ACCOUNT_NAME")) {
            myTable = "Account";
            myColumn = "AccountName";

        } else if (current_Selection_Type.equals("SourceSite")) {
            myTable = "Account";
            myColumn = "SourceSite";

        } else if (current_Selection_Type.equals("REFERENCE")) {
            myTable = "Account";
            myColumn = "Reference";

        } else if (current_Selection_Type.equals("Ref_ID")) {
            myTable = "Account";
            myColumn = "Ref_id";

        } else if (current_Selection_Type.equals("OTHER_DETAILS")) {
            myTable = "Transactions";
            myColumn = "Other_Details";

        } else if (current_Selection_Type.equals("Name")) {
            myTable = "Sent_Site_SMS";
            myColumn = "Name";

        } else if (current_Selection_Type.equals("Number")) {
            myTable = "Sent_Site_SMS";
            myColumn = "Number";

        } else if (current_Selection_Type.equals("Sent")) {
            myTable = "Sent_Site_SMS";
            myColumn = "Sent";
        }

        CB_SSel2.UpdateCB(true,  myTable,  myColumn, Sel, "DISTINCT", CB_SSel2_Curr_Item);
    }

    public void On_PB_Today_Clicked (View view) {
        String Curr_Date = Get_Current_Date_Time(User_Date_Format);
        date1.setText(Curr_Date);
        date2.setText(Curr_Date);
        time1.setText("00:00:00");
        time2.setText("23:59:59");
        //WhereCluase_Trans = "Create_TS >= '" + Curr_Date + " 00:00:00' and Create_TS <= '" + Curr_Date + " 23:59:59'";
        //ErrMsgDialog("This Feature not implemented\n\nPlease Select Date/Time Range manually for now");
    }
}
