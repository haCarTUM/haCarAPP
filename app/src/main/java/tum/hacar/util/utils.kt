package tum.hacar.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by f.baader on 18.11.2017.
 */


fun dateToString(date: Date) : String{

    val sdf = SimpleDateFormat("HH:mm:ss")
    return sdf.format(Date())
}