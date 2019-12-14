package com.shasratech.s_c_31;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.security.GeneralSecurityException;

import static com.shasratech.s_c_31.Globals.*;

public class Login extends AppCompatActivity {

    EditText un,pw;
    Button loginbtn,loginSAbtn;
    private static MySpinner CB_User = null;
    private static final String TAG = "Login_Module";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pw=(EditText)findViewById(R.id.login_password);

        CB_User = findViewById(R.id.login_username);
        CB_User.UpdateCB(true, "AppUsers", "Name", "", "DISTINCT");

        loginbtn=(Button)findViewById(R.id.btnLogin);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=pw.getText().toString();

            //=================
                String ABCD = "a";
                String En_ABCD = "";
                En_ABCD = myAESHelper.encrypt (ABCD, AES_Secret_Key );
                Log.i (TAG, "En_ABCD = " + En_ABCD);

                String De_ABCD = "";
                De_ABCD = myAESHelper.decrypt (En_ABCD, AES_Secret_Key );
                Log.i (TAG, "De_ABCD = " + De_ABCD);

                //=================
                String De_Pass = "";

                String En_pass = myDB_G.Get_Val_from_DB_UD("AppUsers", "Password", "", "Name = '" + CB_User.getSelectedItemI() + "'");
                Log.i (TAG, "============================================= En_pass = " + En_pass);
                if (!En_pass.isEmpty()) {
                    De_Pass = myAESHelper.decrypt(myDB_G.Get_Val_from_DB_UD("AppUsers", "Password", "", "Name = '" + CB_User.getSelectedItemI() + "'"), AES_Secret_Key);
                    Log.i(TAG, "============================================= De_Pass = " + De_Pass);
                }

               // De_Pass = "a";

                Boolean checklogin=  password.equals(De_Pass);

                if(checklogin==true){

                    login_Completed=true;
                    Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Login.this, "Check username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });


        loginSAbtn=(Button)findViewById(R.id.btnSuperAdmin);
        loginSAbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=pw.getText().toString();
                Boolean checklogin=  false; //db.logincheck(email,password);
                if (password.equals("abcd")) {
                    checklogin = true;
                }

                if(checklogin==true){
                    login_Completed=true;
                    Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Login.this, "Check username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
