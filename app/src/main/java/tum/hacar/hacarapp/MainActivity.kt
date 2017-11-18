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
import tum.hacar.data.DriveSample
import tum.hacar.data.DrivingBlob
import java.sql.Blob

class MainActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout

    private var ID : Int = 1
    private var drivingBlobs = mutableListOf<DrivingBlob>()
    private var drivingWildness = 0;

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

    fun sendDataToServer(dataBlob: DrivingBlob){

    }

    fun appendDataToBlob(sample: DriveSample){
        if(drivingBlobs.isEmpty()){
            drivingBlobs.add(DrivingBlob(this.ID, this.drivingWildness, mutableListOf(sample) ))
        }else{if(drivingBlobs.last().driveSamples.size < 100){
            drivingBlobs.last().driveSamples.add(sample);
        }else{
            //Last Blob is finished
            sendDataToServer(drivingBlobs.last())

            //Create new blob
            drivingBlobs.add(DrivingBlob(this.ID, this.drivingWildness, mutableListOf(sample) ))
        }
        }

    }
}
