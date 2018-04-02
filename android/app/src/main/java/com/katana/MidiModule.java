package com.katana;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.media.midi.MidiReceiver;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.bluetooth.BluetoothProfile.GATT;
import static com.facebook.react.common.ReactConstants.TAG;

/**
 * Created by witek on 28.03.2018.
 */

class Device {
    private final MidiDevice midiDevice;
    private final Map<Integer, MidiInputPort> inputPorts;
    private final Map<Integer, MidiOutputPort> outputPorts;

    public Device(MidiDevice midiDevice) {
        this.midiDevice = midiDevice;
        inputPorts = new HashMap<>();
        outputPorts = new HashMap<>();
    }

    public MidiInputPort openInputPort(int portNumber) {
        MidiInputPort port = midiDevice.openInputPort(portNumber);
        inputPorts.put(portNumber, port);
        return port;
    }

    public MidiOutputPort openOutputPort(int portNumber, MidiReceiver receiver) {
        MidiOutputPort port = midiDevice.openOutputPort(portNumber);
        port.connect(receiver);
        outputPorts.put(portNumber, port);
        return port;
    }

    public MidiDevice getMidiDevice() {
        return midiDevice;
    }

    public void send(int port, byte[] data) throws IOException {
        MidiInputPort inputPort = inputPorts.get(port);
        inputPort.send(data, 0, data.length);
    }
}

public class MidiModule extends ReactContextBaseJavaModule {
    BluetoothManager bluetoothManager;
    MidiManager midiManager;
    final Map<Integer, Device> devices;
    final Map<Integer, MidiDeviceInfo> deviceInfos;


    public MidiModule(ReactApplicationContext reactContext) {
        super(reactContext);
        midiManager = (MidiManager)reactContext.getSystemService(Context.MIDI_SERVICE);
        devices = new HashMap<>();
        deviceInfos = new HashMap<>();
        /*if (reactContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            reactContext.getCurrentActivity().finish();
        }*/
    }

    private void emit(String eventName, Object params) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public String getName() {
        return "Midi";
    }

    private BluetoothManager getBluetoothManager() {
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) getReactApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        }
        return bluetoothManager;
    }

    private BluetoothDevice findDevice(String address) {
        List<BluetoothDevice> devices = getBluetoothManager().getConnectedDevices(GATT);
        for (BluetoothDevice device: devices) {
            if (device.getAddress().equals(address)) {
                return device;
            }
        }
        return null;
    }
    @ReactMethod
    public void openBluetoothDevice(String address, final Promise promise) {

        BluetoothDevice connectedDevice = findDevice(address);
        if (connectedDevice != null) {
            midiManager.openBluetoothDevice(connectedDevice, new MidiManager.OnDeviceOpenedListener() {
                @Override
                public void onDeviceOpened(MidiDevice device) {
                    devices.put(device.getInfo().getId(), new Device(device));
                    promise.resolve(mapInfo(device.getInfo()));
                }
            }, null);
        }
    }
    @ReactMethod
    public void getDevices(Promise promise) {
        deviceInfos.clear();
        MidiDeviceInfo[] infos = midiManager.getDevices();
        WritableArray result = Arguments.createArray();
        for (MidiDeviceInfo info: infos) {
            WritableMap map = mapInfo(info);
            result.pushMap(map);
            deviceInfos.put(info.getId(), info);
        }
        promise.resolve(result);
    }

    private WritableMap mapInfo(MidiDeviceInfo info) {
        WritableMap map = Arguments.createMap();
        map.putInt("id", info.getId());
        map.putInt("type", info.getType());
        Bundle properties = info.getProperties();
        String manufacturer = properties.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);
        String product = properties.getString(MidiDeviceInfo.PROPERTY_PRODUCT);
        String name = properties.getString(MidiDeviceInfo.PROPERTY_NAME);
        map.putString("manufacturer", manufacturer);
        map.putString("product", product);
        map.putString("name", name);
        MidiDeviceInfo.PortInfo[] portInfos = info.getPorts();
        WritableArray ports = Arguments.createArray();
        for(MidiDeviceInfo.PortInfo portInfo : portInfos) {
            WritableMap portMap = Arguments.createMap();
            portMap.putInt("number", portInfo.getPortNumber());
            portMap.putString("name", portInfo.getName());
            portMap.putInt("type", portInfo.getType());
            ports.pushMap(portMap);
        }
        map.putArray("ports", ports);
        return map;
    }

    @ReactMethod
    public void openDevice(int id, final Promise promise) {
        MidiDeviceInfo info = deviceInfos.get(id);
        midiManager.openDevice(info, new MidiManager.OnDeviceOpenedListener() {
            @Override
            public void onDeviceOpened(MidiDevice device) {
                devices.put(device.getInfo().getId(), new Device(device));
                promise.resolve(mapInfo(device.getInfo()));
            }
        }, null);
    }

    private Device getDevice(int deviceId) {
        return devices.get(deviceId);
    }
    @ReactMethod
    public void openInputPort(int deviceId, int portNumber, final Promise promise) {
        Device device = devices.get(deviceId);
        device.openInputPort(portNumber);
        promise.resolve(null);
    }

    @ReactMethod
    void openOutputPort(final int deviceId, final int portNumber, final Promise promise) {
        Device device = getDevice(deviceId);
        device.openOutputPort(0, new MidiReceiver() {
            @Override
            public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
                WritableMap params = Arguments.createMap();
                params.putInt("deviceId", deviceId);
                params.putInt("portNumber", portNumber);
                WritableArray data = Arguments.createArray();
                for(int i = 0; i < msg.length; i++) {
                    data.pushInt((int)msg[i]);
                }
                params.putArray("data", data);
                emit("MIDI_DATA_RECEIVED", params);
            }
        });
        promise.resolve(null);
    }

    @ReactMethod
    public void send(int deviceId, int portNumber, ReadableArray data, Promise promise) {
        try {
            byte[] byteData = new byte[data.size()];
            for(int i = 0; i < data.size(); i++) {
                Log.d(TAG, Integer.toHexString(data.getInt(i)));
                byteData[i] = (byte)data.getInt(i);
            }
            Device device = getDevice(deviceId);
            device.send(portNumber, byteData);
            promise.resolve(null);
        } catch(IOException ex) {
            promise.reject(ex);
        }
    }
}
