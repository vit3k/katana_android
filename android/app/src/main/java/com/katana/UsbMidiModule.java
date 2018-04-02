package com.katana;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.support.annotation.NonNull;
import android.util.Log;


import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.apache.commons.codec.binary.Hex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;
import jp.kshoji.driver.midi.util.UsbMidiDriver;

import static com.facebook.react.common.ReactConstants.TAG;

/**
 * Created by witek on 01.04.2018.
 */



public class UsbMidiModule extends ReactContextBaseJavaModule {
    static UsbMidiDriverImpl usbMidiDriver;
    Map<String, MidiOutputDevice> devices;
    Emitter emitter;

    public UsbMidiModule(ReactApplicationContext reactContext) {
        super(reactContext);
        devices = new HashMap<>();

    }

    static void initDriver(Activity activity) {
        usbMidiDriver = new UsbMidiDriverImpl(activity);
        usbMidiDriver.open();
    }

    static void closeDriver() {
        usbMidiDriver.close();
    }
    @Override
    public String getName() {
        return "UsbMidi";
    }

    @ReactMethod
    public void init(Promise promise) {
        emitter = new Emitter(getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class));
        usbMidiDriver.setEmitter(emitter);
        promise.resolve(null);
    }

    @ReactMethod
    public void getOutputDevices(Promise promise) {
        Set<MidiOutputDevice> midiOutputDevices = usbMidiDriver.getMidiOutputDevices();

        devices.clear();
        WritableArray devicesArray = Arguments.createArray();
        for(MidiOutputDevice device : midiOutputDevices) {
            WritableMap deviceMap = Arguments.createMap();
            deviceMap.putString("address", device.getDeviceAddress());
            deviceMap.putString("manufacturer", device.getManufacturerName());
            deviceMap.putString("name", device.getProductName());
            devicesArray.pushMap(deviceMap);
            devices.put(device.getDeviceAddress(), device);
        }
        promise.resolve(devicesArray);
    }

    private MidiOutputDevice getOutputDevice(String address) {
        return devices.get(address);
    }
    @ReactMethod
    public void sendSysex(String address, ReadableArray data, Promise promise) {
        byte[] byteData = new byte[data.size()];
        for(int i = 0; i < data.size(); i++) {
            Log.d(TAG, Integer.toHexString(data.getInt(i)));
            byteData[i] = (byte)data.getInt(i);
        }
        MidiOutputDevice device = getOutputDevice(address);
        device.sendMidiSystemExclusive(0, byteData);
        //device.sendMidiProgramChange(0, 0, 0);
        promise.resolve(null);
    }
}

