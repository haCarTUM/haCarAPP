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
import tum.hacar.server.ServerConnector
import tum.hacar.util.dateToString
import java.sql.Blob

class MainActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout

    private var ID : Int = 1
    private var drivingBlobs = mutableListOf<DrivingBlob>()
    private var drivingWildness = 0;
    private var dataConnector = ServerConnector(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.mainLayout)

        this.title = "HaCarTUM - Safe driving saves Money"

        appendLog("App is starting!")

        layout.numberPicker.maxValue = 5
        layout.numberPicker.minValue = 1
        layout.numberPicker.wrapSelectorWheel = true

        dataConnector.sendDriveData(DrivingBlob(3, 5, mutableListOf<DriveSample>(DriveSample(3f,5f,3f))))

    }

    fun appendLog(text: String) {

        layout.textViewLogs.append(dateToString(Date()) + ": " + text + "\n");
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

            drivingBlobs.last().endTime = Date()

            sendDataToServer(drivingBlobs.last())

            //Create new blob
            drivingBlobs.add(DrivingBlob(this.ID, this.drivingWildness, mutableListOf(sample) ))
        }
        }

    }
}
