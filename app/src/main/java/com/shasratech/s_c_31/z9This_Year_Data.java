package com.shasratech.s_c_31;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.Globals.MainWindow_Width;
import static com.shasratech.s_c_31.Globals.String2Int;
import static com.shasratech.s_c_31.Globals.Trans_Canvas_Height;
import static com.shasratech.s_c_31.Globals.WhereCluase_Trans;
import static com.shasratech.s_c_31.Globals.myColors_int_Arr;
import static com.shasratech.s_c_31.Globals.myDB_G;

public class z9This_Year_Data extends View {
    private final String TAG = "TransCanvas";
    //private static final Object Color = ;

    public z9This_Year_Data(Context context) {
        super(context);
        //setMinimumWidth(100);
        //setMinimumHeight(100);
    }

    public z9This_Year_Data(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public z9This_Year_Data(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public z9This_Year_Data(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas cv) {

        int Width = (MainWindow_Width * 80)/100;

        cv.drawColor(Color.BLACK);


        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(5);

        Paint p_text = new Paint();
        p_text.setColor(Color.WHITE);
        p_text.setStrokeWidth(5);

        List<String> Result = new ArrayList<>();

        String Statement = "";
        if ((WhereCluase_Trans != null) && (!WhereCluase_Trans.isEmpty())) {
            Statement = "Select SUM(Met_Weight), Material from Transactions where " + WhereCluase_Trans  + " and Empty = 'NO' group by Material order by 1 Desc";
        } else {
            Statement = "Select SUM(Met_Weight), Material from Transactions  group by Material order by 1 Desc";
        }

         if (myDB_G.RunQueryOnce(Statement, Result)) {

             int a = 0;
             int b = Result.size();

             char sep = '\007';
             String Separator = "";
             Separator += sep;

             int[] MW_Arr = new int[b];
             int Max_Met_Wt = 0;
             String[] Met_Arr = new String[b];


             for (; a < b; a++) {
                 String[] O2 = (Result.get(a)).split(Separator);
                 String Met_Wt = "";
                 String Material = "";

                 if ((O2.length > 1) && (O2[1] != null)) {
                     Material = O2[1];
                 }

                 if ((O2.length > 0) && (O2[0] != null)) {
                     Met_Wt  = O2[0];
                 }

                 int t = String2Int(Met_Wt);
                 MW_Arr[a] = t;
                 if (t > Max_Met_Wt) Max_Met_Wt = t;

                 Met_Arr[a] = Material;
                // Log.w(TAG, "a = " + a +   ", Material [" + Material + "] Weight is = [" + Met_Wt + "] , t = " + t + ", Max_Met_Wt = " + Max_Met_Wt);
             }

             //int y_f = Height/((2 * b ) + 1);
             int y_f = 70;
             if (Max_Met_Wt == 0) Max_Met_Wt = 1;

             for (a = 0; a < b; a++) {
                 p.setColor(myColors_int_Arr[a % 20]);

                 float h = ((MW_Arr[a] * Width)/(Max_Met_Wt ));
              //   Log.w (TAG, "height = " + h);

              //   cv.drawRect((( 2 * a) + 1) * x_f, h  -10, ((2 * a )+ 2) * x_f, Width - 10, p );
                    cv.drawRect(10, (( 2 * a) + 1) * y_f , h, ((2 * a )+ 2) * y_f, p );
                 Trans_Canvas_Height = ((2 * a )+ 2) * y_f + 50;
            //     Log.i (TAG, "Trans_Canvas_Height ="   + Trans_Canvas_Height);

             }

             Trans_Canvas_Height = ((2 * a )+ 2) * y_f + 100;
            // Log.i (TAG, "Trans_Canvas_Height ="   + Trans_Canvas_Height);

             //cv.rotate(-90);
             p_text.setTextSize(y_f);
             for (a = 0; a < b; a++) {
                // float h = Width -  ((MW_Arr[a] * Width)/Max_Met_Wt);
                 //Log.w (TAG, "height = " + h);
                 float x = 40;
                 float y = ((( 2 * a) + 2) * y_f)  - 10;
                 String text = Met_Arr[a] + "\t\t\t\t\t" + Integer.toString(MW_Arr[a]);

                // Log.w (TAG, "x = " + x + ", y = " + y + ", Text = " + text);

                 cv.drawText( text, x, y,  p_text );
             }

         }
        ViewGroup.LayoutParams lp = getLayoutParams();

        /*if (Trans_Boolean_Bar_Chart_FullSize) {
             lp.height = Trans_Canvas_Height;
         } else {
             lp.height = 500;
         } */
        lp.height = Trans_Canvas_Height;
        setLayoutParams(lp);

    }
}
