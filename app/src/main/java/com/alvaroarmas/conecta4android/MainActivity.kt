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