package rodrigo.unitec.localizadorsismico

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onStart() {
        super.onStart()
        TareaSismos().execute()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14.0f))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
      //  val tarea=TareaSismos()
       // tarea.execute(null,null,null);
    }
    internal class TareaSismos : AsyncTask<Void, Void, Sismo>() {

        override fun doInBackground(vararg params: Void): Sismo? {
            try {
                val url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-11-21&endtime=2017-11-22&minlatitude=10&minlongitude=-120&maxlatitude=90&maxlongitude=90"
                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                var sismo=restTemplate.getForObject(url, Sismo::class.java)
                println("DESPUES DE REST:"+sismo.type)
                return sismo
            } catch (e: Exception) {
                Log.e("ALGO MALOOOOO", e.message, e)
            }

            return null
        }

        override fun onPostExecute(sismo: Sismo?) {

            println("Magnitud del primer sismo:"+sismo?.features?.get(0)?.properties?.mag);


        }
    }
}

