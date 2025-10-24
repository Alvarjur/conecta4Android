package com.alvaroarmas.conecta4android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import java.net.URI

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // button = findViewById<Button>(R.id.connect_button)
    }

    fun goGame(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        WebSocketManager.username = findViewById<EditText>(R.id.client_name).text.toString()
        val uri = "ws://10.0.2.2:3000"

        WebSocketManager.connect(URI(uri))
        WebSocketManager.send("register", "hi")

        // Cambiando de activity
        startActivity(intent)

    }
}