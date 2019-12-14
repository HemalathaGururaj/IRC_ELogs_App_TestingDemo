package com.shasratech.s_c_31;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.shasratech.s_c_31.Globals.*;
import static com.shasratech.s_c_31.MainActivity.*;

public class TransCanvas_Cash_Sales extends View {
    private final String TAG = "TransCanvasCashSales";

    double[] MW_Arr = null;
    double Max_Met_Wt = 0;
    int Max_Met_Len = 0;
    String[] Met_Arr = null;

    public TransCanvas_Cash_Sales(Context context) {
        super(context);
    }

    public TransCanvas_Cash_Sales(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransCanvas_Cash_Sales(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TransCanvas_Cash_Sales(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas cv) {
        int Width = (MainWindow_Width * 75)/100;

        cv.drawColor(Color.BLACK);

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(5);

        Paint p_text = new Paint();
        p_text.setColor(Color.WHITE);
        p_text.setStrokeWidth(5);

        List<String> Result = new ArrayList<>();

        String Statement = "";
        String To_TS = "";
        String From_TS = "";

        int a = 0;
        int b = 0;
        int Gap_with_Items = 35;
        if (Cash_Chart_Type_Changed || DB_Cash_Chart_Data_Changed) {
            switch (main_chart_selection) {
                case MCS_Daily:
                    Cash_Type_View_set_Text("\u20B9 " + "Cash Sales - Daily");

                    To_TS = Get_Current_Date_Time(System_Date_Format) + " 23:59:59";
                    From_TS = Add_Secs_Time_String(To_TS, (-86400 * (60 + 1)));

                    Statement = "select ROUND(SUM(Total_Val)), SUBSTR(Create_TS,0,11) Date from Transactions where Payment = 'CASH' and In_Out = 'SALE' "
                            + " and Create_TS >= '" + From_TS + "' and Create_TS <= '" + To_TS + "'  group by 2 order by 2 Desc;";

                    break;

                case MCS_Monthly:
                    Cash_Type_View_set_Text("\u20B9 " + "Cash Sales - Monthly");

                    To_TS = Get_Current_Date_Time(System_Date_Format) + " 23:59:59";
                    From_TS = Add_Secs_Time_String(To_TS, (-86400 * ((370 * 2) + 10)));
                    From_TS = From_TS.substring(0, 8) + "01 00:00:00";

                    if ((From_TS.isEmpty()) || (To_TS.isEmpty())) return;

                    Statement = "select ROUND(SUM(Total_Val)), SUBSTR(Create_TS,0,8) Date from Transactions where Payment = 'CASH' and  In_Out = 'SALE' "
                            + " and Create_TS >= '" + From_TS + "' and Create_TS <= '" + To_TS + "'  group by 2 order by 2 Desc;";

                    break;
                case MCS_Yearly:
                    Cash_Type_View_set_Text("\u20B9 " + "Cash Sales - Yearly");

                    To_TS = Get_Current_Date_Time(System_Date_Format) + " 23:59:59";
                    From_TS = Add_Secs_Time_String(To_TS, (-86400 * ((370 * 5) + 10)));
                    From_TS = From_TS.substring(0, 5) + "01-01 00:00:00";

                    if ((From_TS.isEmpty()) || (To_TS.isEmpty())) return;

                    Statement = "select ROUND(SUM(Total_Val)), SUBSTR(Create_TS,0,5) Date from Transactions where Payment = 'CASH' and  In_Out = 'SALE' "
                            + " and Create_TS >= '" + From_TS + "' and Create_TS <= '" + To_TS + "'  group by 2 order by 2 Desc;";
                    break;
                default: return;
            }

            if (myDB_G.RunQueryOnce(Statement, Result)) {
                Cash_Chart_Type_Changed = false;
                DB_Cash_Chart_Data_Changed = false;

                a = 0;
                b = Result.size();

                char sep = '\007';
                String Separator = "";
                Separator += sep;

                MW_Arr = new double[b];
                Max_Met_Wt = 0;
                Max_Met_Len = 0;
                Met_Arr = new String[b];

                for (; a < b; a++) {
                    String[] O2 = (Result.get(a)).split(Separator);
                    String Met_Wt = "";
                    String Material = "";

                    if ((O2.length > 1) && (O2[1] != null)) {
                        Material = O2[1];
                    }

                    if ((O2.length > 0) && (O2[0] != null)) {
                        Met_Wt = O2[0];
                    }
                    if (((Met_Wt.length()) + (Material.length())) > Max_Met_Len)
                        Max_Met_Len = Met_Wt.length() + (Material.length());

                    double t = String2Double2P(Met_Wt);
                    MW_Arr[a] = t;
                    if (t > Max_Met_Wt) Max_Met_Wt = t;

                    Met_Arr[a] = Material;
                }
            }
        }

        b = (MW_Arr != null)?MW_Arr.length:0;
        int y_f = 70;
        if (Max_Met_Wt == 0) Max_Met_Wt = 1;

        int top_p = 40;
        int bottom_p = top_p + y_f;

        for (a = 0; a < b; a++) {
            p.setColor(myColors_int_Arr[a % 20]);

            float h = (float)((MW_Arr[a] * Width)/(Max_Met_Wt ));

            cv.drawRect(10, top_p , h,  bottom_p, p );

            top_p += y_f + Gap_with_Items;
            bottom_p = top_p + y_f;
        }

        Trans_Canvas_Height = bottom_p + 20;

        top_p = 40;
        bottom_p = top_p + y_f;

        p_text.setTextSize(y_f);
        for (a = 0; a < b; a++) {
            float x = 40;
            float y = ((( 2 * a) + 2) * y_f)  - 10;
            String RP_Format = Double2String0P_RupeeFormat(MW_Arr[a]);

            cv.drawText( Met_Arr[a], x, bottom_p,  p_text );

            int dist = MainWindow_Width -(180 + (RP_Format.length() * 27));
            cv.drawText(RP_Format, dist, bottom_p, p_text  );

            top_p += y_f + Gap_with_Items;
            bottom_p = top_p + y_f - 10;
        }
        ViewGroup.LayoutParams lp = getLayoutParams();

        lp.width = MainWindow_Width - 100;
        lp.height = Trans_Canvas_Height;
        setLayoutParams(lp);
    }
}
