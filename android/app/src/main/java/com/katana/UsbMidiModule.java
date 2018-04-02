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

class UsbMidiDriverImpl extends UsbMidiDriver {

    private Emitter emitter;

    public void setEmitter(Emitter emitter) {
        this.emitter = emitter;
    }

    protected UsbMidiDriverImpl(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onDeviceAttached(@NonNull UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceAttached(@NonNull MidiInputDevice midiInputDevice) {
        Log.d(TAG, "onMidiInputDeviceAttached: test");

    }

    @Override
    public void onMidiOutputDeviceAttached(@NonNull MidiOutputDevice midiOutputDevice) {
        Log.d(TAG, "onMidiOutputDeviceAttached: test");
    }

    @Override
    public void onDeviceDetached(@NonNull UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceDetached(@NonNull MidiInputDevice midiInputDevice) {

    }

    @Override
    public void onMidiOutputDeviceDetached(@NonNull MidiOutputDevice midiOutputDevice) {

    }

    @Override
    public void onMidiMiscellaneousFunctionCodes(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiCableEvents(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiSystemCommonMessage(@NonNull MidiInputDevice midiInputDevice, int i, byte[] bytes) {

    }

    @Override
    public void onMidiSystemExclusive(@NonNull MidiInputDevice midiInputDevice, int cable, byte[] bytes) {
        //Log.d(TAG, Hex.encodeHexString(bytes));
        WritableMap map = Arguments.createMap();
        map.putString("address", midiInputDevice.getDeviceAddress());
        WritableArray data = Arguments.createArray();
        for(int i = 0; i < bytes.length; i++) {
            data.pushInt((int)bytes[i]);
        }
        map.putArray("data", data);
        emitter.emit("USB_MIDI_DATA_RECEIVED", map);
    }

    @Override
    public void onMidiNoteOff(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiNoteOn(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiPolyphonicAftertouch(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiControlChange(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiProgramChange(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2) {
        Log.d(TAG, "midiProgramChange: " + Integer.toString(i1) + " " + Integer.toString(i2));
    }

    @Override
    public void onMidiChannelAftertouch(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2) {

    }

    @Override
    public void onMidiPitchWheel(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2) {

    }

    @Override
    public void onMidiSingleByte(@NonNull MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiTimeCodeQuarterFrame(@NonNull MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiSongSelect(@NonNull MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiSongPositionPointer(@NonNull MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiTuneRequest(@NonNull MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiTimingClock(@NonNull MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiStart(@NonNull MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiContinue(@NonNull MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiStop(@NonNull MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiActiveSensing(@NonNull MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiReset(@NonNull MidiInputDevice midiInputDevice, int i) {

    }
}

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

class Emitter {
    private final DeviceEventManagerModule.RCTDeviceEventEmitter emitter;

    public Emitter(DeviceEventManagerModule.RCTDeviceEventEmitter emitter) {
        this.emitter = emitter;
    }

    public void emit(String eventName, Object params) {
        this.emitter.emit(eventName, params);
    }
}