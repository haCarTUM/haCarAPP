package tum.hacar.hacarapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import tum.hacar.hacarapp.R.id.textViewLogs
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.mainLayout)

        this.title = "HaCarTUM - Safe driving saves Money"

        appendLog("App is starting!")

        layout.numberPicker.maxValue = 5
        layout.numberPicker.minValue = 1
        layout.numberPicker.wrapSelectorWheel = true

    }

    fun appendLog(text: String) {

        val sdf = SimpleDateFormat("HH:mm:ss")
        val dateString = sdf.format(Date())

        layout.textViewLogs.append(dateString + ": " + text + "\n");
    }

    fun sendDataToServer(data : String){

    }
}
