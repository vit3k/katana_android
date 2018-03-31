package com.katana;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.ReactActivity;

import static com.facebook.react.common.ReactConstants.TAG;

public class MainActivity extends ReactActivity {

    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "katana";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d( "ReactNativeDebugOutput", "MainActivity::onCreate() " + savedInstanceState );

    }


}
