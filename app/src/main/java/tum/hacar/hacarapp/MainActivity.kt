package tum.hacar.hacarapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import tum.hacar.data.DriveSample
import tum.hacar.data.DrivingBlob
import tum.hacar.server.ServerConnector
import tum.hacar.util.dateToString
import java.sql.Blob


class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var layout: LinearLayout

    private lateinit var mSensorManager: SensorManager
    private lateinit var acceSensor: Sensor
    private lateinit var gyroSensor: Sensor

    private var ID: Int = 1
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

        dataConnector.sendDriveData(DrivingBlob(3, 5, mutableListOf<DriveSample>(DriveSample(3f, 5f, 3f))))

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acceSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        gyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                10)

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                10)
    }

    fun appendLog(text: String) {

        layout.textViewLogs.append(dateToString(Date()) + ": " + text + "\n");
    }

    fun sendDataToServer(dataBlob: DrivingBlob) {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun appendDataToBlob(sample: DriveSample) {
        if (drivingBlobs.isEmpty()) {
            drivingBlobs.add(DrivingBlob(this.ID, this.drivingWildness, mutableListOf(sample)))
        } else {
            if (drivingBlobs.last().driveSamples.size < 100) {
                drivingBlobs.last().driveSamples.add(sample);
            } else {
                //Last Blob is finished

                drivingBlobs.last().endTime = Date()

                sendDataToServer(drivingBlobs.last())
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION && event.values.max()!! > 0.1F) {
            createAcceSample(event.values[0], event.values[1], event.values[2])
        } else if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR && event.values.average() >= 0.2F) {
            createGyroSample(event.values[0], event.values[1], event.values[2])
        }
    }

    fun createAcceSample(acX: Float, acY: Float, acZ: Float) {
        textViewAcceleration.text = "Created acceleration sample with " + acX + " " + acY + " " + acZ
        appendDataToBlob(DriveSample(acceX = acX, acceY = acY, acceZ = acZ))
    }

    fun createGyroSample(gyX: Float, gyY: Float, gyZ: Float) {
        textViewGyro.text = "Created gyro sample with " + gyX + " " + gyY + " " + gyZ
        appendDataToBlob(DriveSample(gyroX = gyX, gyroY = gyY, gyroZ = gyZ))
    }
}
