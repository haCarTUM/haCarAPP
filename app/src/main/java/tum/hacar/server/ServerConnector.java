package tum.hacar.server;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import tum.hacar.data.DriveSample;
import tum.hacar.hacarapp.MainActivity;

/**
 * demo client
 */
public class ServerConnector {

    private MainActivity parent;

    public ServerConnector(MainActivity parent) throws MalformedURLException {

    }

    public void sendDriveData(DriveSample data){

    }

}