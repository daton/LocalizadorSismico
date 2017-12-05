package rodrigo.unitec.localizadorsismico

import android.Manifest
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
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.provider.Settings.Secure;
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.security.AccessController.getContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    /**
     * El siguiente provee el punto de entrada para google play services
     */
    protected var mGoogleApiClient: GoogleApiClient? = null
    /**
     * Representa una localizacion geografica.
     */
    protected var mLastLocation: Location? = null
  var lati:Double?=null;
    var longi:Double?=null;
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    protected var mLatitudeLabel: String? = null
    protected var mLongitudeLabel: String? = null
    private lateinit var mMap: GoogleMap
    public  var sismito:Sismo?=null
    public var climita:Clima?=null;
    var temper:Float?=null;





    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)




        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient?.getLastLocation()
                ?.addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Toast.makeText(applicationContext,"Localización "+location.latitude, Toast.LENGTH_LONG).show();

                        val aqui = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(MarkerOptions().position(aqui).title("Aqui estas"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(aqui))
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(16f))

                    }
                }
/******************************************************************************************************************************
  ESte es el id de android que es unico para cada celular!!

**********************************************************************************************************************************/
        val android_id = Settings.Secure.getString(applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID)

        Toast.makeText(applicationContext,android_id,Toast.LENGTH_LONG).show();





    }


    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }


    @SuppressWarnings("MissingPermission")
    private fun  getLastLocation(){
        mFusedLocationClient?.lastLocation?.addOnCompleteListener(OnCompleteListener<Location> {
            @Override fun onComplete(@NonNull task: Task<Location>){
                if(task.isSuccessful && task.result!=null){
                    mLastLocation=task.result;
                    Toast.makeText(applicationContext,"la ultima localización es "+mLastLocation?.latitude, Toast.LENGTH_LONG).show();
                }
            }
        })
    }


    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {




        } else {

            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.

            }
        }
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
        mMap!!.setOnMapClickListener { arg0 ->


                // aqui iria  la conexion a la base de datos junto con el numero de id del celular
                Toast.makeText(applicationContext, "lat  " + arg0.latitude.toString() + " lon " + arg0.longitude, Toast.LENGTH_LONG).show()
lati= arg0.latitude
                longi = arg0.longitude

            println("COMO VARIABLE LOCAL "+     lati);


                TareaClima().execute(null, null, null)

            //Caja
            val dlgAlert = AlertDialog.Builder(this)
            dlgAlert.setMessage("Temperatura de esta zona:"+temper+". Quieres guardar esta estacion meteorológica que has cliqueado?")
            dlgAlert.setTitle("Guardar localizacion")
            dlgAlert.setPositiveButton("Guardar", null)
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()



        }


    }



    inner  class TareaSismos : AsyncTask<Void, Void, Sismo>() {

        override fun doInBackground(vararg params: Void): Sismo? {
            try {
                val url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-11-27&endtime=2017-11-28&minlatitude=10&minlongitude=-120&maxlatitude=90&maxlongitude=90"
                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                var sismo=restTemplate.getForObject(url, Sismo::class.java)
                println("DESPUES DE REST:"+sismo.type)


                var url2="http://api.openweathermap.org/data/2.5/weather?lat="+mLastLocation?.latitude+"&lon="+mLastLocation?.longitude+"&APPID=807b993a5243387b613f8c3038571e74"
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())



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

    inner  class TareaClima : AsyncTask<Void, Void, Clima>() {


        override fun doInBackground(vararg params: Void): Clima? {
            try {



                var url2="http://api.openweathermap.org/data/2.5/weather?lat="+ lati+"&lon="+longi+"&APPID=807b993a5243387b613f8c3038571e74"
                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                var clima=restTemplate.getForObject(url2, Clima::class.java)
                println("DESPUES DE REST:"+clima.id)



                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


                climita=clima;
                return clima
            } catch (e: Exception) {
                Log.e("ALGO MALOOOOO clima", e.message, e)
            }
            return null
        }



        override fun onPostExecute(clima: Clima?) {

            println("Estacion mas cercana :"+clima?.name);
            //   Toast.makeText(applicationContext,"Magnitud "+sismito?.features?.get(0)?.properties?.mag, Toast.LENGTH_LONG).show()
           //aqui van datos de longgitud val lat=clima?.features?.get(0)?.geometry?.coordinates?.get(1);
           //aqui van datos latitud val lon=clima?.features?.get(0)?.geometry?.coordinates?.get(0);

 temper=climita?.main?.temp;
            temper=temper?.minus(273);
            //mMap.addMarker(MarkerOptions().position(sydney).title("Magnitud:"+sismito?.features?.get(0)?.properties?.mag))
            Toast.makeText(applicationContext,"Estacion mas cercana esta en: "+climita?.name+ " Temperatura "+temper,Toast.LENGTH_LONG).show()

        }
    }



}

