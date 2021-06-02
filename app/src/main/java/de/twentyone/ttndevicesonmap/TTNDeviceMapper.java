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

        TextView msg = findViewById(R.id.msg);
        msg.setText("Moin");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String appId = sharedPreferences.getString(TTN_APP_NAME,"f21lora001app");
        String ttnAccount = sharedPreferences.getString(TTN_APP_NAME,"ttn-account-v2.QVKUZ5HWPCK_AachlCuzU47vvY3SxvrXoWtuVLn-xFk");

        ttnDevices = new TTNDevices(appId,ttnAccount,msg);
        ttnDevices.readDeviceDataV2();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawCurrentDevicePositions();
    }

    private void drawCurrentDevicePositions() {
        for (TTNDevice device : ttnDevices.devices) {
            mMap.addMarker(new MarkerOptions().position(device.lastPos()).title(device.id+" "+device.locations.lastElement().timeString));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(device.lastPos()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(device.lastPos(), 15));                   ;

            Polyline line;
            PolylineOptions path = new PolylineOptions();
            line = mMap.addPolyline(path);
            line.setTag(device.id);
            line.setZIndex(100);
            line.setClickable(false);
            line.setColor(Color.GRAY);
            line.setWidth(3);
            line.setPoints(device.getLatLng());
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

    public void ckickDetailsClose(View view) {
        TextView details = findViewById(R.id.details);
        details.setVisibility(TextView.INVISIBLE);
    }
}