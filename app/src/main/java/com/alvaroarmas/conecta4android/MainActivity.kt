package com.alvaroarmas.conecta4android

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TableLayout
import android.widget.TableRow
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.Handshakedata
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.java_websocket.WebSocket
import kotlinx.serialization.json.*


class MainActivity : AppCompatActivity() {
    var curPlayer = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        connectaWS()
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        val numRows = 6
        val numCols = 7
        var listFields = ArrayList<ArrayList<Field>>()


        // Creando los botones para añadir las fichas
        val row = TableRow(this)
        row.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        for(i in 0 until numCols)
        {
            var btn = ImageButton(this)
            btn.apply {
                layoutParams =  TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
                )
                setImageResource(R.drawable.chip_button)
            }

            btn.setOnClickListener {
                for(j in numCols - 2 downTo 0) {
                    if(listFields.get(j).get(i).player == 0) {
                        listFields.get(j).get(i).player = curPlayer
                        listFields.get(j).get(i).updateIv()
                        switchPlayer()
                        break

                    }
                }
            }
            row.addView(btn)
        }
        tableLayout.addView(row)

        // Creando la matriz de fichas


        for(i in 0 until numRows)
        {
            val row = TableRow(this)

            row.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            var row_fields = ArrayList<Field>()
            for(j in 0 until numCols) {
                var iv = ImageView(this)
                iv.apply { layoutParams =  TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f
                )}


                var currentField = Field(i, j, 0, iv)
                row_fields.add(currentField)
                currentField.updateIv()
                row.addView(currentField.iv)
            }

            listFields.add(row_fields)
            tableLayout.addView(row)

        }


    }
    fun switchPlayer()
    {
        if (curPlayer == 1)
        {
            curPlayer = 2
        }
        else
        {
            curPlayer = 1
        }
    }

    class MyWebSocketClient(serverUri: URI) : WebSocketClient(serverUri) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            Log.d("CONNECTION", "Connected")

            val message = ChatMessage("register", "KotlinClient", "Hi")
            val json = Json.encodeToString(message)

            send(Json.encodeToString(message))

        }

        override fun onMessage(message: String?) {
            Log.d("CONNECTION", "Message received: " + message)
            message?.isEmpty()?.let {
                if(!it) {
                    val jsonElement = Json.parseToJsonElement(message)
                    if (jsonElement is JsonObject) {
                        val jsonObject = jsonElement
                        var type = jsonObject["type"]?.jsonPrimitive?.contentOrNull

                        // Aquí se harán cosas dependiendo del tipo de mensaje que llegue
                        if (type.equals("challenge")) {
                            var challenger = jsonObject["challenger"]?.jsonPrimitive?.contentOrNull
                            Log.d("CONNECTION", "Challenger: " + challenger)
                        }


                    }
                }
            }
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            Log.d("CONNECTION", "Closed connection")
        }

        override fun onError(ex: Exception?) {
            Log.d("CONNECTION", "Error")
        }
    }

    fun connectaWS() {
        val uri = URI("ws://10.0.2.2:3000")
        var wsclient = MyWebSocketClient(uri)
        wsclient.connect()
    }

    @Serializable
    data class ChatMessage(
        val type: String,
        val clientName: String,
        val message: String
    )

    fun sendJsonMessage(webSocket: WebSocket) {
        val chatMessage = ChatMessage(
            type = "register",
            clientName = "KotlinClient",
            message = "Hello from Kotlin!"
        )

        val jsonString = Json.encodeToString(chatMessage)
        webSocket.send(jsonString)
    }
}

class Field(val row: Int, val col: Int, var player: Int, var iv: ImageView)
{
    fun updateIv() {
        if (player == 0) {
            iv.setImageResource(R.drawable.chip_blank)
        }
        if (player == 1) {
            iv.setImageResource(R.drawable.chip_red)
        }
        if(player == 2) {
            iv.setImageResource(R.drawable.chip_yellow)
        }
    }
}