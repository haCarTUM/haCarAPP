package tum.hacar.server

import android.util.Log
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.CloudBlobClient
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import org.json.JSONObject

import java.io.File
import java.net.MalformedURLException
import java.net.URL
import javax.xml.namespace.QName
import javax.xml.ws.Service

import tum.hacar.data.DriveSample
import tum.hacar.hacarapp.MainActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import tum.hacar.data.DrivingBlob
import tum.hacar.util.dateToString
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread


/**
 * demo client
 */
class ServerConnector(val parent: MainActivity) {

    // Define the connection-string with your values
    val storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=hacartumfeature9f6f;AccountKey=6GBND5H+oWbcGS21YPX6Rj0odXrm6PheymJWOkWbfopFlrpeFeBIvQ6DJzwSJXjcQ3wFNVtp0ruwZeG3m//Ddw==;EndpointSuffix=core.windows.net"

    val container: CloudBlobContainer;

    val gson: Gson;

    val queu : ConcurrentLinkedQueue<DrivingBlob> = ConcurrentLinkedQueue()

    init {
        // Retrieve storage account from connection-string.
        val storageAccount = CloudStorageAccount.parse(storageConnectionString)

        // Create the blob client.
        val blobClient = storageAccount.createCloudBlobClient()

        // Retrieve reference to a previously created container.
        container = blobClient.getContainerReference("main")

        // Configure GSON
        val gsonBuilder = GsonBuilder()
        gson = gsonBuilder.create()

        runThread()
    }



    fun sendDriveData(data: DrivingBlob) {
       queu.add(data)
    }

    fun runThread(){
        try {
            thread (start = true, priority = Thread.MAX_PRIORITY){

                while(true){
                    if(!queu.isEmpty()){

                        var data =  queu.poll()
                        // Create or overwrite the "myimage.jpg" blob with contents from a local file.
                        val blob = container.getBlockBlobReference("blob-" + data.id + "-" + dateToString(data.startTime) + ".json")

                        // Format to JSON
                        val json = gson.toJson(data)

                        blob.uploadText(json)

                        Log.e("Send", "Sent Data to server!")
                    }
                }



            }
        } catch (e: Exception) {
            // Output the stack trace.
            e.printStackTrace()
            parent.appendLog(e.stackTrace.toString())
        }
    }
}