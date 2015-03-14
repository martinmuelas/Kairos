package ar.com.mmlabs.kairos;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ApiQueryTaskListener {

    /*Declaro objeto GoogleApiClient, utilizado en onCreate*/
    private GoogleApiClient mGoogleApiClient;
    /*Declaro objeto de tipo Location para almacenar la ubicacion actual*/
    private Location mLastLocation;
    private TextView mLongitude;
    private TextView mLatitude;
    private TextView txt_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLongitude = (TextView) findViewById(R.id.txt_longitude);
        mLatitude = (TextView) findViewById(R.id.txt_latitude);
        txt_result = (TextView) findViewById(R.id.txt_result);

        /*Creo instancia de GoogleApiClient que permite acceder a los Google Play Services*/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API) // Se agrega la API que voy a utilizar
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        /*Conectado a Google Play Services!
        * Ingreso a esta instancia si la conexion mGoogleApiClient.connect() fue exitosa
        * */
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null) {
            mLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
            mLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
        }


     }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("onConnectionFailed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickDescargar(View view){
        ApiQueryTask task = new ApiQueryTask(this);
        task.execute();
    }

    /*Método creado como consecuencia de implementar la interfaz ApiQueryTaskListener*/
    @Override
    public void onWeatherUpdated(String string) {
        txt_result.setText(string);
    }
}
