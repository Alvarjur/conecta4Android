package com.alvaroarmas.conecta4android

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TableRow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import android.util.Log

class ViewClients : AppCompatActivity() {
    companion object {
        var clients = ""

        fun updateLayout(activity: Activity, clients:String) {
            var tableLayout = activity.findViewById<TableLayout>(R.id.table_layout)
            tableLayout.removeAllViews() // Limpia antes si quieres refrescar

            for (item in clients.split(",")) {
                Log.d("CLIENT", item)
                val fila = TableRow(activity).apply {
                    layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                }

                val texto = TextView(activity).apply {
                    text = item
                    setPadding(16, 8, 16, 8)
                }

                val boton1 = Button(activity).apply {
                    text = "Challenge"
                    setOnClickListener {
                        Toast.makeText(activity, "Acción 1: $item", Toast.LENGTH_SHORT).show()
                    }
                }



                fila.addView(texto)
                fila.addView(boton1)


                tableLayout.addView(fila)
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
