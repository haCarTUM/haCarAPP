package tum.hacar.hacarapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.LinearLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.themedNumberPicker
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

    private var started = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.mainLayout)

        this.title = "HaCarTUM - Safe driving saves Money"

        appendLog("App is starting!")

        layout.numberPicker.maxValue = 5
        layout.numberPicker.minValue = 1
        layout.numberPicker.wrapSelectorWheel = true

        layout.btn_startStop.onClick {
            started = !started

            if (started) {
                layout.btn_startStop.text = "Stop"
                layout.numberPicker.isEnabled = false
            } else {
                layout.btn_startStop.text = "Start"
                layout.numberPicker.isEnabled = true
            }
        }

        layout.textViewLogs.movementMethod = ScrollingMovementMethod()

        dataConnector.sendDriveData(DrivingBlob(3, 5, mutableListOf<DriveSample>(DriveSample(3f, 5f, 3f))))

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acceSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        gyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL)

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun appendLog(text: String) {
        layout.textViewLogs.append(dateToString(Date()) + ": " + text + "\n");
    }

    fun sendDataToServer(dataBlob: DrivingBlob) {
        if (started) {
            dataConnector.sendDriveData(dataBlob)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun appendDataToBlob(sample: DriveSample) {
        if (drivingBlobs.isEmpty()) {
            drivingBlobs.add(DrivingBlob(this.ID, this.drivingWildness, mutableListOf(sample)))
        } else {
            if (drivingBlobs.last().driveSamples.size < 500) {
                drivingBlobs.last().driveSamples.add(sample);
            } else {
                //Last Blob is finished

                drivingBlobs.last().endTime = Date()

                sendDataToServer(drivingBlobs.last())
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            createAcceSample(event.values[0], event.values[1], event.values[2])
        } else if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            createGyroSample(event.values[0], event.values[1], event.values[2])
        }
    }

    fun createAcceSample(acX: Float, acY: Float, acZ: Float) {
        textViewAcceleration.text = "Created acceleration sample with\n" + acX + "\n" + acY + "\n" + acZ
        appendDataToBlob(DriveSample(acceX = acX, acceY = acY, acceZ = acZ))
    }

    fun createGyroSample(gyX: Float, gyY: Float, gyZ: Float) {
        textViewGyro.text = "Created gyro sample with\n" + gyX + "\n" + gyY + "\n" + gyZ
        appendDataToBlob(DriveSample(gyroX = gyX, gyroY = gyY, gyroZ = gyZ))
    }
}
