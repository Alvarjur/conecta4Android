package com.alvaroarmas.conecta4android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

class CountdownActivity : AppCompatActivity() {
    companion object {
        var countdownTime = 3
        var name1 = ""
        var name2 = ""
        fun updateTime(activity: Activity) {
            activity.runOnUiThread {
                val time = activity.findViewById<TextView>(R.id.countdown)
                time.text = countdownTime.toString()

                var nm1 = activity.findViewById<TextView>(R.id.name1)
                nm1.text = name1

                var nm2 = activity.findViewById<TextView>(R.id.name2)
                nm2.text = name2

            }

        }
        fun goGameActivity(activity: Activity) {
            // Cambiando de activity
            val intent = Intent(activity, GameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_countdown)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        WebSocketManager.countdownActivity = this

    }


}