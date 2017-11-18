package tum.hacar.data

/**
 * Created by f.baader on 18.11.2017.
 */
data class DrivingBlob(

        var id: Int,

        var wildness : Int,

        var driveSamples: MutableList<DriveSample>
) {
}