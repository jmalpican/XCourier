package com.webin.xcourier;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Jonathan Malpica on 3/29/2015.
 */
public class ListaRemitosController extends ActionBarActivity {

    private ImageButton btnImgEncender;
    private ImageButton btnImgApagado;
    private EditText txtBuscar;
    private TextView lblUser;
    private TextView lblFecHorIniValue;
    private TextView lblFecHorFinValue;
    private GridView tblRemitos;

    private Resources res;


    private String[] remitos;
    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_remitos_view);

        //inicializar variables
        iniVariables();
        cargarLstRemitos();

        lblUser.setText("Felix Ricardo");
    }

    private void cargarLstRemitos() {

        //http://190.102.134.78/COURIER_WCF/Xcourier.svc/rest/ObtenerEnvio?usr=rfelix
        String urlString = "http://190.102.134.78/COURIER_WCF/Xcourier.svc/rest/ObtenerEnvio?usr=rfelix";


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_LONG).show();

                if(statusCode==200)
                {

                    try {
                        populateList(new String(response));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: "+ statusCode + " "+throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("xCourier fallo", statusCode + " " + throwable.getMessage());
            }
        });



    }

    private void populateList(String json) throws JSONException {
        // TODO Auto-generated method stub
        JSONArray the_json_array =new JSONArray(json);
        int size = the_json_array.length();
        list=new ArrayList<HashMap<String,String>>();
        HashMap<String,String> temp;


        for (int i = 0; i < size; i++) {

            if (i==0){
                temp=new HashMap<String, String>();
                temp.put("First", "Nro.");
                temp.put("Second", "Nombre");
                temp.put("Third", "Estado");
                temp.put("Fourth", "1");
                temp.put("Cinco", "1");
                list.add(temp);
            }

            temp=new HashMap<String, String>();
            JSONObject objRemito = the_json_array.getJSONObject(i);
            int j = i+1;
            temp.put("First", j+"");
            temp.put("Second", objRemito.getString("srtRecipientName"));
            temp.put("Third", "Por entregar");
            temp.put("Fourth", "2");
            temp.put("Cinco", "2");
            list.add(temp);
        }

        ListViewAdapter adapter=new ListViewAdapter(this, list);
        tblRemitos.setAdapter(adapter);


    }

    private void iniVariables() {

        res = getResources();

//        btnImgEncender = (ImageButton)findViewById(R.id.btnImgEncender);
//        btnImgApagado = (ImageButton)findViewById(R.id.btnImgApagado);
        txtBuscar = (EditText)findViewById(R.id.txtBuscar);
        lblUser = (TextView)findViewById(R.id.lblUser);
//        lblFecHorIniValue = (TextView)findViewById(R.id.lblFecHorIniValue);
//        lblFecHorFinValue = (TextView)findViewById(R.id.lblFecHorFinValue);
        tblRemitos = (GridView)findViewById(R.id.tblRemitos);

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

    public void irDetalleRemito(View view){

        String urlString = "http://190.102.134.78/COURIER_WCF/Xcourier.svc/rest/getRemito?barcode=";


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString+txtBuscar.getText().toString().toUpperCase(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_LONG).show();

                if(statusCode==200)
                {

                    try {
                        populateList(new String(response));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: "+ statusCode + " "+throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("xCourier fallo", statusCode + " " + throwable.getMessage());
            }
        });

    }


}