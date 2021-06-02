package de.twentyone.ttndevicesonmap;

import android.icu.util.TimeZone;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TTNDeviceEntry {
    LatLng ll;
    String timeString = null;

    public TTNDeviceEntry(JSONObject loc) throws JSONException  {
        double lat = loc.getDouble("latitude");
        double lng = loc.getDouble("longitude");
        this.ll = new LatLng(lat,lng);
        this.timeString = loc.getString("time");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            long time = sdf.parse(timeString).getTime()+1000*3600*2;
            SimpleDateFormat tdf = new SimpleDateFormat("dd.MM HH:mm");
            timeString = tdf.format(time);
        }
        catch (Exception e){};

    }

    public String getAddress(){
        String urlStr = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + ll.latitude + "&lon=" + ll.longitude + "&zoom=20&addressdetails=0";
        HttpReader osm = new HttpReader(urlStr,"");
        StringBuilder result = new StringBuilder();
        try {
            osm.start();
            osm.join();
            JSONObject osmResult = new JSONObject(osm.result);
            String displayName = osmResult.getString("display_name");
            String dn[] = displayName.split(", ");
            if (dn.length>1) {
                if (dn[0].length() < 4) result.append(dn[1] + " " + dn[0]);
                else result.append(dn[0].trim() + " | " + dn[1].trim());
            }
            else result.append(dn[0].trim());
        } catch (Exception e) {
            e.printStackTrace();
            result.append(ll.latitude+" - "+ ll.longitude);
        }
        return result.toString();
    }
}


