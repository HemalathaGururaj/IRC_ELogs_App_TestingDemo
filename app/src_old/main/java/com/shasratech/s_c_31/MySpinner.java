package com.shasratech.s_c_31;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;


public class MySpinner extends android.support.v7.widget.AppCompatSpinner {
    static String Curr_Editor_Text = "";
    static String Table = "";
    static String Column = "";
    static String Selection = "";
    static String MinMax = "";
    private ArrayAdapter<String> dataAdapter = null;

    static String TAG = "MySpinner";


    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public  boolean UpdateCB (String Sel) {
        return UpdateCB (false, Table, Column, Selection, MinMax, Sel);
    }

    public  boolean UpdateCB (boolean firstTime, String TableI, String ColumnI, String SelectionI, String MinMax_I) {
        return UpdateCB ( firstTime,  TableI,  ColumnI,  SelectionI,  MinMax_I, Curr_Editor_Text);
    }

    public  boolean UpdateCB (boolean firstTime, String TableI, String ColumnI, String SelectionI, String MinMax_I, String Curr_I) {
        List<String> Result1 = new ArrayList<>();


        Table = TableI;
        Column = ColumnI;
        Selection = SelectionI;
        Curr_Editor_Text = Curr_I;
        MinMax = MinMax_I;

        myDB_G.Get_Val_from_DB_UD(Table, Column, MinMax,  SelectionI, Result1, Column );

        if (firstTime) {
            dataAdapter = new ArrayAdapter<String>(super.getContext(), android.R.layout.simple_spinner_item, Result1);


            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            setAdapter(dataAdapter);
        }

        if ((Curr_I != null) && (!Curr_I.isEmpty())) {
            SetSelectedItem(Curr_I);
        } else if ((dataAdapter.getCount()) > 0) {
            //Log.i (TAG, "Setting Position to 0");
            setSelection(0);
        }
        return true;
    }



    public boolean SetSelectedItem (String item) {
        //Log.i (TAG, "SetSelectedItem this =" + this);
        //Log.i (TAG, "================================== = = = = = = = = = = =  =    = = SetSelectedItem item = " + item + ", Count =" +dataAdapter.getCount());
        if ((item != null) && (!item.isEmpty()) && (dataAdapter != null)) {
            int pos = dataAdapter.getPosition(item);
            //Log.i (TAG, "SetSelectedItem pos = " + pos);
            setSelection(pos);
            return true;
        }
        return false;
    }

    public boolean SetAdapter (ArrayAdapter<String> dataAdapter1) {
        //Log.i (TAG, "SetAdapter this =" + this);
        dataAdapter = dataAdapter1;
        setAdapter(dataAdapter);
        return true;
    }

    public boolean SetItems (String[] Input) {
        //Log.i (TAG, "SetItems this =" + this);
        List<String> categories = new ArrayList<String>();

        int a = 0;
        int b = Input.length;
        //dataAdapter = new ArrayAdapter<String>(super.getContext(), android.R.layout.simple_spinner_item, Result1);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //setAdapter(dataAdapter);

        for (; a < b; a++) {
            String item = Input[a];

            if ((item != null) && (!item.isEmpty())) {
                categories.add(item);
                /* int pos = -1;
                if (dataAdapter != null) {
                    pos = dataAdapter.getPosition(item);
                }
                if (pos < 0) {
                    // Item not found . add it.
                    categories.add(item);
                    //Log.i (TAG, "Added item " + item + "Spinner =====================================");
                } */
            }
        }
        dataAdapter = new ArrayAdapter<String>(super.getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(dataAdapter);
        return true;
    }

    public String getSelectedItemI () {
        String temp = null;
        if (getSelectedItem() != null) temp = getSelectedItem().toString();
        if (temp != null) return temp;
        return "";
    }
}
