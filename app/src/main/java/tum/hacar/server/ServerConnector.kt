package tum.hacar.server

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


/**
 * demo client
 */
class ServerConnector (val parent: MainActivity) {

    // Define the connection-string with your values
 val storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=hacartumblob;AccountKey=hzKpFlg6rpW9jeMxo1UO8VYckLqEGrsEV1yoWxb2sGfsZOFDfCY4DhFcqJHWAlpvHEPgYr1wNEKOAP+By5rc5A==;EndpointSuffix=core.windows.net"

    fun sendDriveData(data: DrivingBlob) {
        try {

            doAsync() {
                // Retrieve storage account from connection-string.
                val storageAccount = CloudStorageAccount.parse(storageConnectionString)

                // Create the blob client.
                val blobClient = storageAccount.createCloudBlobClient()

                // Retrieve reference to a previously created container.
                val container = blobClient.getContainerReference("main")

                // Create or overwrite the "myimage.jpg" blob with contents from a local file.
                val blob = container.getBlockBlobReference("blob-" + data.id + "-" + dateToString(data.startTime) + ".json")

                // Configure GSON
                val gsonBuilder = GsonBuilder()
                gsonBuilder.setPrettyPrinting()
                val gson = gsonBuilder.create()

                // Format to JSON
                val json = gson.toJson(data)
                parent.appendLog("Sending Blob: " + json)

                blob.uploadText(json)
            }



        } catch (e: Exception) {
            // Output the stack trace.
            e.printStackTrace()
        }

    }

}