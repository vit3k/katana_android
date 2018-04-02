
import React, { Component } from 'react';
import { View, FlatList, TouchableOpacity, Text } from 'react-native';
import { UsbMidi, UsbMidiDevice } from './UsbMidi';
import { Katana } from './Katana';

export default class Usb extends Component {
    state = {
        devices: [],
        device: null
    }
    midi = new UsbMidi();
    componentDidMount = async () => {
        this.getOutputDevices();
        await this.midi.init();
    }
    getOutputDevices = async () => {
        let devices = await this.midi.getOutputDevices();
        console.log(devices);
        this.setState({
            devices
        });
    }
    send = async (item) => {
        //this.midi.send(item.address, )
        let midiDevice = new UsbMidiDevice(this.midi, item.address);
        midiDevice.onReceive((data) => {
            console.log(data);
        });
        let katana = new Katana(midiDevice);
        await katana.init();
        //await katana.set([0x60, 0x00, 0x12, 0x14], [1]);
        //await katana.query([0x60, 0x00, 0x12, 0x14]);
    }
    render() {
        return <View>
            <TouchableOpacity onPress={this.getOutputDevices}><Text>Get devices</Text></TouchableOpacity>
            <FlatList
            keyExtractor = {item => item.address}
            data = {this.state.devices}
            renderItem = { ({item}) => <TouchableOpacity onPress={ () => this.send(item)}><Text style={{fontSize: 20}}>{item.name}</Text></TouchableOpacity>}
            />
        </View>
    }
}
