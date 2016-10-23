package es.samsoft.android.recoactsamsoft;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient api;
    private ProgressDialog progressDialog;

    private Button botonReg;
    private Button botonDesReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtenemos los botones
        botonReg=(Button)findViewById(R.id.bt_encender);
        botonDesReg=(Button)findViewById(R.id.bt_apagar);
        //creamos el progress dialog
        progressDialog = ProgressDialog.show(this, "Conectando con la api",
                "Espere por favor");
        //creamos la api
        api=new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //mostramos el dialogo y conectamos con la api
        progressDialog.show();
        api.connect();

    }

    public void registrarServicio(View view) {
        //necesitamos tener controlado si el servicio ya estaba registrado anteriormente
        //asi que usare un SharedPreferences para guardar su estado
        SharedPreferences preferences=getSharedPreferences("preferencias",MODE_PRIVATE);

        if(!preferences.getBoolean("servicioOn",false)) {
            //si el servicio NO estaba registrado lo registramos
            Intent intent = new Intent(this, ReconocimientoService.class);
            PendingIntent pendingIntent =
                    PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            ActivityRecognition.ActivityRecognitionApi
                    .requestActivityUpdates(api, 5000, pendingIntent);
            //subimos al shared prefs la variable de control
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("servicioOn",true);
            editor.apply();
        }
        else{
            //si ya estaba registrado anteriormente mostramos un mensaje al usuario
            Toast.makeText(this,"El servicio ya estaba registrado",Toast.LENGTH_LONG).show();
        }
    }


    public void desregistrarServicio(View view) {
        //obtenemos el shared preferences para preguntar si ya teniamos registrado el servicio
        SharedPreferences preferences=getSharedPreferences("preferencias",MODE_PRIVATE);

        if(preferences.getBoolean("servicioOn",false)) {
            //si estaba registrado lo detenemos
            Intent intent = new Intent(this, ReconocimientoService.class);
            PendingIntent pendingIntent =
                    PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                    api,
                    pendingIntent);
            //subimos al shared prefs la variable de control
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("servicioOn",false);
            editor.apply();
        }
        else{
            //si no avisamos al usuario
            Toast.makeText(this,"El servicio ya estaba detenido",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //si el dialogo se muestra lo cerramos
        if(progressDialog.isShowing())progressDialog.dismiss();
        //habilitamos los botones
        botonReg.setEnabled(true);
        botonDesReg.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //si el dialogo se muestra lo cerramos
        if(progressDialog.isShowing())progressDialog.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //si el dialogo se muestra lo cerramos
        if(progressDialog.isShowing())progressDialog.dismiss();
    }
}
