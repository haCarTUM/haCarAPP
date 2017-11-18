package tum.hacar.data

import java.util.*

/**
 * Created by f.baader on 18.11.2017.
 */
data class DrivingBlob(

        var id: Int,

        var wildness : Int,

        var driveSamples: MutableList<DriveSample>
) {

    var startTime : Date = Date()
    var endTime: Date = Date()
}