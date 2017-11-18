package tum.hacar.data

/**
 * Created by f.baader on 18.11.2017.
 */
data class DriveSample(
        var acceX: Float = 0.0F,
        var acceY: Float = 0.0F,
        var acceZ: Float = 0.0F,

        var gyroX: Float = 0.0F,
        var gyroY: Float = 0.0F,
        var gyroZ: Float = 0.0F
)