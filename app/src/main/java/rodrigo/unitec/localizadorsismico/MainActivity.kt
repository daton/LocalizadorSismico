package rodrigo.unitec.localizadorsismico

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.bind
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.widget.LoginButton
import com.facebook.login.LoginManager





class MainActivity : AppCompatActivity() {


    val callbackManager = CallbackManager.Factory.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        Toast.makeText(applicationContext,"Todo  bien", Toast.LENGTH_LONG).show()
                    }

                    override fun onCancel() {
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                    }
                })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(applicationContext,"Todos bien!!!", Toast.LENGTH_LONG).show()

        var i=Intent(applicationContext , MapsActivity::class.java)
        startActivity(i)


    }

    }

