package rodrigo.unitec.localizadorsismico

import android.content.Context
import android.provider.Settings
import android.util.Log

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

/**
 * Created by rapid on 01/12/2017.
 */

class Probar {
    internal var mMap: GoogleMap? = null
    fun ejercicio(ctx: Context) {

        mMap!!.setOnMapClickListener { arg0 ->
            // TODO Auto-generated method stub
            Log.d("arg0", arg0.latitude.toString() + "-" + arg0.longitude)
        }

    }
}
