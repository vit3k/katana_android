import { NativeEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';
import EventEmitter from 'EventEmitter';

const MidiNative = NativeModules.Midi;
const midiEmitter = new NativeEventEmitter(Midi);

export class Midi {
    constructor() {
        this.handlerMidiData = midiEmitter.addListener('MIDI_DATA_RECEIVED', this.handleMidiData);
        this.eventEmitter = new EventEmitter();
    }
    openBluetoothDevice = async (addr) => {
        let device = await MidiNative.openBluetoothDevice(addr);
        return new MidiDevice(this, device);
    }
    handleMidiData = (data) => {
        this.eventEmitter.emit(`MIDI_DATE_RECEIVED_${data.deviceId}_${data.portNumber}`, data.data);
    }
    subscribe(deviceId, port, handler) {
        this.eventEmitter.addListener(`MIDI_DATE_RECEIVED_${deviceId}_${port}`, handler);
    }
}

export class MidiDevice {
    constructor(midi, device, inputPort = 0, outputPort = 0) {
        MidiNative.openInputPort(device.id, inputPort);
        //MidiNative.openOutputPort(device.id, outputPort);
        this.inputPort = inputPort;
        this.outputPort = outputPort;
        this.device = device;
        this.midi = midi;
    }

    send = (data) => {
        console.log(data);
        MidiNative.send(this.device.id, this.inputPort, data);
    }

    onReceive = (handler) => {
        this.midi.subscribe(this.device.id, this.inputPort, handler);
    }
}

