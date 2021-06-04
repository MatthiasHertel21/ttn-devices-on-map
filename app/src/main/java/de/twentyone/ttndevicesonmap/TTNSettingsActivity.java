package de.twentyone.ttndevicesonmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class TTNSettingsActivity extends AppCompatActivity {

    public static final String TTN_APP_NAME = "ttnAppName";
    public static final String TTN_ACCOUNT = "ttnAccount";
    public static final String TTN_QUERY = "ttnQuery";
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ((TextView)findViewById(R.id.ttnAppName)).setText(String.valueOf(sharedPreferences.getString(TTN_APP_NAME,"f21lora001app")));
        ((TextView)findViewById(R.id.ttnAccount)).setText(String.valueOf(sharedPreferences.getString(TTN_ACCOUNT,"ttn-account-v2.QVKUZ5HWPCK_AachlCuzU47vvY3SxvrXoWtuVLn-xFk")));
        ((TextView)findViewById(R.id.ttnQuery)).setText(String.valueOf(sharedPreferences.getString(TTN_QUERY,"3d")));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
    public void clickSave(View view) {
        sharedPreferences.edit().putString(TTN_APP_NAME,((TextView)findViewById(R.id.ttnAppName)).getText().toString()).apply();
        sharedPreferences.edit().putString(TTN_ACCOUNT,((TextView)findViewById(R.id.ttnAccount)).getText().toString()).apply();
        sharedPreferences.edit().putString(TTN_QUERY,((TextView)findViewById(R.id.ttnQuery)).getText().toString()).apply();
        this.finish();
    }
}