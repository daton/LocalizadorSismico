package rodrigo.unitec.localizadorsismico

/**
 * Created by rapid on 06/12/2017.
 */
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

class Clasesita {

    internal fun metodo(ctx: Context) {
        val builder = AlertDialog.Builder(ctx)
        // Add the buttons
        builder.setPositiveButton("Ok") { dialog, id ->
            // User clicked OK button
        }
        builder.setNegativeButton("Cancelar") { dialog, id ->
            // User cancelled the dialog
        }
        // Set other dialog properties


        // Create the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }
}
