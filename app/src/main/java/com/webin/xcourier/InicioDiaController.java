package com.webin.xcourier;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jonathan Malpica on 3/29/2015.
 */
public class InicioDiaController extends ActionBarActivity {

    private ImageButton btnImgEncender;
    private ImageButton btnImgApagado;
    private TextView lblNroRemiAsigValue;
    private TextView lblUser;
    private TextView lblFecHorIniValue;
    private TextView lblFecHorFinValue;
    private TextView lblRemiOffLineValue;
    private TextView lblLongitude;
    private TextView lblLatitude;
    private double latitud;
    private double altitud;
    private Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_dia_view);

        //inicializar variables
        iniVariables();
    }

    private void iniVariables() {

        res = getResources();

        btnImgEncender = (ImageButton)findViewById(R.id.btnImgEncender);
        btnImgApagado = (ImageButton)findViewById(R.id.btnImgApagado);
        lblNroRemiAsigValue = (TextView)findViewById(R.id.lblNroRemiAsigValue);
        lblUser = (TextView)findViewById(R.id.lblUser);
        lblFecHorIniValue = (TextView)findViewById(R.id.lblFecHorIniValue);
        lblFecHorFinValue = (TextView)findViewById(R.id.lblFecHorFinValue);
        lblRemiOffLineValue = (TextView)findViewById(R.id.lblRemiOffLineValue);
        lblLatitude  = (TextView)findViewById(R.id.latitudeTextView);
        lblLongitude = (TextView)findViewById(R.id.longitudeTextView);
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

    void captureGPS()
    {
        Toast.makeText(getApplicationContext(), "Init GPS", Toast.LENGTH_LONG).show();

        GPSMaps gps = new GPSMaps(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitud = gps.getLatitude();
            altitud= gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitud + "\nLong: " + altitud, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }



    }
    public void iniDia(View view){

        String urlString ="";
        captureGPS();


        urlString = "http://190.102.134.78/COURIER_WCF/Xcourier.svc/rest/DayEndInit?usr=rfelix&activity=0&gpsLat="+ latitud +"&gpsAlti="+ altitud +"&operation=0";


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
                       @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                Toast.makeText(getApplicationContext(),"Conectando...",Toast.LENGTH_LONG).show();

                if(statusCode==200)
                {
                    String str = new String(response);
                    str=str.replace("\"","");
                    Toast.makeText(getApplicationContext(), "Actividad: " + str, Toast.LENGTH_LONG).show();
                    lblRemiOffLineValue.setText(str);
                    lblLatitude.setText(""+ latitud);
                    lblLongitude.setText(""+ altitud);
                }

            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: "+ statusCode + " "+throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("BookFinder", statusCode + " "+throwable.getMessage());
            }
        });

        lblNroRemiAsigValue.setText("75");
        lblFecHorIniValue.setText(new SimpleDateFormat(res.getString(R.string.format_date)).format(new Date()));
        lblUser.setText("Felix Ricardo");

    }

    public void finDia(View view){
        String actividad = lblRemiOffLineValue.getText().toString();
        String urlString ="";
        captureGPS();


        urlString = "http://190.102.134.78/COURIER_WCF/Xcourier.svc/rest/DayEndInit?usr=rfelix&activity=" + actividad + "&gpsLat="+ latitud +"&gpsAlti="+ altitud +"&operation=1";


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                Toast.makeText(getApplicationContext(),"Conectando...",Toast.LENGTH_LONG).show();

                if(statusCode==200)
                {
                    String str = new String(response);
                    str=str.replace("\"","");
                    Toast.makeText(getApplicationContext(), "Fin de Dia: " + str, Toast.LENGTH_LONG).show();
                    lblRemiOffLineValue.setText(str);
                    lblLatitude.setText(""+ latitud);
                    lblLongitude.setText(""+ altitud);
                }

            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: "+ statusCode + " "+throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("BookFinder", statusCode + " "+throwable.getMessage());
            }
        });
        lblFecHorFinValue.setText(new SimpleDateFormat(res.getString(R.string.format_date)).format(new Date()));

    }

    public void irViewLstEntregas(View view){
        Intent i = new Intent(this, ListaRemitosController.class);
        startActivity(i);
    }

}