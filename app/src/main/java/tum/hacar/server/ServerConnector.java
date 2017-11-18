package tum.hacar.server;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * demo client
 */
public class ServerConnector {
    private HaCarServer haCarServer;

    public ServerConnector() throws MalformedURLException {
        String url = "http://localhost:9000/HaCarTUM";
        Service service = Service.create(
                new URL(url + "?wsdl"),
                new QName("http://server.haCarTUM.x17.hackaTUM.selectcode.de/", "HaCarServerImplService"));
        haCarServer = service.getPort(HaCarServer.class);
    }

    public void sendDriveData(float x, float y, float z, int id){
        haCarServer.addDriveData(x,y,z,id);
    }

}