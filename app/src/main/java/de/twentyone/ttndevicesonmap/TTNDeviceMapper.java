package de.twentyone.ttndevicesonmap;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import de.twentyone.ttndevicesonmap.databinding.ActivityMapsBinding;

import static de.twentyone.ttndevicesonmap.TTNSettingsActivity.TTN_ACCOUNT;
import static de.twentyone.ttndevicesonmap.TTNSettingsActivity.TTN_APP_NAME;
import static de.twentyone.ttndevicesonmap.TTNSettingsActivity.TTN_QUERY;

public class TTNDeviceMapper extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private TTNDevices ttnDevices;

    // TTN Credentials
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //drawCurrentDevicePositions();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawCurrentDevicePositions();
    }

    private void drawCurrentDevicePositions() {

        String appId = sharedPreferences.getString(TTN_APP_NAME,"f21lora001app");
        String ttnAccount = sharedPreferences.getString(TTN_ACCOUNT,"ttn-account-v2.QVKUZ5HWPCK_AachlCuzU47vvY3SxvrXoWtuVLn-xFk");
        String ttnQuery = sharedPreferences.getString(TTN_QUERY,"3d");
        ttnDevices = new TTNDevices(appId,ttnAccount);
        ttnDevices.readDeviceDataV2(ttnQuery);

        TextView msg = findViewById(R.id.msg);
        msg.setText("found "+ttnDevices.devices.size() +" devices");
        mMap.clear();

        for (TTNDevice device : ttnDevices.devices) {
            if (device.locations.size()>0) {
                Polyline line;
                PolylineOptions path = new PolylineOptions();
                line = mMap.addPolyline(path);
                line.setTag(device.id);
                line.setZIndex(100);
                line.setClickable(false);
                line.setColor(Color.GRAY);
                line.setWidth(3);
                line.setPoints(device.getLatLng());
                mMap.addMarker(new MarkerOptions().position(device.lastPos()).title(device.id + " " + device.locations.lastElement().timeString));
            }
            else {
                msg.setText("no data for "+device.id);
            }
        }
        if (ttnDevices.devices.size()>0){
            LatLng goPos = ttnDevices.devices.lastElement().lastPos();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(goPos));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(goPos, 15));
        }
    }

    public void clickSetup(View view) {
        startActivity(new Intent(getApplicationContext(), TTNSettingsActivity.class));
    }

    public void clickShowDetails(View view) {
        TextView details = findViewById(R.id.details);
        details.setPadding(30,30,30,30);
        StringBuilder html = new StringBuilder("<h2>TTN Devices Details</h2><br>Application: <b>"+ttnDevices.appId+"</b><br><br>");
        html.append(ttnDevices.devices.size()+(ttnDevices.devices.size()==1 ? " device":" devices")+"<br><br>");
        html.append(ttnDevices.getInfo());
        details.setText(Html.fromHtml(html.toString()));
        details.setVisibility(TextView.VISIBLE);
    }

    public void clickDetailsClose(View view) {
        TextView details = findViewById(R.id.details);
        details.setVisibility(TextView.INVISIBLE);
    }

    public void clickReload(View view) {
        drawCurrentDevicePositions();
    }
}