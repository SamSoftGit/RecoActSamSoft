package es.samsoft.android.recoactsamsoft;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;


public class ReconocimientoService extends IntentService {

    public ReconocimientoService() {
        super("ReconocimientoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            comprobarActividades( result.getProbableActivities() );
        }
    }

    private void comprobarActividades(List<DetectedActivity> actividadesProvables){
        //recorremos la lista de actividades
        for( DetectedActivity activity : actividadesProvables) {
            //preguntamos por el tipo
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    //preguntamos por la provabilidad de que sea esa actividad
                    //si es mayor de 75 (de cien) mostramos un mensaje
                    if(activity.getConfidence()>75){
                        Log.i("samsoftRecAct","En vehiculo");
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    if(activity.getConfidence()>75){
                        Log.i("samsoftRecAct","En bici");
                    }
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    //este lo dejamos vacio por que va implicito en correr y andar
                    break;
                }
                case DetectedActivity.RUNNING: {
                    if(activity.getConfidence()>75){
                        Log.i("samsoftRecAct","Corriendo");
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    if(activity.getConfidence()>75){
                        Log.i("samsoftRecAct","Andando");
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    if(activity.getConfidence()>75){
                        Log.i("samsoftRecAct","Quieto");
                    }
                    break;
                }
                case DetectedActivity.TILTING: {
                    if(activity.getConfidence()>75){
                        Log.i("samsoftRecAct","Tumbado");
                    }
                    break;
                }

                case DetectedActivity.UNKNOWN: {
                    //si es desconocida no decimos nada
                    break;
                }
            }
        }
    }

}
