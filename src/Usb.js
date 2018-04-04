
import React, { Component } from 'react';
import { View, FlatList, TouchableOpacity, Text, StyleSheet } from 'react-native';
//import { UsbMidi, UsbMidiDevice } from './UsbMidi';
//import { Katana } from './Katana';
//import DeviceList from './DeviceList';
import Device from './Device';

export default class Usb extends Component {
    state = {
        device: null
    }
    //midi = new UsbMidi();
    componentDidMount = async () => {
       // await this.midi.init();
    }

    selectDevice = async (device) => {
    /*    let midiDevice = new UsbMidiDevice(this.midi, item.address);
        midiDevice.onReceive((data) => {
            console.log(data);
        });
        let katana = new Katana(midiDevice);
        await katana.init();
        this.setState({ device: katana });*/
    }

    render() {
        return (
        <View style={styles.container}>
            <Device/>
        </View>)
    }
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#EEEEEE',
        flex: 1
    }
});
