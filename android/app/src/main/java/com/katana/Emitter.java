package com.katana;

import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by witek on 02.04.2018.
 */

class Emitter {
    private final DeviceEventManagerModule.RCTDeviceEventEmitter emitter;

    public Emitter(DeviceEventManagerModule.RCTDeviceEventEmitter emitter) {
        this.emitter = emitter;
    }

    public void emit(String eventName, Object params) {
        this.emitter.emit(eventName, params);
    }
}