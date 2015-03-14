package ar.com.mmlabs.kairos;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**Esta clase se encarga de realizar la tarea asincronica de realizar la consulta a la API.
 * Como corresponde, la clase desciende de AsyncTask a la cual se le definen en primer lugar
 * los 4 tipos genericos Params, Progress & Result. Luego se definen los 4 pasos con los que
 * cuenta: onPreExecute, doInBackground, onProgressUpdate & onPostExecute.
 * Created by Martin on 14/03/2015.
 */
public class ApiQueryTask extends AsyncTask<String, Integer, String> {

    private String cityName;
    private String country;
    private String main;
    private String description;
    Double temp;
    Double pressure;
    int humidity;

    private ApiQueryTaskListener mListener;

    public ApiQueryTask(ApiQueryTaskListener listener){
        mListener = listener;
    }

    @Override
    protected void onPreExecute(){
        /*En este caso, no ejecutamos nada en esta instancia*/
    }

    @Override
    protected String doInBackground(String... params) {

        String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=-34.6351795&lon=-58.4691528&units=metric&type=accurate&lang=es";
        String result = "";
        URL url;

        /*Cargo la URL como objeto java URL, que verifica la sintaxis*/
        try{
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            url = null;
        }

        /*Si la URL esta bien la abro y comienzo a leer las lineas de respuesta*/
        if( url != null ){

            InputStreamReader inputStreamReader = null;
            try{
                inputStreamReader = new InputStreamReader(url.openStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while( line != null){
                    result += line; //Voy guardando en el string resultado cada linea de respuesta
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*Ahora tengo supuestamente la respuesta de la API en vi string result, debo parsear
            * el JSON para obtener cada uno de los parametros*/
            if( !result.equals("")){

                /*Intento verificar si el string resultado es objeto JSON*/
                JSONObject jsonRoot;
                try {
                    jsonRoot = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    jsonRoot = null;
                }

                if( jsonRoot != null ){

                    try {
                        cityName = jsonRoot.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject sysObj = jsonRoot.getJSONObject("sys");
                        country = sysObj.getString("country");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject weatherObj = jsonRoot.getJSONObject("weather");
                        main = weatherObj.getString("main");
                        description = weatherObj.getString("description");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject mainObj = jsonRoot.getJSONObject("main");
                        temp = mainObj.getDouble("temp");
                        pressure = mainObj.getDouble("pressure");
                        humidity = mainObj.getInt("humidity");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
         }

        return result = "Las condiciones metereológicas para " + cityName + ", " +
                country + " son: Temperatura: " + temp.toString() + "ºC, Humedad: " +
                humidity + "%";
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        /*En este caso, no ejecutamos nada en esta instancia*/
    }

    @Override
    protected void onPostExecute(String string){
        mListener.onWeatherUpdated(string);
    }
}
