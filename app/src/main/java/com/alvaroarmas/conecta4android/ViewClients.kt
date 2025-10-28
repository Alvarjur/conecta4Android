package com.alvaroarmas.conecta4android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.serialization.json.buildJsonObject
import org.json.JSONObject


class ViewClients : AppCompatActivity() {
    companion object {
        var clients = ""

        fun goCountdownActivity(context: Context) {
            // Cambiando de activity
            val intent = Intent(context, CountdownActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        fun showChallenge(activity: Activity, challenger:String) {
            activity.runOnUiThread {
                AlertDialog.Builder(activity)
                    .setTitle("Challenged!")
                    .setMessage("Accept challenge by $challenger?")
                    .setPositiveButton("Yes") { _, _ ->
                        WebSocketManager.sendAcceptChallenge(challenger)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        WebSocketManager.sendRefusedChallenge(challenger)
                    }
                    .show()
            }


        }
        fun updateLayout(activity: Activity, clients:String) {
            Handler(Looper.getMainLooper()).post {
                var tableLayout = activity.findViewById<TableLayout>(R.id.table_layout)
                tableLayout.removeAllViews() // Limpia antes si quieres refrescar

                for (item in clients.split(",")) {
                    var newItem = item.replace("\"", "")
                    if (newItem != "") {
                        if (newItem != WebSocketManager.username) {
                            Log.d("CLIENT", newItem)
                            val fila = TableRow(activity).apply {
                                layoutParams = TableRow.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                                )
                            }

                            val texto = TextView(activity).apply {
                                text = newItem
                                setPadding(16, 8, 16, 8)
                            }

                            val boton1 = Button(activity).apply {
                                text = "Challenge"
                                setOnClickListener {

                                    WebSocketManager.sendChallenge(newItem)
                                    GameActivity.player = 1
                                }
                            }



                            fila.addView(texto)
                            fila.addView(boton1)


                            tableLayout.addView(fila)
                        }
                    }
                }
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WebSocketManager.viewClientsActivity = this

        setContentView(R.layout.activity_view_clients)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var tblLayout = findViewById<TableLayout>(R.id.table_layout)
        tblLayout.updateLayout(this, clients)

    }


}
