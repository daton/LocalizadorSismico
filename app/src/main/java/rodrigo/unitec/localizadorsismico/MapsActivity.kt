package rodrigo.unitec.localizadorsismico

import android.annotation.SuppressLint
import android.location.Location
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import com.google.android.gms.location.LocationServices
import android.R.string.cancel
import android.content.DialogInterface
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * El siguiente provee el punto de entrada para google play services
     */
    protected var mGoogleApiClient: GoogleApiClient? = null
    /**
     * Representa una localizacion geografica.
     */
    protected var mLastLocation: Location? = null

    protected var mLatitudeLabel: String? = null
    protected var mLongitudeLabel: String? = null

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (mLastLocation != null) {
            Toast.makeText(this,
                    "latitud:" + mLastLocation?.getLatitude(), Toast.LENGTH_LONG).show()
            Toast.makeText(this, "Longitud" + mLastLocation?.getLongitude(), Toast.LENGTH_LONG).show()

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(mLastLocation?.getLatitude()!!, mLastLocation?.getLongitude()!!)

            mMap.addMarker(MarkerOptions().position(sydney).title("Latitud:" + mLastLocation?.getLatitude() + " Long:" + mLastLocation?.getLongitude()))
            mMap.moveCamera(CameraUpdateFactory.zoomTo(18f))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        } else {
            Toast.makeText(this, "Localizacion no detectada", Toast.LENGTH_LONG).show()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        print("Connecccion suspendida");
        mGoogleApiClient?.connect();
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        print( "La conexion falló: el error es  = " + p0.getErrorCode());
    }

    private lateinit var mMap: GoogleMap
  public  var sismito:Sismo?=null

    override fun onStart() {
        super.onStart()

        TareaSismos().execute()
        mGoogleApiClient?.connect();


    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient?.isConnected()!!) {
            mGoogleApiClient?.disconnect()!!
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        buildGoogleApiClient();





    }



    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(19.556665,-99.0228007)
        mMap.addMarker(MarkerOptions().position(sydney).title("UNITEC, Ecatepec"))

        //Comentamos los dos siguientes renglones para que no compitan con el de obtener la geolocalización:
      //  mMap.moveCamera(CameraUpdateFactory.zoomTo(3.5f))
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


      //  val tarea=TareaSismos()
       // tarea.execute(null,null,null);


    }


    inner  class TareaSismos : AsyncTask<Void, Void, Sismo>() {

        override fun doInBackground(vararg params: Void): Sismo? {
            try {
                val url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-11-21&endtime=2017-11-22&minlatitude=10&minlongitude=-120&maxlatitude=90&maxlongitude=90"
                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                var sismo=restTemplate.getForObject(url, Sismo::class.java)
                println("DESPUES DE REST:"+sismo.type)

                sismito=sismo;
                return sismo
            } catch (e: Exception) {
                Log.e("ALGO MALOOOOO", e.message, e)
            }
            return null
        }



         override fun onPostExecute(sismo: Sismo?) {

            println("Magnitud del primer sismo:"+sismo?.features?.get(0)?.properties?.mag);
          //   Toast.makeText(applicationContext,"Magnitud "+sismito?.features?.get(0)?.properties?.mag, Toast.LENGTH_LONG).show()
             val lat=sismo?.features?.get(0)?.geometry?.coordinates?.get(1);
             val lon=sismo?.features?.get(0)?.geometry?.coordinates?.get(0);
             val sydney = LatLng(lat!!, lon!!)

             mMap.addMarker(MarkerOptions().position(sydney).title("Magnitud:"+sismito?.features?.get(0)?.properties?.mag))
             Toast.makeText(applicationContext,"Sismos en este día: "+sismito?.metadata?.count,Toast.LENGTH_LONG ).show()

        }
    }



}

