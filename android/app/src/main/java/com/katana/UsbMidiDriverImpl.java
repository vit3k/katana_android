package com.katana;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;
import jp.kshoji.driver.midi.util.UsbMidiDriver;

import static com.facebook.react.common.ReactConstants.TAG;

/**
 * Created by witek on 02.04.2018.
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
    public void onMidiSystemExclusive(@NonNull MidiInputDevice midiInputDevice, int cable, byte[] bytes) {
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
    public void onMidiInputDeviceAttached(@NonNull MidiInputDevice midiInputDevice) {
        Log.d(TAG, "onMidiInputDeviceAttached: test");

    }

    @Override
    public void onMidiOutputDeviceAttached(@NonNull MidiOutputDevice midiOutputDevice) {
        Log.d(TAG, "onMidiOutputDeviceAttached: test");
    }

    @Override
    public void onMidiProgramChange(@NonNull MidiInputDevice midiInputDevice, int i, int i1, int i2) {
        Log.d(TAG, "midiProgramChange: " + Integer.toString(i1) + " " + Integer.toString(i2));
    }

    @Override
    public void onDeviceAttached(@NonNull UsbDevice usbDevice) {

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