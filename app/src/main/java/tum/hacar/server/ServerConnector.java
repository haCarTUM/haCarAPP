package tum.hacar.server;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import tum.hacar.hacarapp.MainActivity;

/**
 * demo client
 */
public class ServerConnector {
    private HaCarServer haCarServer;
    private MainActivity parent;

    public ServerConnector(MainActivity parent) throws MalformedURLException {
        String url = "http://localhost:9000/HaCarTUM";
        Service service = Service.create(
                new URL(url + "?wsdl"),
                new QName("http://server.haCarTUM.x17.hackaTUM.selectcode.de/", "HaCarServerImplService"));
        haCarServer = service.getPort(HaCarServer.class);
    }

    public void sendDriveData(DriveData data){
        haCarServer.addDriveData(data.getGyroX(),data.getGyroY(),data.getGyroZ(),data.getId());
        parent.appendLog("Sent the following data to the server: " + data.toString());
    }

}