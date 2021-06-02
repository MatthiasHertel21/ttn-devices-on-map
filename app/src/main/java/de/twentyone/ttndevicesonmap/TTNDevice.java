package de.twentyone.ttndevicesonmap;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Vector;

public class TTNDevice  {

    String id;
    Vector<TTNDeviceEntry> locations;

    public TTNDevice(String id){
        this.id = id;
        locations = new Vector<TTNDeviceEntry>();
    }

    public void setData(String entries){
        try {
            JSONArray jsonEntries = new JSONArray(entries);
            for (int i=0;i<jsonEntries.length();i++)
                locations.add(new TTNDeviceEntry(jsonEntries.getJSONObject(i)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<LatLng> getLatLng(){
        Vector ll = new Vector<LatLng>();
        for (TTNDeviceEntry entry : locations) ll.add(entry.ll);
        return ll;
    }

    public LatLng lastPos(){
        return locations.lastElement().ll;
    }

    public String getInfo(){
        StringBuilder sb = new StringBuilder(id+ " | "+this.locations.size()+ " | ");
        if (locations.size()>0){
            TTNDeviceEntry lastEntry = locations.lastElement();
            sb.append(lastEntry.timeString+" | ");
            sb.append(lastEntry.getAddress());
        }
        else
            sb.append("never seen");
        return sb.toString();
    }
}
