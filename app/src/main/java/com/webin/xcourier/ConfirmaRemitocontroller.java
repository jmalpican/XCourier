package com.webin.xcourier;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Adolfo on 11/04/2015.
 */
public class ConfirmaRemitocontroller extends Activity implements OnClickListener{

Button btn;
ImageView img;
Intent i;
Bitmap bmp;
final static int cons = 0;

    private String codigo;
    private double latitud;
    private double altitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirma_remito_view);

        Bundle bundle = getIntent().getExtras();
        codigo=bundle.getString("codigo");

        init();

    }

    public void init(){
        btn = (Button)findViewById(R.id.btnCaptura);
        btn.setOnClickListener(this);
        img = (ImageView)findViewById(R.id.imagen);
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

    @Override
    public void onClick(View v) {
        int id;
        id = v.getId();
        switch(id){
            case R.id.btnCaptura:
                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, cons);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            Bundle ext = data.getExtras();
            bmp = (Bitmap)ext.get("data");
            img.setImageBitmap(bmp);
        }
    }
    void captureGPS() {
        //Toast.makeText(getApplicationContext(), "Init GPS", Toast.LENGTH_LONG).show();

        GPSMaps gps = new GPSMaps(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitud = gps.getLatitude();
            altitud = gps.getLongitude();

            // \n is for new line
            Toast.makeText(this, "Your Location is - \nLat: " + latitud + "\nLong: " + altitud, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    public void grabarYa(View v) {

        //codigo

        String urlString ="";
        captureGPS();


        //RegisterBarcode?usr=rfelix&activity=37&gpsLat="+ latitud +"&gpsAlti="+ altitud +"&barcode=RN026569386CO&strReceivedBy=pedro Garcia&intMotivo=0

        urlString = "http://190.102.134.78/COURIER_WCF/Xcourier.svc/rest/RegisterBarcode?usr=rfelix&activity=37&gpsLat="+ latitud +"&gpsAlti="+ altitud;
        urlString=urlString + "&barcode="+ codigo +"&strReceivedBy=pedroGarcia&intMotivo=0";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_LONG).show();

                if(statusCode==200)
                {
                    String str = new String(response);
                    str=str.replace("\"","");
                    Toast.makeText(getApplicationContext(), "Registro exitoso.. " + str, Toast.LENGTH_LONG).show();

                    irListaRemitos();

                }

            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: "+ statusCode + " "+throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("BookFinder", statusCode + " " + throwable.getMessage());
            }
        });

        
    }

    private void irListaRemitos() {
        Intent i = new Intent(this, ListaRemitosController.class);
        startActivity(i);
    }


}
