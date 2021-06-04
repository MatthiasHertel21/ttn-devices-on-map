package de.twentyone.ttndevicesonmap;

import android.widget.TextView;

import org.json.JSONArray;

import java.util.Vector;

public class TTNDevices {

    String appId; //f.e. f21lora001app
    String ttnAccount; // f.e. ttn-account-v2.QVKUZ5HWPCK_AachlCuzU47vvY3SxvrXoWtuVLn-xFk
    Vector<TTNDevice> devices;

    public TTNDevices(String appId, String ttnAccount){
        this.appId=appId;
        this.ttnAccount = ttnAccount;
        devices = new Vector<TTNDevice>();
    }

    public void readDeviceDataV2(){
        readDeviceDataV2("7d");
    }

    public void readDeviceDataV2(String duration){
        String baseUrl="https://"+appId+".data.thethingsnetwork.org";
        String credentials = "key "+ttnAccount;

        HttpReader rd = new HttpReader(baseUrl+"/api/v2/devices",credentials);
        try {
            rd.start();
            rd.join();
            JSONArray dev = new JSONArray(rd.result);
            for (int i=0; i<dev.length();i++) {
                TTNDevice device = new TTNDevice(dev.getString(i));
                devices.add(device);
                HttpReader devEntries = new HttpReader(baseUrl+"/api/v2/query/"+dev.getString(i)+"?last="+duration,credentials);
                devEntries.start();
                devEntries.join();
                device.setData(devEntries.result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getInfo(){
        StringBuilder sb = new StringBuilder("<b>Device | Positions | Last Seen | Location</b><br>");
        for (TTNDevice device : devices) sb.append(device.getInfo()+"<br>");
        return sb.toString();
    }

}
