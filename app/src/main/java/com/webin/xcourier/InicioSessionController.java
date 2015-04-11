package com.webin.xcourier;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class InicioSessionController extends Activity implements View.OnClickListener {
    private String strUser;
    private EditText userText;
    private EditText passwordText;
    private Button loginButton;
    private static final String WEBSERVICE_URL="http://190.102.134.78/COURIER_WCF/Xcourier.svc/rest/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_session_view);
        initParameters();
    }

    void initParameters(){
        userText=(EditText) findViewById(R.id.UserEditText);
        passwordText=(EditText) findViewById(R.id.PasswordEditText);
        loginButton=(Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);


    }
    public void onClick(View v) {
        // queryBooks(searchTxt.getText().toString());
       queryLogin(userText.getText().toString(),passwordText.getText().toString());
       // Toast.makeText(getApplicationContext(), "welcme " + strUser, Toast.LENGTH_LONG).show();
       /*
        if (!str.equals("bad"))
        {

            Intent activityInitDay= new Intent(this,InicioDiaController.class);
            startActivity(activityInitDay);

        }*/

    }
    private void queryLogin(String userName, String userPassword) {

        String urlString="";
        try{

            //damos formato de url
            urlString= "ValidarUsuario?usr=" + URLEncoder.encode(userName, "UTF-8") + "&pass=" + URLEncoder.encode(userPassword, "UTF-8") ;
            //enviar el request

        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
        AsyncHttpClient client= new AsyncHttpClient();
        client.get(WEBSERVICE_URL+urlString, new JsonHttpResponseHandler(){
/*
            @Override
            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                if(statusCode==200)
                {
                    Toast.makeText(getApplicationContext(),"Error:" + statusCode,Toast.LENGTH_LONG).show();
                }

            }

          */

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                Toast.makeText(getApplicationContext(),"Autenticando...",Toast.LENGTH_LONG).show();

                if(statusCode==200)
                {
                    String str = new String(response);
                    str=str.replace("\"","");
                    strUser=str;
                    if (str.equals("bad"))
                    {
                        Toast.makeText(getApplicationContext(), " ¡ Usuario/Password incorrecto ! ", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Bienvenido " + str, Toast.LENGTH_LONG).show();
                        Intent activityInitDay= new Intent(getApplicationContext(),InicioDiaController.class);
                        startActivity(activityInitDay);
                    }
                }
                //Log.d("xCourierHelp:", jsonObject.toString());
               // ReturnValue=jsonObject.toString();
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error){
                Toast.makeText(getApplicationContext(),"Error:" + statusCode+ " " + throwable.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("xCourierHelp:", statusCode+ " " + throwable.getMessage());
               // ReturnValue=statusCode+ " " + throwable.getMessage();
            }
        });

       // WebInvokeRestClass webRest = new WebInvokeRestClass();
       // Toast.makeText(getApplicationContext(), "Welcome " + webRest.getValidUser(userName,userPassword), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio_session, menu);
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
}
