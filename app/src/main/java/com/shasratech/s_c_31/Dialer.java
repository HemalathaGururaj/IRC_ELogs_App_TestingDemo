package com.shasratech.s_c_31;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


// https://stackoverflow.com/questions/39206733/android-drawing-an-arc-inside-a-circle
public class Dialer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "Dialer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dialer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyView extends View {

        public MyView (Context context) {
            super(context);
        }

        protected  void onDraw (Canvas canvas) {
            super.onDraw(canvas);
            int width = getWidth();
            int Height = getHeight();
            int radius = 10;
            Paint paint = new Paint();
            //paint.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paint);

            int max_dimen = Math.min(width, Height);




            paint.setStrokeWidth(15);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#f4f404"));
            RectF oval = new RectF(50, 50, max_dimen - 50, max_dimen - 50);

            paint.setColor(Color.parseColor("#f4f4F4"));
            canvas.drawArc(oval, 135F, 270F, true, paint);

            paint.setColor(Color.parseColor("#00ffff"));

            paint.setStrokeWidth(15);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(max_dimen/2 - 120, max_dimen - 200 - 100, max_dimen/2 + 140, max_dimen - 200 + 40, paint);

            paint.setColor(Color.parseColor("#20ffff"));
            paint.setTextSize(100);
            paint.setStrokeWidth(8);
            canvas.drawText("090", max_dimen/2 - 80, max_dimen - 200, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#04F4F4"));
            canvas.drawCircle(max_dimen/2, max_dimen/2, 60, paint);
            
            paint.setStrokeWidth(12);
            paint.setColor(Color.parseColor("#000000"));
            int a = 0;
            int b = 19; //18
            float sx = 0;
            float sy = 0;
            float ex = (float)(max_dimen/2);
            float ey = (float)(max_dimen/2);
            float angle_incr = (360 -90)/18;
            float angle = -45;

            int len = (max_dimen/2) - 40;
            Log.i (TAG, "ex = " + ex  + ", ey = " + ey + ", len = " + len);


            for (; a < b; a++) {
                float temp_angle = (angle - (a * angle_incr));
                Log.i (TAG, "temp_angle = " + temp_angle);
               // temp_angle = (float)(temp_angle* Math.PI)/180;

                float abcd_sin =    (float)(Math.sin(Math.toRadians(temp_angle)));
                float abcd_cos =    (float)(Math.cos(Math.toRadians(temp_angle)));
                Log.i (TAG, "temp_angle = " + temp_angle + ", abcd_sin = " + abcd_sin + ", abcd_cos = " + abcd_cos);
                sx = max_dimen/2 + (float)((len) * abcd_sin);
                sy = max_dimen/2 + (float)((len) * abcd_cos);

                ex = max_dimen/2 + (float)((len - 80) * abcd_sin);
                ey = max_dimen/2 + (float)((len - 80) * abcd_cos);
                float ey1 = (float)((len - 200) * Math.cos((225 - (a * 10) )/Math.PI));
                Log.i (TAG, "angle = " + temp_angle + ", sx = " + sx  + ", sy = " + sy + "ex = " + ex  + ", ey1 = " + ey1);
                if (a < 6) {
                    paint.setStrokeWidth(12);
                    paint.setColor(Color.parseColor("#00FF00"));
                } else if (a < 12) {
                    paint.setStrokeWidth(12);
                    paint.setColor(Color.parseColor("#FFFF00"));
                } else {
                    paint.setStrokeWidth(12);
                    paint.setColor(Color.parseColor("#FF0000"));
                }
                canvas.drawLine(sx, sy, ex, ey, paint);

                paint.setColor(Color.parseColor("#000000"));
                paint.setStrokeWidth(4);
                paint.setTextSize(50);
                ex = max_dimen/2 + (float)((len - 100) * abcd_sin) - (float)(30 * abcd_sin);
                ey = max_dimen/2 + (float)((len - 100) * abcd_cos) - (float)(30 * abcd_cos);
                String t1 = Integer.toString(a * 10);
                canvas.drawText(t1, ex, ey, paint);
            }

            paint.setColor(Color.parseColor("#000000"));
            paint.setStrokeWidth(10);
            paint.setTextSize(80);
            String t1 = Integer.toString(a * 10);
            canvas.drawText("Speed", max_dimen/2 - 120, max_dimen/2 - 150, paint);

            paint.setColor(Color.parseColor("#FFFF00"));
            paint.setStrokeWidth(10);
            paint.setTextSize(80);
            t1 = Integer.toString(a * 10);
            canvas.drawText("Kmph", max_dimen/2 - 100, max_dimen/2 + 150, paint);

            paint.setStrokeWidth(25);
            paint.setColor(Color.parseColor("#dF2020"));
            canvas.drawLine((float)(max_dimen/2), 70F, (float)(max_dimen/2), (float)(max_dimen/2 - 70), paint);
        }
    }
}
