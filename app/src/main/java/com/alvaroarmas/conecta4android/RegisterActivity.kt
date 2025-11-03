package com.alvaroarmas.conecta4android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import java.net.URI

class RegisterActivity : AppCompatActivity() {

    companion object {
        fun goGameActivity(context: Context) {
            // Cambiando de activity

                val intent = Intent(context, GameActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

        }

        fun goViewClients(context: Context) {
            val intent = Intent(context, ViewClients::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun showNameNotAvlb(activity: Activity) {
            activity.runOnUiThread {
                AlertDialog.Builder(activity)
                    .setTitle("That name is not available!")
                    .setPositiveButton("Accept") { _, _ ->

                    }
                    .show()
            }
            WebSocketManager.disconnect()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        WebSocketManager.init(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        WebSocketManager.registerActivity = this


        // button = findViewById<Button>(R.id.connect_button)
    }

    fun goGame(view: View) {

        WebSocketManager.username = findViewById<EditText>(R.id.client_name).text.toString()
        var uri = findViewById<EditText>(R.id.connection_url).text.toString()
        var port = findViewById<EditText>(R.id.port).text.toString()
        var protocol = findViewById<EditText>(R.id.protocol).text.toString()
        var finalUri = ""
        if (protocol == "") {
            finalUri += "ws://"
        } else {
            finalUri += "$protocol://"
        }
        if (uri == "localhost") {
            finalUri += "10.0.2.2"
        } else if (uri == "") {
            finalUri += "10.0.2.2"
        } else {
            finalUri += uri
        }
        if (port == "") {
            finalUri += ":3000"
        } else {
            finalUri += ":$port"
        }
        // val uriTest = "ws://10.0.2.2:3000"
        Log.d("CONNECTION", finalUri)
        // Log.d("CONNECTION", uriTest)
        WebSocketManager.connect(URI(finalUri)) {
            Log.d("CONNECTION", "SENDING REGISTER MESSAGE")
            WebSocketManager.send("register", "hi")
            Log.d("CONNECTION", "Register message sent")

            //val intent = Intent(this, ViewClients::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //this.startActivity(intent)
        }




    }

    fun setLocal(view: View) {
        Handler(Looper.getMainLooper()).post {
            var uri = findViewById<EditText>(R.id.connection_url)
            var port = findViewById<EditText>(R.id.port)
            var protocol = findViewById<EditText>(R.id.protocol)

            uri.setText("localhost")
            port.setText("3000")
            protocol.setText("ws")
        }
    }

    fun setProxmox(view: View) {
        Handler(Looper.getMainLooper()).post {
            var uri = findViewById<EditText>(R.id.connection_url)
            var port = findViewById<EditText>(R.id.port)
            var protocol = findViewById<EditText>(R.id.protocol)

            uri.setText("rbellidonavarro.ieti.site")
            port.setText("443")
            protocol.setText("wss")
        }
    }


}