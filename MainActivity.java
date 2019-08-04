package com.xcheko51x.changelog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String URL_SERVIDOR = "http://192.168.0.11/PruebasCanal/VerifcaCambios.php";

    TextView tvCambios;

    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCambios = findViewById(R.id.tvCambios);

        verificaCambios();
    }

    public void verificaCambios() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);
        version = preferences.getString("version", "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SERVIDOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if ((version == "") || (version.equals(response.trim()) == false)) {
                            if(version == "") {
                                tvCambios.setText("NO EXISTE VERSIÓN");
                            } else if(version.equals(response.trim()) == false) {
                                tvCambios.setText("HAY UNA NUEVA VERSION\n" + "Tu: " + version +"\nActual: " + response);
                            }

                            AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                            dialogo.setTitle("ACTUALIZACIÓN");
                            dialogo.setMessage("Hay una nueva actualización");
                            dialogo.setCancelable(true);
                            dialogo.setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences.Editor editor = getSharedPreferences("PREFERENCIAS", MODE_PRIVATE).edit();
                                    editor.putString("version", response.trim());

                                    editor.commit();

                                    Toast.makeText(MainActivity.this, "SE ACTUALIZO LA APLICACIÓN", Toast.LENGTH_LONG).show();

                                }
                            });

                            dialogo.show();

                        } else if(version.equals(response) == true) {
                            tvCambios.setText("ESTA ACTUALIZADO\n" + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvCambios.setText("ERROR");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("versionApp", version.trim());

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
