package com.alvaroarmas.conecta4android

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.java_websocket.WebSocket
import java.net.URI
import androidx.lifecycle.Observer
import java.util.concurrent.ExecutorService


class GameActivity : AppCompatActivity() {

    companion object {
        var grid = mutableListOf<String>()
        var listFields = ArrayList<ArrayList<Field>>()
        var player = 2
        var curPlayer = 1
        var winner = "none"

        fun updateGrid() {
            for(chip in grid) {
                var parts = chip.replace("\"", "").replace("[", "").replace("]", "").split(" ")
                Log.d("PARTS", parts.toString())
                var field = listFields[parts[0].toInt()][parts[1].toInt()]
                field.player = parts[2].toInt()
                field.updateIv()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // Observe LiveData from WebSocketManager
        WebSocketManager.messages.observe(this, Observer { msg ->
            Log.d("WSM", msg)
        })
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        val numRows = 6
        val numCols = 7



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
                    if(winner == "\"none\"") {
                        if (curPlayer == player) {
                            if (listFields.get(j).get(i).player == 0) {
                                WebSocketManager.sendAddChipMessage(i)
                                listFields.get(j).get(i).player = curPlayer
                                listFields.get(j).get(i).updateIv()

                                break
                            }

                        }
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
                iv.setImageResource(R.drawable.chip_blank)
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

        fun updateGrid() {
            for(chip in grid) {
                var parts = chip.split(" ")
                Log.d("PARTS", parts.toString())

            }
        }
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