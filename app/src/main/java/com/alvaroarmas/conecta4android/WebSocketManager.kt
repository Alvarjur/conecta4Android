package com.alvaroarmas.conecta4android

import android.R
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlinx.serialization.json.*

object WebSocketManager {
    lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private var webSocketClient: WebSocketClient? = null

    // LiveData to notify activities
    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> get() = _messages

    var username = "KotlinClient"

    fun connect(uri: URI) {
        if (webSocketClient != null) return // already connected

        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("WebSocket", "Connected")
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
                                // Aceptando el challenge
                                println("Entro en sendStartMatch")
                                val matchJson = buildJsonObject {
                                    put("type", "startMatch")
                                    put("player_1", challenger)
                                    put("player_2", username)
                                }
                                send(Json.encodeToString(matchJson))
                            }

                            if(type.equals("confirmedGame")) {
                                Thread.sleep(3000)
                                RegisterActivity.goGameActivity(appContext)
                            }

                            if(type.equals("drawOrder")) {
                                // Log.d("CONNECTION", "drawOrder")

                                var gridStr = jsonObject["grid"]?.jsonArray
                                gridStr.let { array ->
                                    if (array != null) {
                                        // Actualizando la grid
                                        var resultGrid = mutableListOf<String>()
                                        for (element in array) {
                                            // Log.d("GRID", GameActivity.grid.toString())
                                            resultGrid.add(element.toString())

                                        }
                                        GameActivity.grid = resultGrid
                                        GameActivity.updateGrid()


                                        // Cambiando el turno al correcto
                                        var current_turn = jsonElement["turn"]?.jsonPrimitive?.contentOrNull.toString()

                                        GameActivity.curPlayer = current_turn.toInt()
                                        // Log.d("CURRENT_PLAYER", GameActivity.curPlayer.toString())
                                    }
                                }
                                // Log.d("CONNECTION", gridStr.toString())
                                var winner = jsonElement["winner"]
                                GameActivity.winner = winner.toString()
                                Log.d("WINNER", GameActivity.winner)

                            }


                        }
                    }
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d("WebSocket", "Closed: $reason")
            }

            override fun onError(ex: Exception?) {
                Log.e("WebSocket", "Error: ${ex?.message}", ex)
            }
        }

        webSocketClient?.connect()
    }

    fun sendMessage(message: String) {
        webSocketClient?.send(message)
    }

    fun sendAddChipMessage(col: Int) {
        send("kotlinAddChip", col.toString())
    }

    fun send(type: String, message: String) {
        var jsonObject = buildJsonObject {
            put("type", type)
            put("clientName", username)
            put("message", message)
        }
        webSocketClient?.send(Json.encodeToString(jsonObject))
    }

    fun disconnect() {
        webSocketClient?.close()
        webSocketClient = null
    }
}
