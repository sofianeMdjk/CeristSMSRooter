package com.example.ceristsmsrooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


public class FormActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    Button submitButton=null;
    CheckBox passwordCb=null;
    CheckBox savingCb=null;


    //All the connexion data
    public static String username;
    public static String password;
    public static String host;
    public static String incomingMail;
    public static String subject;
    public static Context context;

    //Elements used to save user infos
    SharedPreferences preferences =null;
    SharedPreferences.Editor editor = null;

    private final String prefUsername="userName";
    private final String prefPassword="password";
    private final String prefHost="host";
    private final String prefIncoming ="incomingMail";
    private final String prefSubject ="subject";

    private EditText mailEt;
    private EditText passwordEt;
    private EditText hostEt;
    private EditText incomingEt;
    private EditText subjectEt;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        submitButton=(Button)this.findViewById(R.id.submitButton);
        this.submitButton.setOnClickListener(this);

        preferences=PreferenceManager.getDefaultSharedPreferences(this);
        editor=preferences.edit();

        //We initialize all the editText and checkBox
         mailEt = (EditText) this.findViewById(R.id.emailEdit);
         passwordEt = (EditText) this.findViewById(R.id.passwordEdit);
         hostEt = (EditText) this.findViewById(R.id.hostEdit);
         incomingEt=(EditText)this.findViewById(R.id.incomingEdit);
         subjectEt=(EditText)this.findViewById(R.id.subjectEdit);

        passwordCb=(CheckBox)this.findViewById(R.id.passwordCb);
        savingCb=(CheckBox)this.findViewById(R.id.saveInfosCb);

        passwordCb.setOnCheckedChangeListener(this);
        savingCb.setOnCheckedChangeListener(this);


        //saved Connexion infos
        if(this.getSharedPreferencesExistance())
        {
            mailEt.setText(preferences.getString(prefUsername,""));
            passwordEt.setText(preferences.getString(prefPassword, ""));
            hostEt.setText(preferences.getString(prefHost,""));
            incomingEt.setText(preferences.getString(prefIncoming, ""));
            subjectEt.setText(preferences.getString(prefSubject, ""));
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form, menu);
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
        System.exit(1);
        return super.onOptionsItemSelected(item);
    }

    public static String getIncomingMail() {
        return incomingMail;
    }

    public static String getSubject() {
        return subject;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getHost() {
        return host;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submitButton: {

                username = mailEt.getText().toString();
                password = passwordEt.getText().toString();
                host = hostEt.getText().toString();
                incomingMail=incomingEt.getText().toString();
                subject=subjectEt.getText().toString();
                context=getApplicationContext();
                if(!emptyField()) {

                    //We save the connexion infos here
                    if(savingCb.isChecked())
                    {
                        editor.putString(prefUsername, username);
                        editor.putString(prefPassword, password);
                        editor.putString(prefHost, host);
                        editor.putString(prefIncoming, incomingMail);
                        editor.putString(prefSubject, subject);
                        editor.commit();
                    }
                    else//we delete them here
                    {

                        editor.clear();
                        editor.commit();
                    }
                    Intent intent = new Intent(this, EmailFetching.class);
                    this.startActivity(intent);
                }
                else
                    Toast.makeText(this,"Missing informations",Toast.LENGTH_LONG).show();
            }break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isChecked()) {
                    passwordEt.setTransformationMethod(new PasswordTransformationMethod());

            }
        else
        {
                passwordEt.setTransformationMethod(new HideReturnsTransformationMethod());
        }
    }


    public boolean emptyField()
    {
        if(incomingMail==null || subject==null || username==null || password==null||host==null)
            return true;
        if(incomingMail.equals("") || subject.equals("") || username.equals("") || password.equals("")||host.equals(""))
            return true;
        return false;
    }
    private boolean getSharedPreferencesExistance()
    {
        if(preferences.contains(this.prefUsername))
            return true;
        else
            return false;
    }
}
