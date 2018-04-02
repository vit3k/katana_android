import { NativeEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';
import EventEmitter from 'EventEmitter';

const UsbMidiNative = NativeModules.UsbMidi;
const usbMidiEmitter = new NativeEventEmitter(UsbMidiNative);

export class UsbMidi {
    constructor() {
        this.handlerMidiData = usbMidiEmitter.addListener('USB_MIDI_DATA_RECEIVED', this.handleMidiData);
        this.eventEmitter = new EventEmitter();

    }

    init = async () => {
        await UsbMidiNative.init();
    }
    handleMidiData = (data) => {
        this.eventEmitter.emit(`USB_MIDI_DATE_RECEIVED_${data.address.replace('/', '')}`, data.data);
    }
    subscribe(address, handler) {
        this.eventEmitter.addListener(`USB_MIDI_DATE_RECEIVED_${address.replace('/', '')}`, handler);
    }
    getOutputDevices = async () => {
        return await UsbMidiNative.getOutputDevices();
    }
    send = async (address, data) => {
        return await UsbMidiNative.sendSysex(address, data);
    }
}

export class UsbMidiDevice {
    constructor(midi, address) {
        this.midi = midi;
        this.address = address;
    }

    onReceive = (handler) => {
        this.midi.subscribe(this.address, handler);
    }

    async send(data) {
        await this.midi.send(this.address, data);
    }
}